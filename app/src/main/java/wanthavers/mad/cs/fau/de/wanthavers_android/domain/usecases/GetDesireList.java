package wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases;

import android.support.annotation.NonNull;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.Desire;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.desire.DesireDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.desire.DesireRepository;

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

        long userId = values.getUserId();


        if(userId == -1) {
            mDesireRepository.getAllDesires(new DesireDataSource.GetAllDesiresCallback() {


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

        }

        if(userId > 0){

            mDesireRepository.getDesiresForUser(userId , new DesireDataSource.GetDesiresForUserCallback() {

                @Override
                public void onDesiresForUserLoaded(List<Desire> desireList) {
                    ResponseValue responseValue = new ResponseValue(desireList);
                    getUseCaseCallback().onSuccess(responseValue);
                }

                @Override
                public void onDataNotAvailable() {
                    getUseCaseCallback().onError();
                }
            });
        }

        /*mDesireRepository.getDesire(values.getUserid(), new DesireDataSource.LoadDesireCallback() {
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
        long mUserId = -1;


        public RequestValues() {
            //TODO add values here if needed for desire query
        }


        /* TODO add getters here if needed
        public long getUserid() {
            return mDesireId;
        }
        */

        public long getUserId(){
            return mUserId;
        }

        public void setUserId(long userId){
            mUserId = userId;
        }
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
