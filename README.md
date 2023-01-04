# DRONE BACKEND SERVICE

Project contains backend for Drone services


#  Requirements

The list of tools required to build and run the project:

1. Open JDK 11.x+
2. Apache Maven 3.5.x+


# Building
In order to build project use:
 ## mvn clean package
If your default java is not from JDK 11 or higher use:
 ## JAVA_HOME=<path_to_jdk_home> mvn package

# Running Jar file
In order to run  use:
## java -jar target/drone-service.jar
If your default java is not from JDK 11 or higher use:
## <path_to_jdk_home>/bin/java -jar target/drone-service.jar

# Running on Docker

In order to run on docker:

Requirements:
 1. Docker installed

# Build Image:
 ## docker build -t drone-service/8080 .
 
# Run Container:
  ## docker run -d -p 8080:8080  --name drone-service-container/8080 drone-service/8080
  
