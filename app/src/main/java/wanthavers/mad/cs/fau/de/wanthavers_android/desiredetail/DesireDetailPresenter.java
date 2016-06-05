package wanthavers.mad.cs.fau.de.wanthavers_android.desiredetail;

import android.support.annotation.NonNull;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Date;
import java.util.List;

import de.fau.cs.mad.wanthavers.common.DesireStatus;
import de.fau.cs.mad.wanthavers.common.Haver;
import de.fau.cs.mad.wanthavers.common.User;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCaseHandler;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.DesireLogic;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.AcceptHaver;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetDesire;
import de.fau.cs.mad.wanthavers.common.Desire;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetHaver;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetHaverList;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetUser;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.SetHaver;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.UpdateHaver;

public class DesireDetailPresenter implements DesireDetailContract.Presenter {


    private final DesireDetailContract.View mDesireDetailView;
    private boolean mFirstLoad = true;
    private DesireLogic mDesireLogic;

    //Use Cases
    private final UseCaseHandler mUseCaseHandler;
    private final GetDesire mGetDesire;
    private final GetHaverList mGetHaverList;
    private final SetHaver mSetHaver;
    private final AcceptHaver mAcceptHaver;
    private final GetUser mGetUser;
    private final UpdateHaver mUpdateHaver;
    private final GetHaver mGetHaver;


    @NonNull
    private long mDesireId;

    public DesireDetailPresenter(DesireLogic desireLogic, @NonNull UseCaseHandler useCaseHandler, @NonNull long desireId,
                                 @NonNull DesireDetailContract.View desireDetailView,
                                 @NonNull AcceptHaver acceptHaver, @NonNull GetDesire getDesire,
                                 @NonNull GetHaverList getHaverList, @NonNull GetUser getUser,
                                 @NonNull SetHaver setHaver, @NonNull UpdateHaver updateHaver,
                                 @NonNull GetHaver getHaver){

        mUseCaseHandler = checkNotNull(useCaseHandler, "useCaseHandler cannot be null");
        mDesireDetailView = checkNotNull(desireDetailView, "desiredetail view cannot be null");
        mDesireId = desireId;
        mGetHaverList = checkNotNull(getHaverList);
        mAcceptHaver = checkNotNull(acceptHaver);
        mGetDesire = checkNotNull(getDesire);
        mSetHaver = checkNotNull(setHaver);
        mGetUser = checkNotNull(getUser);
        mUpdateHaver = checkNotNull(updateHaver);
        mGetHaver = checkNotNull(getHaver);

        mDesireDetailView.setPresenter(this);
        mDesireLogic = desireLogic;
    }


    @Override
    public void start(){
        getDesire();
        loadHavers(false);
    }

    @Override
    public void getDesire(){

        mUseCaseHandler.execute(mGetDesire, new GetDesire.RequestValues(mDesireId),
                new UseCase.UseCaseCallback<GetDesire.ResponseValue>(){


                    @Override
                    public void onSuccess(GetDesire.ResponseValue response) {
                        Desire desire = response.getDesire();
                        mDesireDetailView.showDesire(desire);
                    }

                    @Override
                    public void onError() {
                        //error handling
                    }
                });

    }

    @Override
    public void loadHavers(boolean forceUpdate) {
        loadHavers(forceUpdate || mFirstLoad, true);
        mFirstLoad = false;
    }

    private void loadHavers(boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) {
            mDesireDetailView.setLoadingIndicator(true);
        }

        GetHaverList.RequestValues requestValues = new GetHaverList.RequestValues();

