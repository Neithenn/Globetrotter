package com.globettroter.ezequiel.globetrotter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ezequiel on 19/03/2016.
 */
public class Geoname {

    String toponymName;
    String name;
    String lat;
    String lng;
    String geonameId;
    String countryCode;
    String flc;
    String fcode;

    public static List<Geoname> WORLD = new ArrayList<>();

    public Geoname(String toponymName, String name, String lat, String lng, String geonameId, String countryCode, String flc, String fcode) {
        this.toponymName = toponymName;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.geonameId = geonameId;
        this.countryCode = countryCode;
        this.flc = flc;
        this.fcode = fcode;
    }



    public String getToponymName() {
        return toponymName;
    }


    public String getName() {
        return name;
    }


    public String getLat() {
        return lat;
    }


    public String getLng() {
        return lng;
    }


    public String getGeonameId() {
        return geonameId;
    }


    public String getCountryCode() {
        return countryCode;
    }


    public String getFlc() {
        return flc;
    }


    public String getFcode() {
        return fcode;
    }


}
