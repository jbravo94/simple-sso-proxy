# Ideas

## Backend
* Store secret and skip login
* Encrypt Passwords to make in memory attacks difficulter
* Remove Crossorigin
* Create Validator for App before inserting into routes
* Get first basic auth token and then replace with proxy token in local storage or cookie with same core domain and several sub domains
* Answer with Futures or Flux
* Add CSRF
* Make adapter for inmemory credentials or persistent
* Implement Testbutton for scripts
* Store personal credentials
* Add URL pattern check
* NgRx
* default timeout session secrets
* Implement SSL Cert handling
* make in memory with redis
* Test setCredentials
* Add reactive mongo
* Add Sets Datastructure
* Backup Restore
* Custom Exceptions
* Fix multiple credentials for same user and app
* Integration testing
* Measuring Coverage
* Defensive Programming
* Apply Multithreading
* Implement Checkstyle

## Frontend
* Cypress Frontend e2e tests
* Login Animation
* Add forget password
* App login with multiple promises to create progressbar
* Add custom theming
* Add breadcrumbs
* Add Dialogs forms with pristine
* Adds App in sidebar
* Factory for service testing
* Confirmation for Deletion
* Top Left Toast with autoclose
* Change setting cookie via javascript to set cookie via HTTP Set-Cookie and maybe redirect
* Update scripting api

# Notes

## Backend
* Helper Command JAVA_HOME=/opt/java/jdk-17.0.3+7 mvn clean package -Dmaven.test.skip=true
* Same username / passwords needed for all apps
* Store Certificates in truststore
```
echo -n | openssl s_client -connect demo.mybahmni.org:443 | sed -ne '/-BEGIN CERTIFICATE-/,/-END CERTIFICATE-/p' > cert.pem
keytool -keystore truststore.jks -alias demo.mybahmni.org -import -file cert.pem
-Djavax.net.ssl.trustStore=truststore.jks -Djavax.net.ssl.trustStorePassword=changeit
```
* blocking used for better control in gatewayfilter instead of reactive calls
* Dev Tools > Application > Storage > Tick all options after "Clear Site Data" button > Clear Site Data
* Enable Profile via `-Dspring.profiles.active=dev`
* AspectJ Bytecode weaving needs package phase because of postprocessing after lombok
* Swagger UI: http://localhost:8084/swagger-ui/
* Jacoco directory => target/site/jacoco
* License Update: mvn license:format
* Javadoc: mvn javadoc:javadoc => target/site/apidocs
* Generate Ui Examples: mvn exec:java -Dexec.mainClass="dev.heinzl.simplessoproxy.utils.UiSuggestionGenerator" -Dexec.args="../frontend/src/app/scripting"
* docker exec f620b7d5a083 mongodump --username root --password example --authenticationDatabase admin --db test --out backup
* Caching example for future maybe
```
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
```
* Mongo DB Sharded Example for future maybe
```
version: '2'

services:
  mongodb-sharded:
    image: docker.io/bitnami/mongodb-sharded:5.0
    environment:
      - MONGODB_ADVERTISED_HOSTNAME=mongodb-sharded
      - MONGODB_SHARDING_MODE=mongos
      - MONGODB_CFG_PRIMARY_HOST=mongodb-cfg
      - MONGODB_CFG_REPLICA_SET_NAME=cfgreplicaset
      - MONGODB_REPLICA_SET_KEY=replicasetkey123
      - MONGODB_ROOT_PASSWORD=password123
    ports:
      - "27017:27017"

  mongodb-shard0:
    image: docker.io/bitnami/mongodb-sharded:5.0
    environment:
      - MONGODB_ADVERTISED_HOSTNAME=mongodb-shard0
      - MONGODB_SHARDING_MODE=shardsvr
      - MONGODB_MONGOS_HOST=mongodb-sharded
      - MONGODB_ROOT_PASSWORD=password123
      - MONGODB_REPLICA_SET_MODE=primary
      - MONGODB_REPLICA_SET_KEY=replicasetkey123
      - MONGODB_REPLICA_SET_NAME=shard0
    volumes:
      - 'shard0_data:/bitnami'

  mongodb-cfg:
    image: docker.io/bitnami/mongodb-sharded:5.0
    environment:
      - MONGODB_ADVERTISED_HOSTNAME=mongodb-cfg
      - MONGODB_SHARDING_MODE=configsvr
      - MONGODB_ROOT_PASSWORD=password123
      - MONGODB_REPLICA_SET_MODE=primary
      - MONGODB_REPLICA_SET_KEY=replicasetkey123
      - MONGODB_REPLICA_SET_NAME=cfgreplicaset
    volumes:
      - 'cfg_data:/bitnami'

volumes:
  shard0_data:
    driver: local
  cfg_data:
    driver: local

```

