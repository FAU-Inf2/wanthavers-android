package wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases;

import de.fau.cs.mad.wanthavers.common.DesireFlag;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.flag.FlagDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.flag.FlagRepository;

public class FlagDesire extends UseCase<FlagDesire.RequestValues, FlagDesire.ResponseValue> {

    private final FlagRepository mFlagRepository;

    public FlagDesire(FlagRepository flagRepository) {
        mFlagRepository = flagRepository;
    }

    @Override
    protected void executeUseCase(final RequestValues values) {

        mFlagRepository.flagDesire(values.getDesireId(), values.getDesireFlag(),
                new FlagDataSource.FlagDesire() {
                    public void onDesireFlagCreated(DesireFlag flag) {
                        ResponseValue responseValue = new ResponseValue(flag);
                        getUseCaseCallback().onSuccess(responseValue);
                    }

                    public void onCreateFailed() {
                        getUseCaseCallback().onError();
                    }
                }
        );
    }

    public static final class RequestValues implements UseCase.RequestValues {

        private final long mDesireId;
        private final DesireFlag mDesireFlag;

        public RequestValues(long desireId, DesireFlag desireFlag) {
            mDesireId = desireId;
            mDesireFlag = desireFlag;
        }

        public long getDesireId() {
            return mDesireId;
        }

        public DesireFlag getDesireFlag() {
            return mDesireFlag;
        }

    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private DesireFlag mDesireFlag;

        public ResponseValue(DesireFlag desireFlag) {
            mDesireFlag = desireFlag;
        }

        public DesireFlag getDesireFlag() {
            return mDesireFlag;
        }

    }
}
