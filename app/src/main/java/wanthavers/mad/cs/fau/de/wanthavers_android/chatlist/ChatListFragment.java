package wanthavers.mad.cs.fau.de.wanthavers_android.chatlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import static com.google.common.base.Preconditions.checkNotNull;

import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.ChatlistFragBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.desirelist.ScrollChildSwipeRefreshLayout;

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

        mListAdapter = new ChatListAdapter(new ArrayList<Chat>(0),mPresenter, mChatListViewModel);
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
    public void showLoadingChatsError() {

    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showChatDetailsUi(long chatId) {
        //TODO open chatDetailsUI
        //Intent intent = new Intent(getContext(), ChatDetailActivity.class);
        //intent.putExtra(ChatDetailActivity.EXTRA_DESIRE_ID, desireId);
        //startActivity(intent);
    }
}
