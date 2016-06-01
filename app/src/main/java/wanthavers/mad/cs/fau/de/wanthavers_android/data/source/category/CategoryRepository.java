package wanthavers.mad.cs.fau.de.wanthavers_android.data.source.category;

import android.support.annotation.NonNull;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.Category;

import static com.google.common.base.Preconditions.checkNotNull;

public class CategoryRepository implements CategoryDataSource {
    private static CategoryRepository INSTANCE;

    private final CategoryDataSource categoryRemoteDataSource;

    private final CategoryDataSource categoryLocalDataSource;

    private CategoryRepository(@NonNull CategoryDataSource categoryRemoteDataSource, @NonNull CategoryDataSource categoryLocalDataSource) {
        this.categoryRemoteDataSource = checkNotNull(categoryRemoteDataSource);
        this.categoryLocalDataSource = checkNotNull(categoryLocalDataSource);
    }

    public static CategoryRepository getInstance(CategoryDataSource categoryRemoteDataSource, CategoryDataSource categoryLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new CategoryRepository(categoryRemoteDataSource, categoryLocalDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void getCategory(@NonNull final long categoryId, @NonNull final GetCategoryCallback callback) {
        checkNotNull(categoryId);
        checkNotNull(callback);

        categoryLocalDataSource.getCategory(categoryId, new GetCategoryCallback() {
            @Override
            public void onCategoryLoaded(Category category) {
                callback.onCategoryLoaded(category);
            }

            @Override
            public void onDataNotAvailable() {
                categoryRemoteDataSource.getCategory(categoryId, new GetCategoryCallback() {
                    @Override
                    public void onCategoryLoaded(Category category) {
                        callback.onCategoryLoaded(category);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });
            }
        });
    }

    @Override
    public void getSubcategories(@NonNull final long categoryId, @NonNull final GetSubcategoriesCallback callback) {
        checkNotNull(categoryId);
        checkNotNull(callback);

        categoryLocalDataSource.getSubcategories(categoryId, new GetSubcategoriesCallback() {
            @Override
            public void onSubcategoriesLoaded(List<Category> categories) {
                callback.onSubcategoriesLoaded(categories);
            }

            @Override
            public void onDataNotAvailable() {
                categoryRemoteDataSource.getSubcategories(categoryId, new GetSubcategoriesCallback() {
                    @Override
                    public void onSubcategoriesLoaded(List<Category> categories) {
                        callback.onSubcategoriesLoaded(categories);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });
            }
        });
    }
}
