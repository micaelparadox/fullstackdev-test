# QIMA Full Stack Development Project

## Overview

This project is a full stack application that incorporates OAuth2 authentication, role-based access control, and CRUD operations. It leverages a simple UI built with Leaf, which was chosen for its straightforwardness over more complex frameworks like ReactJS, Angular, or VueJS.

## Prerequisites

- Java 8 or newer
- Maven
- PostgreSQL installed locally or Docker for containerization

## Configuration

This project uses two predefined users for testing:
- Admin User: `micaelparadox@gmail.com` (password: `micaelparadox`)
- Normal User: `user@example.com` (password: `user123`)

Every user who logs in using OAuth2 is automatically assigned an admin role, enabling them to perform CRUD operations.

## Setting Up

### Using Docker

1. Ensure Docker is installed on your machine.
2. Navigate to the root directory where the `docker-compose.yml` file is located.
3. Run the following command to start the services:
   ```bash
   docker-compose up -d
   ```

### Local Setup

1. Install Java and Maven if not already installed.
2. Optionally, set up a local PostgreSQL database.
3. Open the project in Spring Tool Suite (STS) with Lombok installed.

### Building the Project

1. From the command line, navigate to the project root directory.
2. Execute the following Maven commands to clean and build the project:
   ```bash
   mvn clean install
   ```
3. After building, you can run the project using STS or another IDE of your choice.

## Running the Application

After setting up the project, launch it and access the application through:
```
http://localhost:8080
```
You may change the default port in the application settings if necessary.

## Additional Notes

This implementation covers nearly all the requirements specified in the test, including optional features. The decision to implement certain functionalities was straightforward after the main features were completed.

## Contact

For any questions or further assistance, feel free to reach out.

**Micael Santana**


- micaelparadox@gmail.com


_Participant in the QIMA Technical Test_


