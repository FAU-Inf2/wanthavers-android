package wanthavers.mad.cs.fau.de.wanthavers_android.desiredetail;

import android.view.View;

import de.fau.cs.mad.wanthavers.common.Haver;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.DesiredetailFragBinding;

public class DesireDetailActionHandler {

    private DesireDetailContract.Presenter mListener;
    private DesiredetailFragBinding mDesireDetailFragBinding;
    private long mDesireId;

    public DesireDetailActionHandler(long desireId, DesiredetailFragBinding desireDetailFragment, DesireDetailContract.Presenter listener)  {
        mListener = listener;
        mDesireDetailFragBinding = desireDetailFragment;
        mDesireId = desireId;
    }

    public void haverAccept() {
        mListener.setHaver();
        mDesireDetailFragBinding.buttonAcceptDesire.setVisibility(View.GONE);
        mDesireDetailFragBinding.placeholder.setVisibility(View.GONE);
    }

    public void haverSendMessage() {

    }

    public void wanterAccept(long haverId, Haver haver) {
        mListener.acceptHaver(haverId, haver);
        mListener.getAcceptedHaver();
    }

    public void wanterSendMessage() {

    }

    public void setDesireId(long desireId) {
        mDesireId = desireId;
    }

}