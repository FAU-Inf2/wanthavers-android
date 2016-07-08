package wanthavers.mad.cs.fau.de.wanthavers_android.eastereggone;

import android.content.Context;
import android.support.annotation.NonNull;

import de.fau.cs.mad.wanthavers.common.User;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCaseHandler;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.SelectImageLogic;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetUser;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.SetImage;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.UpdateUser;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.SharedPreferencesHelper;
import wanthavers.mad.cs.fau.de.wanthavers_android.welcome.WelcomeContract;

public class EasterEggPresenter implements EasterEggContract.Presenter {
    private final EasterEggContract.View mEasterEggView;
    private final UseCaseHandler mUseCaseHandler;
    private final Context mAppContext;
    private final EasterEggActivity mActivity;

    private SelectImageLogic mImageLogic;
    private final SetImage mSetImage;
    private final UpdateUser mUpdateUser;
    private final GetUser mGetUser;

    private User mLoggedInUser;

    public EasterEggPresenter(@NonNull UseCaseHandler ucHandler, @NonNull EasterEggContract.View view, @NonNull Context appContext,
                              @NonNull EasterEggActivity activity, @NonNull SetImage setImage, @NonNull UpdateUser updateUser, @NonNull GetUser getUser) {

        mUseCaseHandler = ucHandler;
        mEasterEggView = view;
        mAppContext = appContext;
        mActivity = activity;
        mEasterEggView.setPresenter(this);
        mSetImage = setImage;
        mUpdateUser = updateUser;
        mGetUser = getUser;
        mImageLogic = new SelectImageLogic(activity);

    }

    public void start() { //TODO;
        final SharedPreferencesHelper sharedPreferencesHelper = SharedPreferencesHelper.getInstance(SharedPreferencesHelper.NAME_USER, mAppContext);
        long userId = sharedPreferencesHelper.loadLong(SharedPreferencesHelper.KEY_USERID, 6);
    }




    public void openDesireList(){
        mEasterEggView.showDesireList();
    }



}

