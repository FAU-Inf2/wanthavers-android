package wanthavers.mad.cs.fau.de.wanthavers_android.chatdetail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

import wanthavers.mad.cs.fau.de.wanthavers_android.R;

import wanthavers.mad.cs.fau.de.wanthavers_android.chatlist.Chat;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.ChatdetailFragBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.ChatlistFragBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.desirelist.ScrollChildSwipeRefreshLayout;

public class ChatDetailFragment extends Fragment implements  ChatDetailContract.View {


    private ChatDetailContract.Presenter mPresenter;
    private ChatDetailAdapter mListAdapter;
    private ChatDetailViewModel mChatDetailViewModel;
    private ChatdetailFragBinding mChatDetailFragBinding;

    static final String TAG  = ChatDetailActivity.class.getSimpleName();
    static final String USER_ID_KEY = "userId";
    static final String BODY_KEY = "body";
    EditText etMessage;         //TODO once parse server works use databinding
    Button btSend;              //TODO once parse server works use databinding

    public ChatDetailFragment(){
        //Requires empty public constructor
    }

    public static ChatDetailFragment newInstance(){ return new ChatDetailFragment();}


    @Override
    public void setPresenter(@NonNull ChatDetailContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onResume()  {
        super.onResume();
        mPresenter.start();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        mChatDetailFragBinding = ChatdetailFragBinding.inflate(inflater,container,false);

        mChatDetailFragBinding.setChats(mChatDetailViewModel);

        mChatDetailFragBinding.setPresenter(mPresenter);

        if (ParseUser.getCurrentUser() != null) { // start with existing user
            startWithCurrentUser();
        } else { // If not logged in, login as a new anonymous user
            login();
        }

        //Set up chat view
        ListView listView = mChatDetailFragBinding.messageList;
        final String chatUserId = ParseUser.getCurrentUser().getObjectId();

        mListAdapter = new ChatDetailAdapter(new ArrayList<Message>(0),chatUserId ,mPresenter, mChatDetailViewModel);
        listView.setAdapter(mListAdapter);


        // Set up progress indicator  TODO decide whether this is needed
        final ScrollChildSwipeRefreshLayout swipeRefreshLayout = mChatDetailFragBinding.refreshLayout;
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );

        // Set the scrolling view in the custom SwipeRefreshLayout
        swipeRefreshLayout.setScrollUpChild(listView);


        setHasOptionsMenu(true);


        View root = mChatDetailFragBinding.getRoot();
        return root;
    }

    @Override
    public void showMessages(List<Message> messageList) {
        mListAdapter.replaceData(messageList);
        //mChatDetailViewModel.setMessageListSize(messageList.size());
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showLoadingChatsError() {

    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }


    // Begin parse inclusion


    // Create an anonymous user using ParseAnonymousUtils and set sUserId
    void login() {
        ParseAnonymousUtils.logIn(new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Anonymous login failed: ", e);
                } else {
                    startWithCurrentUser();
                }
            }
        });
    }

    // Get the userId from the cached currentUser object
    void startWithCurrentUser() {
        setupMessagePosting();
    }

    // Setup button event handler which posts the entered message to Parse
    void setupMessagePosting() {
        // Find the text field and button

        etMessage = (EditText) mChatDetailFragBinding.etMessage;
        btSend = (Button) mChatDetailFragBinding.btSend;
        // When send button is clicked, create message object on Parse
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = etMessage.getText().toString();
                ParseObject message = ParseObject.create("Message");
                message.put(Message.USER_ID_KEY, ParseUser.getCurrentUser().getObjectId());
                message.put(Message.BODY_KEY, data);
                message.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Toast.makeText(getContext(), "Successfully created message on Parse",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                etMessage.setText(null);
            }
        });

    }
}
