package com.example.lotus_kproject2;

public class LongReviewDataList {
    private String writingId, movId, movName, title, userId, mbti, nickname, writing;

    public LongReviewDataList(String writingId, String movId, String movName, String title, String userId, String mbti, String nickname, String writing) {
        this.writingId = writingId;
        this.movId = movId;
        this.movName = movName;
        this.title = title;
        this.userId = userId;
        this.mbti = mbti;
        this.nickname = nickname;
        this.writing = writing;
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
}
