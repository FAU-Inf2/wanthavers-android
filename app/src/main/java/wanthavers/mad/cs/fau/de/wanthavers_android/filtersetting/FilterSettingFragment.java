package wanthavers.mad.cs.fau.de.wanthavers_android.filtersetting;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.fau.cs.mad.wanthavers.common.Category;
import de.fau.cs.mad.wanthavers.common.Location;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.FiltersettingFragBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.desirelist.DesireListActivity;
import wanthavers.mad.cs.fau.de.wanthavers_android.maps.MapActivity;

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

        mFilterSettingActionHandler = new FilterSettingActionHandler(mPresenter, mFilterSettingFragBinding);

        mFilterSettingFragBinding.setActionHandler(mFilterSettingActionHandler);

        //Set up autocompletetextview
        mListAdapter = new FilterSettingAdapter(getContext(), R.layout.category_item, new ArrayList<Category>(0), mFilterSettingActionHandler);
        final InstantAutoComplete autoCompleteTextView = (InstantAutoComplete) mFilterSettingFragBinding.spinnerCategory;
        //autoCompleteTextView.setThreshold(0);
        autoCompleteTextView.setShowAlways(true);

        /*autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Category selected = (Category) parent.getItemAtPosition(position);
                mListAdapter.setSelected(selected);
            }
        });*/
        autoCompleteTextView.setAdapter(mListAdapter);

        //Set up radius spinner
        Spinner spinner = (Spinner) mFilterSettingFragBinding.spinnerRadius;
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.radius, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        int defaultLocationId = adapter.getPosition("5km");
        spinner.setSelection(defaultLocationId);

        return mFilterSettingFragBinding.getRoot();
    }

    @Override
    public void showDesireList(){
        Intent intent = new Intent(getContext(), DesireListActivity.class);
        startActivity(intent);
    }

    @Override
    public void showMap() {
        Intent intent = new Intent(getContext(), MapActivity.class);
        intent.putExtra("desireTitle", "");
        startActivity(intent);
    }

    @Override
    public Location getLocation() {
        System.out.println("Reached1.0");
        if (getActivity().getIntent().getExtras() == null) {
            System.out.println("Reached1.1");
            showNoLocationSetError();
            return null;
        } else {
            System.out.println("Reached1.2");
            String location = getActivity().getIntent().getExtras().getString("desireLocation");
            double lat = Double.parseDouble(getActivity().getIntent().getExtras().getString("desireLocationLat"));
            double lon = Double.parseDouble(getActivity().getIntent().getExtras().getString("desireLocationLng"));
            Location ret = new Location();
            ret.setLat(lat);
            ret.setLon(lon);
            ret.setFullAddress(location);

            return ret;
        }
    }

    @Override
    public String[] getRadiusArray() {
        return getResources().getStringArray(R.array.radius);
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

    @Override
    public void showNoLocationSetError() {
        showMessage(getString(R.string.get_no_location_set_error));
    }

    @Override
    public void showFilterChangeSuccess() {
        showMessage(getString(R.string.filter_change_success));
    }

    @Override
    public int getPriceClicked() {
        return mFilterSettingActionHandler.getPriceClicked();
    }

    @Override
    public Category getSelectedCategory() {
        InstantAutoComplete instantAutoComplete = mFilterSettingFragBinding.spinnerCategory;
        String input = instantAutoComplete.getText().toString();
        if (input.compareTo("") == 0) {
            Category category = new Category();
            category.setName("all");
            return category;
        }

        for (int i = 0; i < mListAdapter.getCount(); i++) {
            String tmp = mListAdapter.getItem(i).getName();
            if (input.compareTo(tmp) == 0) {
                return mListAdapter.getItem(i);
            }
        }

        showMessage(getString(R.string.no_category_match));

        return null;
    }
}
