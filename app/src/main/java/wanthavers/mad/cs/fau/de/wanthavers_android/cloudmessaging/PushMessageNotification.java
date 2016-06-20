package wanthavers.mad.cs.fau.de.wanthavers_android.cloudmessaging;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

@SuppressLint("ParcelCreator")
public class PushMessageNotification implements Parcelable{


    public String title;

    public String message;

    public PushMessageNotification(String title, String message) {
        this.title = title;
        this.message = message;
    }


    public PushMessageNotification(Parcel in){
        String[] data = new String[2];
        in.readStringArray(data);
        this.title = data[0];
        this.message = data[1];
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeStringArray(new String[] {this.title, this.message});
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