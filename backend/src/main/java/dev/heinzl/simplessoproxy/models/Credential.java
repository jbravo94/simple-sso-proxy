package dev.heinzl.simplessoproxy.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import lombok.Builder;
import lombok.Data;
import lombok.With;

@Document
@Data
@Builder
public class Credential {

    @Id
    private String id;

    @DocumentReference(lazy = true)
    private User user;

    @DocumentReference(lazy = true)
    private App app;

    @With
    private String secret;

}