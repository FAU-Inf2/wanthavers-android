package wanthavers.mad.cs.fau.de.wanthavers_android.settings;

import java.io.File;

import de.fau.cs.mad.wanthavers.common.User;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.BasePresenter;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.BaseView;

public interface SettingsContract {
    interface View extends BaseView<SettingsContract.Presenter> {

        void showFilterSettings();

        void setUser(User user);

        void showGetUserError();

        void showUpdateUserError();

        void showUpdateUserSuccess();

        void showResetPasswordSuccess();

        void showResetPasswordError();

        void showLoadingProgress();

        void endLoadingProgress();

        void showSettingsScreen();

        void showLocationList();

        void showEmptyUserDataError();
    }

    interface Presenter extends BasePresenter {

        void openFilterSettings();

        void getUser(long userId);

        void getUserForImageUpdate(long userId, final File image);

        void upDateUser(User user);

        void sendPWResetToken(String email);

        void upDateUserIncludingMail(final User user);

        void openLocationList();

        void showEmptyUserDataError();
    }
}