        mUseCaseHandler.execute(mGetHaverList, requestValues, new UseCase.UseCaseCallback<GetHaverList.ResponseValue>() {
            @Override
            public void onSuccess(GetHaverList.ResponseValue response) {
                List<Haver> havers = response.getHavers();
                if (!mDesireDetailView.isActive()) {
                    return;
                }
                if (showLoadingUI) {
                    mDesireDetailView.setLoadingIndicator(false);
                }

                processHavers(havers);

            }

            @Override
            public void onError() {
                if (!mDesireDetailView.isActive()) {
                    return;
                }
                mDesireDetailView.showLoadingHaversError();
            }
        });
    }

    //Get own User instance & create haver
    //Equals "accept desire" as haver
    @Override
    public void setHaver() {

        GetUser.RequestValues requestValues = new GetUser.RequestValues(mDesireLogic.getLoggedInUserId());

        mUseCaseHandler.execute(mGetUser, requestValues,
                new UseCase.UseCaseCallback<GetUser.ResponseValue>() {
                    @Override
                    public void onSuccess(GetUser.ResponseValue response) {
                        User user = response.getUser();
                        setHaver(new Haver(user, new Date(), mDesireId));
                    }

                    @Override
                    public void onError() {
                        mDesireDetailView.showSetHaverError();
                    }
                });

    }

    private void setHaver(Haver haver) {

        SetHaver.RequestValues requestValues = new SetHaver.RequestValues(mDesireId, haver);

        mUseCaseHandler.execute(mSetHaver, requestValues,
                new UseCase.UseCaseCallback<SetHaver.ResponseValue>() {

                    @Override
                    public void onSuccess(SetHaver.ResponseValue response) {
                        //Haver haver = response.getHaver();
                        //TODO: refresh view
                    }

                    @Override
                    public void onError() {
                        mDesireDetailView.showSetHaverError();
                    }
                }
        );

    }

    public void getHaver(final long haverId) {

        final GetHaver.RequestValues requestValues = new GetHaver.RequestValues(mDesireId, haverId);

        mUseCaseHandler.execute(mGetHaver, requestValues,
                new UseCase.UseCaseCallback<GetHaver.ResponseValue>() {

                    @Override
                    public void onSuccess(GetHaver.ResponseValue response) {
                        Haver haver = response.getHaver();
                        acceptHaver(haverId, haver);
                    }

                    @Override
                    public void onError() {

                    }
                }
        );
    }

    @Override
    public void acceptHaver(long haverId, Haver haver) {

        AcceptHaver.RequestValues requestValues = new AcceptHaver.RequestValues(mDesireId, haverId, haver);

        mUseCaseHandler.execute(mAcceptHaver, requestValues,
                new UseCase.UseCaseCallback<AcceptHaver.ResponseValue>() {

                    @Override
                    public void onSuccess(AcceptHaver.ResponseValue response) {
                        Haver haver = response.getHaver();
                        /*haver.setAccepted(true);
                        setAcceptedWanter(haver);*/
                        //desire.setStatus(DesireStatus.STATUS_IN_PROGRESS);
                        //removeRejectedHaversFromView
                    }

                    @Override
                    public void onError() {
                        mDesireDetailView.showAcceptHaverError();
                    }

                }
        );
    }

    public void setAcceptedWanter(Haver haver) {
        UpdateHaver.RequestValues requestValues = new UpdateHaver.RequestValues(mDesireId, haver.getId(), haver);

        mUseCaseHandler.execute(mUpdateHaver, requestValues,
                new UseCase.UseCaseCallback<UpdateHaver.ResponseValue>() {

                    @Override
                    public void onSuccess(UpdateHaver.ResponseValue response) {
                        //TODO: refresh view
                    }

                    @Override
                    public void onError() {
                        mDesireDetailView.showAcceptHaverError();
                    }

                }
        );
    }

    private void processHavers(List<Haver> havers) {
        if (havers.isEmpty()) {
            //TODO no havers yet
        } else {
            mDesireDetailView.showHavers(havers);
        }
    }

    public boolean acceptedAsHaver() {
        //TODO
        return false;
    }

    /*public void openChat(User user){
        //TODO: open chat here;
        checkNotNull(user, "user cannot be null!");
        mDesireDetailView.showChatList(user.getID());
    }*/
}
