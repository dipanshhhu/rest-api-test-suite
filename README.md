# REST API Test Suite – User Management API

## Tools & Technologies
Java | REST Assured | TestNG | Maven | Postman

## About This Project
Automated test suite to validate CRUD operations of a REST API
using reqres.in as the test environment. Covers positive and
negative scenarios across 7 test cases.

## Test Coverage
- GET /users — fetch all users and single user
- POST /users — create new user and validate response
- PUT /users — update existing user details
- DELETE /users — delete existing user
- Negative scenarios — invalid user id returns 404

## How to Run
1. Clone the repository
2. Open in IntelliJ IDEA
3. Run testng.xml

## Project Structure
src/test/java/reqres/api/
  └── StudentApiTest.java — main test class with 7 test cases
  └── model/
        └── JsonUser.java — helper model class
