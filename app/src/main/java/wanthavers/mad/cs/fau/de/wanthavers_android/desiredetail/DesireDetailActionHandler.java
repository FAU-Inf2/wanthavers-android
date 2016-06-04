package wanthavers.mad.cs.fau.de.wanthavers_android.desiredetail;

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
    }

    public void haverSendMessage() {

    }

    public void wanterAccept() {

    }

    public void wanterSendMessage() {

    }

    public void setDesireId(long desireId) {
        mDesireId = desireId;
    }

}