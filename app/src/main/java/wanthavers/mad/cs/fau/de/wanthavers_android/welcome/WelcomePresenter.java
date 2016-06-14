package wanthavers.mad.cs.fau.de.wanthavers_android.welcome;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.File;

import de.fau.cs.mad.wanthavers.common.User;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCaseHandler;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.SelectImageLogic;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.CreateUser;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetUser;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.LoginUser;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.SetImage;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.UpdateUser;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.SharedPreferencesHelper;

public class WelcomePresenter implements WelcomeContract.Presenter {
    private final WelcomeContract.View mWelcomeView;
    private final UseCaseHandler mUseCaseHandler;
    private final Context mAppContext;
    private final WelcomeActivity mActivity;

    private SelectImageLogic mImageLogic;
    private final SetImage mSetImage;
    private final UpdateUser mUpdateUser;
    private final GetUser mGetUser;

    private User mLoggedInUser;

    public WelcomePresenter(@NonNull UseCaseHandler ucHandler, @NonNull WelcomeContract.View view, @NonNull Context appContext,
                            @NonNull WelcomeActivity activity, @NonNull SetImage setImage, @NonNull UpdateUser updateUser, @NonNull GetUser getUser) {

        mUseCaseHandler = ucHandler;
        mWelcomeView = view;
        mAppContext = appContext;
        mActivity = activity;
        mWelcomeView.setPresenter(this);
        mSetImage = setImage;
        mUpdateUser = updateUser;
        mGetUser = getUser;
        mImageLogic = new SelectImageLogic(activity);

    }

    public void start() { //TODO;
        final SharedPreferencesHelper sharedPreferencesHelper = SharedPreferencesHelper.getInstance(SharedPreferencesHelper.NAME_USER, mAppContext);
        long userId = sharedPreferencesHelper.loadLong(SharedPreferencesHelper.KEY_USERID, 6);

        getUser(userId);


    }

    public void getUser(long userId){

        GetUser.RequestValues requestValue = new GetUser.RequestValues(userId);

        mUseCaseHandler.execute(mGetUser, requestValue,
                new UseCase.UseCaseCallback<GetUser.ResponseValue>() {
                    @Override
                    public void onSuccess(GetUser.ResponseValue response) {
                        mLoggedInUser = response.getUser();
                    }

                    @Override
                    public void onError() {

                        //if user cannot be loaded redirect straight to desireList view
                        mWelcomeView.showDesireList();
                    }
                });
    }


    public SelectImageLogic getImageLogic(){
        return mImageLogic;
    }

    @Override
    public void selectImageFromDevice(){

        if(mImageLogic.isStoragePermissionGranted()){
            mImageLogic.selectImageForDesire();
        }

    }


    public void setImage(File file){

        final SetImage.RequestValues requestValue = new SetImage.RequestValues(file);

        mUseCaseHandler.execute(mSetImage, requestValue,
                new UseCase.UseCaseCallback<SetImage.ResponseValue>() {
                    @Override
                    public void onSuccess(SetImage.ResponseValue response) {

                        mLoggedInUser.setImage(response.getMedia());
                        updateUser();

                    }

                    @Override
                    public void onError() {
                        mWelcomeView.showMessage(mActivity.getResources().getString(R.string.user_image_upload_failed));
                    }

                });

    }

    private void updateUser(){

        final UpdateUser.RequestValues requestValue = new UpdateUser.RequestValues(mLoggedInUser);

        mUseCaseHandler.execute(mUpdateUser, requestValue,
                new UseCase.UseCaseCallback<UpdateUser.ResponseValue>(){

                    @Override
                    public void onSuccess(UpdateUser.ResponseValue response) {
                        openDesireList();
                    }

                    @Override
                    public void onError() {
                        mWelcomeView.showMessage(mActivity.getResources().getString(R.string.update_user_error));
                    }
                });

    }


    public void openDesireList(){
        mWelcomeView.showDesireList();
    }



}

