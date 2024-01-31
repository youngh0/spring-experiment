package com.example.experiment.geographic.domain;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

@NoArgsConstructor(access = PROTECTED)
@Getter
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

        org.locationtech.jts.geom.Coordinate coordinate = new org.locationtech.jts.geom.Coordinate(longitude,
                latitude);
        Point point = geometryFactory.createPoint(coordinate);
        return new Coordinate(point);
    }
}
