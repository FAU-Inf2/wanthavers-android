package wanthavers.mad.cs.fau.de.wanthavers_android.login;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.VideoView;

import java.io.IOException;

import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCaseHandler;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.appversion.AppVersionLocalDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.appversion.AppVersionRemoteDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.appversion.AppVersionRepository;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.user.UserLocalDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.user.UserRemoteDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.user.UserRepository;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.CreateUser;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetAppVersion;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.LoginUser;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.SendPWResetToken;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.ActivityUtils;

import static com.google.common.base.Preconditions.checkNotNull;

public class LoginActivity extends AppCompatActivity{
    private LoginPresenter mLoginPresenter;

    private CreateUser mCreateUser;
    private LoginUser mLoginUser;
    private GetAppVersion mGetAppVersion;
    private SendPWResetToken mPWReset;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_act);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


            LoginContract.View startUpFragment = (LoginContract.View) getSupportFragmentManager()
                    .findFragmentById(R.id.contentFrame);

            if (startUpFragment == null) {
                startUpFragment = StartUpFragment.newInstance(); //TODO

                ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                        (android.support.v4.app.Fragment)startUpFragment, R.id.contentFrame);
            }

        Context context = getApplicationContext();



        UserRepository userRepository = UserRepository.getInstance(UserRemoteDataSource.getInstance(context), UserLocalDataSource.getInstance(context));
        mCreateUser = new CreateUser(userRepository);
        mLoginUser = new LoginUser(userRepository);
        mPWReset = new SendPWResetToken(userRepository);
        mGetAppVersion = new GetAppVersion(AppVersionRepository.getInstance(AppVersionRemoteDataSource.getInstance(context), AppVersionLocalDataSource.getInstance(context)));

        mLoginPresenter = new LoginPresenter(UseCaseHandler.getInstance(), startUpFragment, getApplicationContext(), this, mCreateUser, mGetAppVersion, mLoginUser, mPWReset);

    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        StartUpFragment startUpFragment = StartUpFragment.newInstance();

        fragmentManager.beginTransaction()
                .replace(R.id.contentFrame,startUpFragment)
                .commit();

        mLoginPresenter = new LoginPresenter(UseCaseHandler.getInstance(), startUpFragment, getApplicationContext(), this, mCreateUser, mGetAppVersion, mLoginUser, mPWReset);
    }

    public void setLoginFragment(){

        FragmentManager fragmentManager = getSupportFragmentManager();

        LoginFragment loginFragment = LoginFragment.newInstance();

        fragmentManager.beginTransaction()
                .replace(R.id.contentFrame,loginFragment)
                .commit();

        mLoginPresenter = new LoginPresenter(UseCaseHandler.getInstance(), loginFragment, getApplicationContext(), this, mCreateUser, mGetAppVersion, mLoginUser, mPWReset);
    }

    public void setRegisterFragment(){

        FragmentManager fragmentManager = getSupportFragmentManager();
        RegisterFragment registerFragment = RegisterFragment.newInstance();

        fragmentManager.beginTransaction()
                .replace(R.id.contentFrame,registerFragment)
                .commit();

        mLoginPresenter = new LoginPresenter(UseCaseHandler.getInstance(), registerFragment, getApplicationContext(), this, mCreateUser, mGetAppVersion, mLoginUser, mPWReset);
    }

}
