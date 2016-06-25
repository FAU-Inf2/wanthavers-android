package wanthavers.mad.cs.fau.de.wanthavers_android.filtersetting;

import android.view.View;
import android.widget.Button;

import de.fau.cs.mad.wanthavers.common.Category;
import de.fau.cs.mad.wanthavers.common.Location;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.FiltersettingFragBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.FiltersettingLocationPopupBinding;

public class FilterSettingActionHandler {

    private FilterSettingContract.Presenter mListener;
    private FiltersettingFragBinding mFilterSettingFragBinding;

    public FilterSettingActionHandler(FilterSettingContract.Presenter listener, FiltersettingFragBinding filtersettingFragBinding) {
        mListener = listener;
        mFilterSettingFragBinding = filtersettingFragBinding;
    }

    public void buttonChangeLocation() {
        mListener.openLocationList();
    }

    public void buttonAddLocation() {
        mListener.openMap(null);
    }

    public void closeLocationList() {
        mListener.closeLocationList();
    }

    public void buttonSubmitLocationName(Location location) {
        mListener.finishLocationCreate(location);
    }

    public void buttonCancelLocationName() {
        mListener.closeNameSelectionDialog();
    }

    public void buttonChooseLocation(Location location) {
        mListener.setLocation(location);
        mListener.closeLocationList();
    }

    public void buttonUpdateLocation(Location location) {
        mListener.openMap(location);
    }

    public void buttonFinishUpdateLocation(Location location) {
        mListener.finishLocationUpdate(location);
    }

    public void buttonDeleteLocation(Location location) {
        mListener.deleteLocation(location);
    }

    public void openCategorySelection() {
        mListener.openCategorySelection();
    }

}
