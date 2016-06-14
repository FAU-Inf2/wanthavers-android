package wanthavers.mad.cs.fau.de.wanthavers_android.welcome;

import java.io.File;

import de.fau.cs.mad.wanthavers.common.User;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.BasePresenter;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.BaseView;

public interface WelcomeContract {
    interface View extends BaseView<Presenter> {

        void showDesireList();

        void showMessage(String message);

    }




    interface Presenter extends BasePresenter {

        void selectImageFromDevice();

        void openDesireList();

        void setImage(File image);
    }
}
