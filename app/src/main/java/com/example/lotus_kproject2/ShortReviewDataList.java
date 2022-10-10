package com.example.lotus_kproject2;

public class ShortReviewDataList {
    private String writingId, movId, movName, userId, mbti, nickname, writing;
    Float star;

    public ShortReviewDataList(String writingId, String movId, String movName, String userId, String mbti, String nickname, String writing, String star) {
        this.writingId = writingId;
        this.movId = movId;
        this.movName = movName;
        this.userId = userId;
        this.mbti = mbti;
        this.nickname = nickname;
        this.writing = writing;
        this.star = Float.valueOf(star);
    }

    public String getMovName() {
        return movName;
    }

    public String getWriting() {
        return writing;
    }

    public String getNickname() {
        return nickname;
    }

    public String getMbti() {
        return mbti;
    }

    public double getStar() {
        return star;
    }
}


