package wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases;

import android.support.annotation.NonNull;

import de.fau.cs.mad.wanthavers.common.Desire;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.desire.DesireDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.desire.DesireRepository;

import static com.google.common.base.Preconditions.checkNotNull;

public class UpdateDesireStatus extends UseCase<UpdateDesireStatus.RequestValues, UpdateDesireStatus.ResponseValue> {

    private final DesireRepository mDesireRepository;

    public UpdateDesireStatus(@NonNull DesireRepository desireRepository) {
        mDesireRepository = checkNotNull(desireRepository, "desire repository cannot be null");
    }

    protected void executeUseCase(final RequestValues values) {

        mDesireRepository.updateDesireStatus(values.getDesireId(), values.getDesireStatus(),
                new DesireDataSource.UpdateDesireStatusCallback() {
                    @Override
                    public void onStatusUpdated(Desire desire) {
                        ResponseValue responseValue = new ResponseValue(desire);
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

        private final long mDesireId;
        private final int mDesireStatus;

        public RequestValues(long desireId, int desireStatus) {
            mDesireId = desireId;
            mDesireStatus = desireStatus;
        }

        public long getDesireId() {
            return mDesireId;
        }

        public int getDesireStatus() {
            return mDesireStatus;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private Desire mDesire;

        public ResponseValue(Desire desire) {
            mDesire = desire;
        }

        public Desire getDesire() {
            return mDesire;
        }

    }

}
