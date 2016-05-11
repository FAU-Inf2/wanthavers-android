package wanthavers.mad.cs.fau.de.wanthavers_android.desirelist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCaseHandler;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.DesireRepository;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.local.DesireLocalDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.remote.DesireRemoteDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetDesireList;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.ActivityUtils;

import static com.google.common.base.Preconditions.checkNotNull;

public class DesireListActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private DesireListPresenter mDesireListPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.desirelist_act);

        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        // Set up the navigation drawer.
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        DesireListFragment desireListFragment = (DesireListFragment)
                getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if (desireListFragment == null) {
            // Create the fragment
            desireListFragment = DesireListFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), desireListFragment, R.id.contentFrame);
        }


        //create fake task repo
        Context context = getApplicationContext();
        checkNotNull(context);

        DesireRepository fake = DesireRepository.getInstance(DesireRemoteDataSource.getInstance(), DesireLocalDataSource.getInstance(context));

        // Create the presenter
        mDesireListPresenter = new DesireListPresenter(UseCaseHandler.getInstance(),desireListFragment,new GetDesireList(fake));

        DesireListViewModel desireListViewModel =
                new DesireListViewModel(getApplicationContext(), mDesireListPresenter);

        desireListFragment.setViewModel(desireListViewModel);

        // Load previously saved state, if available.
        if (savedInstanceState != null) {
            /*TasksFilterType currentFiltering =
                    (TasksFilterType) savedInstanceState.getSerializable(CURRENT_FILTERING_KEY);
            mTasksPresenter.setFiltering(currentFiltering);
            */
        }
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Open the navigation drawer when the home icon is selected from the toolbar.
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.listDesires_navigation_menu_item:
                                // Do nothing, we're already on that screen
                                break;
                            case R.id.settings_navigation_menu_item:
                                Intent intent =
                                        new Intent(DesireListActivity.this, DesireListActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                break;
                            default:
                                break;
                        }
                        // Close the navigation drawer when an item is selected.
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

}
