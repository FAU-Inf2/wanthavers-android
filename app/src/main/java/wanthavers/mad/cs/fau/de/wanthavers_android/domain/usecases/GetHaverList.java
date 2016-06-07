package wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases;

import android.support.annotation.NonNull;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

import de.fau.cs.mad.wanthavers.common.Haver;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.haver.HaverDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.haver.HaverRepository;

public class GetHaverList extends UseCase<GetHaverList.RequestValues, GetHaverList.ResponseValue> {

    private final HaverRepository mHaverRepository;

    //TODO refactoring
    public GetHaverList(@NonNull HaverRepository haverRepository) {
        mHaverRepository = checkNotNull(haverRepository, "haverRepository cannot be null!");
    }


    @Override
    protected void executeUseCase(final RequestValues values) {

        mHaverRepository.getAllHaversForDesire(values.getDesireId(), new HaverDataSource.GetAllHaversForDesireCallback(){

            @Override
            public void onAllHaversForDesireLoaded(List<Haver> havers) {
                ResponseValue responseValue = new ResponseValue(havers);
                getUseCaseCallback().onSuccess(responseValue);
            }

            @Override
            public void onDataNotAvailable() {
                getUseCaseCallback().onError();
            }
        });

    }



    public static final class RequestValues implements UseCase.RequestValues {

        private final long mDesireId;

        public RequestValues(long desireId) {
            mDesireId = desireId;
        }

        public long getDesireId() {
            return mDesireId;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private List<Haver> mHaverList;

        public ResponseValue(@NonNull List<Haver> havers) {
            mHaverList = checkNotNull(havers, "task cannot be null!");
        }

        public List<Haver> getHavers() {
            return mHaverList;
        }
    }

}
