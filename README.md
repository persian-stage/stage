# Stage API

## Description

This is the Backend Project that runs API webserver and database containers.

## Getting Started

### Prerequisites

Ensure you have the following installed on your machine:

- Docker
- Docker Compose

### Installation

1. Create and start the containers:

```sh
docker compose -p stage up -d
```

2. Remove the entire containers ( Backend ):

```sh
docker compose down
```

3. Remove the entire containers ( Backend and Frontend ):

```sh
docker compose -p stage down
```
4. Build and run Stage Container:

```sh
docker compose up -d --build --no-deps springboot 
```

## Usage

Once the containers are up and running, you can access the application and API endpoints as defined in your setup.

## Project Structure

- `Dockerfile`: Contains the instructions to build the Docker image for the application.
- `docker-compose.yml`: Defines the services, networks, and volumes used in your Docker application.
- `nginx.conf`: Nginx configuration file used for reverse proxying requests to the appropriate services.

## Configuration

Ensure that the environment variables and configuration files are correctly set up before starting the application. You may need to modify the `nginx.conf` file or the `compose.yml` file to match your environment.


## Get Backup from Postgres
```
docker exec -t postgres_container pg_dumpall -c -U stage > /tmp/my_backup.sql
```