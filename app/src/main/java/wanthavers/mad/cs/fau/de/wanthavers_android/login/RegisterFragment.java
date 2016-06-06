package wanthavers.mad.cs.fau.de.wanthavers_android.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.RegisterFragBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.StartupFragBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.desirelist.DesireListActivity;

public class RegisterFragment extends Fragment implements LoginContract.View {
    private RegisterFragBinding mViewDataBinding;
    private LoginContract.Presenter mPresenter;


    public RegisterFragment(){
        //Requires empty public constructor
    }
    public static RegisterFragment newInstance(){ return new RegisterFragment();}

    @Override
    public void setPresenter(@NonNull LoginContract.Presenter presenter) {
        //mPresenter = checkNotNull(presenter);
        mPresenter = presenter;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewDataBinding.setPresenter(mPresenter);

    }
    @Override
    public void onResume()  {
        super.onResume();
        mPresenter.start();
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        mViewDataBinding = RegisterFragBinding.inflate(inflater, container, false);
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
    public void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

}