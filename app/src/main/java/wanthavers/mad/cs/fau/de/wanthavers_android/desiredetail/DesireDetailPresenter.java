package wanthavers.mad.cs.fau.de.wanthavers_android.desiredetail;

import android.support.annotation.NonNull;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Date;
import java.util.List;

import de.fau.cs.mad.wanthavers.common.Chat;
import de.fau.cs.mad.wanthavers.common.DesireFlag;
import de.fau.cs.mad.wanthavers.common.DesireStatus;
import de.fau.cs.mad.wanthavers.common.Haver;
import de.fau.cs.mad.wanthavers.common.User;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCaseHandler;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.DesireLogic;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.AcceptHaver;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.FlagDesire;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetAcceptedHaver;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetChatForDesire;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetDesire;
import de.fau.cs.mad.wanthavers.common.Desire;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetHaverList;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetUser;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.SetHaver;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.UpdateDesireStatus;

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
    private final GetAcceptedHaver mGetAcceptedHaver;
    private final GetChatForDesire mGetChatForDesire;
    private final UpdateDesireStatus mUpdateDesireStatus;
    private final FlagDesire mFlagDesire;

    @NonNull
    private long mDesireId;

    public DesireDetailPresenter(DesireLogic desireLogic, @NonNull UseCaseHandler useCaseHandler, @NonNull long desireId,
                                 @NonNull DesireDetailContract.View desireDetailView,
                                 @NonNull AcceptHaver acceptHaver, @NonNull GetDesire getDesire,
                                 @NonNull GetHaverList getHaverList, @NonNull GetUser getUser,
                                 @NonNull SetHaver setHaver, @NonNull GetAcceptedHaver getAcceptedHaver,
                                 @NonNull GetChatForDesire getChatForDesire, @NonNull UpdateDesireStatus updateDesireStatus,
                                 @NonNull FlagDesire flagDesire){

        mUseCaseHandler = checkNotNull(useCaseHandler, "useCaseHandler cannot be null");
        mDesireDetailView = checkNotNull(desireDetailView, "desiredetail view cannot be null");
        mDesireId = desireId;
        mGetHaverList = checkNotNull(getHaverList);
        mAcceptHaver = checkNotNull(acceptHaver);
        mGetDesire = checkNotNull(getDesire);
        mSetHaver = checkNotNull(setHaver);
        mGetUser = checkNotNull(getUser);
        mGetAcceptedHaver = checkNotNull(getAcceptedHaver);
        mGetChatForDesire = checkNotNull(getChatForDesire);
        mUpdateDesireStatus = checkNotNull(updateDesireStatus);
        mFlagDesire = checkNotNull(flagDesire);

        mDesireDetailView.setPresenter(this);
        mDesireLogic = desireLogic;
    }


    @Override
    public void start(){
        mDesireDetailView.showLoadingProgress();
        loadDesire();
        //loadHavers(false);
    }

    @Override
    public void loadDesire(){

        mUseCaseHandler.execute(mGetDesire, new GetDesire.RequestValues(mDesireId),
                new UseCase.UseCaseCallback<GetDesire.ResponseValue>(){


                    @Override
                    public void onSuccess(GetDesire.ResponseValue response) {
                        Desire desire = response.getDesire();
                        loadAcceptedHaver(desire);
                    }

                    @Override
                    public void onError() {
                        mDesireDetailView.showLoadingDesireError();
                    }
                });

    }

    private void loadAcceptedHaver(final Desire desire){

        GetAcceptedHaver.RequestValues requestValues = new GetAcceptedHaver.RequestValues(mDesireId);

        mUseCaseHandler.execute(mGetAcceptedHaver, requestValues,
                new UseCase.UseCaseCallback<GetAcceptedHaver.ResponseValue>() {
                    @Override
                    public void onSuccess(GetAcceptedHaver.ResponseValue response) {
                        Haver haver = response.getHaver();
                        mDesireDetailView.showDesire(desire, haver);
                    }

                    @Override
                    public void onError() {
                        mDesireDetailView.showDesire(desire, null);
                    }
                }
        );

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

        GetHaverList.RequestValues requestValues = new GetHaverList.RequestValues(mDesireId);

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

    public void getAcceptedHaver() {

        GetAcceptedHaver.RequestValues requestValues = new GetAcceptedHaver.RequestValues(mDesireId);

        mUseCaseHandler.execute(mGetAcceptedHaver, requestValues,
                new UseCase.UseCaseCallback<GetAcceptedHaver.ResponseValue>() {
                    @Override
                    public void onSuccess(GetAcceptedHaver.ResponseValue response) {
                        Haver haver = response.getHaver();

                        mDesireDetailView.showAcceptedHaver(haver);
                    }

                    @Override
                    public void onError() {
                        mDesireDetailView.showLoadingHaversError();
                    }
                }
        );

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

    @Override
    public void acceptHaver(long haverId, Haver haver) {

        AcceptHaver.RequestValues requestValues = new AcceptHaver.RequestValues(mDesireId, haverId, haver);

        mUseCaseHandler.execute(mAcceptHaver, requestValues,
                new UseCase.UseCaseCallback<AcceptHaver.ResponseValue>() {

                    @Override
                    public void onSuccess(AcceptHaver.ResponseValue response) {
                        //Haver haver = response.getHaver();
                        openRating();
                    }

                    @Override
                    public void onError() {
                        mDesireDetailView.showAcceptHaverError();
                    }

                }
        );
    }

    @Override
    public void sendMessage(long user2Id) {

        GetChatForDesire.RequestValues requestValues = new GetChatForDesire.RequestValues(user2Id, mDesireId);

        mUseCaseHandler.execute(mGetChatForDesire, requestValues,
                new UseCase.UseCaseCallback<GetChatForDesire.ResponseValue>() {

                    @Override
                    public void onSuccess(GetChatForDesire.ResponseValue response) {
                        openChatDetails(response.getChat());
                    }

                    @Override
                    public void onError() {
                        mDesireDetailView.showGetChatForDesireError();
                    }

                }
        );
    }

    public void closeTransaction() {

        UpdateDesireStatus.RequestValues requestValues = new UpdateDesireStatus.RequestValues(mDesireId, DesireStatus.STATUS_DONE);

        System.out.println("loggedinUserId " + mDesireLogic.getLoggedInUserId());

        mUseCaseHandler.execute(mUpdateDesireStatus, requestValues,
                new UseCase.UseCaseCallback<UpdateDesireStatus.ResponseValue>() {

                    @Override
                    public void onSuccess(UpdateDesireStatus.ResponseValue response) {
                        Desire updatedDesire = response.getDesire();
                        mDesireDetailView.showDesire(updatedDesire, null);
                        //mDesireDetailView.disableButton();
                        //mDesireDetailView.showRating();
                    }

                    @Override
                    public void onError() {
                        mDesireDetailView.showUpdateDesireStatusError();
                    }

                }
        );
    }

    @Override
    public void openDeletionDialog() {
        mDesireDetailView.openDeletionDialog();
    }

    @Override
    public void closeDeletionDialog() {
        mDesireDetailView.closeDeletionDialog();
    }

    @Override
    public void deleteDesire() {

        UpdateDesireStatus.RequestValues requestValues = new UpdateDesireStatus.RequestValues(mDesireId, DesireStatus.STATUS_DELETED);

        mUseCaseHandler.execute(mUpdateDesireStatus, requestValues,
                new UseCase.UseCaseCallback<UpdateDesireStatus.ResponseValue>() {

                    @Override
                    public void onSuccess(UpdateDesireStatus.ResponseValue response) {
                        mDesireDetailView.closeDeletionDialog();
                        mDesireDetailView.closeView();
                    }

                    @Override
                    public void onError() {
                        mDesireDetailView.showDeleteDesireError();
                    }

                }
        );
    }

    public void flagDesire(DesireFlag desireFlag) {

        FlagDesire.RequestValues requestValues = new FlagDesire.RequestValues(mDesireId, desireFlag);

        mUseCaseHandler.execute(mFlagDesire, requestValues,
                new UseCase.UseCaseCallback<FlagDesire.ResponseValue>() {
                    @Override
                    public void onSuccess(FlagDesire.ResponseValue response) {

                    }

                    @Override
                    public void onError() {
                        mDesireDetailView.showFlagDesireError();
                    }
                }
        );
    }

    //TODO: public void submitRating()

    private void processHavers(List<Haver> havers) {
        if (havers.isEmpty()) {
            mDesireDetailView.showAcceptButton(havers);
            //TODO no havers yet
        } else {
            mDesireDetailView.showHavers(havers);
            mDesireDetailView.showAcceptButton(havers);
        }

        mDesireDetailView.endLoadingProgress();
    }

    @Override
    public void openRating() {
        mDesireDetailView.showRating(mDesireId);
    }

    public void openChatDetails(@NonNull Chat chat) {

        if(chat == null){
            mDesireDetailView.showGetChatForDesireError();
            return;
        }

        mDesireDetailView.showChatDetailsUi(chat);
    }


    public void openChatList(User user){
        //TODO: open chat here;
        checkNotNull(user, "user cannot be null!");
        mDesireDetailView.showChatList(user.getId());
    }

    @Override
    public void openReportPopup() {
        mDesireDetailView.showReportPopup();
    }

    @Override
    public void finishReport() {
        DesireFlag desireFlag = mDesireDetailView.getReport();
        desireFlag.setDesireId(mDesireId);
        mDesireDetailView.closeReportPopup();
        flagDesire(desireFlag);
    }

    @Override
    public void closeReportPopup() {
        mDesireDetailView.closeReportPopup();
    }
}
