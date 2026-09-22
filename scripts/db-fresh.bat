@echo off
REM db-fresh.bat — Laravel "php artisan migrate:fresh --seed" equivalent for Windows.
REM
REM This project doesn't use a migration tool (Flyway/Liquibase) — Hibernate
REM creates tables itself via spring.jpa.hibernate.ddl-auto=update. So "fresh"
REM here means: drop the whole database, let the app recreate the schema on
REM boot, then let DataSeeder (which only runs against an empty database)
REM repopulate it with sample data.
REM
REM Usage:
REM   scripts\db-fresh.bat
REM
REM Requires: the mysql CLI on your PATH, and Maven (mvn) installed.
REM Edit DB_NAME / DB_USER / DB_PASS below if they differ from application.properties.

setlocal

set DB_NAME=marketplace_db
set DB_USER=root
set DB_PASS=Crevel@123

echo This will DROP and recreate database '%DB_NAME%'. All data will be lost.
set /p CONFIRM=Continue? [y/N]:
if /i not "%CONFIRM%"=="y" (
    echo Cancelled.
    exit /b 0
)

echo Dropping and recreating '%DB_NAME%'...
mysql -u %DB_USER% -p%DB_PASS% -e "DROP DATABASE IF EXISTS `%DB_NAME%`; CREATE DATABASE `%DB_NAME%`;"

echo Starting the app — Hibernate will recreate the schema, then DataSeeder will populate sample data...
cd /d "%~dp0.."
mvn spring-boot:run

endlocal
