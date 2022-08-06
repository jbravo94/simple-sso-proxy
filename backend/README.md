# Simple SSO Proxy

## Introduction

The main idea is to enable single sign on (SSO) of different applications as they are in their default configuration.

It is clear that there exist better technologies like OIDC or SAML for proper SSO,
therefore this project rather demonstrates the successful application of different technologies.

Nevertheless, this project can enable SSO for different application where there is no possibility for modern 
SSO technologies like OIDC or SAML and the possibility to store passwords in the browser is not an option.

The proxy application is composed of a custom user interface and backend plus several services wired together emphasizing microservice architecture. 

The core concept is that this project is a proxy for all requests to the targeted applications.

SSO is performed via a central login which retrieves the targeted application secrets and modifies subsequent requests
with the authentication parameters.

## Assumptions

* Username and Password is the same on all applications including the proxy itself
* The secret timeout (e.g. session cookie) is the same on all applications including the proxy

## Dataflow

* The user enters the username and the password
* The password is stored temorarily within the proxy application
* The proxy intercepts the HTTP requests and applies custom groovy scripts created in the proxy with its UI
* The proxy logs into the targeted applications and stores the secrets
* The proxy modifies subsequent requests and applies authentication which enables SSO

## User Interface

As user interface serves the purpose to present a central login screen and the capability to configure targeted applications.
I consists of a login screen, applications dashboard for quick links, the menu to list and modify applications.
Furthermore it has a scripting API which offers the capability to write scripts which can intercept http traffic as gateway filters.
These scripts need to be created via reverse engineering the login process with browser developer tools.

### Applied Technologies

* Angular, HTML
* JavaScript, Typescript
* Material Design, Flex, CSS
* Angular Link router
* JWT for authentication and authorization (user and roles)
* Monaco Editor from VS Code

### Applied Knowledge

* Component based architecture
* Dependency Injection
* Basic Navigation (sidebar, header, grid layout)
* Responsive Design emphasized
* Role based authentication (RBAC) with Angular Guards
* HTTP Cookies as proxy token

## Backend

### Applied Technologies

* Spring Boot inclusive Webflux and Cloud Gateway modules
* Maven as dependency management and build automation
* Integration of a groovy scripting engine
* MongoDB ORM Mapper
* HTTP Filters
* JWT Token
* Spring Security
* Spring AOP and AspectJ

### Applied Knowledge

* Reactive programming for regular REST endpoints
* HTTP request modification (method, header, cookie, body, etc.)
* Private / Public Cryptography with SSL
* Generics for Validator
* Mocks, Unit and Integration tests
* Positive and negative testing
* Directly implemented programming pattern 
    * Strategy - to select in-memory or persistent credentials repository
    * Decorator - decorates the credentials repository to apply different handling for secrets
    * Factory - to create scripting API objects
    * Facade - to create single point of access for repositories in scripts
    * Singleton - to validate App POJOs
    * Visitor - used to validate
* Indirectly used programming pattern
    * Builder - provided via Lombok for POJOs
    * Dependency Injection - via Spring Boot
    * Observer - via Webflux
    * Proxy - via automatic implementation of Spring repositories
* Datastructures
    * HashMap
    * List
    * Deque

## Build automation

The backend build automation is performed via Maven.
The frontend build automation is performed via NodeJS.
Each the artifacts of each service (frontend, backend) are bundled within Docker container.

### Applied Technologies

* Maven
* NPM
* Docker

### Applied Knowledge

* Build automation and DevOps
* Containerization

## Setup

The setup encourages microservice architecture.
Therefore the frontend and the backend are built as independent containers in order to enable indiviual devops.

The architecture consists of 3 tiers.

The client tier as GUI provided by this project.
The application tier as business logic provided by this project.
The data tier provided via MongoDB.

Since the GUI (presentation layer) and the application tier (proxy and logic) must be wired together also for the client,
nginx is used as reverse proxy.

In order to route the HTTP requests accordingly to each application, DNS unique subdomain entries should be created, 
pointing at the proxy service. (Hosts entries serve the purpose while testing)

The whole environment is set up in a declarative way via docker-compose which describes the desired state.

### Applied Technologies

* Docker and docker-compose

### Applied Knowledge

* 3 Tier Architecture
* Reverse Proxying
* Basic Networking and Domain Names
* Microservice Architecture
* Declarative Programming

# Next Steps

* Store secret and skip login
* Timeout
* Login Animation

# TODO

* Fix multiple credentials for same user and app
* Exception Handling
* UML Flow
* Cypress Frontend e2e tests
* Unit tests
* Integration testing
* Measuring Coverage
* CORS
* Backup Restore
* O(n)
* Defensive Programming
* (Multithreading)

* Sets

# Notes

https://github.com/eugenp/tutorials/tree/master/persistence-modules/spring-data-mongodb
https://ace.c9.io/

https://medium.com/bliblidotcom-techblog/spring-cloud-gateway-dynamic-routes-from-database-dc938c6665de
https://www.baeldung.com/spring-5-webclient
https://groovy-lang.org/integrating.html#_groovyscriptengine
http://httpbin.org:80

spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017
spring.data.mongodb.authentication-database=admin
spring.data.mongodb.database=test
spring.data.mongodb.username=root
spring.data.mongodb.password=example

* Encrypt Passwords to make in memory attacks difficulter
* Remove Crossorigin

* Create Validator for App before inserting into routes

* Get first basic auth token and then replace with proxy token in local storage or cookie with same core domain and several sub domains

https://www.baeldung.com/spring-security-5-reactive

* Answer with Futures or Flux

CSRF / AspectJ / Bytecode weaving

https://www.stackhawk.com/blog/angular-csrf-protection-guide-examples-and-how-to-enable-it/#:~:text=Cross%2Dsite%20request%20forgery%20(also,unwanted%20actions%20for%20application%20users.
https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter
https://github.com/eugenp/tutorials/blob/master/spring-security-modules/spring-security-web-mvc-custom/src/main/java/com/baeldung/web/controller/LoginController.java

https://www.baeldung.com/spring-security-5-reactive
https://www.baeldung.com/spring-security-authenticationmanagerresolver
https://docs.spring.io/spring-security/site/docs/5.2.5.RELEASE/reference/html/protection-against-exploits-2.html
https://docs.spring.io/spring-security/site/docs/5.2.5.RELEASE/reference/html/protection-against-exploits-2.html
https://www.naturalprogrammer.com/courses/332639/lectures/5902512
https://medium.com/zero-equals-false/protect-rest-apis-with-spring-security-reactive-and-jwt-7b209a0510f1
https://haris-zujo.medium.com/spring-cloud-gateway-request-filtering-and-redirection-9e4b6d559d1a
https://runebook.dev/de/docs/http/headers/set-cookie#:~:text=Set%2DCookie-,Der%20Set%2DCookie%20HTTP%2DAntwortheader%20wird%20verwendet%2C%20um%20ein,in%20derselben%20Antwort%20gesendet%20werden.
https://groovy-lang.org/closures.html#closure-as-object
https://docs.spring.io/spring-security/site/docs/5.2.0.RELEASE/reference/html/jc-erms.html
https://stackoverflow.com/questions/51315378/reactivesecuritycontextholder-getcontext-is-empty-but-authenticationprincipal
https://stackoverflow.com/questions/50015711/spring-security-webflux-body-with-authentication
https://medium.com/javarevisited/spring-webflux-meets-passwordless-authentication-a4be6cab6bfe
https://stackoverflow.com/questions/70050719/how-to-get-current-user-from-reactivesecuritycontextholder-in-webflux
https://www.baeldung.com/java-9-http-client

Make adapter for inmemory credentials or persistent
Implement Testbutton for scripts
store personal credentials

Add URL pattern check

JAVA_HOME=/opt/java/jdk-17.0.3+7 mvn compile

RXnx
unit tests

Reactive
HTTP
Java
Angular

openmrs/ws/rest/v1/location?operator=ALL&s=byTags&tags=Login+Location&v=custom:(name,uuid)

Same username / passwords
subdomains
same cookielength
same baseurl for apps
intercept redirects

change url to baseurl from app
ssl cert
default timeout session secrets
do not store username
dev profile

echo -n | openssl s_client -connect demo.mybahmni.org:443 | sed -ne '/-BEGIN CERTIFICATE-/,/-END CERTIFICATE-/p' > cert.pem
keytool -keystore truststore.jks -alias demo.mybahmni.org -import -file cert.pem

-Djavax.net.ssl.trustStore=truststore.jks -Djavax.net.ssl.trustStorePassword=changeit
-Dspring.profiles.active=dev

make in memory with redis

https://cloud.spring.io/spring-cloud-gateway/multi/multi__tls_ssl.html
https://stackoverflow.com/questions/47434877/how-to-generate-keystore-and-truststore
https://adambien.blog/roller/abien/entry/how_to_connect_to_an
https://nurkiewicz.com/2013/01/cacheable-overhead-in-spring.html
https://devtut.github.io/spring/caching-with-redis-using-spring-boot-for-mongodb.html#the-basic-system

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        final SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(Arrays.asList(new ConcurrentMapCache("users"), new ConcurrentMapCache("apps"),
                new ConcurrentMapCache("credentials")));
        return cacheManager;
    }
}

@SpringBootTest
class SimpleSsoProxyApplicationTests {

	@Test
	void contextLoads() {
	}

}

https://www.baeldung.com/java-balanced-brackets-algorithm

blocking used for better control in gatewayfilter instead of reactive calls
https://www.guru99.com/positive-and-negative-testing.html
https://stackoverflow.com/questions/3555472/mockito-verify-method-arguments

Dev Tools > Application > Storage > Tick all options after "Clear Site Data" button > Clear Site Data

Test setCredentials


aspect
profile

-Dspring.profiles.active=dev

https://medium.com/@KosteRico/spring-aop-in-2021-level-up-your-logging-8d1498242ba2
https://www.javainuse.com/spring/spring-boot-aop
https://github.com/peterhuba/maven-lombok-with-aspectj

AspectJ Bytecode weaving needs package phase because of postprocessing after lombok

JAVA_HOME=/opt/java/jdk-17.0.3+7 mvn clean package -Dmaven.test.skip=true
