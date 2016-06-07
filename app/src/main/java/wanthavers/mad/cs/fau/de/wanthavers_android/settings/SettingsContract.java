package wanthavers.mad.cs.fau.de.wanthavers_android.settings;

import de.fau.cs.mad.wanthavers.common.User;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.BasePresenter;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.BaseView;

public interface SettingsContract {
    interface View extends BaseView<SettingsContract.Presenter> {
        //TODO
        void showFilterSettings();

        void setUser(User user);
    }

    interface Presenter extends BasePresenter {
        //TODO
        void openFilterSettings();

        void getUser(long userId);

        void getUserForMailUpdate(long userId, final String mail);
    }
}
