# self-serve-be

## Backend project for self serve done.

## Tech stack

### spring boot + mysql


## Setup

### IDE

#### Download the latest version of intellij(Intellij 19). https://download-cf.jetbrains.com/idea/ideaIC-2019.1.2-jbr11.exe

### Build tools.

#### Download latest version of gradle(gradle-5.4.1). https://downloads.gradle.org/distributions/gradle-5.4.1-bin.zip

### JDK

#### Download latest version of java 11.
#### Please go to https://www.oracle.com/technetwork/java/javase/downloads/jdk11-downloads-5066655.html.
#### Accept the licence and download(need an oracle account register or login accordingly to download).

### DB.

#### Install mysql server and mysql client. https://www.mysql.com/downloads/

### Steps

#### Install java 11.
#### Unzip gradle5.4.1.zip and put the path as GRADLE_HOME. Delete .gradle folder in c:/users/${account.name}/.gradle
#### Install intellij 2019.
#### Once intelli j is installed please set jdk 11 as the default jdk by following the instructions in https://www.jetbrains.com/help/idea/configuring-mobile-java-sdk.html.
#### Run gradle clean build to build the project.
#### Create a database self_serve in mysql by logging through mysql client.
#### Please change ```spring.datasource.username``` and ```spring.datasource.password``` according to the mysql username and password you are using(will change it later).
#### Run ```SelfServeApplication.java``` by creating a new ```Application```configuration.
##### Choose main class as ```com.tfsc.ilabs.selfservice.SelfServiceApplication```.
##### **Do this step only if proxy is required - Enter vm arguments as ```-Dhttp.proxyHost=localhost -Dhttp.proxyPort=3128```.
##### Enter working directory as ```${directory-where-self-server-be-is-cloned/self-serve-be}```.
#### Server will start and run on 8080.


## Swagger details

##Some useful API collections for CRUD operations on model "https://www.getpostman.com/collections/445666fe64fd0b46f48f"

```
    http://localhost:8080/self-serve/api/swagger-ui.html
```