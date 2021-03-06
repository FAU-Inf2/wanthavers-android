package wanthavers.mad.cs.fau.de.wanthavers_android.filtersetting;

import android.view.View;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.Category;
import de.fau.cs.mad.wanthavers.common.DesireFilter;
import de.fau.cs.mad.wanthavers.common.Location;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.BasePresenter;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.BaseView;

public interface FilterSettingContract {
    interface View extends BaseView<FilterSettingContract.Presenter> {

        void showDesireList();

        void showGetCategoriesError();

        void showFilterChangeSuccess();

        void setLocation(Location location);

        void showCategorySelection();

        void showCategory(Category category);

        void showWrongPricesSet();

        void openLocationList(Location filterLocation);

    }

    interface Presenter extends BasePresenter {

        void getCategory(long categoryId);

        void setFilter(DesireFilter desireFilter);

        void setFilterWithInput(Category category, Location location);

        void resetFilter();

        void openCategorySelection();

        void changeLocation(Location filterLocation);
    }
}
