package wanthavers.mad.cs.fau.de.wanthavers_android.login;

import android.app.FragmentManager;
import android.content.Context;
import android.support.annotation.NonNull;

import de.fau.cs.mad.wanthavers.common.Desire;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCaseHandler;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.SendMessage;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.SetDesire;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.SharedPreferencesHelper;

public class LoginPresenter implements LoginContract.Presenter {
    private final LoginContract.View mLoginView;
    private final UseCaseHandler mUseCaseHandler;
    private final Context mAppContext;

    public LoginPresenter(@NonNull UseCaseHandler ucHandler, @NonNull LoginContract.View view, @NonNull Context appContext) {

        mUseCaseHandler = ucHandler;
        mLoginView = view;
        mAppContext = appContext;
        //mSetDesire = setDesire;

        mLoginView.setPresenter(this);

    }

    public void start() { //TODO;
    }


    /*@Override
    public Desire getEnteredText(Desire desire, String s1, String s2){
        return new Desire();
    }*/

    @Override
    public void login(long userId) {

        SharedPreferencesHelper sharedPreferencesHelper = SharedPreferencesHelper.getInstance(SharedPreferencesHelper.NAME_USER, mAppContext);
        sharedPreferencesHelper.saveLong(SharedPreferencesHelper.KEY_USERID, userId);

        mLoginView.showDesireList();

        //mLoginView.showDesireList();
    }


}

