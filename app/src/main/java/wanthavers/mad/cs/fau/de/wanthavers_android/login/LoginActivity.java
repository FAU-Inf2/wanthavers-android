package wanthavers.mad.cs.fau.de.wanthavers_android.login;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCaseHandler;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.user.UserLocalDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.user.UserRemoteDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.user.UserRepository;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.LoginFragBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.CreateUser;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.ActivityUtils;

import static com.google.common.base.Preconditions.checkNotNull;

public class LoginActivity extends AppCompatActivity {
    private LoginPresenter mLoginPresenter;

    private CreateUser mCreateUser;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_act);




        StartUpFragment startUpFragment = (StartUpFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if (startUpFragment == null) {
            startUpFragment = StartUpFragment.newInstance(); //TODO

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    startUpFragment, R.id.contentFrame);
        }

        /*
        LoginFragment loginFragment = (LoginFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if (loginFragment == null) {
            loginFragment = LoginFragment.newInstance(); //TODO

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    loginFragment, R.id.contentFrame);
        }
        */
        Context context = getApplicationContext();

        UserRepository userRepository = UserRepository.getInstance(UserRemoteDataSource.getInstance(context), UserLocalDataSource.getInstance(context));
        mCreateUser = new CreateUser(userRepository);

        mLoginPresenter = new LoginPresenter(UseCaseHandler.getInstance(), startUpFragment, getApplicationContext(), this, mCreateUser);
    }

    /*@Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }*/
    @Override
    public void onBackPressed() {
        //disables back button
        FragmentManager fragmentManager = getSupportFragmentManager();

        StartUpFragment startUpFragment = StartUpFragment.newInstance();

        fragmentManager.beginTransaction()
                .replace(R.id.contentFrame,startUpFragment)
                .commit();

        mLoginPresenter = new LoginPresenter(UseCaseHandler.getInstance(), startUpFragment, getApplicationContext(), this, mCreateUser);
    }

    public void setLoginFragment(){

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.contentFrame);

        LoginFragment loginFragment = LoginFragment.newInstance();

        fragmentManager.beginTransaction()
                .replace(R.id.contentFrame,loginFragment)
                .commit();

        mLoginPresenter = new LoginPresenter(UseCaseHandler.getInstance(), loginFragment, getApplicationContext(), this, mCreateUser);
    }

    public void setRegisterFragment(){

        FragmentManager fragmentManager = getSupportFragmentManager();
        RegisterFragment registerFragment = RegisterFragment.newInstance();

        fragmentManager.beginTransaction()
                .replace(R.id.contentFrame,registerFragment)
                .commit();

        mLoginPresenter = new LoginPresenter(UseCaseHandler.getInstance(), registerFragment, getApplicationContext(), this, mCreateUser);
    }

}
