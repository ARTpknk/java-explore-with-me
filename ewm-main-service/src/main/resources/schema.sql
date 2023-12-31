DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS subscriptions CASCADE;
DROP TABLE IF EXISTS events CASCADE;
DROP TABLE IF EXISTS requests CASCADE;
DROP TABLE IF EXISTS compilations CASCADE;
DROP TABLE IF EXISTS compilations_events CASCADE;


CREATE TABLE IF NOT EXISTS users (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
  user_name VARCHAR(255) NOT NULL,
  user_email VARCHAR(512) NOT NULL,
  subscribers BIGINT DEFAULT 0 NOT NULL,
  subscriptions BIGINT DEFAULT 0 NOT NULL,
  CONSTRAINT UQ_USER_EMAIL UNIQUE (user_email)
);

CREATE TABLE IF NOT EXISTS categories(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    categories_name VARCHAR NOT NULL,
    CONSTRAINT UQ_CATEGORIES_NAME UNIQUE (categories_name)
);

CREATE TABLE IF NOT EXISTS subscriptions (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    subscriber_id BIGINT NOT NULL,
    creator_id BIGINT NOT NULL,
    CONSTRAINT fk_subscriber_id FOREIGN KEY (subscriber_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_creator_id FOREIGN KEY (creator_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS events(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    annotation VARCHAR NOT NULL,
    category_id BIGINT NOT NULL REFERENCES categories (id),
    confirmed_requests BIGINT,
    created_on TIMESTAMP WITH TIME ZONE NOT NULL,
    description VARCHAR NOT NULL,
    event_date TIMESTAMP WITH TIME ZONE NOT NULL,
    initiator_id BIGINT NOT NULL REFERENCES users (id),
    lat FLOAT NOT NULL,
    lon FLOAT NOT NULL,
    paid BOOLEAN,
    participant_limit BIGINT DEFAULT 0 NOT NULL,
    published_on TIMESTAMP WITH TIME ZONE,
    request_moderation BOOLEAN DEFAULT true NOT NULL,
    state VARCHAR NOT NULL,
    title VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS requests(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    event_id BIGINT NOT NULL REFERENCES events (id) ON DELETE CASCADE,
    requester_id BIGINT NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    status VARCHAR NOT NULL,
    CONSTRAINT UQ_EVENT_REQUESTER UNIQUE (event_id, requester_id)
);


CREATE TABLE IF NOT EXISTS compilations(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    pinned BOOLEAN,
    title VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS compilations_events(
    compilation_id BIGINT NOT NULL REFERENCES compilations (id) ON DELETE CASCADE,
    event_id BIGINT NOT NULL REFERENCES events (id) ON DELETE CASCADE,
    PRIMARY KEY (compilation_id, event_id)
);