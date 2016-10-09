package wanthavers.mad.cs.fau.de.wanthavers_android.desiredetail;

import android.support.annotation.NonNull;
import android.widget.CheckBox;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.fau.cs.mad.wanthavers.common.Chat;
import de.fau.cs.mad.wanthavers.common.DesireFlag;
import de.fau.cs.mad.wanthavers.common.DesireStatus;
import de.fau.cs.mad.wanthavers.common.Haver;
import de.fau.cs.mad.wanthavers.common.Location;
import de.fau.cs.mad.wanthavers.common.User;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCaseHandler;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.DesireLogic;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.AcceptHaver;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.DeleteHaver;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.FlagDesire;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetAcceptedHaver;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetChatForDesire;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetDesire;
import de.fau.cs.mad.wanthavers.common.Desire;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetHaver;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetHaverList;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetUser;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.SetDesire;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.SetHaver;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.UnacceptHaver;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.UpdateDesireStatus;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.UpdateRequestedPrice;

public class DesireDetailPresenter implements DesireDetailContract.Presenter {


    private final DesireDetailContract.View mDesireDetailView;
    private DesireDetailActivity mActivity;
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
    private final DeleteHaver mDeleteHaver;
    private final GetHaver mGetHaver;
    private final UnacceptHaver mUnacceptHaver;
    private final UpdateRequestedPrice mUpdateRequestedPrice;
    private final SetDesire mCreateDesire;

    @NonNull
    private long mDesireId;

    public DesireDetailPresenter(@NonNull DesireDetailActivity activity, DesireLogic desireLogic, @NonNull UseCaseHandler useCaseHandler,
                                 @NonNull long desireId, @NonNull DesireDetailContract.View desireDetailView,
                                 @NonNull AcceptHaver acceptHaver, @NonNull GetDesire getDesire,
                                 @NonNull GetHaverList getHaverList, @NonNull GetUser getUser,
                                 @NonNull SetHaver setHaver, @NonNull GetAcceptedHaver getAcceptedHaver,
                                 @NonNull GetChatForDesire getChatForDesire, @NonNull UpdateDesireStatus updateDesireStatus,
                                 @NonNull FlagDesire flagDesire, @NonNull DeleteHaver deleteHaver,
                                 @NonNull GetHaver getHaver, @NonNull UnacceptHaver unacceptHaver,
                                 @NonNull UpdateRequestedPrice updateRequestedPrice, @NonNull SetDesire createDesire){

        mActivity = checkNotNull(activity, "activity cannot be null");
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
        mDeleteHaver = checkNotNull(deleteHaver);
        mGetHaver = checkNotNull(getHaver);
        mUnacceptHaver = checkNotNull(unacceptHaver);
        mUpdateRequestedPrice = checkNotNull(updateRequestedPrice);
        mCreateDesire = checkNotNull(createDesire);

        mDesireDetailView.setPresenter(this);
        mDesireLogic = desireLogic;
    }


    @Override
    public void start() {
        loadDesire();
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
                        mDesireDetailView.closeView();
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
                        if (desire.isBiddingAllowed()) {
                            mDesireDetailView.setBidder(haver);
                        }
                        mDesireDetailView.showDesire(desire, haver);
                    }

