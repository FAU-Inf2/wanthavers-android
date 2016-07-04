package wanthavers.mad.cs.fau.de.wanthavers_android.filtersetting;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCaseHandler;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.category.CategoryLocalDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.category.CategoryRemoteDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.category.CategoryRepository;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.location.LocationLocalDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.location.LocationRemoteDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.location.LocationRepository;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetCategory;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.ActivityUtils;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.WantHaversTextView;

import static com.google.common.base.Preconditions.checkNotNull;

public class FilterSettingActivity extends AppCompatActivity {

    private FilterSettingPresenter mFilterSettingPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filtersetting_act);

        //set up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        WantHaversTextView abTitle = (WantHaversTextView) findViewById(R.id.toolbar_title);
        abTitle.setText(getString(R.string.filtersetting_title));

        FilterSettingFragment filterSettingFragment = (FilterSettingFragment)
                getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if (filterSettingFragment == null) {
            filterSettingFragment = FilterSettingFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    filterSettingFragment, R.id.contentFrame);
        }

        //create fake repo for categories
        Context context = getApplicationContext();
        checkNotNull(context);

        CategoryRepository categoryRepository = CategoryRepository.getInstance(CategoryRemoteDataSource.getInstance(getApplicationContext()), CategoryLocalDataSource.getInstance(context));
        LocationRepository locationRepository = LocationRepository.getInstance(LocationRemoteDataSource.getInstance(getApplicationContext()), LocationLocalDataSource.getInstance(context));

        mFilterSettingPresenter = new FilterSettingPresenter(UseCaseHandler.getInstance(), filterSettingFragment, this,
                new GetCategory(categoryRepository));

        filterSettingFragment.setPresenter(mFilterSettingPresenter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
