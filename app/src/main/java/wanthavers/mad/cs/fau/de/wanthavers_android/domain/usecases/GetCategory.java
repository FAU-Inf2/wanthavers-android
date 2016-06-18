package wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases;

import de.fau.cs.mad.wanthavers.common.Category;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.category.CategoryDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.category.CategoryRepository;

import static com.google.common.base.Preconditions.checkNotNull;

public class GetCategory extends UseCase<GetCategory.RequestValues, GetCategory.ResponseValue> {

    private final CategoryRepository mCategoryRepository;

    public GetCategory (CategoryRepository categoryRepository) {
        mCategoryRepository = checkNotNull(categoryRepository, "category repository cannot be null");
    }

    @Override
    protected void executeUseCase(final RequestValues values) {
        mCategoryRepository.getCategory(values.getCategoryId(), new CategoryDataSource.GetCategoryCallback() {
            @Override
            public void onCategoryLoaded(Category category) {
                ResponseValue responseValue = new ResponseValue(category);
                getUseCaseCallback().onSuccess(responseValue);
            }

            @Override
            public void onDataNotAvailable() {
                getUseCaseCallback().onError();
            }
        });
    }

    public static final class RequestValues implements UseCase.RequestValues {

        private final long mCategoryId;

        public RequestValues(long categoryId) {
            mCategoryId = categoryId;
        }

        public long getCategoryId() {
            return mCategoryId;
        }

    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private Category mCategory;

        public ResponseValue(Category category) {
            mCategory = category;
        }

        public Category getCategory() {
            return mCategory;
        }

    }

}
