# Foreign_exchange

This repository: https://hub.docker.com/repository/docker/tsonevski/foreign-exchange/general contains the Dockerized version of my Spring Boot application. The application is packaged as a Docker image and can be pulled from Docker Hub.

## Prerequisites

Before you begin, ensure you have met the following requirements:

- Docker is installed and running on your machine.

## Pulling the Docker Image

To pull the Docker image of my Spring Boot application, open a terminal and run the following command:

1. docker pull tsonevski/foreign-exchange
2. docker run -p <yourHost>:8080 -e "SPRING_DATASOURCE_USERNAME=<DBUsername>" -e "SPRING_DATASOURCE_PASSWORD=<DBPassword>" -e "EXCHANGE_KEY=9d31cb57628ed3c7c8e4712f28123251" tsonevski/foreign-exchange
3. Open http://localhost:<YourPort>/swagger-ui/index.html

###Example
   Example for the link if you are using port 8080    http://localhost:8080/swagger-ui/index.html