## Frontend

* npx license-check-and-add add -f license-check-and-add-config.json -r 2022 "Johannes HEINZL"

# Links

## Backend
https://www.baeldung.com/spring-security-5-reactive
https://github.com/eugenp/tutorials/tree/master/persistence-modules/spring-data-mongodb
https://ace.c9.io/
https://medium.com/bliblidotcom-techblog/spring-cloud-gateway-dynamic-routes-from-database-dc938c6665de
https://www.baeldung.com/spring-5-webclient
https://groovy-lang.org/integrating.html#_groovyscriptengine
http://httpbin.org:80
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
https://cloud.spring.io/spring-cloud-gateway/multi/multi__tls_ssl.html
https://stackoverflow.com/questions/47434877/how-to-generate-keystore-and-truststore
https://adambien.blog/roller/abien/entry/how_to_connect_to_an
https://nurkiewicz.com/2013/01/cacheable-overhead-in-spring.html
https://devtut.github.io/spring/caching-with-redis-using-spring-boot-for-mongodb.html#the-basic-system
https://www.baeldung.com/java-balanced-brackets-algorithm
https://www.guru99.com/positive-and-negative-testing.html
https://stackoverflow.com/questions/3555472/mockito-verify-method-arguments
https://medium.com/@KosteRico/spring-aop-in-2021-level-up-your-logging-8d1498242ba2
https://www.javainuse.com/spring/spring-boot-aop
https://github.com/peterhuba/maven-lombok-with-aspectj
https://www.baeldung.com/swagger-2-documentation-for-spring-rest-api
https://github.com/spring-projects/spring-boot/issues/384
https://www.baeldung.com/executable-jar-with-maven
https://maven.apache.org/plugins/maven-shade-plugin/examples/includes-excludes.html
https://stackoverflow.com/questions/37671125/how-to-configure-spring-security-to-allow-swagger-url-to-be-accessed-without-aut
https://www.baeldung.com/maven-java-main-method
https://www.baeldung.com/spring-boot-h2-database
https://rieckpil.de/mongodb-testcontainers-setup-for-datamongotest/

## Frontend
https://github.com/materiahq/ngx-monaco-editor
https://gist.github.com/Ruminat/42364312e90ba5d71875697ede66990e
https://css-tricks.com/quick-css-trick-how-to-center-an-object-exactly-in-the-center/
https://stackoverflow.com/questions/44593237/elevate-md-card-in-angular-material
https://stackoverflow.com/questions/35327929/angular-2-ngmodel-in-child-component-updates-parent-component-property
https://stormpath.com/blog/where-to-store-your-jwts-cookies-vs-html5-web-storage
https://stackoverflow.com/questions/6182315/how-can-i-do-base64-encoding-in-node-js
https://medium.com/angular-shots/shot-3-how-to-add-http-headers-to-every-request-in-angular-fab3d10edc26
https://gist.github.com/mwrouse/05d8c11cd3872c19c684bd1904a2202e
https://stackoverflow.com/questions/61217446/how-can-i-get-suggestions-registercompletionitemprovider-to-show-between-curly
https://codecraft.tv/courses/angular/unit-testing/routing/
https://juristr.com/blog/2021/02/common-chunk-lazy-loading-angular-cli/
https://ngrx.io/guide/store
https://stackoverflow.com/questions/66067320/running-mongorestore-on-docker-once-the-container-starts
https://stackoverflow.com/questions/46438659/correct-syntax-to-do-mongodump-of-mongodb-docker-instance
https://davejansen.com/how-to-dump-restore-a-mongodb-database-from-a-docker-container/
https://www.javainuse.com/spring/boot_swagger3
https://www.testcontainers.org/test_framework_integration/junit_5/

## Setup
https://hub.docker.com/r/bitnami/mongodb-sharded/
https://www.theserverside.com/blog/Coffee-Talk-Java-News-Stories-and-Opinions/Docker-Nginx-reverse-proxy-setup-example
https://unix.stackexchange.com/questions/236084/how-do-i-create-a-service-for-a-shell-script-so-i-can-start-and-stop-it-like-a-d
