package wanthavers.mad.cs.fau.de.wanthavers_android.domain;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.databinding.Bindable;
import android.text.format.DateUtils;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.common.DesireStatus;
import de.fau.cs.mad.wanthavers.common.Haver;
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

    public String getAbsoluteDateString(Date date) {
        DateFormat df = DateFormat.getDateInstance();
        return df.format(date);
    }


    public String getIsoCurrency(String unicode){
        String iso;
        switch(unicode){
            case "\u20AC":
                iso = "EUR";
                break;
            case "\u0024":
                iso = "USD";
                break;
            case "\u00A3":
                iso = "GBP";
                break;
            case "CHF":
                iso = "CHF";
                break;
            default: iso = "EUR";
        }
        return iso;
    }


    public String getCombinedPriceString(Desire desire){
        return getPriceString(desire, desire.getPrice() + desire.getReward());
    }

    public String getCurrencyString(String desireCurrency) {
        Resources resources = mContext.getResources();
        if (desireCurrency == null) {
            return resources.getString(R.string.euro_sign);
        }
        switch (desireCurrency){
            case "EUR":
                return resources.getString(R.string.euro_sign);
            case "USD":
                return resources.getString(R.string.dollar_sign);
            case "GBP":
                return resources.getString(R.string.pound_sign);
            case "CHF":
                return "CHF";
            default:
                return resources.getString(R.string.euro_sign);
        }
    }

    public String getPriceString(Desire desire, double price){

        String priceString = String.valueOf( (int) price);
        String currency = "";
        String desireCurrency = desire.getCurrency();

        currency = getCurrencyString(desireCurrency);

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

    public boolean isDesireInProgress(Desire desire) {
        return (desire.getStatus() == DesireStatus.STATUS_IN_PROGRESS);
    }

    public boolean isDesireFinished(Desire desire) {
        return (desire.getStatus() == DesireStatus.STATUS_DONE);
    }

    //True if desire not deleted or expired!!!
    public boolean isDesireInRegularProcess(Desire desire) {
        return (!isDesireDeleted(desire) && !isDesireExpired(desire));
    }

    public boolean isDesireDeleted(Desire desire) {
        return (desire.getStatus() == DesireStatus.STATUS_DELETED);
    }

    public boolean isDesireExpired(Desire desire) {
        return (desire.getStatus() == DesireStatus.STATUS_EXPIRED);
    }

    public boolean showPrice(Desire desire) {
        return (isDesireInRegularProcess(desire) && !desire.isBiddingAllowed());
    }

    public boolean showBidding(Desire desire) {
        return (isDesireInRegularProcess(desire) && desire.isBiddingAllowed());
    }

    public boolean modifyBidAllowed(Desire desire, Haver haver) {
        if (showBid(desire, haver) && desire.getStatus() == DesireStatus.STATUS_OPEN) {
            return true;
        }
        return false;
    }

    public boolean showBid(Desire desire, Haver bidder) {
        if (desire.isBiddingAllowed() && !isDesireCreator(desire.getCreator().getId())) {
            if (bidder != null) {
                return true;
            }
        }
        return false;
    }

    public boolean showBiddingInfo(Desire desire, Haver bidder) {
        if (desire.isBiddingAllowed() && isDesireCreator(desire.getCreator().getId())) {
            return true;
        } else if (desire.isBiddingAllowed() && !isDesireCreator(desire.getCreator().getId())
                && bidder == null && desire.getStatus() == DesireStatus.STATUS_OPEN) {
            return true;
        }
        return false;
    }

    public boolean canRateUser(Desire desire, Haver haver){

        long loggedInUserId = getLoggedInUserId();

        if(desire.getCreator().getId() == loggedInUserId && desire.getCreatorHasRated() == false
                && desire.getStatus() == DesireStatus.STATUS_DONE){
            return true;

        }else if (haver != null && haver.getUser().getId() == loggedInUserId
                && desire.getHaverHasRated() == false && desire.getStatus() == DesireStatus.STATUS_DONE){
            return true;
        }

        return false;
    }

    public String getLocationDistance(long distanceInMeters) {
        String ret = "";

        if(distanceInMeters < 1000) {
            ret += mContext.getResources().getString(R.string.desire_distance) +  " 1 km";
        } else {
            long km = Math.round(((double) distanceInMeters) / 1000.);
            ret += km + " km";
        }

        return ret;
    }
}
