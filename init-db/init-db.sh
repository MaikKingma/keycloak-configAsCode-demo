#!/bin/bash
set -e

# Create the databases
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
    CREATE DATABASE "event-sourcing-configurator";
    CREATE DATABASE "event-sorcerer-dashboard";
EOSQL

# Connect to the dashboard database and create schema/tables
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname="event-sorcerer-dashboard" <<-EOSQL
    -- Create a dedicated schema for authentication
    CREATE SCHEMA IF NOT EXISTS auth;

    -- Ensure the database user always uses the auth schema
    ALTER ROLE "$POSTGRES_USER" SET search_path TO auth, public;

    -- Restrict access to the schema (only superusers and auth roles can use it)
    REVOKE CREATE ON SCHEMA public FROM PUBLIC;
    GRANT USAGE ON SCHEMA auth TO PUBLIC;
    ALTER DEFAULT PRIVILEGES IN SCHEMA auth REVOKE EXECUTE ON FUNCTIONS FROM PUBLIC;

    -- Create authentication tables in the auth schema
    CREATE TABLE auth.verification_token
    (
      identifier TEXT NOT NULL,
      expires TIMESTAMPTZ NOT NULL,
      token TEXT NOT NULL,

      PRIMARY KEY (identifier, token)
    );

    CREATE TABLE auth.accounts
    (
      id SERIAL PRIMARY KEY,
      "userId" INTEGER NOT NULL,
      type VARCHAR(255) NOT NULL,
      provider VARCHAR(255) NOT NULL,
      "providerAccountId" VARCHAR(255) NOT NULL,
      refresh_token TEXT,
      access_token TEXT,
      expires_at BIGINT,
      id_token TEXT,
      scope TEXT,
      session_state TEXT,
      token_type TEXT
    );

    CREATE TABLE auth.sessions
    (
      id SERIAL PRIMARY KEY,
      "userId" INTEGER NOT NULL,
      expires TIMESTAMPTZ NOT NULL,
      "sessionToken" VARCHAR(255) NOT NULL UNIQUE
    );

    CREATE TABLE auth.users
    (
      id SERIAL PRIMARY KEY,
      name VARCHAR(255),
      email VARCHAR(255) UNIQUE,
      "emailVerified" TIMESTAMPTZ,
      image TEXT
    );
EOSQL
