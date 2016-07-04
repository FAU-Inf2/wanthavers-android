package wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases;

import android.support.annotation.NonNull;

import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.haver.HaverDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.haver.HaverRepository;
import static com.google.common.base.Preconditions.checkNotNull;

public class DeleteHaver extends UseCase<DeleteHaver.RequestValues, DeleteHaver.ResponseValue> {

    private final HaverRepository mHaverRepository;

    public DeleteHaver(@NonNull HaverRepository haverRepository) {
        mHaverRepository = checkNotNull(haverRepository, "haver repository cannot be null");
    }

    @Override
    protected void executeUseCase(final RequestValues values) {

        /*mHaverRepository.deleteHaver(values.getDesireId(), values.getUserId(),
                new HaverDataSource.DeleteHaverCallback() {

                    @Override
                    public void onHaverDeleted () {
                        ResponseValue responseValue = new ResponseValue();
                        getUseCaseCallback().onSuccess(responseValue);
                    }

                    public void onDeleteFailed () {
                        getUseCaseCallback().onError();
                    }
                }
        );*/
    }

    public static final class RequestValues implements UseCase.RequestValues {

        private final long mDesireId, mUserId;

        public RequestValues(long desireId, long userId) {
            mDesireId = desireId;
            mUserId = userId;
        }

        public long getDesireId() {
            return mDesireId;
        }

        public long getUserId() {
            return mUserId;
        }

    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        public ResponseValue() {
            //nothing to do
        }

    }

}
