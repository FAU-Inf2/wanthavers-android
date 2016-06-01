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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.RatingBar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import de.fau.cs.mad.wanthavers.common.User;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCaseHandler;
import wanthavers.mad.cs.fau.de.wanthavers_android.cloudmessaging.RegistrationIntentService;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.desire.DesireRepository;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.desire.DesireLocalDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.desire.DesireRemoteDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.rating.RatingLocalDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.rating.RatingRemoteDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.rating.RatingRepository;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.user.UserLocalDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.user.UserRemoteDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.user.UserRepository;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.NavHeaderBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.DesireLogic;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetAvgRatingForUser;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetDesireList;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetUser;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.ActivityUtils;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.SharedPreferencesHelper;

import static com.google.common.base.Preconditions.checkNotNull;

public class DesireListActivity extends AppCompatActivity {
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

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
        mDrawerLayout.setStatusBarBackground(R.color.colorSecondary);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        NavHeaderBinding navHeaderBinding = null;

        if (navigationView != null) {

            navHeaderBinding = NavHeaderBinding.inflate(LayoutInflater.from(navigationView.getContext()));
            navigationView.addHeaderView(navHeaderBinding.getRoot());
            //TODO - change dummy user to get real logged in user and also get real rating

            User noUser = new User(getString(R.string.noUser),getString(R.string.noUser));
            noUser.setRating(0.0);
            navHeaderBinding.setUser(noUser);

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

        if(navHeaderBinding != null) {
            desireListFragment.setNavBinding(navHeaderBinding);
        }

        //create fake task repo
        Context context = getApplicationContext();
        checkNotNull(context);

        DesireRepository desireRepository = DesireRepository.getInstance(DesireRemoteDataSource.getInstance(context), DesireLocalDataSource.getInstance(context));
        RatingRepository ratingRepository = RatingRepository.getInstance(RatingRemoteDataSource.getInstance(context), RatingLocalDataSource.getInstance(context));
        UserRepository userRepository = UserRepository.getInstance(UserRemoteDataSource.getInstance(context), UserLocalDataSource.getInstance(context));

        // Create the presenter
        mDesireListPresenter = new DesireListPresenter(UseCaseHandler.getInstance(),desireListFragment,new GetDesireList(desireRepository),
                new GetAvgRatingForUser(ratingRepository), new GetUser(userRepository));

        DesireListViewModel desireListViewModel = new DesireListViewModel(context, mDesireListPresenter);

        desireListFragment.setViewModel(desireListViewModel);

        DesireLogic desireLogic = new DesireLogic(context);

        desireListFragment.setDesireLogic(desireLogic);


        // TODO: Check for compatible Google Play service APK
        // Uncomment this to get unique token for cloud messaging
        // Intent intent = new Intent(this, RegistrationIntentService.class);
        // startService(intent);
		
		
        SharedPreferencesHelper sharedPreferencesHelper = SharedPreferencesHelper.getInstance(SharedPreferencesHelper.NAME_USER, getApplicationContext());
        long loggedInUser = sharedPreferencesHelper.loadLong(SharedPreferencesHelper.KEY_USERID, 6L); //Long.valueOf(sharedPreferencesHelper.loadString(SharedPreferencesHelper.KEY_USERID, "6"));
        mDesireListPresenter.getUser(loggedInUser);


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
                //Open the navigation drawer when the home icon is selected from the toolbar.
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
                                /*
                                Intent intent =
                                        new Intent(DesireListActivity.this, DesireListActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                */
                                break;
                            case R.id.createDesire_navigation_menu_item:
                                mDesireListPresenter.createNewDesire();
                                break;
                            case R.id.logout_navigation_menu_item:
                                mDesireListPresenter.createLogout();
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

    @Override
    public void onBackPressed() {
        //disables back button
    }

}
