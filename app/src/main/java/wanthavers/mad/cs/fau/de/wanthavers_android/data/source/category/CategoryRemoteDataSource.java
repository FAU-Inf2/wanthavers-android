package wanthavers.mad.cs.fau.de.wanthavers_android.data.source.category;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.Category;
import wanthavers.mad.cs.fau.de.wanthavers_android.rest.CategoryClient;

import static com.google.common.base.Preconditions.checkNotNull;

public class CategoryRemoteDataSource implements CategoryDataSource {
    private static CategoryRemoteDataSource INSTANCE;

    private CategoryClient categoryClient;

    private CategoryRemoteDataSource(Context context) {
        categoryClient = CategoryClient.getInstance(context);
    }

    public static CategoryRemoteDataSource getInstance(@NonNull Context context) {
        checkNotNull(context);
        if (INSTANCE == null) {
            INSTANCE = new CategoryRemoteDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void getCategory(@NonNull long categoryId, @NonNull GetCategoryCallback callback) {
        try {
            Category category = categoryClient.getCategory(categoryId);
            callback.onCategoryLoaded(category);
        } catch (Throwable t) {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void getSubcategories(@NonNull long categoryId, @NonNull GetSubcategoriesCallback callback) {
        try {
            List<Category> categories = categoryClient.getSubcategories(categoryId);
            callback.onSubcategoriesLoaded(categories);
        } catch (Throwable t) {
            callback.onDataNotAvailable();
        }
    }
}
