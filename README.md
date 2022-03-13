The backend for [GoRecipe](https://github.com/Capstone-Projects-2022-Spring/project-gorecipe)

# How to Install

This application relies on environment variables to store secrets, keys, and database information. The following environment variables must be set before running the application, or it will not run. GoRecipe Core relies on a MySQL database. 

| Environment Variable | Description|
|----------------------|------------|
| RDS_HOSTNAME         | the URL of the database (e.g. 127.0.0.1 or mydb.123456789012.us-east-1.rds.amazonaws.com) |
| RDS_PORT             | 3306 (default MySQL port) |
| RDS_DB_NAME          | the name of the database | 
| RDS_USERNAME         | the database username |
| RDS_PASSWORD         | the database password |
| AWS_ACCESS_KEY_ID    | see [AWS docs](https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/credentials.html) |
| AWS_SECRET_ACCESS_KEY| |
| AWS_DEFAULT_REGION   |  |
| CLARIFAI_API_KEY     | see [Clarifai docs](https://www.clarifai.com/blog/introducing-api-keys-a-safer-way-to-authenticate-your-applications) |


## Easier Way
1. Download the latest release
2. Run java -jar gorecipe-core-x.x.x.jar (note: Java 8 is required)
3. Visit localhost:8080/swagger-ui/#/ to see API endpoints and models

## Harder Way
1. Clone this repository
2. Install [Maven](https://maven.apache.org/install.html)
3. Install the dependancies specified in `pom.xml` by running `mvn install`
4. Run the SpringBoot application by running `mvn spring-boot:run` (note: Java 8 is required)
5. The application will now be running at localhost:8080
6. Visit localhost:8080/swagger-ui/#/ to see API endpoints and models



# How to Test
With the repository cloned and Maven installed, run `mvn test` (note: unit tests require a local MySQL database to be running)
