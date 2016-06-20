package wanthavers.mad.cs.fau.de.wanthavers_android.rating;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.common.Haver;
import de.fau.cs.mad.wanthavers.common.Media;
import de.fau.cs.mad.wanthavers.common.User;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.RatingFragBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.DesireLogic;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.RoundedTransformation;

import static com.google.common.base.Preconditions.checkNotNull;

public class RatingFragment extends Fragment implements RatingContract.View {

    private DesireLogic mDesireLogic;
    private static final String DESIRE_ID = "DESIRE_ID";
    private RatingFragBinding mRatingFragBinding;
    private RatingContract.Presenter mPresenter;

    public RatingFragment() {
        //Requires empty public constructor
    }

    public static RatingFragment newInstance() {
        return new RatingFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mRatingFragBinding = RatingFragBinding.inflate(inflater, container, false);
        mRatingFragBinding.setPresenter(mPresenter);

        return mRatingFragBinding.getRoot();

    }

    @Override
    public void setPresenter(@NonNull RatingContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void showDesire(Desire desire) {
        mRatingFragBinding.setDesire(desire);
        mRatingFragBinding.setDesirelogic(mDesireLogic);

        User creator = desire.getCreator();

        //Show desire image
        Media mediaDesire = desire.getImage();
        if (mediaDesire != null) {
            final ImageView profileView = mRatingFragBinding.imageDesire;
            Picasso.with(mRatingFragBinding.getRoot().getContext()).load(mediaDesire.getLowRes()).transform(new RoundedTransformation(200,0)).into(profileView);
        } else{
            //else case is neccessary as the image is otherwise overwritten on scroll
            final ImageView profileView = mRatingFragBinding.imageDesire;
            profileView.setImageResource(R.drawable.no_pic);
        }

        //Show Wanter image
        Media mediaWanter = creator.getImage();
        if (mediaWanter != null) {
            final ImageView profileView = mRatingFragBinding.imageWanter;
            Picasso.with(mRatingFragBinding.getRoot().getContext()).load(mediaWanter.getLowRes()).transform(new RoundedTransformation(200,0)).into(profileView);
        } else{
            //else case is neccessary as the image is otherwise overwritten on scroll
            final ImageView profileView = mRatingFragBinding.imageWanter;
            profileView.setImageResource(R.drawable.no_pic);
        }
        mPresenter.getAcceptedHaver();
    }

    @Override
    public void showAcceptedHaver(Haver haver) {
        //Show Haver image
        Media mediaHaver = haver.getUser().getImage();
        if (mediaHaver != null) {
            final ImageView profileView = mRatingFragBinding.imageHaver;
            Picasso.with(mRatingFragBinding.getRoot().getContext()).load(mediaHaver.getLowRes()).transform(new RoundedTransformation(200,0)).into(profileView);
        } else{
            //else case is neccessary as the image is otherwise overwritten on scroll
            final ImageView profileView = mRatingFragBinding.imageHaver;
            profileView.setImageResource(R.drawable.no_pic);
        }
    }

    private void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showLoadingHaverError() {
        showMessage(getString(R.string.loading_haver_error));
    }

    @Override
    public void showLoadingDesireError() {
        showMessage(getString(R.string.loading_desire_error));
    }

    @Override
    public void showCreateRatingError() {
        showMessage(getString(R.string.create_rating_error));
    }

    public void setDesireLogic(DesireLogic desireLogic) {
        mDesireLogic = desireLogic;
    }
}