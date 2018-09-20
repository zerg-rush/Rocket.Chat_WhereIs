# Rocket.Chat WhereIs bot
## [NCDC House of Talents 2018](https://www.ncdc.eu/career/hot/house-of-talents-3/)

![House of Talents](https://www.ncdc.eu/wp-content/themes/ncdc/img/HoT-logo.png)

## Rocket.Chat WhereIs bot providing location data for specific user, and devices with specified IP and MAC address

## Project modules structure
All components of application are combined in one multi module IntelliJ project with following modules/packages:
- app - WhereIs plugin compliant with Rocket.Chat Apps specification,
- backend - backend service app providing translation level, persistence and business logic for whole application,
- netsvc - low level network service providing actual real time data from network infrastructure devices,
- rocket.chat - Docker configuration for container with Rocket.Chat server (with MongoDB database)

## Used technological stack (languages, technologies and tools):

- [Rocket.Chat 0.70-develop](https://github.com/RocketChat/Rocket.Chat),
- [Docker CE 18.09.01](https://github.com/docker/docker-ce),
- [Oracle Java JDK 10.0.2](http://www.oracle.com/technetwork/java/javase/downloads/jdk10-downloads-4416644.html),
- [JavaScript](https://en.wikipedia.org/wiki/JavaScript),
- [TypeScript 2.9.2](https://www.typescriptlang.org/),
- [node.js 10.11.0](https://nodejs.org/en/),
- [npm 6.4.1](https://www.npmjs.com/),
- [Lombok 1.18.2](https://projectlombok.org/),
- [Spring 5.0.8](https://spring.io/),
- [Spring Boot 2.0.5](https://spring.io/projects/spring-boot),
- [Hibernate 5.2.17](http://hibernate.org/),
- [WSL (Windows Subsystem Linux @ Windows 10)](https://en.wikipedia.org/wiki/Windows_Subsystem_for_Linux),
- [Ubuntu 18.04](https://www.ubuntu.com/),
- [bash 4.4.19](https://www.gnu.org/software/bash/),
- [JSON-server 0.14.0](https://github.com/typicode/json-server),
- [JetBrains IntelliJ IDEA Ultimate 2018.3](https://www.jetbrains.com/idea/),
- [Apache Maven 3.3.9](https://maven.apache.org/),
- [H2 db 1.4.197](http://www.h2database.com/html/main.html),
- [Bootstrap 3.3.7](https://github.com/twbs/bootstrap),
- [Thymeleaf 3.0.9](https://www.thymeleaf.org/),
- [Log4j2 2.11.1](https://logging.apache.org/log4j/2.x/),
- [curl 7.58.0](https://curl.haxx.se/),
- [jq 1.5](https://stedolan.github.io/jq/),


## WhereIs command syntax:

```
Hi, my name is WhereIs v1.0.1, and I am bot created to help you to locate other users at company facilities.

I can provide you location data for specified Rocket.Chat users, and devices with specific IP and MAC addresses. Just call me (using "/whereis") and provide me the name of user which you are looking for, or IP/MAC address.

Usage scenarios:
	/whereis user_nick - displays location data for user with specified nick,
	/whereis IP_ADDR - displays location data for device with specified IP address,
	/whereis MAC_ADDR - displays location data for device with specified MAC address,
	/whereis [help] - displays this detailed information about myself

See following examples:
	/whereis jan
	/whereis 192.168.1.2
	/whereis 01-23-45-67-89-AB

```


How to get help with details about bot usage:

![whereis help](images/whereis_help.gif)


## Rocket.Chat interface for WhereIs bot

WhereIs App configuration panel with log viewer:

![whereis_app_configration_and_logs](images/whereis_app_configration_and_logs.gif)


User roles management (whereis vs. whereis-details):

![whereis user roles and details level](images/whereis_user_roles_and_details_level.gif)


detailed level information for request about specific user:

![whereis user details](images/whereis_user_details.gif)


detailed level information for request about device with specific IP address:

![test](images/whereis_details_IP_address.png)


detailed level information for request about device with specific MAC address:

![whereis_details_MAC_address](images/whereis_details_MAC_address.png)


detailed level information for request about specific user in case he/she is detected in more than one location:

![whereis_details_with_3_locations](images/whereis_details_with_3_locations.png)


## Example responses for typical scenarios

basic level information for request about specific user:

![whereis_user_basic_level_information](images/whereis_user_basic_level_information.png)


basic level information for request about device with specific IP address:

![whereis_IP_address_basic_level_information](images/whereis_IP_address_basic_level_information.png)


basic level information for request about device with specific MAC address:

![whereis_MAC_address_basic_level_information](images/whereis_MAC_address_basic_level_information.png)


## Management web console:

start page:

![whereis management console](images/whereis_management_console.jpg)


list of locations:

![whereis locations list](images/whereis_locations_list.gif)


edit location details:

![whereis edit location](images/whereis_edit_location.png)


list of users:

![whereis users list](images/whereis_users_list.gif)


edit user details:

![whereis edit user](images/whereis_edit_user.png)


## Error handling and logging:

error message in case user is not authorized to call /whereis command (lack of whereis role for this account):

![whereis not authorized](images/whereis_not_authorized.png)


log entry on backend server side:

![whereis not authorized log](images/whereis_not_authorized_log.png)


error message in case bot is asked about user which does not exist:

![whereis unkown user](images/whereis_unkown_user.png)


log entry on backend server side:

![whereis user not exists log](images/whereis_user_not_exists_log.png)


## Logging

Debug logs are available in separatelly on Rocket.Chat App management (created by WhereIs App bot):

![whereis administration logs](images/whereis_administration_logs.png)

![whereis administration logs 2nd](images/whereis_administration_logs_2nd.png)


and /logs/ folder (created by backend service):

![whereis backend logs](images/whereis_backend_logs.png)


## Low level network service REST API specification:

```java
public class NetworkServiceResponse {

    private String id;
    private String fullname;
    private List<UserLocation> locations;

})
```

```java
public class UserLocation {

    private String index;
    private LocationType type;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastActivity;

    @Enumerated(EnumType.STRING)
    @Column(length = 7)
    private StatusType status;

    @Range(min = 1, max = 10, message = "Signal strength must be in range 1-10")
    private String signalStrength;
    private String ip;
    private String mac;

})
```

## WhereIs backend service REST API specification:

```java
public class RocketChatRequest {

    @NotEmpty
    @ApiModelProperty(value = "Represents command text as entered by Rocket.Chat user", required = true, position = 1,
            example = "jan")
    private String command;

    @JsonProperty("mode")
    @NotEmpty
    @ApiModelProperty(value = "Represents requested work mode (USER, IP, MAC or NOT_FOUND)", required = true, position = 2,
            example = "USER")
    private WhereIsMode mode;

    @NotEmpty
    @ApiModelProperty(value = "Represents id value (username, IP or MAC address)", required = true, position = 3,
            example = "jan")
    private String id;

    @NotNull
    @ApiModelProperty(value = "Represents existence of user with requested username", required = true, position = 4,
            example = "true")
    private Boolean exists;

    @NotNull
    @ApiModelProperty(value = "Represents full name of user", required = true, position = 5,
            example = "Jan Kowalski")
    private String fullname;

    @ApiModelProperty(value = "Represents url for user avatar", required = true, position = 6,
            example = USER_AVATARS_URL + "jan.jpg")
    private String avatarUrl;

    @ApiModelProperty(value = "Represents user roles array (i.e. [\"admin\", \"whereis-admin\"])", required = true,
            position = 7, dataType = "List", example = "admin,whereis-admin,whereis-details")
    private String[] senderRoles;

})
```

```java
public enum WhereIsMode {

    USER,
    IP,
    MAC,
    NOT_FOUND,
    NOT_AUTHORIZED,
    SYNTAX;

})
```


## Development environment setup:

1. Clone repository
```
git clone https://github.com/zerg-rush/Rocket.Chat_WhereIs.git
```

2. Install node.js dependencies in app and netsvc folders
```
sudo apt install curl jq maven npm -y
sudo npm install -g n
sudo n latest
sudo npm install -g npm
sudo npm install -g @rocket.chat/apps-cli
cd app
npm install
cd ..
cd netsvc
npm install
cd ..
```

3. Update URLs for backend service and low level network service accordingly to your network infrastructure:

a. edit file app\WhereIsApp.ts:
```
    packageValue: 'http://whereis-backend:8080/api/whereis',
```

b. edit file backend\src\main\java\pl\aszul\hot\rwb\config\AppConfig.java:
```
    public static final String ROCKET_CHAT_URL = "http://192.168.99.100:3000/";
    public static final String NETWORK_SERVICE_URL = "http://whereis-netsvc:8081/whereis/";
    public static final String BACKEND_HOST = "http://localhost:8080/";
```

c. edit file rocket.chat\create_users.sh:
```
rocketchat_url=http://192.168.99.100:3000
```

d. edit file app\deploy.sh:
```
HOST=http://192.168.99.100:3000/
```    
    
4. start RocketChat.Chat server in Docker container:
```
cd rocket.chat
docker-compose up
```

it should download all necessary containers:

![whereis docker compose up 1](images/whereis_docker_compose_up_1.png)


and start Rocket.Chat server:

![whereis docker compose up 2](images/whereis_docker_compose_up_2.png)


5. got to local Rocket.Chat webpage (http://192.168.99.100:3000/) and complete first run configuration procedure:

![whereis rocket.chat first run](images/whereis_rocket.chat_first_run.png)

![whereis rocket.chat first run finish](images/whereis_rocket.chat_first_run_finish.png)


6. create new roles (whereis, whereis-details, whereis-admin) using Rocket.Chat administration panel (options \ Administration \ Permissions \ New Role):

![whereis rocket.chat permissions](images/whereis_rocket.chat_permissions.png)

![whereis rocket.chat permissions_new_role](images/whereis_rocket.chat_permissions_new_role.png)


7. manually create few Rocket.Chat users for test and assign them some whereis roles:

![whereis rocket.chat permissions_add_role](images/whereis_rocket.chat_permissions_add_role.png)


8. create dummy users on Rocket.Chat server (if necessary, use chmod to add executable attribute to script file):
```
cd rocket.chat
./create_users.sh
```

![whereis_rocket.chat_added_users](images/whereis_rocket.chat_added_users.png)


9. enable apps support in Rocket.Chat (options \ Administration \ Apps \ Enable):

![whereis_rocket.chat_enable_apps](images/whereis_rocket.chat_enable_apps.png)

7. install WhereIs bot App (aka plugin) on Rocket.Chat server (if necessary, use chmod to add executable attribute to script file):
```
cd app
./deploy.sh docker install
```


8. activate WhereIs bot App after installation (options \ Administration \ Apps \ Installed \ WhereIs \ Activate):

![whereis_rocket.chat_app_activation](images/whereis_rocket.chat_app_activation.png)


## Starting service in development environment:

1. start RocketChat.Chat server in Docker container:
```
cd rocket.chat
docker-compose up
```

![whereis docker ready](images/whereis_docker_ready.png)


2. deploy current version of WhereIs bot to Rocket.Chat server:
```
cd app
./deploy.sh docker update
```

![whereis app deploy](images/whereis_app_deploy.png)


3. fire up backend service (set below environment variables if you need override default locations of maps and avatars files):
```
export LOCATION_MAPS_FOLDER=/home/rocket/whereis/maps
export USER_AVATARS_FOLDER=/home/rocket/whereis/avatars
cd backend
mvn spring-boot:run
```

![whereis backend startup](images/whereis_backend_startup.png)


4. fire up netsvc low level network mockup service (if necessary, use chmod to add executable attribute to script file):
```
cd netsvc
./start_netsvc.sh
```

5. Rocket.Chat should be available at address http://docker_url:3000/

6. WhereIs management web console should be available at address http://backend_url:8080/

## Provided developer scripts:

| Command                        | Description                                                                          |
| ------------------------------ | ------------------------------------------------------------------------------------ |
| `/rocket.chat/create_users.sh` | Automated creation of 32 dummy test users on Rocket.Chat server                      |
| `/app/deploy.sh`               | Unattended packaging and deployment of WhereIs App plugin to the Rocket.Chat server. |
| `/netsvc/start_netsvc.sh`      | Start low level network mockup server                                                |
| `/netsvc/test_user.sh`         | Test low level network mockup server in USER mode                                    |
| `/netsvc/test_ip.sh`           | Test low level network mockup server in IP address mode                              |
| `/netsvc/test_mac.sh`          | Test low level network mockup server in MAC address mode                             |


![whereis test user](images/whereis_test_user.png)

![whereis test ip](images/whereis_test_ip.png)

![whereis test mac](images/whereis_test_mac.png)


## Folders used for files storage

| Command          | Description                                                  |
| ---------------- | ------------------------------------------------------------ |
| `/maps/`         | Map images for locations served by backend service.          |
| `/avatars/`      | Avatars for Rocket.Chat users served by backend service.     |
| `/db/`           | Database files for backend service.                          |
| `/logs/`         | Log files for backend service.                               |
| `/logs/archive/` | Archived log files (from previous days) for backend service. |


## Remarks

- Security is now disabled because problems with JWT and Thymeleaf. Work in progress...
