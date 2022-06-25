package dev.heinzl.simplessoproxy.configs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

import com.mongodb.Block;
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