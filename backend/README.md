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

https://cloud.spring.io/spring-cloud-gateway/multi/multi__tls_ssl.html
https://stackoverflow.com/questions/47434877/how-to-generate-keystore-and-truststore
https://adambien.blog/roller/abien/entry/how_to_connect_to_an
