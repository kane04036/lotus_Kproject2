package com.example.lotus_kproject2;

public class MovieDataList {
    private String movCode, movName, movImg, releaseDate;

    public MovieDataList(String movCode, String movName, String movImg, String releaseDate) {
        this.movCode = movCode;
        this.movName = movName;
        this.movImg = movImg;
        this.releaseDate = releaseDate;
    }

    public String getMovCode() {
        return movCode;
    }

    public String getMovName() {
        return movName;
    }

    public String getMovImg() {
        return movImg;
    }

    public String getReleaseDate() {
        return releaseDate;
    }
}
