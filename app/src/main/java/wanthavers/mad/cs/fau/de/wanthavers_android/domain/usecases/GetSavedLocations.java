package wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.Location;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.location.LocationDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.location.LocationRepository;

import static com.google.common.base.Preconditions.checkNotNull;

public class GetSavedLocations extends UseCase<GetSavedLocations.RequestValues, GetSavedLocations.ResponseValue> {

    private final LocationRepository mLocationRepository;

    public GetSavedLocations(LocationRepository locationRepository) {
        mLocationRepository = checkNotNull(locationRepository, "location repository cannot be null");
    }

    @Override
    protected void executeUseCase(final RequestValues values) {

        mLocationRepository.getSavedLocationsForLoggedInUser(new LocationDataSource.GetSavedLocationsForLoggedInUserCallback() {
            @Override
            public void onSavedLocationsForLoggedInUserLoaded(List<Location> locations) {
                ResponseValue responseValue = new ResponseValue(locations);
                getUseCaseCallback().onSuccess(responseValue);
            }

            @Override
            public void onDataNotAvailable() {
                getUseCaseCallback().onError();
            }
        });
    }

    public static final class RequestValues implements UseCase.RequestValues {

        //no values requested

        public RequestValues() {

        }

    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private List<Location> mLocations;

        public ResponseValue(List<Location> locations) {
            mLocations = checkNotNull(locations, "task cannot be null");
        }

        public List<Location> getLocations() {
            return mLocations;
        }

    }

}
