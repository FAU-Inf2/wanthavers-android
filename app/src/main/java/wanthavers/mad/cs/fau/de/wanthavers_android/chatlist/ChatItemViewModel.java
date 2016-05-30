package wanthavers.mad.cs.fau.de.wanthavers_android.chatlist;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.BitmapRegionDecoder;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import de.fau.cs.mad.wanthavers.common.Chat;
import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.common.User;
import wanthavers.mad.cs.fau.de.wanthavers_android.BR;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;

public class ChatItemViewModel extends BaseObservable {

    long mLoggedInUserId;

    @Bindable
    Chat mChat;

    @Bindable
    User mUser1;

    @Bindable
    User mUser2;

    @Bindable
    Desire mDesire;

    @Bindable
    ImageView mProfilePic;

    public ChatItemViewModel(Chat chat, long loggedInUserId) {
        mLoggedInUserId = loggedInUserId;
        mUser1 = chat.getUserObject1();
        mUser2 = chat.getUserObject2();
        mDesire = chat.getDesire();
        mChat = chat;
    }

    public User getUser1() { return mUser1; }


    public void setUser1(User user){
        mUser1 = user;
        notifyPropertyChanged(BR.user1);
    }

    public User getUser2() { return mUser2; }

    public void setUser2(User user){
        mUser2 = user;
        notifyPropertyChanged(BR.user2);
    }

    public Desire getDesire() { return mDesire; }

    public void setDesire(Desire desire){
        mDesire = desire;
        notifyPropertyChanged(BR.desire);
    }

    public ImageView getProfilePic(){return mProfilePic;}

    public void setProfilePic(ImageView imageView){
        mProfilePic = imageView;
        notifyPropertyChanged(BR.profilePic);
    }

    public Chat getChat() { return mChat; }

    public void setChat(Chat chat){
        mChat = chat;
        notifyPropertyChanged(BR.chat);
    }

    @Bindable
    public User getOtherUser(){

        if(mUser1.getID() == mLoggedInUserId){
            return mUser2;
        }

        return mUser1;
    }
}