package wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases;

import android.support.annotation.NonNull;

import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import de.fau.cs.mad.wanthavers.common.Desire;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.DesireRepository;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.DesireDataSource;

import static com.google.common.base.Preconditions.checkNotNull;

public class GetDesire extends UseCase<GetDesire.RequestValues, GetDesire.ResponseValue> {

    private final DesireRepository mDesireRepository;


    public GetDesire(@NonNull DesireRepository desireRepository) {
        mDesireRepository = checkNotNull(desireRepository, "desireRepository cannot be null!");
    }


    @Override
    protected void executeUseCase(final RequestValues values) {

        mDesireRepository.getDesire(values.getDesireId(), new DesireDataSource.GetDesireCallback() {
            @Override
            public void onDesireLoaded(Desire desire) {
                ResponseValue responseValue = new ResponseValue(desire);
                getUseCaseCallback().onSuccess(responseValue);
            }

            @Override
            public void onDataNotAvailable() {
                getUseCaseCallback().onError();
            }
        });
    }



    public static final class RequestValues implements UseCase.RequestValues {

        private final long mDesireId;

        public RequestValues(@NonNull long desireId) {
            mDesireId = checkNotNull(desireId, "desireId cannot be null!");
        }

        public long getDesireId() {
            return mDesireId;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private Desire mDesire;

        public ResponseValue(@NonNull Desire desire) {
            mDesire = checkNotNull(desire, "desire cannot be null!");
        }

        public Desire getDesire() {
            return mDesire;
        }
    }

}
