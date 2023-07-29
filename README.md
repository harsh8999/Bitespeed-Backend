# Bitespeed-Backend

# FluxKart REST API with MySQL using Docker Compose

## Prerequisites
Before running the application, ensure you have the following software installed on your system:

Docker (https://www.docker.com/get-started)
Docker Compose (https://docs.docker.com/compose/install/)


## Installation
1. Clone this repository to your local machine.
```bash
git clone https://github.com/harsh8999/Bitespeed-Backend.git
```

2. Build the jar file
```bash
mvn install
```
3. Build the Docker image for the Spring Boot application
```bash
docker-compose build
```

4. Start the containers using Docker Compose.
```bash
docker-compose up
```
The Spring Boot REST API server should now be running at http://{localhost}:8080.
localhost can be changed with the container's Ip address


## API Endpoints

1. To post and get the data
POST: '/identify'

Request Data: {
	"email"?: string,
	"phoneNumber"?: string
}


