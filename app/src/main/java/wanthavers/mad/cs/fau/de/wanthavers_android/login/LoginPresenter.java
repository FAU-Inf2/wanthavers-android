package wanthavers.mad.cs.fau.de.wanthavers_android.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.View;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.fau.cs.mad.wanthavers.common.AppVersion;
import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.common.User;
import wanthavers.mad.cs.fau.de.wanthavers_android.BuildConfig;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCaseHandler;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.CreateUser;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetAppVersion;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.LoginUser;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.SendMessage;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.SendPWResetToken;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.SetDesire;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.UpdateUser;
import wanthavers.mad.cs.fau.de.wanthavers_android.rest.RestClient;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.SharedPreferencesHelper;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.WantHaversTextButton;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.WantHaversTextView;

public class LoginPresenter implements LoginContract.Presenter {
    private final LoginContract.View mLoginView;
    private final UseCaseHandler mUseCaseHandler;
    private final Context mAppContext;
    private final LoginActivity mActivity;
    private final CreateUser mCreateUser;
    private final GetAppVersion mGetAppVersion;
    private final LoginUser mLoginUser;
    private final SendPWResetToken mSendPWResetToken;
    private final UpdateUser mUpdateUser;

    public LoginPresenter(@NonNull UseCaseHandler ucHandler, @NonNull LoginContract.View view, @NonNull Context appContext,
                          @NonNull LoginActivity activity, @NonNull CreateUser createUser, @NonNull GetAppVersion getAppVersion, @NonNull LoginUser loginUser, @NonNull SendPWResetToken sendPWResetToken,
                          @NonNull UpdateUser updateUser) {

        mUseCaseHandler = ucHandler;
        mLoginView = view;
        mAppContext = appContext;
        //mSetDesire = setDesire;
        mActivity = activity;
        mLoginView.setPresenter(this);
        mCreateUser = createUser;
        mGetAppVersion = getAppVersion;
        mLoginUser = loginUser;
        mSendPWResetToken = sendPWResetToken;
        mUpdateUser = updateUser;

    }

    public void start() { //TODO;
        checkAppVersion(BuildConfig.VERSION_CODE, AppVersion.OS.ANDROID, BuildConfig.APPLICATION_ID);

        final SharedPreferencesHelper sharedPreferencesHelper = SharedPreferencesHelper.getInstance(SharedPreferencesHelper.NAME_USER, mAppContext);
        String loggedInUserMail = sharedPreferencesHelper.loadString(SharedPreferencesHelper.KEY_USER_EMAIL, null);
        String loggedInUserPw = sharedPreferencesHelper.loadString(SharedPreferencesHelper.KEY_PASSWORD, null);

        //redirect immediately if user is logged in
        if (loggedInUserMail != null) {
            login(loggedInUserMail,loggedInUserPw, false);
        }

    }


    /*@Override
    public Desire getEnteredText(Desire desire, String s1, String s2){
        return new Desire();
    }*/

    @Override
    public void loginUserWithInput() {


        EditText emailView = (EditText) mActivity.findViewById(R.id.email);
        EditText passwordView = (EditText) mActivity.findViewById(R.id.password);

        String email = emailView.getText().toString();
        String password = passwordView.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            mLoginView.showMessage(mActivity.getResources().getString(R.string.login_empty_text));
            return;
        }

