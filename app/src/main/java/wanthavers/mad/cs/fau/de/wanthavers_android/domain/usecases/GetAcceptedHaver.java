package wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases;

import android.support.annotation.NonNull;

import de.fau.cs.mad.wanthavers.common.Haver;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.haver.HaverDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.haver.HaverRepository;

import static com.google.common.base.Preconditions.checkNotNull;

public class GetAcceptedHaver extends UseCase<GetAcceptedHaver.RequestValues, GetAcceptedHaver.ResponseValue> {

    private final HaverRepository mHaverRepository;

    public GetAcceptedHaver(@NonNull HaverRepository haverRepository) {
        mHaverRepository = checkNotNull(haverRepository, "haver repository cannot be null");
    }

    protected void executeUseCase(final RequestValues values) {

        mHaverRepository.getAcceptedHaverForDesire(values.getDesireId(),
                new HaverDataSource.GetAcceptedHaverForDesireCallback() {

                    @Override
                    public void onHaverLoaded(Haver haver) {
                        ResponseValue responseValue = new ResponseValue(haver);
                        getUseCaseCallback().onSuccess(responseValue);
                    }

                    public void onDataNotAvailable() {
                        getUseCaseCallback().onError();
                    }

                }
        );
    }

    public static final class RequestValues implements UseCase.RequestValues {

        public final long mDesireId;

        public RequestValues(@NonNull long desireId) {
            mDesireId = checkNotNull(desireId, "desireId cannot be null");
        }

        public long getDesireId() {
            return mDesireId;
        }

    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        public Haver mHaver;

        public ResponseValue(@NonNull Haver haver) {
            mHaver = checkNotNull(haver, "haver cannot be null");
        }

        public Haver getHaver() {
            return mHaver;
        }

    }

}
