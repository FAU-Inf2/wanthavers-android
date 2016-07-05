package wanthavers.mad.cs.fau.de.wanthavers_android.locationlist;

import android.support.annotation.NonNull;

import de.fau.cs.mad.wanthavers.common.DesireFilter;
import de.fau.cs.mad.wanthavers.common.Location;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCaseHandler;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.WantHaversApplication;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.CreateLocation;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.DeleteLocation;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetSavedLocations;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.UpdateLocation;


import static com.google.common.base.Preconditions.checkNotNull;

public class LocationListPresenter implements LocationListContract.Presenter {

    private final UseCaseHandler mUseCaseHandler;
    private final CreateLocation mCreateLocation;
    private final UpdateLocation mUpdateLocation;
    private final GetSavedLocations mGetSavedLocations;
    private final DeleteLocation mDeleteLocation;
    private final LocationListContract.View mLocationListView;
    private final LocationListActivity mActivity;

    public LocationListPresenter(@NonNull UseCaseHandler useCaseHandler, @NonNull LocationListContract.View locationListView,
                                 @NonNull LocationListActivity locationListActivity,
                                 @NonNull CreateLocation createLocation, @NonNull GetSavedLocations getSavedLocations,
                                 @NonNull UpdateLocation updateLocation, @NonNull DeleteLocation deleteLocation) {
        mUseCaseHandler = checkNotNull(useCaseHandler, "useCaseHandler cannot be null");
        mLocationListView = checkNotNull(locationListView, "locationlist view cannot be null");
        mCreateLocation = checkNotNull(createLocation);
        mGetSavedLocations = checkNotNull(getSavedLocations);
        mUpdateLocation = checkNotNull(updateLocation);
        mDeleteLocation = checkNotNull(deleteLocation);
        mActivity = checkNotNull(locationListActivity, "locationlistactivity cannot be null");
    }

    @Override
    public void start() {
        getSavedLocations();
    }

    public void getSavedLocations() {

        GetSavedLocations.RequestValues requestValues = new GetSavedLocations.RequestValues();

        mUseCaseHandler.execute(mGetSavedLocations, requestValues, new UseCase.UseCaseCallback<GetSavedLocations.ResponseValue>() {
            @Override
            public void onSuccess(GetSavedLocations.ResponseValue response) {
                mLocationListView.showSavedLocations(response.getLocations());
            }

            @Override
            public void onError() {
                mLocationListView.showGetSavedLocationsError();
            }
        });

    }

    public void createLocation(Location location) {

        CreateLocation.RequestValues requestValues = new CreateLocation.RequestValues(location);

        mUseCaseHandler.execute(mCreateLocation, requestValues, new UseCase.UseCaseCallback<CreateLocation.ResponseValue>() {
            @Override
            public void onSuccess(CreateLocation.ResponseValue response) {
                getSavedLocations();
            }

            @Override
            public void onError() {
                mLocationListView.showCreateLocationError();
            }
        });

    }

    public void updateLocation(final Location location) {
        UpdateLocation.RequestValues requestValues = new UpdateLocation.RequestValues(location.getId(), location);

        mUseCaseHandler.execute(mUpdateLocation, requestValues, new UseCase.UseCaseCallback<UpdateLocation.ResponseValue>() {
            @Override
            public void onSuccess(UpdateLocation.ResponseValue response) {
                checkFilterLocationUpdate(response.getLocation());
                Location curFilterLocation = mLocationListView.getFilterLocation();
                if (curFilterLocation != null) {
                    if (curFilterLocation.getId() == location.getId()) {
                        mLocationListView.setFilterLocation(response.getLocation());
                    }
                }
                getSavedLocations();
            }

            @Override
            public void onError() {
                mLocationListView.showUpdateLocationError();
            }
        });
    }

    public void deleteLocation(final Location location) {
        DeleteLocation.RequestValues requestValues = new DeleteLocation.RequestValues(location.getId());

        mUseCaseHandler.execute(mDeleteLocation, requestValues, new UseCase.UseCaseCallback<DeleteLocation.ResponseValue>() {
            @Override
            public void onSuccess(DeleteLocation.ResponseValue response) {
                checkFilterLocationDelete(location);

                Location curFilterLocation = mLocationListView.getFilterLocation();
                if (curFilterLocation != null) {
                    if (curFilterLocation.getId() == location.getId()) {
                        mLocationListView.setFilterLocation(null);
                    }
                }

                getSavedLocations();
            }

            @Override
            public void onError() {
                mLocationListView.showDeleteLocationError();
            }
        });
    }

    @Override
    public void openMap(Location location) {
        mLocationListView.showMap(location);
    }

    @Override
    public void closeLocationList(Location location) {

        //supports back-press with updated data
        if (location == null) {
            mLocationListView.closeLocationList(mLocationListView.getFilterLocation());
        } else {
            mLocationListView.closeLocationList(location);
        }
    }

    @Override
    public void closeNameSelectionDialog() {
        mLocationListView.closeLocationNameDialog();
    }

    @Override
    public void finishLocationCreate(Location location) {
        String locationName = mLocationListView.getNameInput();
        if (locationName == null) {
            //show no empty string
            return;
        }
        location.setDescription(locationName);
        createLocation(location);
        closeNameSelectionDialog();
    }

    @Override
    public void finishLocationUpdate(Location location) {
        String locationName = mLocationListView.getNameInput();
        if (locationName == null) {
            //show no empty string
            return;
        }
        location.setDescription(locationName);
        updateLocation(location);
        closeNameSelectionDialog();
    }

    public void checkFilterLocationUpdate(Location location) {
        DesireFilter curFilter = WantHaversApplication.
                getDesireFilter(mActivity.getApplicationContext());
        Location curFilterLocation = curFilter.getLocation();

        if (curFilterLocation == null) {
            return;
        } else if (curFilterLocation.getId() == location.getId()) {
            curFilter.setLocation(location);
            curFilter.setLat(location.getLat());
            curFilter.setLon(location.getLon());
            WantHaversApplication.setDesireFilter(curFilter, mActivity.getApplicationContext());
        }
    }

    public void checkFilterLocationDelete(Location location) {
        DesireFilter curFilter = WantHaversApplication.
                getDesireFilter(mActivity.getApplicationContext());
        Location curFilterLocation = curFilter.getLocation();

        if (curFilterLocation == null) {
            return;
        } else if (curFilterLocation.getId() == location.getId()) {
            curFilter.setLocation(null);
            curFilter.setLat(null);
            curFilter.setLon(null);
            WantHaversApplication.setDesireFilter(curFilter, mActivity.getApplicationContext());
        }
    }

}
