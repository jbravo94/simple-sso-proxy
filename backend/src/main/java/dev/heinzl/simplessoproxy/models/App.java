package dev.heinzl.simplessoproxy.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

@Document
@Data
@Builder
public class App {

    @Id
    private String id;
    private String name;
    private String baseUrl;
    private String loginScript;
    private String logoutScript;
    private String proxyScript;
    private String resetScript;

}