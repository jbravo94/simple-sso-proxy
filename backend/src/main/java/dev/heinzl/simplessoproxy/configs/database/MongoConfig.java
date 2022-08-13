/*
 * The MIT License
 * Copyright © 2022 Johannes HEINZL
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package dev.heinzl.simplessoproxy.configs.database;

import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

import com.mongodb.Block;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.connection.ClusterSettings;

@Profile("!h2")
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