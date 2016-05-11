package wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import de.fau.cs.mad.wanthavers.common.Desire;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.DesireDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.DesireRepository;

import static com.google.common.base.Preconditions.checkNotNull;

public class GetDesireList extends UseCase<GetDesireList.RequestValues, GetDesireList.ResponseValue> {

    private final DesireRepository mDesireRepository;


    public GetDesireList(@NonNull DesireRepository desireRepository) {
        mDesireRepository = checkNotNull(desireRepository, "desireRepository cannot be null!");
    }


    @Override
    protected void executeUseCase(final RequestValues values) {

        /*
        List<Desire> desireList = new ArrayList<Desire>(0);
        desireList.add(new Desire());
        ResponseValue responseValue = new ResponseValue(desireList);
        getUseCaseCallback().onSuccess(responseValue);
        */
        //TODO for some reason repo does not work - @Nico please check why

        mDesireRepository.getAllDesires(new DesireDataSource.GetAllDesires(){


            @Override
            public void onAllDesiresLoaded(List<Desire> desireList) {
                ResponseValue responseValue = new ResponseValue(desireList);
                getUseCaseCallback().onSuccess(responseValue);
            }

            @Override
            public void onDataNotAvailable() {
                getUseCaseCallback().onError();
            }
        });


        /*mDesireRepository.getDesire(values.getDesireId(), new DesireDataSource.LoadDesireCallback() {
            @Override
            public void onDesireLoaded(List<Desire> desireList) {
                ResponseValue responseValue = new ResponseValue(desireList);
                getUseCaseCallback().onSuccess(responseValue);
            }

            @Override
            public void onDataNotAvailable() {
                getUseCaseCallback().onError();
            }
        });
        */

    }



    public static final class RequestValues implements UseCase.RequestValues {

        //TODO add values here if needed for desire query e.g.:private final long mDesireId;

        public RequestValues() {
            //TODO add values here if needed for desire query
        }


        /* TODO add getters here if needed
        public long getDesireId() {
            return mDesireId;
        }
        */
    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private List<Desire> mDesireList;

        public ResponseValue(@NonNull List<Desire> desires) {
            mDesireList = checkNotNull(desires, "task cannot be null!");
        }

        public List<Desire> getDesires() {
            return mDesireList;
        }
    }

}
