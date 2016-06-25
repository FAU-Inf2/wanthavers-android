package wanthavers.mad.cs.fau.de.wanthavers_android.categorylist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import de.fau.cs.mad.wanthavers.common.Category;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.CategorylistFragBinding;

import static com.google.common.base.Preconditions.checkNotNull;

public class CategoryListFragment extends Fragment implements CategoryListContract.View {

    private CategoryListContract.Presenter mPresenter;
    private CategorylistFragBinding mCategorylistFragBinding;
    private CategoryListAdapter mCategoryListAdapter;

    public CategoryListFragment() {
        //requires empty public constructor
    }

    public static CategoryListFragment newInstance() {
        return new CategoryListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mCategorylistFragBinding = CategorylistFragBinding.inflate(inflater, container, false);

        RecyclerView recyclerView = mCategorylistFragBinding.categoryList;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        //set up category list
        mCategoryListAdapter = new CategoryListAdapter(new ArrayList<Category>(0), mPresenter);
        recyclerView.setAdapter(mCategoryListAdapter);

        return mCategorylistFragBinding.getRoot();
    }

    @Nullable
    @Override
    public void setPresenter(@NonNull CategoryListContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void showCategories(List<Category> categoryList) {
        mCategoryListAdapter.replaceData(categoryList);
    }

    @Override
    public void returnCategory(Category category) {
        Intent intent = new Intent();
        intent.putExtra("selectedCategory", category);
        getActivity().setResult(1, intent);
        getActivity().finish();
    }

    private void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showGetCategoriesError() {
        showMessage(getString(R.string.get_categories_error));
    }
}
