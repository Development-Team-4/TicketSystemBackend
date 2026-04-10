CREATE
EXTENSION IF NOT EXISTS "uuid-ossp";


CREATE TABLE topics
(
    id uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255) NOT NULL,
    description VARCHAR(2000)
);


CREATE TABLE categories
(
    id uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
    topic_id uuid NOT NULL,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(2000),

    CONSTRAINT fk_category_topic
        FOREIGN KEY (topic_id)
            REFERENCES topics (id)
            ON DELETE CASCADE
);


CREATE TABLE category_staff
(
    category_id uuid NOT NULL,
    user_id uuid NOT NULL,
    PRIMARY KEY (category_id, user_id),

    CONSTRAINT fk_category_staff_category
        FOREIGN KEY (category_id)
            REFERENCES categories (id)
            ON DELETE CASCADE
);


CREATE TABLE tickets
(
    id uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
    subject VARCHAR(2000) NOT NULL,
    description VARCHAR(2000),
    status VARCHAR(50) NOT NULL CHECK (status IN ('OPEN', 'ASSIGNED', 'IN_PROGRESS', 'RESOLVED', 'CLOSED')),

    category_id uuid NOT NULL,
    created_by  uuid NOT NULL,
    assignee_id uuid,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now(),

    CONSTRAINT fk_ticket_category
        FOREIGN KEY (category_id)
            REFERENCES categories (id)
);


CREATE TABLE comments
(
    id uuid PRIMARY KEY DEFAULT uuid_generate_v4(),

    ticket_id uuid NOT NULL,
    author_id uuid NOT NULL,

    content VARCHAR(2000) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now(),

    CONSTRAINT fk_comment_ticket
        FOREIGN KEY (ticket_id)
            REFERENCES tickets (id)
            ON DELETE CASCADE
);


CREATE INDEX idx_ticket_status ON tickets (status);
CREATE INDEX idx_ticket_category ON tickets (category_id);
CREATE INDEX idx_ticket_assignee ON tickets (assignee_id);
CREATE INDEX idx_ticket_created_at ON tickets (created_at);
CREATE INDEX idx_comment_ticket ON comments (ticket_id);





-- АУДИТ ТАБЛИЦЫ ТИКЕТОВ

CREATE TABLE ticket_audit
(
    id uuid PRIMARY KEY DEFAULT uuid_generate_v4(),

    ticket_id uuid NOT NULL,

    operation VARCHAR(10) NOT NULL,

    old_subject VARCHAR(2000),
    new_subject VARCHAR(2000),

    old_description VARCHAR(2000),
    new_description VARCHAR(2000),

    old_status VARCHAR(50),
    new_status VARCHAR(50),

    old_assignee_id uuid,
    new_assignee_id uuid,

    changed_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE INDEX idx_audit_ticket_id ON audit_log(ticket_id);


CREATE
OR REPLACE FUNCTION ticket_audit_trigger()
RETURNS TRIGGER AS
$$
    BEGIN
        IF (TG_OP = 'INSERT') THEN
            INSERT INTO ticket_audit(
                ticket_id, operation,
                new_subject, new_description, new_status,
                new_assignee_id
            )
            VALUES (
                NEW.id, TG_OP,
                NEW.subject, NEW.description, NEW.status,
                NEW.assignee_id
            );
    RETURN new;

    ELSIF (TG_OP = 'UPDATE') THEN
            INSERT INTO ticket_audit(
                ticket_id, operation,
                old_subject, new_subject,
                old_description, new_description,
                old_status, new_status,
                old_assignee_id, new_assignee_id
            )
            VALUES (
                NEW.id, TG_OP,
                OLD.subject, NEW.subject,
                OLD.description, NEW.description,
                OLD.status, NEW.status,
                OLD.assignee_id, NEW.assignee_id
            );
    RETURN new;

    ELSIF (TG_OP = 'DELETE') THEN
            INSERT INTO ticket_audit(
                ticket_id, operation,
                old_subject, old_description, old_status,
                old_assignee_id
            )
            VALUES (
                OLD.id, TG_OP,
                OLD.subject, OLD.description, OLD.status,
                OLD.assignee_id
            );
    RETURN old;
    END IF;

    RETURN NULL;
    END;
$$
LANGUAGE plpgsql;


CREATE TRIGGER ticket_audit_trigger
    AFTER INSERT OR UPDATE OR DELETE
                    ON tickets
                        FOR EACH ROW
                        EXECUTE FUNCTION ticket_audit_trigger();