package com.globettroter.ezequiel.globetrotter;

/**
 * Created by Ezequiel on 15/02/2016.
 */
public class FacebookDataConteiner {

    private String url_picture;
    private String name;
    private String id;


    public FacebookDataConteiner(String url_picture, String name, String id) {
        this.url_picture = url_picture;
        this.name = name;
        this.id = id;
    }

    public String getUrl_picture() {
        return url_picture;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setUrl_picture(String url_picture) {
        this.url_picture = url_picture;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }
}
