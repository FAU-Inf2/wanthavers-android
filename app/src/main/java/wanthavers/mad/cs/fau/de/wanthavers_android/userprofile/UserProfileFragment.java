package wanthavers.mad.cs.fau.de.wanthavers_android.userprofile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.common.Media;
import de.fau.cs.mad.wanthavers.common.User;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.UserprofileFragBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.DesireLogic;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.CircularImageView;

import static com.google.common.base.Preconditions.checkNotNull;

public class UserProfileFragment extends Fragment implements UserProfileContract.View {

    private UserProfileContract.Presenter mPresenter;
    private UserprofileFragBinding mUserprofileFragBinding;
    private DesireLogic mDesireLogic;
    private UserProfileAdapter mUserProfileAdapter;
    private int mSentDataCount = 0;

    public UserProfileFragment() {
        //requires empty public constructor
    }

    public static UserProfileFragment newInstance() {
        return new UserProfileFragment();
    }

    @Override
    public void setPresenter(UserProfileContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mUserprofileFragBinding = UserprofileFragBinding.inflate(inflater, container, false);

        User user = (User) getActivity().getIntent().getSerializableExtra("user");
        mUserprofileFragBinding.setUser(user);
        mPresenter.setUserId(user.getId());
        mUserprofileFragBinding.setDesireLogic(mDesireLogic);

        //show User image
        Media mediaUser = user.getImage();
        if (mediaUser != null) {
            final CircularImageView profileView = mUserprofileFragBinding.userProfilePicture;
            Picasso.with(mUserprofileFragBinding.getRoot().getContext()).load(mediaUser.getLowRes()).into(profileView);
        } else{
            //else case is neccessary as the image is otherwise overwritten on scroll
            final ImageView profileView = mUserprofileFragBinding.userProfilePicture;
            profileView.setImageResource(R.drawable.no_pic);
        }

        //set up desire history
        RecyclerView recyclerView = mUserprofileFragBinding.userProfileDesireHistory;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        //set up list
        mUserProfileAdapter = new UserProfileAdapter(new ArrayList<Desire>(0));
        recyclerView.setAdapter(mUserProfileAdapter);

        setHasOptionsMenu(true);

        return mUserprofileFragBinding.getRoot();
    }

    @Override
    public void setDesireLogic(DesireLogic desireLogic) {
        mDesireLogic = desireLogic;
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
    public void showDesireHistory(List<Desire> desireList) {
        mUserProfileAdapter.replaceData(desireList);
    }

    @Override
    public void showFinishedDesireStatistics(int finishedDesires) {
        mUserprofileFragBinding.userProfileFinishedDesires.setText(Integer.toString(finishedDesires));
        if (finishedDesires > 0) {
            mUserprofileFragBinding.userProfileDesireHistoryCard.setVisibility(View.VISIBLE);
        } else {
            mUserprofileFragBinding.userProfileDesireHistoryCard.setVisibility(View.GONE);
        }
        finishLoading();
    }

    @Override
    public void showCanceledDesireStatistics(int canceledDesires) {
        mUserprofileFragBinding.userProfileCanceledDesires.setText(Integer.toString(canceledDesires));
        finishLoading();
    }

    private void finishLoading() {
        mSentDataCount++;
        if (mSentDataCount == 2) {
            mSentDataCount = 0;
            mUserprofileFragBinding.userProfileLoadingScreen.setVisibility(View.GONE);
            mUserprofileFragBinding.userProfileMainScreen.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showGetFinishedDesiresError() {
        if (isAdded()) {
            showMessage(getString(R.string.user_profile_loading_error));
        }
    }

    @Override
    public void showGetCanceledDesiresError() {
        if (isAdded()) {
            showMessage(getString(R.string.user_profile_loading_error));
        }
    }

    private void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }
}
