package wanthavers.mad.cs.fau.de.wanthavers_android.login;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.VideoView;

import de.fau.cs.mad.wanthavers.common.User;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.LoginFragBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.StartupFragBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.desirelist.DesireListActivity;
import wanthavers.mad.cs.fau.de.wanthavers_android.eastereggone.EasterEggActivity;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.CenterCropVideoView;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.SharedPreferencesHelper;
import wanthavers.mad.cs.fau.de.wanthavers_android.welcome.WelcomeActivity;

public class StartUpFragment extends Fragment implements LoginContract.View {
    private StartupFragBinding mViewDataBinding;
    private LoginContract.Presenter mPresenter;
    private double mFreeDummyUsersCounter = 0;

    private MediaPlayer mp = null;
    CenterCropVideoView mVideoView = null;

    public StartUpFragment(){
        //Requires empty public constructor
    }
    public static StartUpFragment newInstance(){ return new StartUpFragment();}

    @Override
    public void setPresenter(@NonNull LoginContract.Presenter presenter) {
        //mPresenter = checkNotNull(presenter);
        mPresenter = presenter;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewDataBinding.setPresenter(mPresenter);
        checkButtons();
        mPresenter.start();  //TODO JuG check if needed

    }
    @Override
    public void onResume()  {
        super.onResume();

        startVideo();
        checkButtons();
        mPresenter.start();
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        /*View view = inflater.inflate(R.layout.login_frag, container, false);
        mViewDataBinding = LoginFragBinding.bind(view);
        */
        //setHasOptionsMenu(true);
        //setRetainInstance(true);

        mViewDataBinding = StartupFragBinding.inflate(inflater, container, false);
        mViewDataBinding.setPresenter(mPresenter);
        mViewDataBinding.setView(this);

        return mViewDataBinding.getRoot();
    }

    @Override
    public void onStart(){
        super.onStart();
        mVideoView = (CenterCropVideoView) getActivity().findViewById(R.id.video);
        startVideo();
    }

    @Override
    public void showDesireList() {
        Intent intent = new Intent(getContext(), DesireListActivity.class);
        startActivity(intent);
    }

    @Override
    public void showWelcomeView() {
        Intent intent = new Intent(getContext(), WelcomeActivity.class);
        startActivity(intent);
    }

    @Override
    public void showMessage(String message) {

        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showResetPasswordSuccess() {
        showMessage(getString(R.string.password_reset_success));
    }

    @Override
    public void showResetPasswordError() {
        showMessage(getString(R.string.password_reset_error));
    }

    @Override
    public void showResetPasswordDialog() {

    }

    @Override
    public void changePassword() {

    }

    @Override
    public void closeResetPasswordDialog() {

    }

    @Override
    public void toggleRegButton(){}

    public void startVideo() {

        mVideoView.setVideoURI(Uri.parse("android.resource://" + getActivity().getPackageName() + "/"
                + R.raw.app_video_reverse));

        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                mVideoView.start();
            }
        });

        mVideoView.start();
    }

    @Override
    public void onPause(){
        super.onPause();
        mVideoView.stopPlayback();
    }

    public void freeDummyUsers(){


        mFreeDummyUsersCounter++;

        if(mFreeDummyUsersCounter > 10) {
            Intent intent = new Intent(getContext(), EasterEggActivity.class);
            startActivity(intent);
        }
        /*
        mFreeDummyUsersCounter++;

        if(mFreeDummyUsersCounter > 0){
            //mViewDataBinding.dummyUserButtons.setVisibility(View.VISIBLE);
        }
        */
    }

    @Override
    public void checkButtons() {
        final SharedPreferencesHelper sharedPreferencesHelper = SharedPreferencesHelper.getInstance(SharedPreferencesHelper.NAME_USER, getContext());
        String loggedInUserMail = sharedPreferencesHelper.loadString(SharedPreferencesHelper.KEY_USER_EMAIL, null);

        //buttons invisible if user is logged in
        if (loggedInUserMail != null) {
            mViewDataBinding.buttonSignin.setVisibility(View.GONE);
            mViewDataBinding.buttonSignup.setVisibility(View.GONE);
        } else {
            mViewDataBinding.buttonSignin.setVisibility(View.VISIBLE);
            mViewDataBinding.buttonSignup.setVisibility(View.VISIBLE);
        }
    }
}
