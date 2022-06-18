package dev.heinzl.simplessoproxy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

import com.mongodb.Block;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.connection.ClusterSettings;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Override
    protected String getDatabaseName() {
        return "test";
    }

    @Override
    public MongoClient mongoClient() {
        /*
         * ConnectionString connectionString = new
         * ConnectionString("mongodb://test2:test2@localhost:27017/test");
         * MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
         * .applyConnectionString(connectionString)
         * 
         * .build();
         * 
         * 
         * .credential(MongoCredential.createCredential(
         * "test2",
         * "test",
         * "test2".toCharArray()))
         * return MongoClients.create(mongoClientSettings);
         * 
         */

        /*
         * spring.data.mongodb.host=localhost
         * spring.data.mongodb.port=27017
         * spring.data.mongodb.authentication-database=admin
         * spring.data.mongodb.database=test
         * spring.data.mongodb.username=root
         * spring.data.mongodb.password=example
         */
        MongoCredential credential = MongoCredential.createCredential("root", "admin",
                "example".toCharArray());
        Block<ClusterSettings.Builder> localhost = builder -> builder
                .hosts(Collections.singletonList(new ServerAddress("localhost", 27017)));
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyToClusterSettings(localhost)
                .credential(credential)
                .build();
        return MongoClients.create(settings);

    }

    @Override
    public Collection getMappingBasePackages() {
        return Collections.singleton("dev.heinzl");
    }
}