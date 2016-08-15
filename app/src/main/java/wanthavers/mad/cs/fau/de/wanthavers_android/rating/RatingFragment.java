package wanthavers.mad.cs.fau.de.wanthavers_android.rating;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import wanthavers.mad.cs.fau.de.wanthavers_android.desiredetail.DesireDetailActivity;
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

        setHasOptionsMenu(true);

        return mRatingFragBinding.getRoot();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.rating_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_finish_rating:
                mPresenter.finishRating(mRatingFragBinding.getDesire(), mRatingFragBinding.getHaver());
               break;
        }
        return true;
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

        System.out.println("was here");
        mRatingFragBinding.setDesire(desire);
        mRatingFragBinding.setDesirelogic(mDesireLogic);

        User creator = desire.getCreator();

        //Show desire image
        Media mediaDesire = desire.getImage();
        if (mediaDesire != null) {
            final ImageView profileView = mRatingFragBinding.imageDesire;
            Picasso.with(mRatingFragBinding.getRoot().getContext()).load(mediaDesire.getLowRes()).transform(new RoundedTransformation(1000,0)).into(profileView);
        } else{
            //else case is neccessary as the image is otherwise overwritten on scroll
            final ImageView profileView = mRatingFragBinding.imageDesire;
            profileView.setImageResource(R.drawable.no_pic);
        }

        //Show Wanter image
        Media mediaWanter = creator.getImage();
        if (mediaWanter != null) {
            final ImageView profileView = mRatingFragBinding.imageWanter;
            Picasso.with(mRatingFragBinding.getRoot().getContext()).load(mediaWanter.getLowRes()).transform(new RoundedTransformation(1000,0)).into(profileView);
        } else{
            //else case is neccessary as the image is otherwise overwritten on scroll
            final ImageView profileView = mRatingFragBinding.imageWanter;
            profileView.setImageResource(R.drawable.no_pic);
        }
    }

    @Override
    public void showAcceptedHaver(Haver haver) {
        //Show Haver image
        mRatingFragBinding.setHaver(haver);

        Media mediaHaver = haver.getUser().getImage();
        if (mediaHaver != null) {
            final ImageView profileView = mRatingFragBinding.imageHaver;
            Picasso.with(mRatingFragBinding.getRoot().getContext()).load(mediaHaver.getLowRes()).transform(new RoundedTransformation(1000,0)).into(profileView);
        } else{
            //else case is neccessary as the image is otherwise overwritten on scroll
            final ImageView profileView = mRatingFragBinding.imageHaver;
            profileView.setImageResource(R.drawable.no_pic);
        }
    }

    @Override
    public void showRatingScreen() {
        mRatingFragBinding.ratingLoadingScreen.setVisibility(View.GONE);
        mRatingFragBinding.ratingMainScreen.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNoRatingSet() {
        showMessage(getString(R.string.no_rating_set));
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

    @Override
    public void closeRatingWindow() {
        getActivity().finish();
    }
}
