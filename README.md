# user-registration-angular-java
This is will register user via spring boot and angular implementation
# how run
clone this repo "clone https://github.com/firewire01/user-registration-angular-java.git" 
You can run it either separetely or join by docker compose
## Run mannually
#### Go to each folder:
#### folder name "java" then run cmd command "mvn clean install" then "mvn spring-boot:run", make user you have maven properly installed
> ref: https://phoenixnap.com/kb/install-maven-windows
#### java is running in http://localhost:8080/
#### folder name name "angular-14-registration-login-example-master" then run cmd command "ng serve" make sure you properlly installed npm, ng angular to run the UI.
> ref: https://www.simplilearn.com/how-to-install-angular-and-nodejs-on-windows-article
#### angular is running in http://localhost:4200/
## Run with docker compose
#### Go to the folder where the docker-compose.yml is. 
#### run docker-compose up
#### Notes: make sure you properlly installed docker and docker compose 
> ref: https://docs.docker.com/desktop/install/windows-install/
#### The joined application is map in  http://localhost:8081 there you can access the UI directly
[![image](https://github.com/firewire01/user-registration-angular-java/assets/6559144/182be1a3-ba2b-46a0-9376-0f15390df6b1)]

# Test via Postman
For test with postman i added a postman collection that you can import to postman 
> https://www.postman.com/downloads/ postman installation guide
![image](https://github.com/firewire01/user-registration-angular-java/assets/6559144/5c801f4a-65a7-41ca-8a53-0f151af3178b)

# What is the features of the app:
### Can Register, update profile(except user credentials), get by ID, get all, get all emails sent, log in, log out
### JWT implementation for the log in and log out functionality
### UI Angular 14 with jwt log in and log out.
### Swagger for documentations
> access here http://localhost:8080/swagger-ui/index.html#/
### ![image](https://github.com/firewire01/user-registration-angular-java/assets/6559144/fc8462f8-c7f8-4987-95cb-749b30019b3a)
### Global error handling.
### Email functionality.
### Spring Mapper implementation with generics implementation
### H2 DB in memory DB
### Inversion of Control Services design
### Fully tested junits.
![image](https://github.com/firewire01/user-registration-angular-java/assets/6559144/f023873c-1264-45b3-a8f1-731d11f726f9)
