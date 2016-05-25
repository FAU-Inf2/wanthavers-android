package wanthavers.mad.cs.fau.de.wanthavers_android.chatlist;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.BitmapRegionDecoder;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.common.User;
import wanthavers.mad.cs.fau.de.wanthavers_android.BR;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;

public class ChatItemViewModel extends BaseObservable {


    User mUser;
    Desire mDesire;
    ImageView mProfilePic;

    public ChatItemViewModel() {

    }

    @Bindable
    public User getUser() { return mUser; }

    @Bindable
    public void setUser(User user){
        mUser = user;
        notifyPropertyChanged(BR.user);
    }

    @Bindable
    public Desire getDesire() { return mDesire; }

    @Bindable
    public void setDesire(Desire desire){
        mDesire = desire;
        notifyPropertyChanged(BR.desire);
    }

    @Bindable
    public ImageView getProfilePic(){return mProfilePic;}

    @Bindable
    public void setProfilePic(ImageView imageView){
        mProfilePic = imageView;
        notifyPropertyChanged(BR.profilePic);
    }

}