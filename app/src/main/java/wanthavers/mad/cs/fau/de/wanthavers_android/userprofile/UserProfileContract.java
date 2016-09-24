package wanthavers.mad.cs.fau.de.wanthavers_android.userprofile;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.Desire;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.BasePresenter;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.BaseView;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.DesireLogic;

public interface UserProfileContract {

    interface View extends BaseView<Presenter> {

        void setDesireLogic(DesireLogic desireLogic);

        void showDesireHistory(List<Desire> desireList);

        void showFinishedDesireStatistics(int finishedDesires);

        void showCanceledDesireStatistics(int canceledDesires);

        void showGetFinishedDesiresError();

        void showGetCanceledDesiresError();

    }

    interface Presenter extends BasePresenter {

        void setUserId(long userId);

    }

}
