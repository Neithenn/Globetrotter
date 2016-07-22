package com.globettroter.ezequiel.globetrotter;

/**
 * Created by Ezequiel on 29/02/2016.
 */
public class DataContainer_MyPlaces {

    private String name_place;
    private String id_google_place;
    private String geonameid;
    private String continent;
    private String country_name;
    private String city_name;
    private String date;
    private String points;

    public DataContainer_MyPlaces(String name_place, String id_google_place, String geonameid, String continent, String country_name, String city_name, String date, String points) {
        this.name_place = name_place;
        this.id_google_place = id_google_place;
        this.geonameid = geonameid;
        this.continent = continent;
        this.country_name = country_name;
        this.city_name = city_name;
        this.date = date;
        this.points = points;
    }


    public String getName_place() {
        return name_place;
    }

    public String getId_google_place() {
        return id_google_place;
    }

    public String getGeonameid() {
        return geonameid;
    }

    public String getContinent() {
        return continent;
    }

    public String getCountry_name() {
        return country_name;
    }

    public String getCity_name() {
        return city_name;
    }

    public String getDate() {
        return date;
    }

    public String getPoints() {
        return points;
    }
}
