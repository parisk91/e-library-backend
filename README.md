**Overview**:
Library project is an e-Library application, where the users can borrow books from a database, 
and the admins can edit the authors, books and users. This is a Spring Boot-based backend 
application designed to provide a RESTful API with secure authentication using JWT (JSON Web Tokens). 
The application uses Flyway for database migrations and runs inside a Docker container for easy 
deployment and scalability.

**Technologies Used**:
Spring Boot: Backend framework
Docker: Containerization
Flyway: Database migration tool
JWT: Security and authentication
REST API: For communication with clients
Database: PostgreSQL (environment:
    - 'POSTGRES_DB=LibraryDatabase')

**Prerequisites**:
Docker and Docker Compose (container_name:library-project-database, ports: 5432:5432)
Java 17 
Git

**Installation**:
Clone the Repository: git clone http://github.com/parisk91/e-library-backend
