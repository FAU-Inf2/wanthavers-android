package wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases;

import android.content.Context;
import android.support.annotation.NonNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.common.DesireFilter;
import de.fau.cs.mad.wanthavers.common.DesireStatus;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.WantHaversApplication;
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

        DesireFilter desireFilter = WantHaversApplication.getDesireFilter(values.getContext());

        if(desireListType == DesireListType.ALL_DESIRES) {

            List<Integer> statusFilter = new ArrayList<>();
            statusFilter.add(DesireStatus.STATUS_OPEN);

            desireFilter.setStatus(statusFilter);
        }

        if(desireListType == DesireListType.MY_DESIRES){
            Long tmpDesireid = desireFilter.getLastDesireId();
            desireFilter = new DesireFilter();
            if(tmpDesireid != null) {
                desireFilter.setLastDesireId(tmpDesireid);
            }
            List<Integer> statusFilter = new ArrayList<>();
            statusFilter.add(DesireStatus.STATUS_OPEN);
            statusFilter.add(DesireStatus.STATUS_DONE);
            statusFilter.add(DesireStatus.STATUS_IN_PROGRESS);
            statusFilter.add(DesireStatus.STATUS_DELETED);
            statusFilter.add(DesireStatus.STATUS_EXPIRED);
            desireFilter.setCreatorId(userId);
            desireFilter.setHaverId(userId);
            desireFilter.setStatus(statusFilter);
        }

        if (desireListType == DesireListType.FINISHED_DESIRES) {
            List<Integer> statusFilter = new ArrayList<>();
            statusFilter.add(DesireStatus.STATUS_DONE);
            desireFilter.setLimit(100000);
            desireFilter.setCreatorId(userId);
            desireFilter.setHaverId(userId);
            desireFilter.setStatus(statusFilter);
        }

        if (desireListType == DesireListType.CANCELED_DESIRES) {
            //TODO
        }

        /*if(desireListType == DesireListType.MY_TRANSACTIONS){
            Long tmpDesireid = desireFilter.getLastDesireId();
            desireFilter = new DesireFilter();
            if(tmpDesireid != null) {
                desireFilter.setLastDesireId(tmpDesireid);
            }

            List<Integer> statusFilter = new ArrayList<>();
            statusFilter.add(DesireStatus.STATUS_OPEN);
            statusFilter.add(DesireStatus.STATUS_DONE);
            statusFilter.add(DesireStatus.STATUS_IN_PROGRESS);
            desireFilter.setStatus(statusFilter);
            desireFilter.setHaverId(userId);
        }*/

        mDesireRepository.getDesiresByFilter(desireFilter,
                new DesireDataSource.GetDesiresByFilterCallback(){


                    @Override
                    public void onDesiresByFilterLoaded(List<Desire> desireList) {
                        ResponseValue responseValue = new ResponseValue(desireList);
                        getUseCaseCallback().onSuccess(responseValue);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        getUseCaseCallback().onError();
                    }
                });




        /*
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

        */
    }

    public static final class RequestValues implements UseCase.RequestValues {

        long mUserId = -1;
        DesireListType mDesireListType;
        Context mContext;

        public RequestValues(DesireListType desireListType, Context context) {
            mDesireListType = desireListType;
            mContext = context;
        }

        public long getUserId(){
            return mUserId;
        }

        public void setUserId(long userId){
            mUserId = userId;
        }

        public DesireListType getDesireListType(){ return mDesireListType;}

        public Context getContext(){ return  mContext;}

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
