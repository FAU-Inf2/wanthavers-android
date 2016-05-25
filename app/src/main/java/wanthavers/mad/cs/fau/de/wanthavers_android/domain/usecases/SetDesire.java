package wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases;

import android.support.annotation.NonNull;

import de.fau.cs.mad.wanthavers.common.Desire;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.desire.DesireDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.desire.DesireRepository;

import static com.google.common.base.Preconditions.checkNotNull;

public class SetDesire extends UseCase<SetDesire.RequestValues, SetDesire.ResponseValue> {

    private final DesireRepository mDesireRepository;


    public SetDesire(@NonNull DesireRepository desireRepository) {
        mDesireRepository = checkNotNull(desireRepository, "desireRepository cannot be null!");
    }


    @Override
    protected void executeUseCase(final RequestValues values) {
        mDesireRepository.createDesire(values.getDesire(),new DesireDataSource.CreateDesireCallback(){

            @Override
            public void onDesireCreated(Desire desire) {
                ResponseValue responseValue = new ResponseValue(desire);
                getUseCaseCallback().onSuccess(responseValue);
            }

            @Override
            public void onCreateFailed() {
                getUseCaseCallback().onError();
            }

        });


    }



    public static final class RequestValues implements UseCase.RequestValues {

        private final Desire mDesire;

        public RequestValues(Desire desire) {
            mDesire = desire;
        }

        public Desire getDesire(){
        return mDesire;
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
