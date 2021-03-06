package wanthavers.mad.cs.fau.de.wanthavers_android.desirelist;

import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.common.User;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.BasePresenter;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.BaseView;

public interface DesireListContract {


    interface View extends BaseView<Presenter> {

        void showMessage(String message);

        void showDesires(List<DesireItemViewModel> desires);

        void showLoadingDesiresError();

        void showDesireListScreen();

        boolean isActive();  //TODO check if needed

        void showDesireDetailsUi(long desireId);

        void setLoadingIndicator(final boolean active);

        void showChatList(long userId);

        void showNewDesire();

        void showLogout();

        void showMyDesires();

        void showMyTransactions();

        void showAllDesires();

        void showSettings();

        void showFilterSettings();

        public void setUser(User user);
        //TODO void showNoTasks();

        void getCurrentGpsPosition();

        void showAbout();

        void showLicenses();
    }



    interface Presenter extends BasePresenter {

        void loadDesires(boolean forceUpdate, boolean showLoadingUi, boolean loadOlderDesires);

       // void loadRatingsForDesires(final List<DesireItemViewModel> desireList);

        void createNewDesire();

        void createLogout();

        void openDesireDetails(@NonNull Desire desire);

        void openChat(@NonNull User user);

        void openAllDesires();

        void openMyDesires();

        void openMyTransactions();

        void openSettings();

        void openFilterSettings();

        void loadCurrentGpsPosition();

        void createAbout();

        //void deleteToken();

        //TODO - add filter options here

    }
}
