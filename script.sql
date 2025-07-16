CREATE TABLE payment (
                         id BIGSERIAL PRIMARY KEY,
                         correlation_id UUID UNIQUE NOT NULL,
                         amount NUMERIC(19, 4) NOT NULL,
                         status integer NOT NULL,
                         processor integer,
                         requested_at TIMESTAMP WITH TIME ZONE NOT NULL
);
