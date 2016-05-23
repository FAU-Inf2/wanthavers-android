package wanthavers.mad.cs.fau.de.wanthavers_android.chatlist;

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
import static com.google.common.base.Preconditions.checkNotNull;
import de.fau.cs.mad.wanthavers.common.User;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCaseHandler;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.chat.ChatLocalDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.chat.ChatRemoteDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.chat.ChatRepository;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.desire.DesireRepository;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.desire.DesireLocalDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.desire.DesireRemoteDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.NavHeaderBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.desirelist.DesireListActivity;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetChatList;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.ActivityUtils;


public class ChatListActivity extends AppCompatActivity {


    private DrawerLayout mDrawerLayout;
    private ChatListPresenter mChatListPresenter;
    public static final String USER_ID = "USER_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatlist_act);


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

        if (navigationView != null) {

            NavHeaderBinding navHeaderBinding = NavHeaderBinding.inflate(LayoutInflater.from(navigationView.getContext()));
            navigationView.addHeaderView(navHeaderBinding.getRoot());
            //TODO - change dummy user to get real logged in user and also get real rating
            User user = new User("TestUser", "testuser@testuser.de");
            navHeaderBinding.setUser(user);
            RatingBar itemRateBar = (RatingBar) navHeaderBinding.getRoot().findViewById(R.id.nav_header_temRatingBar);
            itemRateBar.setRating(2.0f);

            setupDrawerContent(navigationView);

        }

        ChatListFragment chatListFragment = (ChatListFragment)
                getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if (chatListFragment == null) {
            // Create the fragment
            chatListFragment = ChatListFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), chatListFragment, R.id.contentFrame);
        }


        //create chatRepo
        Context context = getApplicationContext();
        checkNotNull(context);

        ChatRepository chatRepo = ChatRepository.getInstance(ChatRemoteDataSource.getInstance(getApplicationContext()), ChatLocalDataSource.getInstance(context));

        // Create the presenter
        mChatListPresenter = new ChatListPresenter(UseCaseHandler.getInstance(),chatListFragment,new GetChatList(chatRepo));


        // Load previously saved state, if available.
        if (savedInstanceState != null) {
            /*TasksFilterType currentFiltering =
                    (TasksFilterType) savedInstanceState.getSerializable(CURRENT_FILTERING_KEY);
            mTasksPresenter.setFiltering(currentFiltering);
            */
        }


        ChatListViewModel chatListViewModel =
                new ChatListViewModel(getApplicationContext(), mChatListPresenter);

        chatListFragment.setViewModel(chatListViewModel);
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
                                Intent intent = new Intent(getApplicationContext(), DesireListActivity.class);
                                startActivity(intent);
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
