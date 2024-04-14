package com.api.books.services.models.entities;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class DTOCosmicEntity {

    private Map<String, String > URIs = new HashMap<>();

    public void newURI(String title, String URI) {
        URIs.put(title, URI);
    }
}
