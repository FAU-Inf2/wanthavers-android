package wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases;

import android.support.annotation.NonNull;

import de.fau.cs.mad.wanthavers.common.Haver;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.haver.HaverDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.haver.HaverRepository;

import static com.google.common.base.Preconditions.checkNotNull;

public class AcceptHaver extends UseCase<AcceptHaver.RequestValues, AcceptHaver.ResponseValue> {

    private final HaverRepository mHaverRepository;


    public AcceptHaver(@NonNull HaverRepository haverRepository) {
        mHaverRepository = checkNotNull(haverRepository, "haverRepository cannot be null!");
    }


    @Override
    protected void executeUseCase(final RequestValues values) {

        mHaverRepository.acceptHaver(values.getDesireId(), values.getHaverId(), values.getHaver(),
                new HaverDataSource.AcceptHaverForDesireCallback() {
                    @Override
                    public void onAcceptHaverForDesire(Haver haver){
                        ResponseValue responseValue = new ResponseValue(haver);
                        getUseCaseCallback().onSuccess(responseValue);
                    }

                    public void onAcceptFailed() {
                        getUseCaseCallback().onError();
                    }
                });

    }



    public static final class RequestValues implements UseCase.RequestValues {

        private final long mDesireId;
        private final long mHaverId;
        private final Haver mHaver;

        public RequestValues(@NonNull long desireId, @NonNull long haverId, @NonNull Haver haver) {
            mDesireId = checkNotNull(desireId);
            mHaverId = checkNotNull(haverId);
            mHaver = checkNotNull(haver);

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

        private Haver mHaver;

        public ResponseValue(@NonNull Haver haver) {
            mHaver = haver;
        }

        public Haver getHaver() {
            return mHaver;
        }

    }

}
