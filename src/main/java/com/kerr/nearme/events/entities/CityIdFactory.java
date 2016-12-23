package com.kerr.nearme.events.entities;

/**
 * Created by allankerr on 2016-12-23.
 */
public final class CityIdFactory {

    private static final char DELIMITER = ';';

    static String create(String locality, String administrativeArea, String country) {
        StringBuilder builder = new StringBuilder();
        builder.append(locality);
        builder.append(DELIMITER);
        builder.append(administrativeArea);
        builder.append(DELIMITER);
        builder.append(country);
        return builder.toString();
    }
}
