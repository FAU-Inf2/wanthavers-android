package wanthavers.mad.cs.fau.de.wanthavers_android.domain;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import wanthavers.mad.cs.fau.de.wanthavers_android.R;

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

        if(date != null) {
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            dateString = mSimpleDateFormat.format(date);
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
}
