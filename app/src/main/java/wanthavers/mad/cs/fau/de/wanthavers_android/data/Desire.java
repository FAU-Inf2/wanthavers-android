package wanthavers.mad.cs.fau.de.wanthavers_android.data;

import android.support.annotation.Nullable;

public class Desire {

    private final String mId;

    @Nullable
    private final String mTitle;

    @Nullable
    private final String mDescription;

    private boolean completed = false;


    public Desire(String id, @Nullable String title, @Nullable String description){
        mId = id;
        mTitle = title;
        mDescription = description;


    }

    public String getId() { return mId; }

    @Nullable
    public String getTitle() { return mTitle; }

    @Nullable
    public String getDescription() { return mDescription;}

    public boolean isCompleted(){
        return this.completed;
    }
}
