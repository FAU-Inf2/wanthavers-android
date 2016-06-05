package wanthavers.mad.cs.fau.de.wanthavers_android.data.source.category;

import android.support.annotation.NonNull;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.Category;
import de.fau.cs.mad.wanthavers.common.Desire;

public interface CategoryDataSource {

    interface GetCategoryCallback {

        void onCategoryLoaded(Category category);

        void onDataNotAvailable();

    }

    interface GetSubcategoriesCallback {

        void onSubcategoriesLoaded(List<Category> categories);

        void onDataNotAvailable();

    }

    interface GetAllDesiresForCategoryCallback {

        void onAllDesiresForCategoryLoaded(List<Desire> desires);

        void onDataNotAvailable();

    }

    void getCategory(@NonNull long categoryId, @NonNull GetCategoryCallback callback);

    void getSubcategories(@NonNull long categoryId, @NonNull boolean recursive, @NonNull GetSubcategoriesCallback callback);

    void getAllDesiresForCategory(@NonNull long categoryId, @NonNull boolean recursive, @NonNull GetAllDesiresForCategoryCallback callback);

}
