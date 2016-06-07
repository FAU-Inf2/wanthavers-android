package wanthavers.mad.cs.fau.de.wanthavers_android.desiredetail;

import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import de.fau.cs.mad.wanthavers.common.Haver;
import de.fau.cs.mad.wanthavers.common.Media;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.DesiredetailFragBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.RoundedTransformation;

public class DesireDetailActionHandler {

    private DesireDetailContract.Presenter mListener;
    private DesiredetailFragBinding mDesireDetailFragBinding;

    public DesireDetailActionHandler(DesiredetailFragBinding desireDetailFragment, DesireDetailContract.Presenter listener)  {
        mListener = listener;
        mDesireDetailFragBinding = desireDetailFragment;
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
        mDesireDetailFragBinding.setHaver(haver);
        mDesireDetailFragBinding.haverList.setVisibility(View.GONE);
        mDesireDetailFragBinding.noHavers.setVisibility(View.GONE);
        mDesireDetailFragBinding.acceptedHaverBar.setVisibility(View.VISIBLE);

        Media mediaHaver = haver.getUser().getImage();
        if (mediaHaver != null) {
            final ImageView profileView = mDesireDetailFragBinding.imageAcceptedHaver;
            Picasso.with(mDesireDetailFragBinding.getRoot().getContext()).load(mediaHaver.getLowRes()).transform(new RoundedTransformation(200,0)).into(profileView);
        } else{
            //else case is neccessary as the image is otherwise overwritten on scroll
            final ImageView profileView = mDesireDetailFragBinding.imageAcceptedHaver;
            profileView.setImageResource(R.drawable.no_pic);
        }
    }

    public void wanterSendMessage() {

    }

}