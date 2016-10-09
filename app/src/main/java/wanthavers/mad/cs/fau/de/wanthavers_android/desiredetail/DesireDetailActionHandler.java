package wanthavers.mad.cs.fau.de.wanthavers_android.desiredetail;

import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.common.DesireStatus;
import de.fau.cs.mad.wanthavers.common.Haver;
import de.fau.cs.mad.wanthavers.common.User;

public class DesireDetailActionHandler {

    private DesireDetailContract.Presenter mListener;

    public DesireDetailActionHandler(DesireDetailContract.Presenter listener)  {
        mListener = listener;
    }

    public void openChat(long user2Id) {
        mListener.openChat(user2Id);
    }

    public void wanterAcceptDesire(long haverId, Haver haver) {
        mListener.acceptHaver(haverId, haver);
    }

    public void buttonHaverAcceptDesire(boolean biddingAllowed) {
        if (biddingAllowed) {
            mListener.openModifyBidPopup(true);
        } else {
            mListener.setHaver(false);
        }
    }

    public void openModifyBidPopup() {
        mListener.openModifyBidPopup(false);
    }

    public void submitModifiedBid() {
        mListener.updateRequestedPrice();
    }

    public void buttonSubmitBid() {
        mListener.setHaver(true);
    }

    public void buttonCancelBid() {
        mListener.closeModifyBidPopup();
    }

    public void buttonUnacceptHaver() {
        mListener.openUnacceptHaverPopup();
    }

    public void buttonSumbmitUnacceptHaver(Haver haver) {
        mListener.unacceptHaver(haver);
        mListener.closeUnacceptHaverPopup();
    }

    public void buttonCancelUnacceptHaver() {
        mListener.closeUnacceptHaverPopup();
    }

    public void openHaverCancelPopup() {
        mListener.openDeleteHaverPopup();
    }

    public void buttonSubmitHaverCancel(Desire desire, Haver haver) {
        if (desire.getStatus() == DesireStatus.STATUS_IN_PROGRESS) {
            mListener.unacceptAndDeleteHaver(haver);
        } else if (desire.getStatus() == DesireStatus.STATUS_OPEN) {
            mListener.deleteHaver();
        }
    }

    public void buttonCancelHaverCancel() {
        mListener.closeHaverCancelPopup();
    }

    public void openRating() {
        mListener.openRating();
    }

    public void openUserProfile(User user) {
        mListener.openUserProfile(user);
    }

    public void buttonSubmitReport() {
        mListener.finishReport();
    }

    public void buttonCancelReport() {
        mListener.closeReportPopup();
    }

    public void buttonSubmitDeletion() {
        mListener.deleteDesire();
    }

    public void buttonCancelDeletion() {
        mListener.closeDeletionPopup();
    }

    public void onPressedLocation(double lat, double lng){
        mListener.openMap(lat,lng);
    }

    public void buttonCloseTransaction() {
        mListener.closeTransaction();
    }

    public void openRecreateDesirePopup() {
        mListener.openRecreateDesirePopup();
    }

    public void buttonSubmitRecreateDesire(Desire desire) {
        mListener.recreateDesire(desire);
    }

    public void buttonCancelRecreateDesire() {
        mListener.closeRecreateDesirePopup();
    }

}