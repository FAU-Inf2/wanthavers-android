package wanthavers.mad.cs.fau.de.wanthavers_android.login;

import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.common.User;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.BasePresenter;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.BaseView;

public interface LoginContract {
    interface View extends BaseView<Presenter> {

        void showDesireList();

        void showWelcomeView();

        void showMessage(String message);

        void showResetPasswordSuccess();

        void showResetPasswordError();

        void showResetPasswordDialog();

        void changePassword();

        void closeResetPasswordDialog();

        void toggleRegButton();

        void checkButtons();
    }




    interface Presenter extends BasePresenter {

        void login(long userId);

        void login(String userMail,String userPw, boolean isRegistering);

        void loginUserWithInput();

        void openUserUpdateView(User user);

        void openLoginView();

        void openRegisterView();

        void registerUser();

        void sendPWResetToken(String email);

        void checkAppVersion(int versionCode, int os, String appId);

        void resetPassword();

        void submitResetPassword();

        void cancelResetPassword();

        void toggleRegButton();

        void openAgb();

        void submitFirstLastName(User user);

        void logout();
    }
}
