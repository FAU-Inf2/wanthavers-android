package wanthavers.mad.cs.fau.de.wanthavers_android.chatlist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.ScrollView;

import static com.google.common.base.Preconditions.checkNotNull;
import de.fau.cs.mad.wanthavers.common.User;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCaseHandler;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.WantHaversApplication;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.chat.ChatLocalDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.chat.ChatRemoteDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.chat.ChatRepository;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.desire.DesireRepository;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.desire.DesireLocalDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.desire.DesireRemoteDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.user.UserLocalDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.user.UserRemoteDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.user.UserRepository;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.NavHeaderBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.desirelist.DesireListActivity;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetChatList;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetDesire;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetUser;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.ActivityUtils;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.SharedPreferencesHelper;


public class ChatListActivity extends AppCompatActivity {

    private ChatListPresenter mChatListPresenter;
    public static final String USER_ID = "USER_ID";
    private long mLoggedInUserId;

    private BroadcastReceiver notificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mChatListPresenter.loadChats(true);
            abortBroadcast();
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
        setContentView(R.layout.chatlist_act);

        SharedPreferencesHelper sharedPreferencesHelper = SharedPreferencesHelper.getInstance(SharedPreferencesHelper.NAME_USER, getApplicationContext());
        mLoggedInUserId = sharedPreferencesHelper.loadLong(SharedPreferencesHelper.KEY_USERID, 6L); //Long.valueOf(sharedPreferencesHelper.loadString(SharedPreferencesHelper.KEY_USERID, "6"));



        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        ab.setTitle("");

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
        mChatListPresenter = new ChatListPresenter(UseCaseHandler.getInstance(),chatListFragment,new GetChatList(chatRepo),mLoggedInUserId);


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
    public void onResume(){
        super.onResume();

        WantHaversApplication.setNewMessages(false);

        SharedPreferencesHelper sharedPreferencesHelper = SharedPreferencesHelper.getInstance(SharedPreferencesHelper.NAME_USER, getApplicationContext());
        long loggedInUser = sharedPreferencesHelper.loadLong(SharedPreferencesHelper.KEY_USERID, 6L); //Long.valueOf(sharedPreferencesHelper.loadString(SharedPreferencesHelper.KEY_USERID, "6"));
        mChatListPresenter.setLoggedInUser(loggedInUser);
        mChatListPresenter.loadChats(true);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
