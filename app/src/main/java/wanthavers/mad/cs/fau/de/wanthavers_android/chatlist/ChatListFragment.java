package wanthavers.mad.cs.fau.de.wanthavers_android.chatlist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

import de.fau.cs.mad.wanthavers.common.Chat;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.chatdetail.ChatDetailActivity;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.desire.DesireLocalDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.desire.DesireRemoteDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.desire.DesireRepository;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.user.UserLocalDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.user.UserRemoteDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.user.UserRepository;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.ChatlistFragBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.desirelist.ScrollChildSwipeRefreshLayout;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetDesire;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetUser;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.SharedPreferencesHelper;

public class ChatListFragment extends Fragment implements  ChatListContract.View {

    private ChatListContract.Presenter mPresenter;
    private ChatListAdapter mListAdapter;
    private ChatListViewModel mChatListViewModel;

    public ChatListFragment(){
        //Requires empty public constructor
    }

    public static ChatListFragment newInstance(){ return new ChatListFragment();}


    @Override
    public void setPresenter(@NonNull ChatListContract.Presenter presenter) {
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


        ChatlistFragBinding chatListFragBinding = ChatlistFragBinding.inflate(inflater,container,false);

        chatListFragBinding.setChats(mChatListViewModel);

        chatListFragBinding.setPresenter(mPresenter);



        //Set up desire view
        ListView listView = chatListFragBinding.desiresList;
        Context context = getContext().getApplicationContext();
        UserRepository chatRepo = UserRepository.getInstance(UserRemoteDataSource.getInstance(context),
                UserLocalDataSource.getInstance(context));
        GetUser userGetter = new GetUser(chatRepo);
        DesireRepository desireRepo = DesireRepository.getInstance(DesireRemoteDataSource.getInstance(context),
                DesireLocalDataSource.getInstance(context));
        GetDesire desireGetter = new GetDesire(desireRepo);


        SharedPreferencesHelper sharedPreferencesHelper = SharedPreferencesHelper.getInstance(SharedPreferencesHelper.NAME_USER, getContext().getApplicationContext());
        final long loggedInUserId = Long.valueOf(sharedPreferencesHelper.loadString(SharedPreferencesHelper.KEY_USERID, "1"));

        mListAdapter = new ChatListAdapter(new ArrayList<Chat>(0),mPresenter, mChatListViewModel,userGetter, loggedInUserId, desireGetter );
        listView.setAdapter(mListAdapter);


        // Set up progress indicator  TODO decide whether this is needed
        final ScrollChildSwipeRefreshLayout swipeRefreshLayout = chatListFragBinding.refreshLayout;
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );

        // Set the scrolling view in the custom SwipeRefreshLayout
        swipeRefreshLayout.setScrollUpChild(listView);


        setHasOptionsMenu(true);

        View root = chatListFragBinding.getRoot();
        return root;
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showLoadingChatsError() {
        showMessage(getString(R.string.loading_chats_error));
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showChatDetailsUi(Chat chat) {
        //TODO open chatDetailsUI
        Intent intent = new Intent(getContext(), ChatDetailActivity.class);
        intent.putExtra(ChatDetailActivity.EXTRA_CHAT_ID, chat.getObjectId());
        startActivity(intent);
    }

    private void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }


    public void setViewModel(ChatListViewModel viewModel){mChatListViewModel = viewModel;}

    public void showChats(List<Chat> chatList){
        mListAdapter.replaceData(chatList);
        mChatListViewModel.setChatListSize(chatList.size());
    }
}
