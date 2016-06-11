package wanthavers.mad.cs.fau.de.wanthavers_android.settings;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import java.io.File;

import static com.google.common.base.Preconditions.checkNotNull;

import de.fau.cs.mad.wanthavers.common.Media;
import de.fau.cs.mad.wanthavers.common.User;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCaseHandler;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.CreateImage;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetUser;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.UpdateUser;
import wanthavers.mad.cs.fau.de.wanthavers_android.rest.RestClient;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.SharedPreferencesHelper;

public class SettingsPresenter implements SettingsContract.Presenter {

    private final Context mAppContext;
    private final SettingsContract.View mSettingsView;
    private final UseCaseHandler mUseCaseHandler;
    private final GetUser mGetUser;
    private final UpdateUser mUpdateUser;
    private final CreateImage mCreateImage;

    public SettingsPresenter(@NonNull Context appContext, @NonNull UseCaseHandler useCaseHandler, @NonNull SettingsContract.View settingsView,
                             @NonNull GetUser getUser, @NonNull UpdateUser updateUser, @NonNull CreateImage createImage) {
        mAppContext = checkNotNull(appContext);
        mUseCaseHandler = checkNotNull(useCaseHandler, "useCaseHandler cannot be null");
        mSettingsView = checkNotNull(settingsView, "settings view cannont be null");
        mGetUser = checkNotNull(getUser);
        mUpdateUser = checkNotNull(updateUser);
        mCreateImage = checkNotNull(createImage);

        mSettingsView.setPresenter(this);
    }

    public void getUser(long userId){

        GetUser.RequestValues requestValue = new GetUser.RequestValues(userId);

        mUseCaseHandler.execute(mGetUser, requestValue,
                new UseCase.UseCaseCallback<GetUser.ResponseValue>() {
                    @Override
                    public void onSuccess(GetUser.ResponseValue response) {
                       mSettingsView.setUser(response.getUser());
                    }

                    @Override
                    public void onError() {
                        mSettingsView.showGetUserError();
                    }
                });
    }

    @Override
    public void start() {
        //TODO ?
    }

    public void getUserForMailUpdate(long userId, final String mail) {
        GetUser.RequestValues requestValue = new GetUser.RequestValues(userId);

        mUseCaseHandler.execute(mGetUser, requestValue,
                new UseCase.UseCaseCallback<GetUser.ResponseValue>() {
                    @Override
                    public void onSuccess(GetUser.ResponseValue response) {
                        User user = response.getUser();
                        final String email = mail;
                        user.setEmail(email);
                        upDateUserMail(user, mail);
                    }

                    @Override
                    public void onError() {
                        mSettingsView.showUpdateUserError();
                    }
                });
    }

    public void upDateUserMail(User user, final String email) {

        UpdateUser.RequestValues requestValue = new UpdateUser.RequestValues(user);

        mUseCaseHandler.execute(mUpdateUser, requestValue,
                new UseCase.UseCaseCallback<UpdateUser.ResponseValue>() {

                    @Override
                    public void onSuccess(UpdateUser.ResponseValue response) {
                        mSettingsView.showUpdateUserSuccess();
                        final SharedPreferencesHelper sharedPreferencesHelper = SharedPreferencesHelper.getInstance(SharedPreferencesHelper.NAME_USER, mAppContext);
                        sharedPreferencesHelper.saveString(SharedPreferencesHelper.KEY_USER_EMAIL, email);
                        RestClient.triggerSetNewBasicAuth();
                    }

                    @Override
                    public void onError() {
                        mSettingsView.showUpdateUserError();
                    }
                }
        );
    }

    public void getUserForImageUpdate(long userId, final File image) {

        //System.out.println("getUserForImageUpdate reached");

        GetUser.RequestValues requestValue = new GetUser.RequestValues(userId);

        mUseCaseHandler.execute(mGetUser, requestValue,
                new UseCase.UseCaseCallback<GetUser.ResponseValue>() {
                    @Override
                    public void onSuccess(GetUser.ResponseValue response) {
                        final User user = response.getUser();
                        uploadImage(user, image);
                    }

                    @Override
                    public void onError() {
                        //System.out.println("getUserForImageUpdate error");
                        mSettingsView.showUpdateUserError();
                    }
                });
    }

    public void uploadImage(final User user, File image) {

        //System.out.println("uploadImage reached");

        CreateImage.RequestValues requestValue = new CreateImage.RequestValues(image);

        mUseCaseHandler.execute(mCreateImage, requestValue,
                new UseCase.UseCaseCallback<CreateImage.ResponseValue>() {

                    @Override
                    public void onSuccess(CreateImage.ResponseValue response) {
                        Media media = response.getMedia();
                        user.setImage(media);
                        upDateUserImage(user, media);
                    }

                    @Override
                    public void onError() {
                        //System.out.println("uploadImage error");
                        mSettingsView.showUpdateUserError();
                    }
                }
        );
    }

    public void upDateUserImage(final User user, Media media) {

        //System.out.println("upDateUserImage reached");

        UpdateUser.RequestValues requestValue = new UpdateUser.RequestValues(user);

        mUseCaseHandler.execute(mUpdateUser, requestValue,
                new UseCase.UseCaseCallback<UpdateUser.ResponseValue>() {

                    @Override
                    public void onSuccess(UpdateUser.ResponseValue response) {
                        mSettingsView.showUpdateUserSuccess();
                        mSettingsView.setUser(user);
                    }

                    @Override
                    public void onError() {
                        //System.out.println("upDateUserImage error");
                        mSettingsView.showUpdateUserError();
                    }
                }
        );

    }

    /*public void updateUserMail(User user, String email) {

        final String email = mail;
        user.setEmail(email);

        UpdateUser.RequestValues requestValue = new UpdateUser.RequestValues(user);

        mUseCaseHandler.execute(mUpdateUser, requestValue,
                new UseCase.UseCaseCallback<UpdateUser.ResponseValue>() {

                    @Override
                    public void onSuccess(UpdateUser.ResponseValue response) {
                        mSettingsView.showUpdateUserSuccess();
                    }

                    @Override
                    public void onError() {
                        mSettingsView.showUpdateUserError();
                    }
                }
        );
    }*/

    @Override
    public void openFilterSettings() {
        mSettingsView.showFilterSettings();
    }

}