                    @Override
                    public void onError() {
                        showUnacceptedHaverView(desire);
                    }
                }
        );

    }

    @Override
    public void loadHavers(boolean forceUpdate) {
        loadHavers();
        mFirstLoad = false;
    }

    private void loadHavers() {

        GetHaverList.RequestValues requestValues = new GetHaverList.RequestValues(mDesireId);

        mUseCaseHandler.execute(mGetHaverList, requestValues, new UseCase.UseCaseCallback<GetHaverList.ResponseValue>() {
            @Override
            public void onSuccess(GetHaverList.ResponseValue response) {
                List<Haver> havers = response.getHavers();
                if (!mDesireDetailView.isActive()) {
                    return;
                }

                mDesireDetailView.showHavers(havers);

            }

            @Override
            public void onError() {
                mDesireDetailView.showLoadingHaversError();
            }
        });
    }

    //Get own User instance & create haver
    //Equals "accept desire" as haver
    @Override
    public void setHaver(final boolean biddingAllowed) {

        mActivity.findViewById(R.id.accept_desire).setClickable(false);

        if (biddingAllowed && mDesireDetailView.getBidInput() == -1) {
            return;
        }
        if (biddingAllowed) {
            mDesireDetailView.closeAcceptDesirePopup();
        }

        GetUser.RequestValues requestValues = new GetUser.RequestValues(mDesireLogic.getLoggedInUserId());

        mUseCaseHandler.execute(mGetUser, requestValues,
                new UseCase.UseCaseCallback<GetUser.ResponseValue>() {
                    @Override
                    public void onSuccess(GetUser.ResponseValue response) {

                        User user = response.getUser();
                        Haver haver = new Haver(user, new Date(), mDesireId);

                        if (biddingAllowed) {
                            haver.setRequestedPrice(mDesireDetailView.getBidInput());
                        }

                        setHaver(haver, biddingAllowed);
                    }

                    @Override
                    public void onError() {
                        mActivity.findViewById(R.id.accept_desire).setClickable(true);
                        mDesireDetailView.showSetHaverError();
                    }
                });

    }

    private void setHaver(Haver haver, final boolean biddingAllowed) {

        final SetHaver.RequestValues requestValues = new SetHaver.RequestValues(mDesireId, haver);

        mUseCaseHandler.execute(mSetHaver, requestValues,
                new UseCase.UseCaseCallback<SetHaver.ResponseValue>() {

                    @Override
                    public void onSuccess(SetHaver.ResponseValue response) {
                        if (biddingAllowed) {
                            mDesireDetailView.setBidder(response.getHaver());
                        }
                        loadDesire();
                    }

                    @Override
                    public void onError() {
                        mActivity.findViewById(R.id.accept_desire).setClickable(true);
                        mDesireDetailView.showSetHaverError();
                    }
                }
        );

    }

    @Override
    public void updateRequestedPrice() {

        double bid = mDesireDetailView.getBidInput();

        if (mDesireDetailView.getBidInput() == -1) {
            mDesireDetailView.closeAcceptDesirePopup();
            return;
        }

        UpdateRequestedPrice.RequestValues requestValues =
                new UpdateRequestedPrice.RequestValues(mDesireId, mDesireLogic.getLoggedInUserId(), bid);

        mUseCaseHandler.execute(mUpdateRequestedPrice, requestValues,
                new UseCase.UseCaseCallback<UpdateRequestedPrice.ResponseValue>() {
                    @Override
                    public void onSuccess(UpdateRequestedPrice.ResponseValue response) {
                        mDesireDetailView.setBidder(response.getHaver());
                        mDesireDetailView.closeAcceptDesirePopup();
                    }

                    @Override
                    public void onError() {
                        mDesireDetailView.closeAcceptDesirePopup();
                        mDesireDetailView.showUpdateRequestedPriceError();
                    }
                });

    }

    @Override
    public void acceptHaver(long haverId, Haver haver) {

        AcceptHaver.RequestValues requestValues = new AcceptHaver.RequestValues(mDesireId, haverId, haver);

        mUseCaseHandler.execute(mAcceptHaver, requestValues,
                new UseCase.UseCaseCallback<AcceptHaver.ResponseValue>() {

                    @Override
                    public void onSuccess(AcceptHaver.ResponseValue response) {
                        loadDesire();
                        mDesireDetailView.showHavers(new ArrayList<Haver>(0));
                    }

                    @Override
                    public void onError() {
                        mDesireDetailView.showAcceptHaverError();
                    }

                }
        );
    }

    @Override
    public void unacceptHaver(Haver haver) {
        UnacceptHaver.RequestValues requestValues = new UnacceptHaver.RequestValues(mDesireId, haver.getId(), haver);

        mUseCaseHandler.execute(mUnacceptHaver, requestValues,
                new UseCase.UseCaseCallback<UnacceptHaver.ResponseValue>() {
                    @Override
                    public void onSuccess(UnacceptHaver.ResponseValue response) {
                        loadDesire();
                    }

                    @Override
                    public void onError() {
                        mDesireDetailView.showUnacceptHaverError();
                    }
                });
    }

    @Override
    public void unacceptAndDeleteHaver(Haver haver) {
        UnacceptHaver.RequestValues requestValues = new UnacceptHaver.RequestValues(mDesireId, haver.getId(), haver);

        mUseCaseHandler.execute(mUnacceptHaver, requestValues,
                new UseCase.UseCaseCallback<UnacceptHaver.ResponseValue>() {
                    @Override
                    public void onSuccess(UnacceptHaver.ResponseValue response) {
                        deleteHaver();
                    }

                    @Override
                    public void onError() {
                        mDesireDetailView.showUnacceptHaverError();
                    }
                });
    }

    @Override
    public void openChat(long user2Id) {

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

    @Override
    public void closeTransaction() {

        UpdateDesireStatus.RequestValues requestValues = new UpdateDesireStatus.RequestValues(mDesireId, DesireStatus.STATUS_DONE);

        mUseCaseHandler.execute(mUpdateDesireStatus, requestValues,
                new UseCase.UseCaseCallback<UpdateDesireStatus.ResponseValue>() {

                    @Override
                    public void onSuccess(UpdateDesireStatus.ResponseValue response) {
                        Desire updatedDesire = response.getDesire();
                        Haver haver = mDesireDetailView.getAcceptedHaver();
                        mDesireDetailView.showDesire(updatedDesire, haver);
                        mDesireDetailView.showTransactionSuccessMessage();
                    }

                    @Override
                    public void onError() {
                        mDesireDetailView.showUpdateDesireStatusError();
                    }

                }
        );
    }

    @Override
    public void deleteDesire() {

        UpdateDesireStatus.RequestValues requestValues = new UpdateDesireStatus.RequestValues(mDesireId, DesireStatus.STATUS_DELETED);

        mUseCaseHandler.execute(mUpdateDesireStatus, requestValues,
                new UseCase.UseCaseCallback<UpdateDesireStatus.ResponseValue>() {

                    @Override
                    public void onSuccess(UpdateDesireStatus.ResponseValue response) {
                        mDesireDetailView.closeDeleteDesirePopup();
                        mDesireDetailView.closeView();
                    }

                    @Override
                    public void onError() {
                        mDesireDetailView.closeDeleteDesirePopup();
                        mDesireDetailView.showDeleteDesireError();
                    }

                }
        );
    }

    private void flagDesire(DesireFlag desireFlag) {

        FlagDesire.RequestValues requestValues = new FlagDesire.RequestValues(mDesireId, desireFlag);

        mUseCaseHandler.execute(mFlagDesire, requestValues,
                new UseCase.UseCaseCallback<FlagDesire.ResponseValue>() {
                    @Override
                    public void onSuccess(FlagDesire.ResponseValue response) {
                        mDesireDetailView.closeReportPopup();
                    }

                    @Override
                    public void onError() {
                        mDesireDetailView.closeReportPopup();
                        mDesireDetailView.showFlagDesireError();
                    }
                }
        );
    }

    private void showUnacceptedHaverView(final Desire desire) {

        GetHaver.RequestValues requestValues = new GetHaver.RequestValues(mDesireId, mDesireLogic.getLoggedInUserId());

        mUseCaseHandler.execute(mGetHaver, requestValues,
                new UseCase.UseCaseCallback<GetHaver.ResponseValue>() {
                    @Override
                    public void onSuccess(GetHaver.ResponseValue response) {
                        if (response.getHaver() != null) {
                            if (desire.isBiddingAllowed()) {
                                mDesireDetailView.setBidder(response.getHaver());
                            }
                            mDesireDetailView.showDesire(desire, null);
                            mDesireDetailView.showDesireOpen(true);
                        } else {
                            mDesireDetailView.showDesire(desire, null);
                            mDesireDetailView.showDesireOpen(false);
                        }
                    }

                    @Override
                    public void onError() {
                        mDesireDetailView.showLoadingDesireError();
                        mDesireDetailView.closeView();
                    }
                }
        );
    }

    @Override
    public void deleteHaver () {

        DeleteHaver.RequestValues requestValues = new DeleteHaver.RequestValues(mDesireId, mDesireLogic.getLoggedInUserId());

        mUseCaseHandler.execute(mDeleteHaver, requestValues,
                new UseCase.UseCaseCallback<DeleteHaver.ResponseValue>() {
                    @Override
                    public void onSuccess(DeleteHaver.ResponseValue response) {
                        mDesireDetailView.showDesireOpen(false);
                        mDesireDetailView.closeDeleteHaverPopup();
                    }

                    @Override
                    public void onError() {
                        mDesireDetailView.closeDeleteHaverPopup();
                        mDesireDetailView.showDeleteHaverError();
                    }
                }
        );
    }


    private void openChatDetails(@NonNull Chat chat) {

        if(chat == null){
            mDesireDetailView.showGetChatForDesireError();
            return;
        }

        mDesireDetailView.showChatDetailsUi(chat);
    }

    @Override
    public void finishReport() {
        DesireFlag desireFlag = mDesireDetailView.getReport();
        desireFlag.setDesireId(mDesireId);
        closeReportPopup();
        flagDesire(desireFlag);
    }

    @Override
    public void recreateDesire(Desire desire) {
        if (mDesireDetailView.getInstantRecreate()) {
            instantRecreateDesire(desire);
        } else {
            mDesireDetailView.closeRecreateDesirePopup();
            mDesireDetailView.showDesireCreate(desire);
        }
    }

    private void instantRecreateDesire(Desire desire) {

        Desire newDesire = new Desire();

        newDesire.setTitle(desire.getTitle());
        newDesire.setDescription(desire.getDescription());
        newDesire.setPrice(desire.getPrice());
        newDesire.setCurrency(desire.getCurrency());
        newDesire.setBiddingAllowed(desire.isBiddingAllowed());
        newDesire.setCategoryId(desire.getCategoryId());
        newDesire.setCreator(desire.getCreator());
        newDesire.setValidTimespan(desire.getValidTimespan());
        newDesire.setDropzone_lat(desire.getDropzone_lat());
        newDesire.setDropzone_long(desire.getDropzone_long());
        newDesire.setDropzone_string(desire.getDropzone_string());
        newDesire.setImage(desire.getImage());

        SetDesire.RequestValues requestValues = new SetDesire.RequestValues(newDesire);

        mUseCaseHandler.execute(mCreateDesire, requestValues,
                new UseCase.UseCaseCallback<SetDesire.ResponseValue>() {
                    @Override
                    public void onSuccess(SetDesire.ResponseValue response) {
                        closeRecreateDesirePopup();
                        mDesireDetailView.closeView();
                    }

                    @Override
                    public void onError() {
                        closeRecreateDesirePopup();
                        mDesireDetailView.showRecreateDesireError();
                    }
                });
    }

    /**
     *
     * Redirect to Fragment
     *
     */

    @Override
    public void openMap(double lat, double lng){
        Location location = new Location();
        location.setLat(lat);
        location.setLon(lng);
        mDesireDetailView.showMap(location);
    }

    @Override
    public void openRating() {
        mDesireDetailView.showRating(mDesireId);
    }

    @Override
    public void openUserProfile(User user) {
        mDesireDetailView.showUserProfile(user);
    }

    /**
     *
     * Popups
     *
     */

    @Override
    public void closeDeletionPopup() {
        mDesireDetailView.closeDeleteDesirePopup();
    }

    @Override
    public void openDeleteHaverPopup() {
        mDesireDetailView.showDeleteHaverPopup();
    }

    @Override
    public void closeHaverCancelPopup() {
        mDesireDetailView.closeDeleteHaverPopup();
    }

    @Override
    public void openModifyBidPopup(boolean initialCall) {
        mDesireDetailView.showAcceptDesirePopup(initialCall);
    }

    @Override
    public void closeModifyBidPopup() {
        mDesireDetailView.closeAcceptDesirePopup();
    }

    @Override
    public void openRecreateDesirePopup() {
        mDesireDetailView.openRecreateDesirePopup();
    }

    @Override
    public void closeRecreateDesirePopup() {
        mDesireDetailView.closeRecreateDesirePopup();
    }

    @Override
    public void openReportPopup() {
        mDesireDetailView.showReportPopup();
    }

    @Override
    public void closeReportPopup() {
        mDesireDetailView.closeReportPopup();
    }

    @Override
    public void openUnacceptHaverPopup() {
        mDesireDetailView.showUnacceptHaverPopup();
    }

    @Override
    public void closeUnacceptHaverPopup() {
        mDesireDetailView.closeUnacceptHaverPopup();
    }

}
