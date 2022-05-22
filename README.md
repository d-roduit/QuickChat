<div align="center">
<h1 align="center">
   QuickChat
</h1>

<p align="center">
    <strong>QuickChat is an anonymous chat website to easily discuss on any topic you want without storing any message.<br/>What happens on QuickChat stays on QuickChat 😉</strong>
</p>

</div>

<div align="center">
    <a href="https://github.com/d-roduit/QuickChat">
        <img src="demo_screenshots/homescreen.png" align="center" width="80%" alt="QuickChat homescreen screenshot">
       <br/>
       <br/>
        <img src="demo_screenshots/chat.png" align="center" width="80%" alt="QuickChat chat screenshot">
    </a>
</div>
<br>

## Table of Contents

1. [Getting Started](#getting-started)
2. [Technologies](#technologies)
3. [Guides and resources](#guides-and-resources)
4. [Authors](#authors)
5. [License](#license)

## <a name="getting-started"></a>Getting Started

### Running the website

You will need to follow the steps below in order to run the website :

1. Install the Java runtime on your computer (if not already done). JDK17 or higher is required.
2. Download the project files to your computer and open the project in your prefered IDE (Eclipse, IntelliJ IDEA, etc.)
3. Configure the database connection information by adding the following values to the project's environment variables:
   - `SPRING_DATASOURCE_URL=jdbc:postgresql://<host>:<port>/<database_name>`
   - `SPRING_DATASOURCE_USERNAME=<username>`
   - `SPRING_DATASOURCE_PASSWORD=<password>`

   _N. B. : The project is ready to be used with PostgreSQL, but you can of course use another database. Just change the database and database driver values in the `application.properties` file, download the driver for your database via Maven and set the SPRING_DATASOURCE_URL environment variable to the correct URL. If needed, more info on [HowToDoInJava - Spring Boot DataSource Configuration](https://howtodoinjava.com/spring-boot2/datasource-configuration/)._
4. Run the website via the IDE (it will provide you with the website URL, something like `http://localhost:8080`)

Once the 4 steps have been done, **you are ready to chat with your friends via QuickChat!**

## <a name="technologies"></a>Technologies

The main language used in this project is Java (JDK version 17) and uses the Spring framwork to develop the web application.

The front-end uses :
- Bootstrap
- SockJS _(for communication over websockets)_

The back-end uses :
- Thymeleaf _(Java template engine)_
- PostgreSQL

Moreover, the software project management used is Maven.

You can find all the links to the technologies used in the [Guides and resources](#guides-and-resources) section

## <a name="guides-and-resources"></a>Guides and resources
* [Spring](https://spring.io/) - Development framework
* [Apache Tomcat](http://tomcat.apache.org/) - Web server
* [Thymeleaf](https://www.thymeleaf.org/) - Server-side Java template engine
* [Bootstrap](https://getbootstrap.com/) - Front-End toolkit
* [SockJS](https://github.com/sockjs/sockjs-client) - WebSocket client library
* [JUnit](https://junit.org/) - Java testing framework
* [PostgreSQL](https://www.postgresql.org/) - Database
* [Maven](https://maven.apache.org/) - Software project management tool

## <a name="authors"></a>Authors

<table>
   <tbody>
      <tr>
         <td align="center" valign="top" width="100%">
            <a href="https://github.com/d-roduit">
            <img src="https://github.com/d-roduit.png?s=75" width="75" height="75"><br />
            Daniel Roduit
            </a>
         </td>
      </tr>
   </tbody>
</table>

## <a name="license"></a>License

This project is licensed under the MIT License
