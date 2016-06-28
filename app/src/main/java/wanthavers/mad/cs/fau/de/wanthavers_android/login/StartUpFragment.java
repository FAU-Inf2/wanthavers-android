package wanthavers.mad.cs.fau.de.wanthavers_android.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.LoginFragBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.StartupFragBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.desirelist.DesireListActivity;
import wanthavers.mad.cs.fau.de.wanthavers_android.welcome.WelcomeActivity;

public class StartUpFragment extends Fragment implements LoginContract.View {
    private StartupFragBinding mViewDataBinding;
    private LoginContract.Presenter mPresenter;


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
        mPresenter.start();  //TODO JuG check if needed

    }
    @Override
    public void onResume()  {
        super.onResume();

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

        return mViewDataBinding.getRoot();
    }

    /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.desire_create_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }*/


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
}
