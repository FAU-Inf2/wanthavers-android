package wanthavers.mad.cs.fau.de.wanthavers_android.login;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import de.fau.cs.mad.wanthavers.common.User;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.user.UserLocalDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.user.UserRemoteDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.user.UserRepository;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.LoginFragBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.LoginSetFirstLastNameBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.ResetpasswordPopupBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.desirelist.DesireListActivity;
import wanthavers.mad.cs.fau.de.wanthavers_android.welcome.WelcomeActivity;

public class LoginFragment extends Fragment implements LoginContract.View {
    private LoginFragBinding mViewDataBinding;
    private LoginContract.Presenter mPresenter;
    private Dialog mResetPassword;
    private ResetpasswordPopupBinding mResetpasswordPopupBinding;
    private Dialog mSetFirstLastNameDialog;
    private LoginSetFirstLastNameBinding mloginSetFirstLastNameBinding;


    public LoginFragment(){
        //Requires empty public constructor
    }
    public static LoginFragment newInstance(){ return new LoginFragment();}

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


        /*View view = inflater.inflate(R.layout.login_frag, container, false);
        mViewDataBinding = LoginFragBinding.bind(view);
        */
        //setHasOptionsMenu(true);
        //setRetainInstance(true);

        mViewDataBinding = LoginFragBinding.inflate(inflater, container, false);
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
        mResetPassword = new Dialog(getContext());

        mResetpasswordPopupBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.resetpassword_popup, null, false);
        mResetPassword.setContentView(mResetpasswordPopupBinding.getRoot());

        mResetpasswordPopupBinding.setPresenter(mPresenter);

        mResetPassword.show();
    }

    @Override
    public void changePassword() {
        if (mResetpasswordPopupBinding.resetPasswordMail.getText().toString().equals("")) {
            showMessage(getString(R.string.no_email_set));
        } else {
            String email = mResetpasswordPopupBinding.resetPasswordMail.getText().toString();
            mPresenter.sendPWResetToken(email);
        }
    }

    @Override
    public void closeResetPasswordDialog() {
        mResetPassword.dismiss();
    }


    @Override
    public void toggleRegButton(){}

    @Override
    public void checkButtons() {

    }

    @Override
    public void showSetNameDialog(User user) {

        mSetFirstLastNameDialog = new Dialog(getContext());

        mloginSetFirstLastNameBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.login_set_first_last_name, null, false);
        mSetFirstLastNameDialog.setContentView(mloginSetFirstLastNameBinding.getRoot());

        mloginSetFirstLastNameBinding.setUser(user);
        mloginSetFirstLastNameBinding.setActionHandler(mPresenter);

        mSetFirstLastNameDialog.show();

    }

    @Override
    public User updateUserData(User user) {

        EditText firstNameView = mloginSetFirstLastNameBinding.userFirstName;
        EditText lastNameView = mloginSetFirstLastNameBinding.userLastName;

        String firstName = firstNameView.getText().toString();
        String lastName = lastNameView.getText().toString();

        if (firstName.equals("") || lastName.equals("")) {
            showMessage(getResources().getString(R.string.login_empty_text));
            return null;
        }

        user.setFirstName(firstName);
        user.setName(firstName);
        user.setLastName(lastName);

        return user;
    }

    @Override
    public void closeSetNameDialog() {
        mSetFirstLastNameDialog.dismiss();
    }

    ;
}
