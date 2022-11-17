package com.example.lotus_kproject2;

public class UserDataList {
    String nickname, mbti, userId, isFollow;
    public UserDataList(String nickname, String mbti, String userId,String isFollow){
        this.userId = userId;
        this.mbti = mbti;
        this.nickname = nickname;
        this.isFollow = isFollow;
    }
    public UserDataList(String nickname, String mbti, String userId){
        this.userId = userId;
        this.mbti = mbti;
        this.nickname = nickname;
    }

    public String getUserId() {
        return userId;
    }

    public String getIsFollow() {
        return isFollow;
    }

    public String getNickname() {
        return nickname;
    }

    public String getMbti() {
        return mbti;
    }
}

