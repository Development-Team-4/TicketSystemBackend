CREATE
EXTENSION IF NOT EXISTS "uuid-ossp";


CREATE TABLE topics
(
    id uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255) NOT NULL,
    description text
);


CREATE TABLE categories
(
    id uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
    topic_id uuid NOT NULL,
    name VARCHAR(255) NOT NULL,
    description text,

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
    subject text NOT NULL,
    description text,
    status VARCHAR(50) NOT NULL
        CHECK (status IN ('OPEN', 'IN_PROGRESS', 'RESOLVED', 'CLOSED')),

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

    content text NOT NULL,
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