#!/bin/bash
set -e

# Create the additional database
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
    CREATE DATABASE "event-sourcing-configurator";
EOSQL
