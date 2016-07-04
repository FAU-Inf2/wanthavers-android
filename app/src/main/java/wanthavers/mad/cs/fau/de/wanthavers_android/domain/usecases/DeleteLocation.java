package wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases;

import android.support.annotation.NonNull;

import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.location.LocationDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.location.LocationRepository;

import static com.google.common.base.Preconditions.checkNotNull;

public class DeleteLocation extends UseCase<DeleteLocation.RequestValues, DeleteLocation.ResponseValue> {

    private final LocationRepository mLocationRepository;

    public DeleteLocation(@NonNull LocationRepository locationRepository) {
        mLocationRepository = checkNotNull(locationRepository, "location repository cannot be null");
    }

    @Override
    protected void executeUseCase(final RequestValues requestValues) {

        mLocationRepository.deleteLocation(requestValues.getDesireId(), new LocationDataSource.DeleteLocationCallback() {

            @Override
            public void onLocationDeleted() {
                ResponseValue responseValue = new ResponseValue();
                getUseCaseCallback().onSuccess(responseValue);
            }

            @Override
            public void onDeleteFailed() {
                getUseCaseCallback().onError();
            }

        });
    }

    public static final class RequestValues implements UseCase.RequestValues {

        private final long mLocationId;

        public RequestValues(long locationId) {
            mLocationId = locationId;
        }

        public long getDesireId() {
            return mLocationId;
        }

    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        public ResponseValue() {
            //no response
        }

    }

}
