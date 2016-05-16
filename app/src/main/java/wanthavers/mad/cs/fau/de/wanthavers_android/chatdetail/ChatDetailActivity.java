package wanthavers.mad.cs.fau.de.wanthavers_android.chatdetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.interceptors.ParseLogInterceptor;

import static com.google.common.base.Preconditions.checkNotNull;
import de.fau.cs.mad.wanthavers.common.User;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCaseHandler;
import wanthavers.mad.cs.fau.de.wanthavers_android.chatdetail.ChatDetailFragment;
import wanthavers.mad.cs.fau.de.wanthavers_android.chatdetail.ChatDetailPresenter;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.DesireRepository;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.local.DesireLocalDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.remote.DesireRemoteDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.NavHeaderBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.desiredetail.DesireDetailActivity;
import wanthavers.mad.cs.fau.de.wanthavers_android.desirelist.DesireListActivity;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetChatList;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetMessageList;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.ActivityUtils;

public class ChatDetailActivity extends AppCompatActivity {


    private DrawerLayout mDrawerLayout;
    private ChatDetailPresenter mChatDetailPresenter;
    public static final String USER_ID = "USER_ID";

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

        ChatDetailFragment chatDetailFragment = (ChatDetailFragment)
                getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if (chatDetailFragment == null) {
            // Create the fragment
            chatDetailFragment = ChatDetailFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), chatDetailFragment, R.id.contentFrame);
        }

        //create chatRepo
        Context context = getApplicationContext();
        checkNotNull(context);

        DesireRepository chatRepo = DesireRepository.getInstance(DesireRemoteDataSource.getInstance(getApplicationContext()), DesireLocalDataSource.getInstance(context));

        // Create the presenter
        mChatDetailPresenter = new ChatDetailPresenter(UseCaseHandler.getInstance(),chatDetailFragment,new GetMessageList(chatRepo));


        /* TODO decide whether viewModel needed
        DesireListViewModel desireListViewModel =
                new DesireListViewModel(getApplicationContext(), mDesireListPresenter);

        chatListFragment.setViewModel(desireListViewModel);

        */

        // Load previously saved state, if available.
        if (savedInstanceState != null) {
            /*TasksFilterType currentFiltering =
                    (TasksFilterType) savedInstanceState.getSerializable(CURRENT_FILTERING_KEY);
            mTasksPresenter.setFiltering(currentFiltering);
            */
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }



}