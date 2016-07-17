package com.example.ian.project1spotifystreamerwindows;

/**
 * Created by chernyee on 7/2/2015.
 */
public class ArtistData {
    String name;
    String description;
    int image;
    String imageURL;
    String id;

    public ArtistData(String name, int image){
        this.name = name;
        this.image = image;
    }

    public ArtistData(String name, String imageURL, String id){
        this.name = name;
        this.imageURL = imageURL;
        this.id = id;
    }

    public ArtistData(String name, String imageURL) {
        this.name = name;
        this.imageURL = imageURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
