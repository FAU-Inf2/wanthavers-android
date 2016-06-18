package wanthavers.mad.cs.fau.de.wanthavers_android.domain;

import android.text.format.DateUtils;

import java.util.Date;

import wanthavers.mad.cs.fau.de.wanthavers_android.R;

public class ChatLogic {




    public String getDateString(Date date) {

        String dateString = "";

        Date curDate = new Date();

        if(date != null) {
            return (String) DateUtils.getRelativeTimeSpanString(date.getTime(),curDate.getTime(), DateUtils.MINUTE_IN_MILLIS);
        }

        return dateString;
    }

}
