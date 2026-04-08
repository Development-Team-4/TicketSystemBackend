CREATE
EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE notifications
(
    id uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id uuid NOT NULL,
    ticket_id uuid NOT NULL,
    type VARCHAR(50) NOT NULL CHECK (type IN ('ASSIGNMENT', 'COMMENT', 'STATUS_CHANGE')),
    title text NOT NULL,
    message text NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE INDEX idx_notification_user ON notifications (user_id);
-- CREATE INDEX idx_notification_ticket ON notifications (ticket_id);
CREATE INDEX idx_notification_created_at ON notifications (created_at);