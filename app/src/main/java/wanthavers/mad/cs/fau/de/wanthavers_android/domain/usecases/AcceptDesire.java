package wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases;

import android.support.annotation.NonNull;

import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.DesireRepository;

import static com.google.common.base.Preconditions.checkNotNull;

public class AcceptDesire extends UseCase<AcceptDesire.RequestValues, AcceptDesire.ResponseValue> {

    private final DesireRepository mDesireRepository;


    public AcceptDesire(@NonNull DesireRepository desireRepository) {
        mDesireRepository = checkNotNull(desireRepository, "desireRepository cannot be null!");
    }


    @Override
    protected void executeUseCase(final RequestValues values) {

        long acceptedDesire = values.getCompletedTask();
        mDesireRepository.acceptDesire(acceptedDesire);
        getUseCaseCallback().onSuccess(new ResponseValue());
    }



    public static final class RequestValues implements UseCase.RequestValues {

        private final long mCompletedDesire;

        public RequestValues(@NonNull long completedTask) {
            mCompletedDesire = checkNotNull(completedTask, "completedTask cannot be null!");
        }

        public long getCompletedTask() {
            return mCompletedDesire;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {
    }

}
