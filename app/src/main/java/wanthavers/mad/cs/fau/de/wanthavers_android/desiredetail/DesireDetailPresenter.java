package wanthavers.mad.cs.fau.de.wanthavers_android.desiredetail;

import android.support.annotation.NonNull;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Date;
import java.util.List;

import de.fau.cs.mad.wanthavers.common.Haver;
import de.fau.cs.mad.wanthavers.common.User;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCaseHandler;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.DesireLogic;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.AcceptDesire;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetDesire;
import de.fau.cs.mad.wanthavers.common.Desire;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetHaverList;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetUser;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.SetHaver;

public class DesireDetailPresenter implements DesireDetailContract.Presenter {


    private final DesireDetailContract.View mDesireDetailView;
    private boolean mFirstLoad = true;
    private DesireLogic mDesireLogic;

    //Use Cases
    private final UseCaseHandler mUseCaseHandler;
    private final GetDesire mGetDesire;
    private final GetHaverList mGetHaverList;
    private final SetHaver mSetHaver;
    private final AcceptDesire mAcceptDesire;
    private final GetUser mGetUser;


    @NonNull
    private long mDesireId;

    public DesireDetailPresenter(DesireLogic desireLogic, @NonNull UseCaseHandler useCaseHandler, @NonNull long desireId,
                                 @NonNull DesireDetailContract.View desireDetailView,
                                 @NonNull AcceptDesire acceptDesire, @NonNull GetDesire getDesire,
                                 @NonNull GetHaverList getHaverList, @NonNull GetUser getUser,
                                 @NonNull SetHaver setHaver){

        mUseCaseHandler = checkNotNull(useCaseHandler, "useCaseHandler cannot be null");
        mDesireDetailView = checkNotNull(desireDetailView, "desiredetail view cannot be null");
        mDesireId = desireId;
        mGetHaverList = checkNotNull(getHaverList);
        mAcceptDesire = checkNotNull(acceptDesire);
        mGetDesire = checkNotNull(getDesire);
        mSetHaver = checkNotNull(setHaver);
        mGetUser = checkNotNull(getUser);

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
                    }

                    @Override
                    public void onError() {
                        mDesireDetailView.showSetHaverError();
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

    /*public void openChat(User user){
        //TODO: open chat here;
        checkNotNull(user, "user cannot be null!");
        mDesireDetailView.showChatList(user.getID());
    }*/
}
