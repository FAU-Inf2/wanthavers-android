package wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases;

import android.support.annotation.NonNull;

import de.fau.cs.mad.wanthavers.common.Location;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.location.LocationDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.location.LocationRepository;

import static com.google.common.base.Preconditions.checkNotNull;

public class GetLocation extends UseCase<GetLocation.RequestValues, GetLocation.ResponseValue> {

    private final LocationRepository mLocationRepository;

    public GetLocation(@NonNull LocationRepository locationRepository) {
        mLocationRepository = checkNotNull(locationRepository, "location repository cannot be null");
    }

    @Override
    protected void executeUseCase(final RequestValues requestValues) {

        mLocationRepository.getLocation(requestValues.getLocationId(), new LocationDataSource.GetLocationCallback() {
            @Override
            public void onLocationLoaded(Location location) {
                ResponseValue responseValue = new ResponseValue(location);
                getUseCaseCallback().onSuccess(responseValue);
            }

            @Override
            public void onDataNotAvailable() {
                getUseCaseCallback().onError();
            }
        });

    }

    public static final class RequestValues implements UseCase.RequestValues {

        private final long mLocationId;

        public RequestValues(long locationId) {
            mLocationId = locationId;
        }

        public long getLocationId() {
            return mLocationId;
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
