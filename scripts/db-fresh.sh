#!/usr/bin/env bash
#
# db-fresh.sh — Laravel "php artisan migrate:fresh --seed" equivalent.
#
# This project doesn't use a migration tool (Flyway/Liquibase) — Hibernate
# creates tables itself via spring.jpa.hibernate.ddl-auto=update. So "fresh"
# here means: drop the whole database, let the app recreate the schema on
# boot, then let DataSeeder (which only runs against an empty database)
# repopulate it with sample data.
#
# Usage:
#   ./scripts/db-fresh.sh
#
# Requires: the `mysql` CLI on your PATH, and Maven (`mvn`) installed.
# Reads the DB name/user/password straight out of application.properties,
# so it stays in sync if you change them there.

set -euo pipefail
cd "$(dirname "$0")/.."

PROPS="src/main/resources/application.properties"

get_prop() {
  grep -E "^$1=" "$PROPS" | tail -n1 | cut -d'=' -f2- | tr -d '\r'
}

DB_URL=$(get_prop "spring.datasource.url")
DB_USER=$(get_prop "spring.datasource.username")
DB_PASS=$(get_prop "spring.datasource.password")

# pull the database name out of jdbc:mysql://host:port/dbname[?params]
DB_NAME=$(echo "$DB_URL" | sed -E 's#.*/([^/?]+).*#\1#')

if [ -z "$DB_NAME" ]; then
  echo "Could not read the database name from $PROPS — check spring.datasource.url"
  exit 1
fi

echo "This will DROP and recreate database '$DB_NAME'. All data will be lost."
read -p "Continue? [y/N] " confirm
if [[ "$confirm" != "y" && "$confirm" != "Y" ]]; then
  echo "Cancelled."
  exit 0
fi

echo "Dropping and recreating '$DB_NAME'..."
mysql -u "$DB_USER" -p"$DB_PASS" -e "DROP DATABASE IF EXISTS \`$DB_NAME\`; CREATE DATABASE \`$DB_NAME\`;"

echo "Starting the app — Hibernate will recreate the schema, then DataSeeder will populate sample data..."
mvn spring-boot:run
