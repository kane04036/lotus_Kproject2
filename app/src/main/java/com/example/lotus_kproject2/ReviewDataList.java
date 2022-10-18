package com.example.lotus_kproject2;

public class ReviewDataList {
    private String writingId, movId, movName, title, userId, mbti, nickname, writing;
    private float star;

    public ReviewDataList(String writingId, String movId, String movName, String title, String userId, String mbti, String nickname, String writing) {
        this.writingId = writingId;
        this.movId = movId;
        this.movName = movName;
        this.title = title;
        this.userId = userId;
        this.mbti = mbti;
        this.nickname = nickname;
        this.writing = writing;
    }
    public ReviewDataList(String writingId, String movId, String movName, String userId, String mbti, String nickname, String writing,float star) {
        this.writingId = writingId;
        this.movId = movId;
        this.movName = movName;
        this.userId = userId;
        this.mbti = mbti;
        this.nickname = nickname;
        this.writing = writing;
        this.star = star;
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

    public Float getStar(){return star;}
}
