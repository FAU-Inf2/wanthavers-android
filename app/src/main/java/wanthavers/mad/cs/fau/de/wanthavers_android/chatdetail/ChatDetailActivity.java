package wanthavers.mad.cs.fau.de.wanthavers_android.chatdetail;

import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

import de.fau.cs.mad.wanthavers.common.AppChatLastSeen;
import de.fau.cs.mad.wanthavers.common.Chat;
import de.fau.cs.mad.wanthavers.common.Media;
import de.fau.cs.mad.wanthavers.common.User;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCaseHandler;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.chat.ChatLocalDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.chat.ChatRemoteDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.chat.ChatRepository;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.desire.DesireRepository;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.desire.DesireLocalDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.desire.DesireRemoteDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.ormlite.AppChatLastSeenDatabaseHelper;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.user.UserLocalDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.user.UserRemoteDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.user.UserRepository;
import wanthavers.mad.cs.fau.de.wanthavers_android.desirelist.DesireListActivity;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.FlagUser;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetMessageList;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.SendMessage;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.ActivityUtils;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.RoundedTransformation;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.SharedPreferencesHelper;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.WantHaversTextView;

public class ChatDetailActivity extends AppCompatActivity {


    private ChatDetailPresenter mChatDetailPresenter;
    public static final String EXTRA_CHAT_ID = "CHAT_ID";
    public Chat mChat;


    private BroadcastReceiver notificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mChatDetailPresenter.loadMessages(true, false);
            abortBroadcast();
        }
    };


    @Override
    protected void onStart() {
        super.onStart();

        IntentFilter filter = new IntentFilter("WH_PUSH_NOTIFICATION_BROADCAST");
        filter.setPriority(1);

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

        setContentView(R.layout.chatdetail_act);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("");

        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);

        String chatId = getIntent().getStringExtra(EXTRA_CHAT_ID);

        ChatDetailFragment chatDetailFragment = (ChatDetailFragment)
                getSupportFragmentManager().findFragmentById(R.id.contentFrame);



        if (chatDetailFragment == null) {
            // Create the fragment
            chatDetailFragment = ChatDetailFragment.newInstance(chatId);
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), chatDetailFragment, R.id.contentFrame);
        }

        //create chatRepo
        Context context = getApplicationContext();
        checkNotNull(context);

        ChatRepository chatRepo = ChatRepository.getInstance(ChatRemoteDataSource.getInstance(getApplicationContext()), ChatLocalDataSource.getInstance(context));
        UserRepository userRepo = UserRepository.getInstance(UserRemoteDataSource.getInstance(getApplicationContext()), UserLocalDataSource.getInstance(context));

        // Create the presenter  also sets the presenter for the view
        mChatDetailPresenter = new ChatDetailPresenter(UseCaseHandler.getInstance(), chatId, chatDetailFragment,
                new GetMessageList(chatRepo), new SendMessage(chatRepo), new FlagUser(userRepo));


        // Load previously saved state, if available.
        if (savedInstanceState != null) {
            /*TasksFilterType currentFiltering =
                    (TasksFilterType) savedInstanceState.getSerializable(CURRENT_FILTERING_KEY);
            mTasksPresenter.setFiltering(currentFiltering);
            */
        }

        ChatDetailViewModel chatListViewModel =
                new ChatDetailViewModel(getApplicationContext(), mChatDetailPresenter);

        chatDetailFragment.setViewModel(chatListViewModel);

        //WantHaversTextView toolbarTitle = (WantHaversTextView) = findViewById(R.id.toolbar_title);


        //delete LastChat
        AppChatLastSeenDatabaseHelper appChatLastSeenDatabaseHelper = AppChatLastSeenDatabaseHelper.getInstance(context);
        appChatLastSeenDatabaseHelper.deleteById(chatId);


        mChat = (Chat) getIntent().getSerializableExtra("ChatOjbect");

        if(mChat != null) {
            SharedPreferencesHelper sharedPreferencesHelper = SharedPreferencesHelper.getInstance(SharedPreferencesHelper.NAME_USER, getApplicationContext());
            long loggedInUser = sharedPreferencesHelper.loadLong(SharedPreferencesHelper.KEY_USERID, 6L); //Long.valueOf(sharedPreferencesHelper.loadString(SharedPreferencesHelper.KEY_USERID, "6"));
            User otherUser = getOtherUser(mChat.getUserObject1(), mChat.getUserObject2(), loggedInUser);

            WantHaversTextView toolbarTitle = (WantHaversTextView) findViewById(R.id.toolbar_title);
            toolbarTitle.setText(otherUser.getName());

            ImageView otherUserPicture = (ImageView) findViewById(R.id.toolbar_picture);
            Media m = otherUser.getImage();

            if (m != null) {
                Picasso.with(context).load(m.getLowRes()).transform(new RoundedTransformation(1000, 0)).into(otherUserPicture);
            }

        }
    }

    public User getOtherUser(User user1, User user2, long loggedInUser){

        if(user1.getId() == loggedInUser){
            return user2;
        }

        return user1;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chat_detail_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_flag_user){
            SharedPreferencesHelper sharedPreferencesHelper = SharedPreferencesHelper.getInstance(SharedPreferencesHelper.NAME_USER, getApplicationContext());
            long loggedInUser = sharedPreferencesHelper.loadLong(SharedPreferencesHelper.KEY_USERID, 6L); //Long.valueOf(sharedPreferencesHelper.loadString(SharedPreferencesHelper.KEY_USERID, "6"));
            User otherUser = getOtherUser(mChat.getUserObject1(), mChat.getUserObject2(), loggedInUser);
            mChatDetailPresenter.openFlagUserPopup(otherUser);
        }

        if (item.getItemId() == android.R.id.home) {
            if(isTaskRoot()) {
                Intent intent = new Intent(getApplicationContext(), DesireListActivity.class);
                startActivity(intent);
            }

            this.finish();
            return true;
        }
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {

        if(isTaskRoot()) {
            Intent intent = new Intent(getApplicationContext(), DesireListActivity.class);
            startActivity(intent);
        }

        onBackPressed();
        return true;
    }



}