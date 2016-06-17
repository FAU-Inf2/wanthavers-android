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
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.LoginUser;
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
    private final LoginUser mLoginUser;

    public LoginPresenter(@NonNull UseCaseHandler ucHandler, @NonNull LoginContract.View view, @NonNull Context appContext,
                          @NonNull LoginActivity activity, @NonNull CreateUser createUser, @NonNull LoginUser loginUser) {

        mUseCaseHandler = ucHandler;
        mLoginView = view;
        mAppContext = appContext;
        //mSetDesire = setDesire;
        mActivity = activity;
        mLoginView.setPresenter(this);
        mCreateUser = createUser;
        mLoginUser = loginUser;

    }

    public void start() { //TODO;
        final SharedPreferencesHelper sharedPreferencesHelper = SharedPreferencesHelper.getInstance(SharedPreferencesHelper.NAME_USER, mAppContext);
        String loggedInUserMail =  sharedPreferencesHelper.loadString(SharedPreferencesHelper.KEY_USER_EMAIL,null);

        //redirect immediately if user is logged in
        if(loggedInUserMail != null){
            mLoginView.showDesireList();
        }

    }


    /*@Override
    public Desire getEnteredText(Desire desire, String s1, String s2){
        return new Desire();
    }*/

    @Override
    public void loginUserWithInput(){


        EditText emailView = (EditText) mActivity.findViewById(R.id.email);
        EditText passwordView = (EditText) mActivity.findViewById(R.id.password);

        String email = emailView.getText().toString();
        String password = passwordView.getText().toString();

        if(email.isEmpty() || password.isEmpty() ){
            mLoginView.showMessage( mActivity.getResources().getString(R.string.login_empty_text));
            return;
        }

        login(email, password, false);
    }


    public void login(long userId){

        //this is just a shortcut implemented for testing
        final SharedPreferencesHelper sharedPreferencesHelper = SharedPreferencesHelper.getInstance(SharedPreferencesHelper.NAME_USER, mAppContext);
        sharedPreferencesHelper.saveLong(SharedPreferencesHelper.KEY_USERID, userId);

        int userIdForSwitchCase = (int) userId;


        switch(userIdForSwitchCase){
            case 1 : sharedPreferencesHelper.saveString(sharedPreferencesHelper.KEY_USER_EMAIL, "com.mail@yoda");
                     sharedPreferencesHelper.saveString(sharedPreferencesHelper.KEY_PASSWORD, "test");
                     break;
            case 2 : sharedPreferencesHelper.saveString(sharedPreferencesHelper.KEY_USER_EMAIL, "jon@doe.com");
                     sharedPreferencesHelper.saveString(sharedPreferencesHelper.KEY_PASSWORD, "test");
                     break;
            case 3 : sharedPreferencesHelper.saveString(sharedPreferencesHelper.KEY_USER_EMAIL, "m.muster@xyz.de");
                     sharedPreferencesHelper.saveString(sharedPreferencesHelper.KEY_PASSWORD, "test");
                     break;
            default: sharedPreferencesHelper.saveString(sharedPreferencesHelper.KEY_USER_EMAIL, "com.mail@yoda");
                     sharedPreferencesHelper.saveString(sharedPreferencesHelper.KEY_PASSWORD, "test");
                     break;
        }

        RestClient.triggerSetNewBasicAuth();
        mLoginView.showDesireList();
    }


    @Override
    public void login(String userMail, String userPw, final boolean isRegistering) {

        final SharedPreferencesHelper sharedPreferencesHelper = SharedPreferencesHelper.getInstance(SharedPreferencesHelper.NAME_USER, mAppContext);
        sharedPreferencesHelper.saveString(SharedPreferencesHelper.KEY_USER_EMAIL, userMail);
        sharedPreferencesHelper.saveString(SharedPreferencesHelper.KEY_PASSWORD, userPw);

        RestClient.triggerSetNewBasicAuth();

        LoginUser.RequestValues requestValue = new LoginUser.RequestValues();

        mUseCaseHandler.execute(mLoginUser, requestValue,
                new UseCase.UseCaseCallback<LoginUser.ResponseValue>() {

                    @Override
                    public void onSuccess(LoginUser.ResponseValue response) {

                        User user = response.getUser();
                        sharedPreferencesHelper.saveLong(SharedPreferencesHelper.KEY_USERID, user.getId());

                        if(isRegistering){
                            mLoginView.showWelcomeView();
                            return;
                        }

                        mLoginView.showDesireList();
                    }

                    @Override
                    public void onError() {
                        // The view may not be able to handle UI updates anymore
                        mLoginView.showMessage(mActivity.getResources().getString(R.string.userLoginFailed));
                    }
                });

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
        User user =  new User(username.getText().toString(),email.getText().toString());
        user.setBirthday(datePicked);

        registerUser(user, password.getText().toString());
    }




    private void registerUser(User user, final String password){


        CreateUser.RequestValues requestValue = new CreateUser.RequestValues(user, password);

        mUseCaseHandler.execute(mCreateUser, requestValue,
                new UseCase.UseCaseCallback<CreateUser.ResponseValue>() {
                    @Override
                    public void onSuccess(CreateUser.ResponseValue response) {
                        login(response.getUser().getEmail(), password, true);
                    }

                    @Override
                    public void onError() {
                        // The view may not be able to handle UI updates anymore
                        mLoginView.showMessage(mActivity.getResources().getString(R.string.userCreationFailed));
                    }
                });
    }
}

