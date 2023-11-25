package com.carlos.apijavafx;

public class Coctel {
    private String id;
    private String name;
    private String alcoholic;
    private String image;

    public Coctel(String id, String name, String alcoholic, String image) {
        this.id = id;
        this.name = name;

        this.alcoholic = alcoholic;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getAlcoholic() {
        return alcoholic;
    }

    public void setAlcoholic(String alcoholic) {
        this.alcoholic = alcoholic;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }




        @Override
        public String toString() {
            return "Id= " + id + " || Nombre= " + name + " || Alcholic = " + alcoholic;
        }
    }

