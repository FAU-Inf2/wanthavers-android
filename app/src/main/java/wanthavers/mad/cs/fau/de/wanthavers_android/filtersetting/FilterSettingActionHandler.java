package wanthavers.mad.cs.fau.de.wanthavers_android.filtersetting;

import android.widget.Button;

import de.fau.cs.mad.wanthavers.common.Category;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.FiltersettingFragBinding;

public class FilterSettingActionHandler {

    private FilterSettingContract.Presenter mListener;
    private FiltersettingFragBinding mFilterSettingFragBinding;
    private int priceClicked; //0=no_button; 1=small; 2=middle; 3=large
    private int colorMainText;
    private int colorPrimary;
    private Category mSelectedCategory;

    public FilterSettingActionHandler(FilterSettingContract.Presenter listener, FiltersettingFragBinding filtersettingFragBinding) {
        mListener = listener;
        mFilterSettingFragBinding = filtersettingFragBinding;
        priceClicked = 0;
        colorPrimary = mFilterSettingFragBinding.getRoot().getResources().getColor(R.color.colorPrimary);
        colorMainText = mFilterSettingFragBinding.getRoot().getResources().getColor(R.color.colorMainText);
        mSelectedCategory = null;
    }

    public void buttonChangeLocation() {
        mListener.openMap();
    }

    public void buttonChangeColor(int clicked) {
        if (priceClicked == 0) {
            invertColor(clicked, true);
            priceClicked = clicked;
        }else if (priceClicked != clicked) {
            invertColor(clicked, true);
            invertColor(priceClicked, false);
            priceClicked = clicked;
        } else {
            invertColor(clicked, false);
            priceClicked = 0;
        }
    }

    /**
     *
     * @param clicked: buttonId (1=small; 2=middle; 3=large)
     * @param isOriginal true if button is in original color; false if inverted
     */
    private void invertColor(int clicked, boolean isOriginal) {
        Button button;
        switch (clicked) {
            case 1:
                button = mFilterSettingFragBinding.buttonSmallPrice;
                break;
            case 2:
                button = mFilterSettingFragBinding.buttonMediumPrice;
                break;
            case 3:
                button = mFilterSettingFragBinding.buttonLargePrice;
                break;
            default:
                //should not be reached
                button = null;
                break;
        }

        if (isOriginal) {
            button.setBackgroundColor(colorMainText);
            button.setTextColor(colorPrimary);
        } else {
            button.setBackgroundColor(colorPrimary);
            button.setTextColor(colorMainText);
        }

    }

    public void setCategory(Category category) {
        mSelectedCategory = category;
    }

    public Category getCategory() {
        return mSelectedCategory;
    }

    public int getPriceClicked() {
        return priceClicked;
    }

}
