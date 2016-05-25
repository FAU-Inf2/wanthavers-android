package wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases;

import android.support.annotation.NonNull;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

import de.fau.cs.mad.wanthavers.common.Haver;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.haver.HaverDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.haver.HaverRepository;

/**
 * Created by Oliver Lutz on 24.05.2016.
 */
public class GetHaverList extends UseCase<GetHaverList.RequestValues, GetHaverList.ResponseValue> {

    private final HaverRepository mHaverRepository;
    private long mdesireID;


    public GetHaverList(@NonNull HaverRepository haverRepository, long desireID) {
        mHaverRepository = checkNotNull(haverRepository, "haverRepository cannot be null!");
        mdesireID = desireID;
    }


    @Override
    protected void executeUseCase(final RequestValues values) {

        mHaverRepository.getAllHaversForDesire(mdesireID, new HaverDataSource.GetAllHaversForDesireCallback(){

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


        public RequestValues() {
            //TODO add values here if needed for haver query
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
