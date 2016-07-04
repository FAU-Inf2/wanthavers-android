package wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases;

import android.support.annotation.NonNull;

import de.fau.cs.mad.wanthavers.common.Haver;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.haver.HaverDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.haver.HaverRepository;

import static com.google.common.base.Preconditions.checkNotNull;

public class GetHaver extends UseCase<GetHaver.RequestValues, GetHaver.ResponseValue>{

    private final HaverRepository mHaverRepository;

    public GetHaver(@NonNull HaverRepository haverRepository) {
        mHaverRepository = checkNotNull(haverRepository, "haver repository cannot be null");
    }

    protected void executeUseCase(final RequestValues values) {

        mHaverRepository.getHaver(values.getDesireId(), values.getUserId(),
                new HaverDataSource.GetHaverCallback() {

                    @Override
                    public void onHaverLoaded(Haver haver) {
                        ResponseValue responseValue = new ResponseValue(haver);
                        getUseCaseCallback().onSuccess(responseValue);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        getUseCaseCallback().onError();
                    }

                }
        );
    }

    public static final class RequestValues implements UseCase.RequestValues {

        private final long mDesireId;
        private final long mUserId;

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

        private Haver mHaver;

        public ResponseValue(Haver haver) {
            mHaver = haver;
        }

        public Haver getHaver() {
            return mHaver;
        }

    }

}
