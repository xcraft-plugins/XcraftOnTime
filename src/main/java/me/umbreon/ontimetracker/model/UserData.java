package me.umbreon.ontimetracker.model;

public class UserData {
    private long playTime;
    private String joinedDate;

    public UserData() {

    }

    public UserData(long playTime, String joinedDate){
        super();
        this.playTime = playTime;
        this.joinedDate = joinedDate;
    }

    public long getPlayTime(){
        return playTime;
    }

    public void setPlayTime(long playTime){
        this.playTime = playTime;
    }

    public String getJoinedDate(){
        return joinedDate;
    }

}
