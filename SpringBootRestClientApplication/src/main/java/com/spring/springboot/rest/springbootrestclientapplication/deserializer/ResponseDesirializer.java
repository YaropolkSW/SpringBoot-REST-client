package com.spring.springboot.rest.springbootrestclientapplication.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.spring.springboot.rest.springbootrestclientapplication.entity.Response;

import java.io.IOException;

public class ResponseDesirializer extends StdDeserializer<Response> {
    public ResponseDesirializer() {
        this(null);
    }

    public ResponseDesirializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Response deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        final Response response = new Response();
        final JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        final String name = node.get("display_name").asText();
        final double longitude =  node.get("lon").asDouble();
        final double latitude = node.get("lat").asDouble();
        response.setName(name);
        response.setLongitude(longitude);
        response.setLatitude(latitude);
        return response;
    }
}
