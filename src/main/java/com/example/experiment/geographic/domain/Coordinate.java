package com.example.experiment.geographic.domain;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PROTECTED)
@Entity
public class Coordinate {

    private static final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private Point point;

    private Coordinate(Point point) {
        this.point = point;
    }

    public static Coordinate of(double longitude, double latitude) {
        com.vividsolutions.jts.geom.Coordinate coordinate = new com.vividsolutions.jts.geom.Coordinate(longitude,
                latitude);
        Point point = geometryFactory.createPoint(coordinate);
        return new Coordinate(point);
    }
}
