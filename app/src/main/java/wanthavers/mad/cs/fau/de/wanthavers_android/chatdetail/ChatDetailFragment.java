package wanthavers.mad.cs.fau.de.wanthavers_android.chatdetail;

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


        ChatdetailFragBinding chatdetailFragBinding = ChatdetailFragBinding.inflate(inflater,container,false);

        chatdetailFragBinding.setChats(mChatDetailViewModel);

        chatdetailFragBinding.setPresenter(mPresenter);



        //Set up desire view
        ListView listView = chatdetailFragBinding.desiresList;

        mListAdapter = new ChatDetailAdapter(new ArrayList<Chat>(0),mPresenter, mChatDetailViewModel);
        listView.setAdapter(mListAdapter);


        // Set up progress indicator  TODO decide whether this is needed
        final ScrollChildSwipeRefreshLayout swipeRefreshLayout = chatdetailFragBinding.refreshLayout;
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );

        // Set the scrolling view in the custom SwipeRefreshLayout
        swipeRefreshLayout.setScrollUpChild(listView);


        setHasOptionsMenu(true);

        View root = chatdetailFragBinding.getRoot();
        return root;
    }

    @Override
    public void showMessages(List<Chat> chatList) {

    }

    @Override
    public void showLoadingChatsError() {

    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

}
