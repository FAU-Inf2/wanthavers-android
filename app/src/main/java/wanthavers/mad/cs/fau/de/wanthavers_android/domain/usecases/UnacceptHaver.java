package wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases;

import android.support.annotation.NonNull;

import de.fau.cs.mad.wanthavers.common.Haver;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.haver.HaverDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.haver.HaverRepository;

import static com.google.common.base.Preconditions.checkNotNull;

public class UnacceptHaver extends UseCase<UnacceptHaver.RequestValues, UnacceptHaver.ResponseValue> {

    private final HaverRepository mHaverRepository;

    public UnacceptHaver(@NonNull HaverRepository haverRepository) {
        mHaverRepository = checkNotNull(haverRepository, "haverRepository cannot be null!");
    }

    @Override
    protected void executeUseCase(final RequestValues values) {
        mHaverRepository.unacceptHaver(values.getDesireId(), values.getHaverId(), values.getHaver(),
                new HaverDataSource.UnacceptHaverForDesireCallback() {
                    @Override
                    public void onUnacceptHaverForDesire(Haver haver) {
                        ResponseValue responseValue = new ResponseValue(haver);
                        getUseCaseCallback().onSuccess(responseValue);
                    }

                    @Override
                    public void onUnacceptFailed() {
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
