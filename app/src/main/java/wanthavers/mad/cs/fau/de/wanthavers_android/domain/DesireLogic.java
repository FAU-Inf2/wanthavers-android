package wanthavers.mad.cs.fau.de.wanthavers_android.domain;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.databinding.Bindable;
import android.text.format.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.SharedPreferencesHelper;

public class DesireLogic {

    Context mContext;

    public DesireLogic(Context context){
        mContext = context;
    }

    public int getBackgroundColor(int colorIndex){
        Resources res = mContext.getResources();
        TypedArray bgColors = res.obtainTypedArray(R.array.desireBackgroundColors);

        int color = bgColors.getColor(colorIndex,0);
        return  color;
    }

    public String getDateString(Date date) {

        String dateString = mContext.getString(R.string.no_time_available);



        Date curDate = new Date();

        if(date != null) {
            return (String) DateUtils.getRelativeTimeSpanString(date.getTime(),curDate.getTime(), DateUtils.MINUTE_IN_MILLIS);
        }


        return dateString;
    }


    public String getPriceString(double price){

        String priceString = String.valueOf( (int) price);

        Resources resources = mContext.getApplicationContext().getResources();

        Locale current = resources.getConfiguration().locale;

        if(current.getCountry().equals("DE")){

        }
        String currency = "";
        String curCountry = current.getCountry();

        switch (curCountry){
            case "DE": currency = resources.getString(R.string.euro_sign);
                break;
            case "US": currency = resources.getString(R.string.dollar_sign);
                break;
            default: currency = resources.getString(R.string.euro_sign);
        }

        priceString += " " + currency;

        return priceString;
    }

    public long getLoggedInUserId() {
        SharedPreferencesHelper sharedPreferencesHelper = SharedPreferencesHelper.getInstance(SharedPreferencesHelper.NAME_USER, mContext);
        long loggedInUser = sharedPreferencesHelper.loadLong(SharedPreferencesHelper.KEY_USERID, 6L);
        return loggedInUser;
    }

    public boolean isDesireCreator(long creatorId) {
        if (creatorId == getLoggedInUserId()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean acceptedDesire() {
        //isCreator
        //isHaver
        return false;
    }
}
