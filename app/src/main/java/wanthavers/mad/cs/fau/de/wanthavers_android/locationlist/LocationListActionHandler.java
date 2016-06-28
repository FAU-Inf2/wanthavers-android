package wanthavers.mad.cs.fau.de.wanthavers_android.locationlist;

import de.fau.cs.mad.wanthavers.common.Location;

public class LocationListActionHandler {

    private LocationListContract.Presenter mListener;

    public LocationListActionHandler(LocationListContract.Presenter listener) {
        mListener = listener;
    }

    public void buttonAddLocation() {
        mListener.openMap(null);
    }

    public void closeLocationList(Location filterLocation) {
        mListener.closeLocationList(filterLocation);
    }

    public void buttonSubmitLocationName(Location location) {
        mListener.finishLocationCreate(location);
    }

    public void buttonCancelLocationName() {
        mListener.closeNameSelectionDialog();
    }

    public void buttonChooseLocation(Location location) {
        mListener.closeLocationList(location);
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

}
