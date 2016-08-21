package wanthavers.mad.cs.fau.de.wanthavers_android.desirecreate;

import java.io.File;
import java.util.List;

import de.fau.cs.mad.wanthavers.common.Category;
import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.common.Media;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.BasePresenter;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.BaseView;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.SelectImageLogic;

public interface DesireCreateContract {
    interface View extends BaseView<Presenter> {

        void showMedia(Desire d);

        void showMessage(String message);

        void showNextDesireCreateStep();

        void showCategorySelection();

        void showCategory(Category category);

        void showGetCategoriesError();

        void showCategories(List<Category> categories);

        void selectExpirationDate();

        //boolean isStoragePermissionGranted();

        //void selectImageForDesire();
    }




    interface Presenter extends BasePresenter {

        void createNextDesireCreateStep();

        void setDesire(Desire desire);

        void setImage(File file, Desire desire);

        void selectImageFromDevice();

        SelectImageLogic getImageLogic();

        void openCategorySelection();

        void selectExpirationDate();

        void showInfo();

    }
}
