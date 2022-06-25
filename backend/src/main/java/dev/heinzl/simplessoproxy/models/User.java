package dev.heinzl.simplessoproxy.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NonNull;

@Document
@Data
public class User {

    @Id
    private String id;
    @NonNull
    private String username;
    @NonNull
    private String password;

}