package com.globettroter.ezequiel.globetrotter;

/**
 * Created by Ezequiel on 15/02/2016.
 */
public class FacebookDataConteiner {

    //private String url_picture;
    private String name;
    private String id;
    private String title;
    private String points;


    public FacebookDataConteiner( String name, String id, String title, String points) {
       // this.url_picture = url_picture;
        this.name = name;
        this.id = id;
        this.title=title;
        this.points=points;
    }


    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }
}
