package wanthavers.mad.cs.fau.de.wanthavers_android.desiredetail;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.Haver;
import de.fau.cs.mad.wanthavers.common.User;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.BasePresenter;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.BaseView;
import de.fau.cs.mad.wanthavers.common.Desire;
public interface DesireDetailContract {


    interface View extends BaseView<Presenter> {

        void showDesire(Desire desire);

        void showHavers(List<Haver> haver);

        void setLoadingIndicator(final boolean active);

        void showLoadingHaversError();

        //void showChatList(long userid);

        void showSetHaverError();

        boolean isActive();
    }



    interface Presenter extends BasePresenter {

        void getDesire();

        void loadHavers(boolean forceUpdate);

        void setHaver();

        //void openChat(User user);

    }
}
