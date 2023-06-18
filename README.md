# Coursework - web application for accounting for goods in stock

The web application with which the warehouse can take into account and automate the accounting of goods in the warehouse of an online sock store.
The external interface of the application is presented as a REST API.
The API client (Swagger-ui) is used as the UI part of the application.

## Technologies in the project ##
![IntelliJ IDEA](https://img.shields.io/badge/IntelliJIDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white) ![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=java&logoColor=white) ![Apache Maven](https://img.shields.io/badge/Apache%20Maven-C71A36?style=for-the-badge&logo=Apache%20Maven&logoColor=white) ![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white) 
![Git](https://img.shields.io/badge/git-%23F05033.svg?style=for-the-badge&logo=git&logoColor=white) ![GitHub](https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white) ![Swagger](https://img.shields.io/badge/-Swagger-%23Clojure?style=for-the-badge&logo=swagger&logoColor=white) ![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white) ![Postman](https://img.shields.io/badge/postman-%23ED8B00.svg?style=for-the-badge&logo=postman&logoColor=white)

Backend:
- Java 17
- Maven
- Spring Boot
- Spring Web
- GIT 	 
- REST
- Swagger 	
- Stream API
- Postman

### User`s functionality: ###

A user (warehouse employee) has the ability to:

- take into account the arrival, write-off and issue of socks;
- find out the total number of socks of a certain color and composition at a given time;
- additionally be able to parse (read and convert data) files with product data;

Also implemented the possibility:
- store the operations of receiving and issuing socks in memory and upload them as a JSON file and vice versa - load data into the application from a JSON file.
- export data in the current state, form JSON from data in memory, write it to a file and upload it on request.
- import data, accept a json file with data as input and replace the data in memory with them.
