package wanthavers.mad.cs.fau.de.wanthavers_android.chatdetail;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

import de.fau.cs.mad.wanthavers.common.Message;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;

import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.ChatdetailFragBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.chatdetail.ScrollChildSwipeRefreshLayout;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.SharedPreferencesHelper;

public class ChatDetailFragment extends Fragment implements  ChatDetailContract.View {


    private ChatDetailContract.Presenter mPresenter;
    private ChatDetailAdapter mListAdapter;
    private RecyclerView mRecyclerView;
    private ChatDetailViewModel mChatDetailViewModel;
    private ChatdetailFragBinding mChatDetailFragBinding;
    private ChatDetailActionHandler mChatDetailActionHandler;
    private static final String CHAT_ID = "CHAT_ID";
    private long mLoggedInUser;

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
        getAndSetUser();

        if(mListAdapter != null){
            mListAdapter.setLoggedInUser(mLoggedInUser);
        }

        if(mChatDetailActionHandler != null){
            mChatDetailActionHandler.setLoggedInUser(mLoggedInUser);
        }

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
        mRecyclerView = mChatDetailFragBinding.messageList;

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        mListAdapter = new ChatDetailAdapter(new ArrayList<Message>(0),mLoggedInUser ,mPresenter);
        mRecyclerView.setAdapter(mListAdapter);


        final ScrollChildSwipeRefreshLayout swipeRefreshLayout = mChatDetailFragBinding.refreshLayout;
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );


        if (Build.VERSION.SDK_INT >= 23) {
            mRecyclerView.setOnScrollChangeListener(new RecyclerView.OnScrollChangeListener() {

                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                    if(!v.canScrollVertically(1)){
                        mPresenter.loadMessages(true,false);
                    }
                }
            });
        } else {
            mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    if(!recyclerView.canScrollVertically(1)){
                        mPresenter.loadMessages(true, false);
                    }
                }
            });
        }


        mRecyclerView.addOnLayoutChangeListener(new RecyclerView.OnLayoutChangeListener(){


            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if(bottom < oldBottom) {
                    mPresenter.loadMessages(true, false);
                }
            }
        });


        swipeRefreshLayout.setScrollUpChild(mRecyclerView);


        //set up action handler
        mChatDetailActionHandler = new ChatDetailActionHandler(mLoggedInUser, mChatDetailFragBinding, mPresenter);
        mChatDetailFragBinding.setActionHandler(mChatDetailActionHandler);


        setHasOptionsMenu(true);


        View root = mChatDetailFragBinding.getRoot();
        return root;
    }


    public void getAndSetUser(){
        SharedPreferencesHelper sharedPreferencesHelper = SharedPreferencesHelper.getInstance(SharedPreferencesHelper.NAME_USER, getContext().getApplicationContext());
        mLoggedInUser = sharedPreferencesHelper.loadLong(SharedPreferencesHelper.KEY_USERID, 6L);
    }


    public static ChatDetailFragment newInstance(String chatId){
        Bundle arguments = new Bundle();
        arguments.putString(CHAT_ID, chatId);
        ChatDetailFragment fragment = new ChatDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }



    @Override
    public void showMessages(List<Message> messageList, boolean loadOldMessages) {
        mListAdapter.replaceData(messageList);

        mChatDetailViewModel.setMessageListSize(messageList.size());
        if(!loadOldMessages) {
            mRecyclerView.scrollToPosition(messageList.size() - 1);
        }
    }

    public void showUpdatedMessageListonSendSuccess(Message message){
        List<Message> messages = mListAdapter.getList();

        messages.add(message);
        showMessages(messages, false);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }



    @Override
    public void setLoadingIndicator(final boolean active) {
        if (getView() == null) {
            return;
        }


        final SwipeRefreshLayout srl =
                (SwipeRefreshLayout) getView().findViewById(R.id.refresh_layout);

        // Make sure setRefreshing() is called after the layout is done with everything else.
        srl.post(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(active);
            }
        });
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
