package wanthavers.mad.cs.fau.de.wanthavers_android.util;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

public class WantHaversTextButton extends Button {


    public WantHaversTextButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/Mallanna-Regular.ttf"));
    }
}
