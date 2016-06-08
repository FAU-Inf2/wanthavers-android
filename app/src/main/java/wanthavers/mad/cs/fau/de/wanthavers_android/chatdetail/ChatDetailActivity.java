package wanthavers.mad.cs.fau.de.wanthavers_android.chatdetail;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ScrollView;

import static com.google.common.base.Preconditions.checkNotNull;

import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCaseHandler;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.chat.ChatLocalDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.chat.ChatRemoteDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.chat.ChatRepository;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.desire.DesireRepository;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.desire.DesireLocalDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.desire.DesireRemoteDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetMessageList;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.SendMessage;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.ActivityUtils;

public class ChatDetailActivity extends AppCompatActivity {


    private ChatDetailPresenter mChatDetailPresenter;
    public static final String EXTRA_CHAT_ID = "CHAT_ID";
    private MessageAlarm mMessageAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.chatdetail_act);




        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.message);

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

        // Create the presenter  also sets the presenter for the view
        mChatDetailPresenter = new ChatDetailPresenter(UseCaseHandler.getInstance(),chatId, chatDetailFragment,
                new GetMessageList(chatRepo), new SendMessage(chatRepo));


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

        MessageService.setPresenter(mChatDetailPresenter);
        MessageService.setActive(true);
        Intent intent = new Intent(this, MessageService.class);
        startService(intent);

    }


    @Override
    public void onPause(){
        super.onPause();
        MessageService.setActive(false);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        MessageService.setActive(false);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }



}