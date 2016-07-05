package wanthavers.mad.cs.fau.de.wanthavers_android.desirelist;

import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;


import de.fau.cs.mad.wanthavers.common.DesireFilter;
import de.fau.cs.mad.wanthavers.common.User;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCaseHandler;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.WantHaversApplication;
import wanthavers.mad.cs.fau.de.wanthavers_android.cloudmessaging.MessageNotificationType;
import wanthavers.mad.cs.fau.de.wanthavers_android.cloudmessaging.PushMessageNotification;
import wanthavers.mad.cs.fau.de.wanthavers_android.cloudmessaging.RegistrationIntentService;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.cloudmessagetoken.CloudMessageTokenLocalDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.cloudmessagetoken.CloudMessageTokenRemoteDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.cloudmessagetoken.CloudMessageTokenRepository;
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
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.DeleteToken;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetAvgRatingForUser;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetDesireList;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetUser;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.ActivityUtils;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.SharedPreferencesHelper;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.WantHaversTextView;

import static com.google.common.base.Preconditions.checkNotNull;
import static wanthavers.mad.cs.fau.de.wanthavers_android.desirelist.DesireListType.*;

public class DesireListActivity extends AppCompatActivity {
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String EXTRA_FRAGMENT_ID = "FRAGMENT_ID";
    private int backButtonCount = 0;

    private DrawerLayout mDrawerLayout;
    private DesireListPresenter mDesireListPresenter;
    private DesireListFragment mDesireListFragment;


    private BroadcastReceiver notificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            PushMessageNotification pushMessageNotification = intent.getExtras().getParcelable("WH_PUSH_NOTIFICATION");

