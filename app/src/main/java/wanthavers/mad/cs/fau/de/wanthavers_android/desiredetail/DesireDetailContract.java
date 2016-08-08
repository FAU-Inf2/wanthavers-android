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

        //void showAcceptButton(List<Haver> havers);

        void showAcceptedHaver(Haver haver);

        void showDesire(Desire desire, Haver haver);

        void showHavers(List<Haver> haver);

        void showChatList(long userid);

        void showChatDetailsUi(Chat chat);

        void setLoadingIndicator(final boolean active);

        void showHaverAcceptStatus();

        void showLoadingHaversError();

        void showAcceptHaverError();

        void showLoadingDesireError();

        void showSetHaverError();

        void showGetChatForDesireError();

        void showUpdateDesireStatusError();

        void showDeleteHaverError();

        void showFlagDesireError();

        void showReportPopup();

        boolean isActive();

        void closeReportPopup();

        DesireFlag getReport();

        void endLoadingProgress();

        void showRating(long desireId);

        void closeView();

        void closeAcceptDesirePopup();

        void showDeleteDesireError();

        void openDeletionDialog();

        void closeDeletionDialog();

        void showMap(Location location);

        void hideFinishDesire();

        void showUnacceptedHaverView(boolean active);

        void showTransactionSuccessMessage();
    }



    interface Presenter extends BasePresenter {

        void deleteHaver();

        void loadDesire();

        void loadHavers(boolean forceUpdate);

        void setHaver();

        void acceptHaver(long haverId, Haver haver);

        void getAcceptedHaver();

        void sendMessage(long user2Id);

        void closeTransaction();

        //void getHaver(final long haverId);

        void showUnacceptedHaverView(Desire desire);

        void openChatList(User user);

        void openReportPopup();

        void finishReport();

        void openRating();

        void closeAcceptDesirePopup();

        void closeReportPopup();

        void deleteDesire();

        void openDeletionDialog();

        void closeDeletionDialog();

        void createMap(double lat, double lng);

    }
}
