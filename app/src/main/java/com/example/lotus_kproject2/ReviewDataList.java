package com.example.lotus_kproject2;

public class ReviewDataList {
    private String writingId, movId, movName, title, userId, mbti, nickname, writing;
    private float star;
    private int likeNum;

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
    public ReviewDataList(String writingId, String movId, String movName, String userId, String mbti, String nickname, String writing,float star, int likeNum) {
        this.writingId = writingId;
        this.movId = movId;
        this.movName = movName;
        this.userId = userId;
        this.mbti = mbti;
        this.nickname = nickname;
        this.writing = writing;
        this.star = star;
        this.likeNum = likeNum;
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

    public int getLikeNum() {
        return likeNum;
    }
}
