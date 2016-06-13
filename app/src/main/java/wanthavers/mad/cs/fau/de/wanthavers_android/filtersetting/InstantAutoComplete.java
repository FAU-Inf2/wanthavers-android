package wanthavers.mad.cs.fau.de.wanthavers_android.filtersetting;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AutoCompleteTextView;

public class InstantAutoComplete extends AutoCompleteTextView {

    private boolean showAlways;

    public InstantAutoComplete(Context context) {
        super(context);
    }

    public InstantAutoComplete(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InstantAutoComplete(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setShowAlways(boolean showAlways) {
        this.showAlways = showAlways;
    }

    @Override
    public boolean enoughToFilter() {
        return showAlways || super.enoughToFilter();
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);

        showDropDownIfFocused();
    }

    private void showDropDownIfFocused() {
        if (enoughToFilter() && isFocused() && getWindowVisibility() == View.VISIBLE) {
            showDropDown();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        showDropDownIfFocused();
    }

    /*private int myThreshold;

    public InstantAutoComplete  (Context context) {
        super(context);
    }

    public InstantAutoComplete  (Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public InstantAutoComplete  (Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    //set threshold 0.
    public void setThreshold(int threshold) {
        if (threshold < 0) {
            threshold = 0;
        }
        myThreshold = threshold;
    }
    //if threshold   is 0 than return true
    public boolean enoughToFilter() {
        return true;
    }
    //invoke on focus
    protected void onFocusChanged(boolean focused, int direction,
                                  Rect previouslyFocusedRect) {
        //skip space and backspace
        super.performFiltering("", 67);
        // TODO Auto-generated method stub
        super.onFocusChanged(focused, direction, previouslyFocusedRect);

    }

    protected void performFiltering(CharSequence text, int keyCode) {
        // TODO Auto-generated method stub
        super.performFiltering(text, keyCode);
    }

    public int getThreshold() {
        return myThreshold;
    }*/

    /*private int myThreshold;

    public InstantAutoComplete  (Context context) {
        super(context);
    }

    public InstantAutoComplete  (Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public InstantAutoComplete  (Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setThreshold(int threshold) {
        if (threshold < 0) {
            threshold = 0;
        }
        myThreshold = threshold;
    }

    @Override
    public boolean enoughToFilter() {
        return getText().length() >= myThreshold;
    }

    @Override
    public int getThreshold() {
        return myThreshold;
    }*/

    /*public InstantAutoComplete(Context context) {
        super(context);
    }

    public InstantAutoComplete(Context arg0, AttributeSet arg1) {
        super(arg0, arg1);
    }

    public InstantAutoComplete(Context arg0, AttributeSet arg1, int arg2) {
        super(arg0, arg1, arg2);
    }

    @Override
    public boolean enoughToFilter() {
        return true;
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction,
                                  Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (focused) {
            if (getText().toString().length() == 0) {
                // We want to trigger the drop down, replace the text.
                setText(" ");
                showDropDown();
            }
        }
    }*/

}