        login(email, password, false);
    }


    public void login(long userId) {

        /*
        //this is just a shortcut implemented for testing
        final SharedPreferencesHelper sharedPreferencesHelper = SharedPreferencesHelper.getInstance(SharedPreferencesHelper.NAME_USER, mAppContext);
        sharedPreferencesHelper.saveLong(SharedPreferencesHelper.KEY_USERID, userId);

        int userIdForSwitchCase = (int) userId;


        switch (userIdForSwitchCase) {
            case 1:
                    sharedPreferencesHelper.saveString(sharedPreferencesHelper.KEY_USER_EMAIL, "com.mail@yoda");
                sharedPreferencesHelper.saveString(sharedPreferencesHelper.KEY_PASSWORD, "test");
                break;
            case 2:
                sharedPreferencesHelper.saveString(sharedPreferencesHelper.KEY_USER_EMAIL, "jon@doe.com");
                sharedPreferencesHelper.saveString(sharedPreferencesHelper.KEY_PASSWORD, "test");
                break;
            case 3:
                sharedPreferencesHelper.saveString(sharedPreferencesHelper.KEY_USER_EMAIL, "m.muster@xyz.de");
                sharedPreferencesHelper.saveString(sharedPreferencesHelper.KEY_PASSWORD, "test");
                break;
            default:
                sharedPreferencesHelper.saveString(sharedPreferencesHelper.KEY_USER_EMAIL, "com.mail@yoda");
                sharedPreferencesHelper.saveString(sharedPreferencesHelper.KEY_PASSWORD, "test");
                break;
        }

        RestClient.triggerSetNewBasicAuth();
        mLoginView.showDesireList();
        */
    }


    @Override
    public void login(final String userMail, final String userPw, final boolean isRegistering) {

        final SharedPreferencesHelper sharedPreferencesHelper = SharedPreferencesHelper.getInstance(SharedPreferencesHelper.NAME_USER, mAppContext);
        sharedPreferencesHelper.saveString(SharedPreferencesHelper.KEY_USER_EMAIL, userMail);
        sharedPreferencesHelper.saveString(SharedPreferencesHelper.KEY_PASSWORD, userPw);
        LoginUser.RequestValues requestValue = new LoginUser.RequestValues();
        RestClient.triggerSetNewBasicAuth();

        mUseCaseHandler.execute(mLoginUser, requestValue,
                new UseCase.UseCaseCallback<LoginUser.ResponseValue>() {

                    @Override
                    public void onSuccess(LoginUser.ResponseValue response) {

                        User user = response.getUser();
                        sharedPreferencesHelper.saveLong(SharedPreferencesHelper.KEY_USERID, user.getId());


                        if (isRegistering) {
                            mLoginView.showWelcomeView();
                            return;
                        }

                        String defaultLanguage = Locale.getDefault().toString();

                        if(user.getLangCode() == null || defaultLanguage.compareTo(user.getLangCode()) != 0){
                            user.setLangCode(defaultLanguage);
                            updateUser(user);
                        }

                        mLoginView.showDesireList();
                    }

                    @Override
                    public void onError() {
                        // The view may not be able to handle UI updates anymore
                        sharedPreferencesHelper.saveString(SharedPreferencesHelper.KEY_USER_EMAIL, null);
                        sharedPreferencesHelper.saveString(SharedPreferencesHelper.KEY_PASSWORD, null);
                        sharedPreferencesHelper.saveLong(SharedPreferencesHelper.KEY_USERID, -1);
                        RestClient.triggerSetNewBasicAuth();

                        //TODO: show buttons
                        mLoginView.checkButtons();

                        mLoginView.showMessage(mActivity.getResources().getString(R.string.userLoginFailed));
                    }
                });

    }


    public void updateUser(final User user){

        UpdateUser.RequestValues requestValue = new UpdateUser.RequestValues(user);

        mUseCaseHandler.execute(mUpdateUser, requestValue,
                new UseCase.UseCaseCallback<UpdateUser.ResponseValue>() {

                    @Override
                    public void onSuccess(UpdateUser.ResponseValue response) {
                        mLoginView.showDesireList();
                    }

                    @Override
                    public void onError() {
                        mLoginView.showDesireList();
                    }
                }
        );
    }


    @Override
    public void openLoginView() {
        mActivity.setLoginFragment();
    }

    @Override
    public void openRegisterView() {
        mActivity.setRegisterFragment();
    }


    @Override
    public void registerUser() {

        //get fields and check if all fields are there
        //set locale for user


        EditText email = (EditText) mActivity.findViewById(R.id.email);
        EditText password = (EditText) mActivity.findViewById(R.id.password);
        EditText firstname = (EditText) mActivity.findViewById(R.id.first_name);
        EditText lastname = (EditText) mActivity.findViewById(R.id.last_name);
        DatePicker datePicker = (DatePicker) mActivity.findViewById(R.id.RegisterCalender);

        Calendar cal = Calendar.getInstance();
        cal.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
        Date datePicked = cal.getTime();


        if (email.getText().toString().isEmpty() || password.getText().toString().isEmpty()
                || firstname.getText().toString().isEmpty() || lastname.getText().toString().isEmpty()) {
            mLoginView.showMessage(mActivity.getResources().getString(R.string.login_empty_text));
            return;
        }

        //put together user object
        User user = new User(firstname.getText().toString(), email.getText().toString());
        user.setFirstName(firstname.getText().toString());
        user.setLastName(lastname.getText().toString());
        user.setLangCode(Locale.getDefault().toString());
        user.setBirthday(datePicked);
        registerUser(user, password.getText().toString());
    }


    private void registerUser(User user, final String password) {


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

    public void resetPassword() {
        mLoginView.showResetPasswordDialog();
    }

    public void sendPWResetToken(String email) {

        SendPWResetToken.RequestValues requestValues = new SendPWResetToken.RequestValues(email);


        mUseCaseHandler.execute(mSendPWResetToken, requestValues,
                new UseCase.UseCaseCallback<SendPWResetToken.ResponseValue>() {

                    @Override
                    public void onSuccess(SendPWResetToken.ResponseValue responseValue) {
                        mLoginView.closeResetPasswordDialog();
                        mLoginView.showResetPasswordSuccess();
                    }

                    @Override
                    public void onError() {
                        mLoginView.closeResetPasswordDialog();
                        mLoginView.showResetPasswordError();
                    }

                }
        );
    }

    @Override
    public void checkAppVersion(int versionCode, int os, String appId) {
        GetAppVersion.RequestValues requestValues = new GetAppVersion.RequestValues(versionCode, os, appId);

        mUseCaseHandler.execute(mGetAppVersion, requestValues, new UseCase.UseCaseCallback<GetAppVersion.ResponseValue>() {
            @Override
            public void onSuccess(GetAppVersion.ResponseValue response) {
                checkAppVersion(response.getAppVersion());
            }

            @Override
            public void onError() {
                mLoginView.showMessage(mActivity.getResources().getString(R.string.get_app_version_error));
            }
        });
    }

    private void checkAppVersion(AppVersion appVersion) {
        //latest version -> exit
        if (appVersion.getVersionCode() == BuildConfig.VERSION_CODE) {
            return;
        }

        if (appVersion.isForceUpdate()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
            builder.setMessage(mActivity.getResources().getString(R.string.app_version_force_text))
                    .setTitle(mActivity.getResources().getString(R.string.app_version_force_title))
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            android.os.Process.killProcess(android.os.Process.myPid());
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    @Override
    public void submitResetPassword() {
        mLoginView.changePassword();
    }

    @Override
    public void cancelResetPassword() {
        mLoginView.closeResetPasswordDialog();
    }

    @Override
    public void toggleRegButton(){ mLoginView.toggleRegButton(); }


    @Override
    public void openAgb() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://wanthaver.com/agbs.html"));
        mActivity.startActivity(browserIntent);
    }
}

