package wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases;

import android.support.annotation.NonNull;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.Category;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.category.CategoryDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.category.CategoryRepository;

public class GetSubcategories extends UseCase<GetSubcategories.RequestValues, GetSubcategories.ResponseValue> {

    private final CategoryRepository mCategoryRepository;

    public GetSubcategories(CategoryRepository categoryRepository) {
        mCategoryRepository = categoryRepository;
    }

    protected void executeUseCase(final RequestValues values) {

        mCategoryRepository.getSubcategories(values.getCategoryId(), values.getRecursive(),
                new CategoryDataSource.GetSubcategoriesCallback() {

                    @Override
                    public void onSubcategoriesLoaded(List<Category> categories) {
                        ResponseValue responseValue = new ResponseValue(categories);
                        getUseCaseCallback().onSuccess(responseValue);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        getUseCaseCallback().onError();
                    }

                }
        );
    }

    public static final class RequestValues implements UseCase.RequestValues {

        private final long mCategoryId;
        private final boolean mRecursive;

        public RequestValues(@NonNull long categoryId, boolean recursive) {
            mCategoryId = categoryId;
            mRecursive = recursive;
        }

        public long getCategoryId() {
            return mCategoryId;
        }

        public boolean getRecursive() {
            return mRecursive;
        }

    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private List<Category> mCategories;

        public ResponseValue(List<Category> categories) {
            mCategories = categories;
        }

        public List<Category> getCategories() {
            return mCategories;
        }

    }

}
