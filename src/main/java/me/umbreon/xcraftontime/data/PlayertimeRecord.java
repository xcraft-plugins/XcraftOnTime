package me.umbreon.xcraftontime.data;

import java.util.UUID;

public class PlayertimeRecord {

    private String playername;
    private long playtime;
    private UUID uuid;

    public PlayertimeRecord(String playername, long playtime, UUID uuid) {
        this.playername = playername;
        this.playtime = playtime;
        this.uuid = uuid;
    }

    public String getPlayername() {
        return playername;
    }

    public long getPlaytime() {
        return playtime;
    }

    public UUID getUuid(){
        return uuid;
    }
}
