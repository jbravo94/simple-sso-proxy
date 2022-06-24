package dev.heinzl.simplessoproxy.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NonNull;

@Document
@Data
public class Credential {

    @Id
    private String id;
    @NonNull
    private String name;
    @NonNull
    private Integer age;

}