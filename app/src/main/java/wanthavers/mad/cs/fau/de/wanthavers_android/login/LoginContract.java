package wanthavers.mad.cs.fau.de.wanthavers_android.login;

import de.fau.cs.mad.wanthavers.common.Desire;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.BasePresenter;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.BaseView;

public interface LoginContract {
    interface View extends BaseView<Presenter> {

        void showDesireList();

        void showMessage(String message);

    }




    interface Presenter extends BasePresenter {

        void login(long userId);

        void loginUserWithInput();

        void openLoginView();

        void openRegisterView();

        void registerUser();

    }
}
