package wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases;

import android.support.annotation.NonNull;

import de.fau.cs.mad.wanthavers.common.Haver;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.haver.HaverDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.haver.HaverRepository;

import static com.google.common.base.Preconditions.checkNotNull;

public class UpdateRequestedPrice extends UseCase<UpdateRequestedPrice.RequestValues, UpdateRequestedPrice.ResponseValue> {

    private final HaverRepository mHaverRepository;

    public UpdateRequestedPrice(@NonNull HaverRepository haverRepository) {
        mHaverRepository = checkNotNull(haverRepository, "haver repository cannot be null");
    }

    @Override
    protected void executeUseCase(RequestValues values) {

        mHaverRepository.updateRequestedPrice(values.getDesireId(), values.getUserId(), values.getRequestedPrice(),
                new HaverDataSource.UpdateRequestedPriceCallback() {
                    @Override
                    public void onRequestedPriceUpdated(Haver haver) {
                        ResponseValue responseValue = new ResponseValue(haver);
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
        private final long mUserId;
        private final double mRequestedPrice;

        public RequestValues(long desireId, long userId, double requestedPrice) {
            mDesireId = desireId;
            mUserId = userId;
            mRequestedPrice = requestedPrice;
        }

        public long getDesireId() {
            return mDesireId;
        }

        public long getUserId() {
            return mUserId;
        }

        public double getRequestedPrice() {
            return mRequestedPrice;
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
