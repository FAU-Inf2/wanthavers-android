package wanthavers.mad.cs.fau.de.wanthavers_android.categorylist;

import android.support.annotation.NonNull;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.Category;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCaseHandler;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetSubcategories;

import static com.google.common.base.Preconditions.checkNotNull;

public class CategoryListPresenter implements CategoryListContract.Presenter {

    private final CategoryListContract.View mCategoryListView;
    private final UseCaseHandler mUseCaseHandler;

    private final GetSubcategories mGetSubcategories;

    public CategoryListPresenter(@NonNull UseCaseHandler useCaseHandler, @NonNull CategoryListContract.View categoryListView,
                                 @NonNull GetSubcategories getSubcategories) {
        mCategoryListView = checkNotNull(categoryListView, "categorylist view cannot be null");
        mUseCaseHandler = checkNotNull(useCaseHandler, "useCaseHandler cannot be null");
        mGetSubcategories = checkNotNull(getSubcategories);
    }

    @Override
    public void start() {
        loadCategories();
    }

    public void loadCategories() {

        GetSubcategories.RequestValues requestValues = new GetSubcategories.RequestValues(0, false);

        mUseCaseHandler.execute(mGetSubcategories, requestValues, new UseCase.UseCaseCallback<GetSubcategories.ResponseValue>() {

            @Override
            public void onSuccess(GetSubcategories.ResponseValue response) {
                List<Category> categories = response.getCategories();
                mCategoryListView.showCategories(categories);
            }

            @Override
            public void onError() {
                mCategoryListView.showGetCategoriesError();
            }

        });
    }

    @Override
    public void selectCategory(Category category) {
        mCategoryListView.returnCategory(category);
    }

}
