package wanthavers.mad.cs.fau.de.wanthavers_android.data.source.category;

import android.content.Context;
import android.support.annotation.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

public class CategoryLocalDataSource implements CategoryDataSource {
    private static CategoryLocalDataSource INSTANCE;

    private Context context;

    private CategoryLocalDataSource(Context context) {
        this.context = context;
    }

    public static CategoryLocalDataSource getInstance(@NonNull Context context) {
        checkNotNull(context);
        if (INSTANCE == null) {
            INSTANCE = new CategoryLocalDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void getCategory(@NonNull long categoryId, @NonNull GetCategoryCallback callback) {
        //TODO: alter this method when we decide to store categories locally
        callback.onDataNotAvailable();
    }

    @Override
    public void getSubcategories(@NonNull long categoryId, @NonNull boolean recursive, @NonNull GetSubcategoriesCallback callback) {
        //TODO: alter this method when we decide to store categories locally
        callback.onDataNotAvailable();
    }

    @Override
    public void getAllDesiresForCategory(@NonNull long categoryId, @NonNull boolean recursive, @NonNull GetAllDesiresForCategoryCallback callback) {
        //TODO: alter this method when we decide to store categories locally
        callback.onDataNotAvailable();
    }
}
