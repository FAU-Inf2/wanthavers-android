package wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases;

import android.support.annotation.NonNull;

import de.fau.cs.mad.wanthavers.common.Haver;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.haver.HaverDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.haver.HaverRepository;

import static com.google.common.base.Preconditions.checkNotNull;

public class UpdateHaver extends UseCase<UpdateHaver.RequestValues, UpdateHaver.ResponseValue> {

    private final HaverRepository mHaverRepository;

    public UpdateHaver(@NonNull HaverRepository haverRepository) {
        mHaverRepository = checkNotNull(haverRepository, "haver repository cannot be null");
    }

    protected void executeUseCase(final RequestValues values) {

        mHaverRepository.updateHaver(values.getDesireId(), values.getHaverId(), values.getHaver(),
                new HaverDataSource.UpdateHaverCallback() {

                    public void onHaverUpdated(Haver haver) {
                        ResponseValue responseValue = new ResponseValue();
                        getUseCaseCallback().onSuccess(responseValue);
                    }

                    public void onUpdateFailed() {
                        getUseCaseCallback().onError();
                    }

                }
        );
    }

    public static final class RequestValues implements UseCase.RequestValues {

        private final long mDesireId;
        private final long mHaverId;
        private final Haver mHaver;

        public RequestValues(long desireId, long haverId, Haver haver) {
            mDesireId = desireId;
            mHaverId = haverId;
            mHaver = haver;
        }

        public long getDesireId() {
            return mDesireId;
        }

        public long getHaverId() {
            return mHaverId;
        }

        public Haver getHaver() {
            return mHaver;
        }

    }

    public static final class ResponseValue implements UseCase.ResponseValue {

    }
}
