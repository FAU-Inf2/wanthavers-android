package wanthavers.mad.cs.fau.de.wanthavers_android.categorylist;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.Category;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.BasePresenter;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.BaseView;

public interface CategoryListContract {

    interface View extends BaseView<Presenter> {

        void showCategories(List<Category> categoryList);

        void showGetCategoriesError();

        void returnCategory(Category category);

    }

    interface Presenter extends BasePresenter {

        void selectCategory(Category category);

    }

}
