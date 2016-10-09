package wanthavers.mad.cs.fau.de.wanthavers_android.desiredetail;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.Chat;
import de.fau.cs.mad.wanthavers.common.DesireFlag;
import de.fau.cs.mad.wanthavers.common.Haver;
import de.fau.cs.mad.wanthavers.common.Location;
import de.fau.cs.mad.wanthavers.common.User;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.BasePresenter;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.BaseView;
import de.fau.cs.mad.wanthavers.common.Desire;


public interface DesireDetailContract {


    interface View extends BaseView<Presenter> {

        void showDesire(Desire desire, Haver acceptedHaver);

        void showDesireOpen(boolean isUnacceptedHaver);

        void showHavers(List<Haver> haver);

        boolean isActive();



        //Getter & Setter

        void setBidder(Haver bidder);

        double getBidInput();

        Haver getAcceptedHaver();

        boolean getInstantRecreate();

        DesireFlag getReport();



        //leave current view

        void closeView();

        void showChatDetailsUi(Chat chat);

        void showDesireCreate(Desire desire);

        void showMap(Location location);

        void showRating(long desireId);

        void showUserProfile(User user);



        //open/close popups

        void showAcceptDesirePopup(boolean initialCall);

        void closeAcceptDesirePopup();

        void closeDeleteDesirePopup();

        void showDeleteHaverPopup();

        void closeDeleteHaverPopup();

        void openRecreateDesirePopup();

        void closeRecreateDesirePopup();

        void showReportPopup();

        void closeReportPopup();

        void showUnacceptHaverPopup();

        void closeUnacceptHaverPopup();



        //Snackbar messages

        void showAcceptHaverError();

        void showDeleteDesireError();

        void showDeleteHaverError();

        void showFlagDesireError();

        void showGetChatForDesireError();

        void showLoadingDesireError();

        void showLoadingHaversError();

        void showSetHaverError();

        void showRecreateDesireError();

        void showTransactionSuccessMessage();

        void showUnacceptHaverError();

        void showUpdateDesireStatusError();

        void showUpdateRequestedPriceError();

    }



    interface Presenter extends BasePresenter {

        //UseCases/Server interaction

        void loadDesire();

        void loadHavers(boolean forceUpdate);

        void setHaver(boolean biddingAllowed);

        void updateRequestedPrice();

        void acceptHaver(long haverId, Haver haver);

        void unacceptHaver(Haver haver);

        void unacceptAndDeleteHaver(Haver haver);

        void openChat(long user2Id);

        void closeTransaction();

        void deleteDesire();

        void deleteHaver();

        void finishReport();

        void recreateDesire(Desire desire);


        //Redirect to Fragment

        void openMap(double lat, double lng);

        void openRating();

        void openUserProfile(User user);



        //Popups

        void closeDeletionPopup();

        void openDeleteHaverPopup();

        void closeHaverCancelPopup();

        void openModifyBidPopup(boolean initialCall);

        void closeModifyBidPopup();

        void openRecreateDesirePopup();

        void closeRecreateDesirePopup();

        void openReportPopup();

        void closeReportPopup();

        void openUnacceptHaverPopup();

        void closeUnacceptHaverPopup();

    }
}
