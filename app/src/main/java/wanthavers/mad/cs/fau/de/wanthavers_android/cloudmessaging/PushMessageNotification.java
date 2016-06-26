package wanthavers.mad.cs.fau.de.wanthavers_android.cloudmessaging;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

@SuppressLint("ParcelCreator")
public class PushMessageNotification implements Parcelable{

    public String mMessageNotificationType;
    public String message;
    public String mDesireId;
    public String mDesireTitle;
    public String mChatId;
    public String mSender;

    public PushMessageNotification(String messageNotificationType, String message) {
        this.mMessageNotificationType = messageNotificationType;
        this.message = message;
    }


    public PushMessageNotification(Parcel in){
        String[] data = new String[6];
        in.readStringArray(data);
        this.mMessageNotificationType = data[0];
        this.message = data[1];
        this.mDesireId = data[2];
        this.mDesireTitle = data[3];
        this.mChatId = data[4];
        this.mSender = data[5];
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeStringArray(new String[] {mMessageNotificationType,message, mDesireId, mDesireTitle, mChatId, mSender});
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public PushMessageNotification createFromParcel(Parcel in) {
            return new PushMessageNotification(in);
        }

        public PushMessageNotification[] newArray(int size) {
            return new PushMessageNotification[size];
        }
    };
}
