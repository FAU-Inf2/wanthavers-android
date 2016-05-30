package wanthavers.mad.cs.fau.de.wanthavers_android.chatdetail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

import de.fau.cs.mad.wanthavers.common.Message;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;

import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.ChatdetailFragBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.ChatlistFragBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.desirelist.ScrollChildSwipeRefreshLayout;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.SharedPreferencesHelper;

public class ChatDetailFragment extends Fragment implements  ChatDetailContract.View {


    private ChatDetailContract.Presenter mPresenter;
    private ChatDetailAdapter mListAdapter;
    private ChatDetailViewModel mChatDetailViewModel;
    private ChatdetailFragBinding mChatDetailFragBinding;
    private static final String CHAT_ID = "CHAT_ID";

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




        //Set up chat view
        RecyclerView recyclerView = mChatDetailFragBinding.messageList;

        SharedPreferencesHelper sharedPreferencesHelper = SharedPreferencesHelper.getInstance(SharedPreferencesHelper.NAME_USER, getContext().getApplicationContext());
        final long chatUserId = sharedPreferencesHelper.loadLong(SharedPreferencesHelper.KEY_USERID, 6L); //Long.valueOf(sharedPreferencesHelper.loadString(SharedPreferencesHelper.KEY_USERID, "6"));

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        mListAdapter = new ChatDetailAdapter(new ArrayList<Message>(0),chatUserId ,mPresenter);
        recyclerView.setAdapter(mListAdapter);

        //set up action handler
        ChatDetailActionHandler chatDetailActionHandler = new ChatDetailActionHandler(chatUserId, mChatDetailFragBinding, mPresenter);
        mChatDetailFragBinding.setActionHandler(chatDetailActionHandler);


        setHasOptionsMenu(true);


        View root = mChatDetailFragBinding.getRoot();
        return root;
    }


    public static ChatDetailFragment newInstance(String chatId){
        Bundle arguments = new Bundle();
        arguments.putString(CHAT_ID, chatId);
        ChatDetailFragment fragment = new ChatDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }



    @Override
    public void showMessages(List<Message> messageList) {
        mListAdapter.replaceData(messageList);
        mChatDetailViewModel.setMessageListSize(messageList.size());
    }

    public void showUpdatedMessageListonSendSuccess(Message message){
        List<Message> messages = mListAdapter.getList();

        messages.add(message);
        showMessages(messages);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }



    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showLoadingMessagesError() {
        showMessage(getString(R.string.loading_chats_error));
    }


    @Override
    public void showSendMessageError() {
        showMessage(getString(R.string.send_message_error));
    }

    public void setViewModel(ChatDetailViewModel viewModel){mChatDetailViewModel = viewModel;}

    private void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

}
