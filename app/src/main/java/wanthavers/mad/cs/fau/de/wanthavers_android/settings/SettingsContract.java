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
    }

    interface Presenter extends BasePresenter {

        void openFilterSettings();

        void getUser(long userId);

        void getUserForMailUpdate(long userId, final String mail);

        void getUserForImageUpdate(long userId, final File image);

        //void updateUserMail(User user, String email);
    }
}
