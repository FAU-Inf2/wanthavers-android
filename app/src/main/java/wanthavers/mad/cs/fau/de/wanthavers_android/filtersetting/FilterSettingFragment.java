package wanthavers.mad.cs.fau.de.wanthavers_android.filtersetting;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;

import de.fau.cs.mad.wanthavers.common.Category;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.FiltersettingFragBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.desirelist.DesireListActivity;

import static com.google.common.base.Preconditions.checkNotNull;

public class FilterSettingFragment extends Fragment implements FilterSettingContract.View {
    private FilterSettingContract.Presenter mPresenter;
    private FiltersettingFragBinding mFilterSettingFragBinding;
    private FilterSettingAdapter mListAdapter;
    private FilterSettingActionHandler mFilterSettingActionHandler;

    public FilterSettingFragment() {
        //Requires empty public constructor
    }

    public static FilterSettingFragment newInstance() {
        return new FilterSettingFragment();
    }

    @Override
    public void setPresenter(@NonNull FilterSettingContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        mFilterSettingFragBinding  = FiltersettingFragBinding.inflate(inflater,container,false);

        mFilterSettingFragBinding.setPresenter(mPresenter);
        mFilterSettingFragBinding.setActionHandler(mFilterSettingActionHandler);

        mListAdapter = new FilterSettingAdapter(getContext(), R.layout.category_item, new ArrayList<Category>(0), mFilterSettingActionHandler);
        final InstantAutoComplete autoCompleteTextView = (InstantAutoComplete) mFilterSettingFragBinding.spinnerCategory;
        autoCompleteTextView.setThreshold(0);
        autoCompleteTextView.setShowAlways(true);

        autoCompleteTextView.setAdapter(mListAdapter);

        return mFilterSettingFragBinding.getRoot();
    }

    @Override
    public void showDesireList(){
        Intent intent = new Intent(getContext(), DesireListActivity.class);
        startActivity(intent);
    }

    @Override
    public void showCategories(List<Category> categories) {
        mListAdapter.replaceData(categories);
        mListAdapter.getFilter().filter(null);
    }

    private void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showGetCategoriesError() {
        showMessage(getString(R.string.get_categories_error));
    }
}
