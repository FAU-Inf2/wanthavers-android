package wanthavers.mad.cs.fau.de.wanthavers_android.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import de.fau.cs.mad.wanthavers.common.User;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.RegisterFragBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.StartupFragBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.desirelist.DesireListActivity;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.WantHaversTextButton;
import wanthavers.mad.cs.fau.de.wanthavers_android.welcome.WelcomeActivity;

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
    public void toggleRegButton(){
        CheckBox regCheckBox = mViewDataBinding.agbCheckBox;
        WantHaversTextButton regButton =  mViewDataBinding.buttonSignup;

        if(regCheckBox.isChecked()){
            regButton.setEnabled(true);
            regButton.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.border_primary_nocorner));
        }else{
            regButton.setEnabled(false);
            regButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.light_grey));
        }
    }

    @Override
    public void checkButtons() {

    }

    @Override
    public void showSetNameDialog(User user) {

    }

    @Override
    public User updateUserData(User user) {
        return null;
    }

    @Override
    public void closeSetNameDialog() {

    }


}
