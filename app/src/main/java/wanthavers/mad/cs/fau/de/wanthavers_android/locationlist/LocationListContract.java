package wanthavers.mad.cs.fau.de.wanthavers_android.locationlist;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.Location;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.BasePresenter;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.BaseView;

public interface LocationListContract {

    interface View extends BaseView<LocationListContract.Presenter> {

        void showSavedLocations(List<Location> locationList);

        void closeLocationList(Location location);

        void showMap(Location location);

        String getNameInput();

        void closeLocationNameDialog();

        Location getFilterLocation();

        void setFilterLocation(Location location);

        //Handle Errors

        void showGetSavedLocationsError();

        void showCreateLocationError();

        void showUpdateLocationError();

        void showDeleteLocationError();

    }

    interface Presenter extends BasePresenter {

        void openMap(Location location);

        void closeLocationList(Location location);

        void closeNameSelectionDialog();

        void finishLocationCreate(Location location);

        void finishLocationUpdate(Location location);

        void deleteLocation(final Location location);

    }

}
