package wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases;

import de.fau.cs.mad.wanthavers.common.Location;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.location.LocationDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.location.LocationRepository;

import static com.google.common.base.Preconditions.checkNotNull;

public class CreateLocation extends UseCase<CreateLocation.RequestValues, CreateLocation.ResponseValue> {

    private final LocationRepository mLocationRepository;

    public CreateLocation(LocationRepository locationRepository) {
        mLocationRepository = checkNotNull(locationRepository, "location repository cannot be null");
    }

    @Override
    protected void executeUseCase(final RequestValues values) {

        mLocationRepository.createLocation(values.getLocation(), new LocationDataSource.CreateLocationCallback() {
            @Override
            public void onLocationCreated(Location location) {
                ResponseValue responseValue = new ResponseValue(location);
                getUseCaseCallback().onSuccess(responseValue);
            }

            @Override
            public void onCreateFailed() {
                getUseCaseCallback().onError();
            }
        });

    }

    public static final class RequestValues implements UseCase.RequestValues {

        private final Location mLocation;

        public RequestValues(Location location) {
            mLocation = location;
        }

        public Location getLocation() {
            return mLocation;
        }

    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private Location mLocation;

        public ResponseValue(Location location) {
            mLocation = location;
        }

        public Location getLocation() {
            return mLocation;
        }

    }

}
