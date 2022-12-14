package com.example.lotus_kproject2;

import java.util.ArrayList;

public class ReviewDataList {
    private String writingId, movId, movName, title, userId, mbti, nickname, writing,isLike;
    private float star;
    private Integer likeNum;
    private MovieDataList movieData;

    public ReviewDataList(String writingId, String movId, String movName, String title, String userId, String mbti, String nickname, String writing, Integer likeNum,String isLike) {
        this.writingId = writingId;
        this.movId = movId;
        this.movName = movName;
        this.title = title;
        this.userId = userId;
        this.mbti = mbti;
        this.nickname = nickname;
        this.writing = writing;
        this.likeNum = likeNum;
        this.isLike = isLike;
    }
    public ReviewDataList(String writingId, String movId, String movName, String title, String userId, String mbti, String nickname, String writing, Integer likeNum, MovieDataList movieData, String isLike) {
        this.writingId = writingId;
        this.movId = movId;
        this.movName = movName;
        this.title = title;
        this.userId = userId;
        this.mbti = mbti;
        this.nickname = nickname;
        this.writing = writing;
        this.likeNum = likeNum;
        this.movieData = movieData;
        this.isLike = isLike;
    }

    public ReviewDataList(String writingId, String movId, String movName, String userId, String mbti, String nickname, String writing,float star, Integer likeNum,String isLike) {
        this.writingId = writingId;
        this.movId = movId;
        this.movName = movName;
        this.userId = userId;
        this.mbti = mbti;
        this.nickname = nickname;
        this.writing = writing;
        this.star = star;
        this.likeNum = likeNum;
        this.isLike = isLike;
    }

    public ReviewDataList(String writingId, String movId, String movName, String userId, String mbti, String nickname,
                          String writing, float star, Integer likeNum, MovieDataList movieData,String isLike) {
        this.writingId = writingId;
        this.movId = movId;
        this.movName = movName;
        this.userId = userId;
        this.mbti = mbti;
        this.nickname = nickname;
        this.writing = writing;
        this.star = star;
        this.likeNum = likeNum;
        this.movieData = movieData;
        this.isLike = isLike;
    }


    public String getTitle() {
        return title;
    }

    public String getMbti() {
        return mbti;
    }

    public String getNickname() {
        return nickname;
    }

    public String getWriting() {
        return writing;
    }

    public String getMovName() {
        return movName;
    }

    public String getWritingId() { return writingId; }

    public String getUserId() { return userId; }

    public Float getStar() {return star;}

    public Integer getLikeNum() {
        return likeNum;
    }

    public MovieDataList getMovieData() {
        return movieData;
    }

    public String getIsLike() {
        return isLike;
    }

    public String getMovId() {
        return movId;
    }

    public void setIsLike(String isLike) {
        this.isLike = isLike;
    }

    public void setLikeNum(Integer likeNum){
        this.likeNum = likeNum;
    }
}
