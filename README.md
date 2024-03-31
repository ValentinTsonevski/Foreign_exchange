# Foreign_exchange

## Tests
If you run all the tests at once and some of them don't pass, run them individually and they will pass. I think this is caused by the third API for fetching the currency exchange rate.

## Docker Repository
This repository: https://hub.docker.com/repository/docker/tsonevski/foreign-exchange/general contains the Dockerized version of my Spring Boot application. The application is packaged as a Docker image and can be pulled from Docker Hub.

## Prerequisites

Before you begin, ensure you have met the following requirements:

- Docker is installed and running on your machine.

## Pulling the Docker Image

To pull the Docker image of my Spring Boot application, open a terminal and run the following command:

1. you need to be logged into your docker desktop account and after that in the terminal type: docker login
2. docker pull tsonevski/foreign-exchange
3. change yourHost with your port -> docker run -p yourHost:8080 tsonevski/foreign-exchange
3. In this URL change YourPort  with the port you are using http://localhost:YourPort/swagger-ui/index.html

### Example
    if you are using port 8080 use this link ->   http://localhost:8080/swagger-ui/index.html
