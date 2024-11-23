CREATE DATABASE keycloakdb;
CREATE USER keycloakuser WITH PASSWORD 'keycloakuser';
GRANT ALL PRIVILEGES ON DATABASE keycloakdb TO keycloakuser;