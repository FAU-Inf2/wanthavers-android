package wanthavers.mad.cs.fau.de.wanthavers_android.util;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

public class WantHaversEditText extends EditText{

    public WantHaversEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/EstandarRd-Regular.ttf"));
        //this.setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/Raleway-Regular.ttf"));
    }
}
