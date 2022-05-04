## DESCRIPTION
Basic demo for using Spring REST

## UDEMY COURSE LINK
https://www.udemy.com/course/spring-hibernate-tutorial/

## Docker related commands
https://spring.io/guides/gs/spring-boot-docker/

- Force Java 11

`export JAVA_HOME=$(/usr/libexec/java_home -v 11)`

`export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-11.0.5.jdk/Contents/Home`

- Basic example

`./mvnw clean package -Dmaven.test.skip=true`

`docker build -t lalotor/spring-crm-rest-demo .`

`docker run --rm -p 8081:8081 lalotor/spring-crm-rest-demo`

- Improve performance using layers
  - Use DEPENDENCY parameter

`./mvnw clean package -Dmaven.test.skip=true`

`mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)`

`docker build -t lalotor/spring-crm-rest-demo .`

`docker run --rm -p 8081:8081 lalotor/spring-crm-rest-demo`

- Debugging

`docker run --rm -e "JAVA_TOOL_OPTIONS=-agentlib:jdwp=transport=dt_socket,address=5005,server=y,suspend=n" -p 8081:8081 -p 5005:5005 lalotor/spring-crm-rest-demo`
