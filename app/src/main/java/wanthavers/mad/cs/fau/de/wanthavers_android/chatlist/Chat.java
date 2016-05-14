package wanthavers.mad.cs.fau.de.wanthavers_android.chatlist;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;


public class Chat {
    public static final String CHAT_ID = "chatId";


    private long id;


    private String title;


    private long user_id1;


    private long user_id2;


    private long desire_id;


    private Date creation_time;

    private int colorIndex;


    public Chat() {}

    public Chat(String title, long userId1, long userId2, long desireId, int colorIndex) {
        this.title = title;
        this.user_id1 = userId1;
        this.user_id2 = userId2;
        this.desire_id = desireId;
        this.colorIndex = colorIndex;
    }

    @JsonProperty
    public long getID() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @JsonProperty
    public long getUser1() {return this.user_id1;}

    public void setUser1(long userId) {this.user_id1 = userId;}

    @JsonProperty
    public long getUser2() { return this.user_id2;}

    public void setUser2(long userId) {this.user_id2 = userId;}

    @JsonProperty
    public long getDesireId() {
        return this.desire_id;
    }

    public void setDesireId(long desireId) { this.desire_id = desireId; }

    @JsonProperty
    public String getTitle() { return this.title;}

    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty
    public int getColorIndex() { return colorIndex; }

    public void setColorIndex(int colorIndex) { this.colorIndex = colorIndex; }

}
