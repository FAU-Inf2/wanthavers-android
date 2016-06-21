package wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases;

import android.support.annotation.NonNull;

import de.fau.cs.mad.wanthavers.common.Location;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.location.LocationDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.location.LocationRepository;

import static com.google.common.base.Preconditions.checkNotNull;

public class UpdateLocation extends UseCase<UpdateLocation.RequestValues, UpdateLocation.ResponseValue> {

    private final LocationRepository mLocationRepository;

    public UpdateLocation(@NonNull LocationRepository locationRepository) {
        mLocationRepository = checkNotNull(locationRepository, "location repository cannot be null");
    }

    @Override
    protected void executeUseCase(final RequestValues values) {

        mLocationRepository.updateLocation(values.getLocationId(), values.getLocation(),
                new LocationDataSource.UpdateLocationCallback() {

                    @Override
                    public void onLocationUpdated(Location location) {
                        ResponseValue responseValue = new ResponseValue(location);
                        getUseCaseCallback().onSuccess(responseValue);
                    }

                    @Override
                    public void onUpdateFailed() {
                        getUseCaseCallback().onError();
                    }

                }
        );

    }

    public static final class RequestValues implements UseCase.RequestValues {

        private final long mLocationId;
        private final Location mLocation;

        public RequestValues(@NonNull long locationId, @NonNull Location location) {
            mLocationId = checkNotNull(locationId);
            mLocation = checkNotNull(location);
        }

        public long getLocationId() {
            return mLocationId;
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
