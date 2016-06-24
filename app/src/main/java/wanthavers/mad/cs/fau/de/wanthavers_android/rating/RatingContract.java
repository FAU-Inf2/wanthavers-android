package wanthavers.mad.cs.fau.de.wanthavers_android.rating;

import de.fau.cs.mad.wanthavers.common.Category;
import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.common.Haver;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.BasePresenter;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.BaseView;

public interface RatingContract {

    interface View extends BaseView<Presenter> {

        void showDesire(Desire desire);

        void showLoadingDesireError();

        void showAcceptedHaver(Haver haver);

        void showLoadingHaverError();

        void showCreateRatingError();

        void showNoRatingSet();

        void closeRatingWindow();

    }

    interface Presenter extends BasePresenter {

        void getAcceptedHaver();

        void finishRating(Desire desire, Haver haver);

    }

}
