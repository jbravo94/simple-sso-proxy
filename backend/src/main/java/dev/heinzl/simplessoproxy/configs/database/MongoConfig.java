package dev.heinzl.simplessoproxy.configs.database;

import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
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

    @Value("${spring.data.mongodb.authenticationDatabase}")
    String authenticationDatabase;

    @Value("${spring.data.mongodb.username}")
    String username;

    @Value("${spring.data.mongodb.password}")
    String password;

    @Value("${spring.data.mongodb.port}")
    int port;

    @Value("${spring.data.mongodb.database}")
    String database;

    @Value("${spring.data.mongodb.host}")
    String host;

    @Override
    protected String getDatabaseName() {
        return database;
    }

    @Override
    public MongoClient mongoClient() {

        MongoCredential credential = MongoCredential.createCredential(username, authenticationDatabase,
                password.toCharArray());
        Block<ClusterSettings.Builder> localhost = builder -> builder
                .hosts(Collections.singletonList(new ServerAddress(host, port)));
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