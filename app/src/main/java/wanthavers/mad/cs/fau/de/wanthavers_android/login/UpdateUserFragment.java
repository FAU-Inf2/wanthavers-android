package wanthavers.mad.cs.fau.de.wanthavers_android.login;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.fau.cs.mad.wanthavers.common.User;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.UpdateuserFragBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.desirelist.DesireListActivity;

public class UpdateUserFragment extends Fragment implements LoginContract.View {

    private LoginContract.Presenter mPresenter;
    private UpdateuserFragBinding mViewDataBinding;
    private static User mUser;

    public UpdateUserFragment(){
        //Requires empty public constructor
    }
    public static UpdateUserFragment newInstance(User user){
        mUser = user;
        return new UpdateUserFragment();
    }

    @Override
    public void setPresenter(@NonNull LoginContract.Presenter presenter) {
        //mPresenter = checkNotNull(presenter);
        mPresenter = presenter;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewDataBinding.setActionHandler(mPresenter);
    }

    @Override
    public void onResume()  {
        super.onResume();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mViewDataBinding = UpdateuserFragBinding.inflate(inflater, container, false);
        mViewDataBinding.setActionHandler(mPresenter);
        mViewDataBinding.setUser(mUser);

        return mViewDataBinding.getRoot();
    }

    @Override
    public void showDesireList() {
        Intent intent = new Intent(getContext(), DesireListActivity.class);
        startActivity(intent);
    }

    @Override
    public void showWelcomeView() {

    }

    @Override
    public void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showResetPasswordSuccess() {

    }

    @Override
    public void showResetPasswordError() {

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
    public void toggleRegButton() {

    }

    @Override
    public void checkButtons() {

    }
}
