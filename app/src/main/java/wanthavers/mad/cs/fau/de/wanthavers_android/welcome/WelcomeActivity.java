package wanthavers.mad.cs.fau.de.wanthavers_android.welcome;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCaseHandler;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.media.MediaLocalDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.media.MediaRemoteDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.media.MediaRepository;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.user.UserLocalDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.user.UserRemoteDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.user.UserRepository;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.CreateUser;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetUser;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.LoginUser;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.SetImage;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.UpdateUser;
import wanthavers.mad.cs.fau.de.wanthavers_android.login.LoginFragment;
import wanthavers.mad.cs.fau.de.wanthavers_android.login.LoginPresenter;
import wanthavers.mad.cs.fau.de.wanthavers_android.login.RegisterFragment;
import wanthavers.mad.cs.fau.de.wanthavers_android.login.StartUpFragment;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.ActivityUtils;

public class WelcomeActivity extends AppCompatActivity {
    private WelcomePresenter mWelcomePresenter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_act);




        WelcomeFragment welcomeFragment = (WelcomeFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if (welcomeFragment == null) {
            welcomeFragment = WelcomeFragment.newInstance();

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    welcomeFragment, R.id.contentFrame);
        }


        Context context = getApplicationContext();

        MediaRepository mediaRepo = MediaRepository.getInstance(MediaRemoteDataSource.getInstance(context), MediaLocalDataSource.getInstance(context));
        SetImage setImage = new SetImage(mediaRepo);
        UserRepository userRepository = UserRepository.getInstance(UserRemoteDataSource.getInstance(context), UserLocalDataSource.getInstance(context));
        UpdateUser updateUser = new UpdateUser(userRepository);
        GetUser getUser = new GetUser(userRepository);

        mWelcomePresenter = new WelcomePresenter(UseCaseHandler.getInstance(), welcomeFragment, getApplicationContext(), this,setImage, updateUser,getUser);
    }

    /*@Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }*/
    @Override
    public void onBackPressed() {
        //disables back button
        //TODO load desireList view
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }

    }
}
