package com.kerr.nearme.events.entities;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/**
 * Created by allankerr on 2016-12-23.
 */
@Entity
public class City {

    @Id
    private String cityId;

    protected String locality;

    protected String administrativeArea;

    protected String country;

    public City(String locality, String administrativeArea, String country) {
        this.cityId = CityIdFactory.create(locality, administrativeArea, country);
        this.locality = locality;
        this.administrativeArea = administrativeArea;
        this.country = country;
    }
}