            if(pushMessageNotification.mMessageNotificationType.equals(MessageNotificationType.CHAT_MESSAGE.toString())){
                MenuView.ItemView chatItem = (MenuView.ItemView) findViewById(R.id.menu_chat);
                Drawable iconNewMessage = getResources().getDrawable(R.drawable.wh_chat_icon_new_message_light,null);
                chatItem.setIcon(iconNewMessage);
            }
        }
    };


    @Override
    protected void onStart() {
        super.onStart();

        IntentFilter filter = new IntentFilter("WH_PUSH_NOTIFICATION_BROADCAST");
        filter.setPriority(10);

        registerReceiver(notificationReceiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(notificationReceiver);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.desirelist_act);

        DesireListType desireListType = (DesireListType) getIntent().getSerializableExtra(EXTRA_FRAGMENT_ID);

        if(desireListType == null){
            desireListType = DesireListType.ALL_DESIRES;
        }


        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.wh_menu_icon_light);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("");

        WantHaversTextView abTitle = (WantHaversTextView) findViewById(R.id.toolbar_title);

        switch(desireListType){
            case ALL_DESIRES: abTitle.setText(getString(R.string.listDesires_title));
                break;
            case MY_DESIRES: abTitle.setText(getString(R.string.myDesires_title));
                break;
            case MY_TRANSACTIONS: abTitle.setText(getString(R.string.myTransactions_title));
                break;
            default: abTitle.setText(getString(R.string.wanthavers_text));
        }

        // Set up the navigation drawer.
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackground(R.color.colorPrimary);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        NavHeaderBinding navHeaderBinding = null;

        if (navigationView != null) {

            navHeaderBinding = NavHeaderBinding.inflate(LayoutInflater.from(navigationView.getContext()));
            navigationView.addHeaderView(navHeaderBinding.getRoot());
            //TODO - change dummy user to get real logged in user and also get real rating

            User noUser = new User(getString(R.string.noUser),getString(R.string.noUser));
            noUser.setRating(0);
            navHeaderBinding.setUser(noUser);

            setupDrawerContent(navigationView);

        }

        mDesireListFragment = (DesireListFragment)
                getSupportFragmentManager().findFragmentById(R.id.contentFrame);


        if (mDesireListFragment == null) {
            // Create the fragment
            mDesireListFragment = DesireListFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), mDesireListFragment, R.id.contentFrame);
        }

        if(navHeaderBinding != null) {
            mDesireListFragment.setNavBinding(navHeaderBinding);
        }

        //create fake task repo
        Context context = getApplicationContext();
        checkNotNull(context);

        DesireRepository desireRepository = DesireRepository.getInstance(DesireRemoteDataSource.getInstance(context), DesireLocalDataSource.getInstance(context));
        RatingRepository ratingRepository = RatingRepository.getInstance(RatingRemoteDataSource.getInstance(context), RatingLocalDataSource.getInstance(context));
        UserRepository userRepository = UserRepository.getInstance(UserRemoteDataSource.getInstance(context), UserLocalDataSource.getInstance(context));

        CloudMessageTokenRepository tokenRepository = CloudMessageTokenRepository.getInstance(CloudMessageTokenRemoteDataSource.getInstance(context), CloudMessageTokenLocalDataSource.getInstance(context));

        // Create the presenter
        mDesireListPresenter = new DesireListPresenter(UseCaseHandler.getInstance(),mDesireListFragment,new GetDesireList(desireRepository),
                new GetAvgRatingForUser(ratingRepository), new GetUser(userRepository), getApplicationContext(), new DeleteToken(tokenRepository));

        mDesireListPresenter.setDesireListType(desireListType);

        DesireListViewModel desireListViewModel = new DesireListViewModel(context, mDesireListPresenter);

        mDesireListFragment.setViewModel(desireListViewModel);

        DesireLogic desireLogic = new DesireLogic(context);

        mDesireListFragment.setDesireLogic(desireLogic);


        // TODO: Check for compatible Google Play service APK
        // Uncomment this to get unique token for cloud messaging
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
		
		
        SharedPreferencesHelper sharedPreferencesHelper = SharedPreferencesHelper.getInstance(SharedPreferencesHelper.NAME_USER, getApplicationContext());
        long loggedInUser = sharedPreferencesHelper.loadLong(SharedPreferencesHelper.KEY_USERID, 6L); //Long.valueOf(sharedPreferencesHelper.loadString(SharedPreferencesHelper.KEY_USERID, "6"));
        mDesireListPresenter.getUser(loggedInUser);


        mDesireListPresenter.setUser(loggedInUser);
        updateChatIconOnNewMessageReceived();

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
                                WantHaversApplication.setCurDesireFilter(new DesireFilter());
                                mDesireListPresenter.openAllDesires();
                                break;
                            case R.id.myDesires_navigation_menu_item:
                                WantHaversApplication.setCurDesireFilter(new DesireFilter());
                                mDesireListPresenter.openMyDesires();
                                break;
                            case R.id.myTransactions_navigation_menu_item:
                                WantHaversApplication.setCurDesireFilter(new DesireFilter());
                                mDesireListPresenter.openMyTransactions();
                                break;
                            case R.id.settings_navigation_menu_item:
                                mDesireListPresenter.openSettings();
                                break;
                            case R.id.createDesire_navigation_menu_item:
                                mDesireListPresenter.createNewDesire();
                                break;
                            case R.id.logout_navigation_menu_item:
                                mDesireListPresenter.createLogout();
                                break;
                            case R.id.about_app:
                                mDesireListPresenter.createAbout();
                                break;
                            default:
                                break;
                        }
                        // Close the navigation drawer when an item is selected.
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });

    }

    @Override
    public void onBackPressed() {
        //finish();
        //System.exit(0);

        if(backButtonCount < 1) {
            mDrawerLayout.closeDrawers();
        }
        if(backButtonCount >= 1)
        {
            backButtonCount = 0;
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, getString(R.string.close_App_on_BackButton), Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }

    @Override
    public void onResume(){
        super.onResume();

        backButtonCount = 0;

        updateChatIconOnNewMessageReceived();

        SharedPreferencesHelper sharedPreferencesHelper = SharedPreferencesHelper.getInstance(SharedPreferencesHelper.NAME_USER, getApplicationContext());
        long loggedInUser = sharedPreferencesHelper.loadLong(SharedPreferencesHelper.KEY_USERID, 6L); //Long.valueOf(sharedPreferencesHelper.loadString(SharedPreferencesHelper.KEY_USERID, "6"));
        mDesireListPresenter.getUser(loggedInUser);
    }


    public void updateChatIconOnNewMessageReceived(){


        MenuView.ItemView chatItem = (MenuView.ItemView) findViewById(R.id.menu_chat);
        Drawable iconNewMessage;
        Drawable iconNoNewMessage;
        if(android.os.Build.VERSION.SDK_INT >= 21) {
            iconNewMessage = getResources().getDrawable(R.drawable.wh_chat_icon_new_message_light, null);
            iconNoNewMessage = getResources().getDrawable(R.drawable.wh_chat_icon_light, null);
        } else {
            iconNewMessage = getResources().getDrawable(R.drawable.wh_chat_icon_new_message_light);
            iconNoNewMessage = getResources().getDrawable(R.drawable.wh_chat_icon_light);
        }

        if(chatItem != null) {

            if (WantHaversApplication.getNewMessages()) {
                chatItem.setIcon(iconNewMessage);
            }else{
                chatItem.setIcon(iconNoNewMessage);
            }
        }
    }


}
