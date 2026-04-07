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





-- АУДИТ

CREATE TABLE audit_log
(
    id uuid PRIMARY KEY DEFAULT uuid_generate_v4(),

    table_name VARCHAR(255) NOT NULL,
    operation VARCHAR(10) NOT NULL,

    record_id uuid NOT NULL,

    old_data JSONB,
    new_data JSONB,

    changed_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE INDEX idx_audit_table_name ON audit_log(table_name);
CREATE INDEX idx_audit_record_id ON audit_log(record_id);
CREATE INDEX idx_audit_changed_at ON audit_log(changed_at);


CREATE OR REPLACE FUNCTION audit_trigger_function()
RETURNS TRIGGER AS
$$
    BEGIN
        IF (TG_OP = 'INSERT') THEN
            INSERT INTO audit_log(table_name, operation, record_id, new_data)
            VALUES (TG_TABLE_NAME, TG_OP, NEW.id, to_jsonb(NEW));
    RETURN NEW;

    ELSIF (TG_OP = 'UPDATE' AND OLD IS DISTINCT FROM NEW) THEN
            INSERT INTO audit_log(table_name, operation, record_id, old_data, new_data)
            VALUES (TG_TABLE_NAME, TG_OP, NEW.id, to_jsonb(OLD), to_jsonb(NEW));
    RETURN NEW;

    ELSIF (TG_OP = 'DELETE') THEN
            INSERT INTO audit_log(table_name, operation, record_id, old_data)
            VALUES (TG_TABLE_NAME, TG_OP, OLD.id, to_jsonb(OLD));
    RETURN OLD;
    END IF;

    RETURN NULL;
    END;
$$
    LANGUAGE plpgsql;


CREATE TRIGGER audit_tickets_trigger
    AFTER INSERT OR UPDATE OR DELETE
                    ON tickets
                        FOR EACH ROW
                        EXECUTE FUNCTION audit_trigger_function();

CREATE TRIGGER audit_comments_trigger
    AFTER INSERT OR UPDATE OR DELETE
                    ON comments
                        FOR EACH ROW
                        EXECUTE FUNCTION audit_trigger_function();

CREATE TRIGGER audit_categories_trigger
    AFTER INSERT OR UPDATE OR DELETE
                    ON categories
                        FOR EACH ROW
                        EXECUTE FUNCTION audit_trigger_function();

CREATE TRIGGER audit_topics_trigger
    AFTER INSERT OR UPDATE OR DELETE
                    ON topics
                        FOR EACH ROW
                        EXECUTE FUNCTION audit_trigger_function();
