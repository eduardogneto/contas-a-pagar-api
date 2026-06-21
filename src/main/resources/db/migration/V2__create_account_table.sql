CREATE TABLE account (
    id           UUID           PRIMARY KEY DEFAULT gen_random_uuid(),
    due_date     DATE           NOT NULL,
    payment_date DATE,
    amount       NUMERIC(15, 2) NOT NULL,
    description  VARCHAR(255)   NOT NULL,
    status       VARCHAR(20)    NOT NULL DEFAULT 'PENDING',
    supplier_id  BIGINT         NOT NULL,

    CONSTRAINT fk_account_supplier FOREIGN KEY (supplier_id) REFERENCES supplier(id)
);

CREATE INDEX idx_account_due_date   ON account (due_date);
CREATE INDEX idx_account_status     ON account (status);
CREATE INDEX idx_account_supplier   ON account (supplier_id);
