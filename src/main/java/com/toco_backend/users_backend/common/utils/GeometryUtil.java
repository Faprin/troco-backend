package com.toco_backend.users_backend.common.utils;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;

public class GeometryUtil {

    public static final int SRID = 4326; // WGS 84
    public static final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), SRID);

    public static Point createPoint(Double latitude, Double longitude) {
        if (latitude == null || longitude == null) {
            return null;
        }
        return geometryFactory.createPoint(new Coordinate(longitude, latitude));
    }
}
