package wanthavers.mad.cs.fau.de.wanthavers_android.desirelist;

import android.support.annotation.NonNull;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.common.User;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.BasePresenter;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.BaseView;

public interface DesireListContract {


    interface View extends BaseView<Presenter> {

        void showDesires(List<DesireItemViewModel> desires);

        void showLoadingDesiresError();

        boolean isActive();  //TODO check if needed

        void showDesireDetailsUi(long desireId);

        void setLoadingIndicator(final boolean active);

        void showChatList(long userId);

        void showNewDesire();

        void showSettings();

        public void setUser(User user);
        //TODO void showNoTasks();
    }



    interface Presenter extends BasePresenter {

        void loadDesires(boolean forceUpdate);

       // void loadRatingsForDesires(final List<DesireItemViewModel> desireList);

        void createNewDesire();

        void openDesireDetails(@NonNull Desire desire);

        void openChat(@NonNull User user);

        void openSettings();

        //TODO - add filter options here

    }
}
