package wanthavers.mad.cs.fau.de.wanthavers_android.filtersetting;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;

import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCaseHandler;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.category.CategoryLocalDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.category.CategoryRemoteDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.category.CategoryRepository;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.location.LocationLocalDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.location.LocationRemoteDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.location.LocationRepository;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.CreateLocation;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.DeleteLocation;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetSavedLocations;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetSubcategories;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.UpdateLocation;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.ActivityUtils;

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
        actionBar.setTitle(R.string.filtersetting_title);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

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
                new GetSubcategories(categoryRepository), new CreateLocation(locationRepository),
                new GetSavedLocations(locationRepository), new UpdateLocation(locationRepository),
                new DeleteLocation(locationRepository));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
