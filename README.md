# Simple SSO Proxy

## Introduction

[![Simple SSO Proxy](misc/preview.png?raw=true)](https://youtu.be/3G6M-R20uFk "Simple SSO Proxy")

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

![Dataflow](misc/dataflow.png?raw=true "Dataflow")

```
@startuml
User -> Proxy: Enters the username and the password
Proxy -> Proxy: Stores password temorarily
Proxy -> Application: Logs into the targeted applications 
Proxy -> Proxy: Optionally stores application secrets
User -> Application: Proxy intercepts the HTTP requests / responses and \n\
applies custom groovy scripts created in the proxy with its UI. \n\
This enables authentication which enables SSO
@enduml
```

## Architecture

![Architecture](misc/architecture.png?raw=true "Architecture")

```
@startuml
[Reverse Proxy] as ReverseProxy
[Frontend] as Frontend
[Backend] as Backend
[Database] as Database

User -> ReverseProxy
ReverseProxy --> Frontend
ReverseProxy --> Backend
Backend --> Database
@enduml
```

## User Interface

As user interface serves the purpose to present a central login screen and the capability to configure targeted applications.
I consists of a login screen, applications dashboard for quick links, the menu to list and modify applications.
Furthermore it has a scripting API which offers the capability to write scripts which can intercept http traffic as gateway filters.
These scripts need to be created via reverse engineering the login process with browser developer tools.

### Applied Technologies

* Angular, HTML
* JavaScript, Typescript
* Material Design, Flex, CSS
* Angular Link Router and Modules
* JWT for authentication and authorization (user and roles)
* Monaco Editor from VS Code

### Applied Knowledge

* Component based architecture
* Dependency Injection
* Basic Navigation (sidebar, header, grid layout)
* Responsive Design emphasized
* Role based authentication (RBAC) with Angular Guards
* HTTP Cookies as proxy token

### Testing

This sections shows the result of the applied unit testing.

![Frontend Coverage Overview](misc/frontend-coverage-overview.png?raw=true "Frontend Coverage Overview")

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

### Testing

This sections shows the results of the applied unit testing.

![Backend Coverage Overview](misc/backend-coverage-overview.png?raw=true "Backend Coverage Overview")
![Backend Coverage](misc/backend-coverage.png?raw=true "Backend Coverage")

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

# Open TODOs

* Add Video
* Finalize Docker-compose
* Add example for setup
