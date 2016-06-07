package wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases;

import android.support.annotation.NonNull;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.Desire;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.desire.DesireDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.desire.DesireRepository;
import wanthavers.mad.cs.fau.de.wanthavers_android.desirelist.DesireListType;

import static com.google.common.base.Preconditions.checkNotNull;

public class GetDesireList extends UseCase<GetDesireList.RequestValues, GetDesireList.ResponseValue> {

    private final DesireRepository mDesireRepository;


    public GetDesireList(@NonNull DesireRepository desireRepository) {
        mDesireRepository = checkNotNull(desireRepository, "desireRepository cannot be null!");
    }


    @Override
    protected void executeUseCase(final RequestValues values) {

        long userId = values.getUserId();

        DesireListType desireListType = values.getDesireListType();

        if(desireListType == DesireListType.ALL_DESIRES) {

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

        if(desireListType == DesireListType.MY_DESIRES){

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

        if(desireListType == DesireListType.MY_TRANSACTIONS){

            //TODO: set status correctly
            mDesireRepository.getDesiresAsHaver(userId , null, new DesireDataSource.GetDesiresAsHaverCallback() {

                @Override
                public void onDesiresAsHaverLoaded(List<Desire> desireList) {
                    ResponseValue responseValue = new ResponseValue(desireList);
                    getUseCaseCallback().onSuccess(responseValue);
                }

                @Override
                public void onDataNotAvailable() {
                    getUseCaseCallback().onError();
                }
            });
        }

    }

    public static final class RequestValues implements UseCase.RequestValues {

        long mUserId = -1;
        DesireListType mDesireListType;

        public RequestValues(DesireListType desireListType) {
            mDesireListType = desireListType;
        }

        public long getUserId(){
            return mUserId;
        }

        public void setUserId(long userId){
            mUserId = userId;
        }

        public DesireListType getDesireListType(){ return mDesireListType;}

        public void setDesireListType(DesireListType desireListType){mDesireListType = desireListType;}

    }


    public static final class ResponseValue implements UseCase.ResponseValue {

        private List<Desire> mDesireList;

        public ResponseValue(@NonNull List<Desire> desires) {
            mDesireList = checkNotNull(desires, "desire cannot be null!");
        }

        public List<Desire> getDesires() {
            return mDesireList;
        }
    }

}
