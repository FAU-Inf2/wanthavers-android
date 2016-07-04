package wanthavers.mad.cs.fau.de.wanthavers_android.util;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class WantHaversTextView extends TextView {


    public WantHaversTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/Raleway-Regular.ttf"));
    }
}
