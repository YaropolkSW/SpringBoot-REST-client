package com.spring.springboot.rest.springbootrestclientapplication.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.spring.springboot.rest.springbootrestclientapplication.dto.CoordinatePoint;
import com.spring.springboot.rest.springbootrestclientapplication.dto.Geojson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GeojsonDesirializer extends StdDeserializer<Geojson> {
    private final static String WRONG_DATA = "Wrong data type!";

    public GeojsonDesirializer() {
        this(null);
    }

    public GeojsonDesirializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Geojson deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        final JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        final String type = node.get("type").asText();
        final Iterator<JsonNode> coordinatesJsonArray = node.findValue("coordinates").iterator();
        final List<CoordinatePoint> listOfCoordinates;

        switch (type) {
            case "Point":
                listOfCoordinates = getCoordinatesFromPoint(coordinatesJsonArray);
                break;
            case "LineString":
                listOfCoordinates = getCoordinatesFromLineString(coordinatesJsonArray);
                break;
            case "Polygon":
                listOfCoordinates = getCoordinatesFromPolygon(coordinatesJsonArray);
                break;
            case "MultiPoint":
                listOfCoordinates = getCoordinatesFromMultipoint(coordinatesJsonArray);
                break;
            case "MultiLineString":
                listOfCoordinates = getCoordinatesFromMultiLineString(coordinatesJsonArray);
                break;
            case "MultiPolygon":
                listOfCoordinates = getCoordinatesFromMultiPolygon(coordinatesJsonArray);
                break;
            default:
                throw new IllegalStateException(WRONG_DATA);
        }
        final CoordinatePoint exactCoordinate = getExactCoordinate(listOfCoordinates);

        return Geojson.builder()
                .type(type)
                .coordinatePoint(exactCoordinate)
                .build();
    }

    private List<CoordinatePoint> getCoordinatesFromMultiPolygon(Iterator<JsonNode> coordinatesJsonArray) {
        final List<CoordinatePoint> points = new ArrayList<>();

        while (coordinatesJsonArray.hasNext()) {
            points.addAll(getCoordinatesFromPolygon(coordinatesJsonArray.next().iterator()));
        }

        return points;
    }

    private List<CoordinatePoint> getCoordinatesFromMultiLineString(Iterator<JsonNode> coordinatesJsonArray) {
        final List<CoordinatePoint> points = new ArrayList<>();

        while (coordinatesJsonArray.hasNext()) {
            points.addAll(getCoordinatesFromLineString(coordinatesJsonArray.next().iterator()));
        }

        return points;
    }

    private List<CoordinatePoint> getCoordinatesFromMultipoint(Iterator<JsonNode> coordinatesJsonArray) {
        final List<CoordinatePoint> points = new ArrayList<>();

        while (coordinatesJsonArray.hasNext()) {
            points.addAll(getCoordinatesFromPoint(coordinatesJsonArray.next().iterator()));
        }

        return points;
    }

    private List<CoordinatePoint> getCoordinatesFromPolygon(Iterator<JsonNode> coordinatesJsonArray) {
        final List<CoordinatePoint> points = new ArrayList<>();

        while (coordinatesJsonArray.hasNext()) {
            points.addAll(getCoordinatesFromLineString(coordinatesJsonArray.next().iterator()));
        }

        return points;
    }

    private List<CoordinatePoint> getCoordinatesFromLineString(Iterator<JsonNode> coordinatesJsonArray) {
        final List<CoordinatePoint> points = new ArrayList<>();

        while (coordinatesJsonArray.hasNext()) {
            points.addAll(getCoordinatesFromPoint(coordinatesJsonArray.next().iterator()));
        }

        return points;
    }

    private List<CoordinatePoint> getCoordinatesFromPoint(Iterator<JsonNode> coordinatesJsonArray) {
        final List<CoordinatePoint> points = new ArrayList<>();

        while (coordinatesJsonArray.hasNext()) {
            final CoordinatePoint point = CoordinatePoint.builder()
                    .longitude(coordinatesJsonArray.next().asDouble())
                    .latitude(coordinatesJsonArray.next().asDouble())
                    .build();

            points.add(point);
        }

        return points;
    }

    private CoordinatePoint getExactCoordinate(List<CoordinatePoint> listOfCoordinates) {
        double lon = 0;
        double lat = 0;

        for (CoordinatePoint point : listOfCoordinates) {
            lon += point.getLongitude();
            lat += point.getLatitude();
        }

        return CoordinatePoint.builder()
                .longitude((double) Math.round((lon / listOfCoordinates.size()) * 1000) / 1000)
                .latitude((double) Math.round((lat / listOfCoordinates.size()) * 1000) / 1000)
                .build();
    }
}
