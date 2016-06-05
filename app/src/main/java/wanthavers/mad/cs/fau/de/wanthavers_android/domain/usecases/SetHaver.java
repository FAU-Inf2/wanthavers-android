package wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases;

import android.support.annotation.NonNull;

import de.fau.cs.mad.wanthavers.common.Haver;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.haver.HaverDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.haver.HaverRepository;

import static com.google.common.base.Preconditions.checkNotNull;

public class SetHaver extends UseCase<SetHaver.RequestValues, SetHaver.ResponseValue> {

    private final HaverRepository mHaverRepository;

    public SetHaver(@NonNull HaverRepository haverRepository) {
        mHaverRepository = checkNotNull(haverRepository, "haver repository cannot be null");
    }

    protected void executeUseCase(final RequestValues values) {

        mHaverRepository.createHaver(values.getDesireId(), values.getHaver(), new HaverDataSource.CreateHaverCallback() {

            @Override
            public void onHaverCreated(Haver haver) {
                ResponseValue responseValue = new ResponseValue(/*haver*/);
                getUseCaseCallback().onSuccess(responseValue);
            }

            @Override
            public void onCreateFailed() {
                getUseCaseCallback().onError();
            }
        });

    }

    public static final class RequestValues implements UseCase.RequestValues {

        private final long mDesireId;
        private final Haver mHaver;

        public RequestValues(long desireId, Haver haver) {
            mDesireId = desireId;
            mHaver = haver;
        }

        public long getDesireId () {
            return mDesireId;
        }

        public Haver getHaver() {
            return mHaver;
        }

    }

    public static final class ResponseValue implements  UseCase.ResponseValue {

        /*private Haver mHaver;

        public ResponseValue(@NonNull Haver haver) {
            mHaver = haver;
        }

        public Haver getHaver() {
            return mHaver;
        }*/

        public ResponseValue() {

        }

    }

}
