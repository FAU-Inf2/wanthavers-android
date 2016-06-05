package wanthavers.mad.cs.fau.de.wanthavers_android.login;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.common.User;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCaseHandler;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.CreateUser;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.SendMessage;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.SetDesire;
import wanthavers.mad.cs.fau.de.wanthavers_android.rest.RestClient;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.SharedPreferencesHelper;

public class LoginPresenter implements LoginContract.Presenter {
    private final LoginContract.View mLoginView;
    private final UseCaseHandler mUseCaseHandler;
    private final Context mAppContext;
    private final LoginActivity mActivity;
    private final CreateUser mCreateUser;

    public LoginPresenter(@NonNull UseCaseHandler ucHandler, @NonNull LoginContract.View view, @NonNull Context appContext,
                          @NonNull LoginActivity activity, @NonNull CreateUser createUser) {

        mUseCaseHandler = ucHandler;
        mLoginView = view;
        mAppContext = appContext;
        //mSetDesire = setDesire;
        mActivity = activity;
        mLoginView.setPresenter(this);
        mCreateUser = createUser;

    }

    public void start() { //TODO;
    }


    /*@Override
    public Desire getEnteredText(Desire desire, String s1, String s2){
        return new Desire();
    }*/

    @Override
    public void loginUserWithInput(){


        EditText email = (EditText) mActivity.findViewById(R.id.email);
        EditText password = (EditText) mActivity.findViewById(R.id.password);

        if(email.getText().toString().isEmpty() || password.getText().toString().isEmpty() ){
            mLoginView.showMessage( mActivity.getResources().getString(R.string.login_empty_text));
            return;
        }



        login(6);
    }



    @Override
    public void login(long userId) {

        SharedPreferencesHelper sharedPreferencesHelper = SharedPreferencesHelper.getInstance(SharedPreferencesHelper.NAME_USER, mAppContext);
        sharedPreferencesHelper.saveLong(SharedPreferencesHelper.KEY_USERID, userId);
        sharedPreferencesHelper.saveString(SharedPreferencesHelper.KEY_PASSWORD, "test");

        RestClient.triggerSetNewBasicAuth();

        mLoginView.showDesireList();

        //mLoginView.showDesireList();
    }


    @Override
    public void openLoginView(){
        mActivity.setLoginFragment();
    }

    @Override
    public void openRegisterView(){
        mActivity.setRegisterFragment();
    }


    @Override
    public void registerUser(){

        //get fields and check if all fields are there

        EditText email = (EditText) mActivity.findViewById(R.id.email);
        EditText password = (EditText) mActivity.findViewById(R.id.password);
        EditText username = (EditText) mActivity.findViewById(R.id.name);
        DatePicker datePicker = (DatePicker) mActivity.findViewById(R.id.RegisterCalender);

        Calendar cal = Calendar.getInstance();
        cal.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
        Date datePicked = cal.getTime();


        if(email.getText().toString().isEmpty() || password.getText().toString().isEmpty() || username.getText().toString().isEmpty() ){
            mLoginView.showMessage( mActivity.getResources().getString(R.string.login_empty_text));
            return;
        }

        //put together user object

        User user =  new User(username.toString(),email.toString());
        user.setBirthday(datePicked);

        registerUser(user, password.toString());
    }




    private void registerUser(User user, String password){


        CreateUser.RequestValues requestValue = new CreateUser.RequestValues(user, password);

        mUseCaseHandler.execute(mCreateUser, requestValue,
                new UseCase.UseCaseCallback<CreateUser.ResponseValue>() {
                    @Override
                    public void onSuccess(CreateUser.ResponseValue response) {
                        login(response.getUser().getID());
                    }

                    @Override
                    public void onError() {
                        // The view may not be able to handle UI updates anymore
                        mLoginView.showMessage(mActivity.getResources().getString(R.string.userCreationFailed));
                    }
                });
    }
}

