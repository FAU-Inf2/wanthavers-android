package wanthavers.mad.cs.fau.de.wanthavers_android.chatlist;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.databinding.DataBindingUtil;

import de.fau.cs.mad.wanthavers.common.Chat;
import de.fau.cs.mad.wanthavers.common.User;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCaseHandler;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.user.UserRepository;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.ChatItemBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetUser;

import java.util.List;

public class ChatListAdapter extends BaseAdapter {


    private List<Chat> mChatList;
    private final GetUser mGetUser;
    private User mUser;
    private long mLoggedInUserId;

    private ChatListContract.Presenter mUserActionsListener;
    private ChatListViewModel mChatListViewModel;

    public ChatListAdapter(List<Chat> chats, ChatListContract.Presenter itemListener,
                           ChatListViewModel chatListViewModel,
                           @NonNull GetUser getUser, long loggedInUserId) {
        setList(chats);
        mUserActionsListener = itemListener;
        mChatListViewModel = chatListViewModel;
        mGetUser = getUser;
        mLoggedInUserId = loggedInUserId;
    }

    public void replaceData(List<Chat> chatList) {
        setList(chatList);
    }

    private void setList(List<Chat> chatList) {
        mChatList = chatList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mChatList != null ? mChatList.size() : 0;
    }

    @Override
    public Chat getItem(int i) {
        return mChatList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Chat chat = getItem(i);
        ChatItemBinding binding;

        if (view == null) {
            // Inflate
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

            // Create the binding
            binding = ChatItemBinding.inflate(inflater, viewGroup, false);

        } else {
            binding = DataBindingUtil.getBinding(view);
        }

        // We might be recycling the binding for another task, so update it.
        // Create the action handler for the view
        ChatListItemActionHandler itemActionHandler = new ChatListItemActionHandler(mUserActionsListener);
        binding.setActionHandler(itemActionHandler);

        binding.setChats(mChatListViewModel);
        binding.setChat(chat);

        // get User and bind to view

        long otherUserId = getOtherUserId(chat.getUser1(),chat.getUser2());
        executeUserUseCase(otherUserId);

        binding.setUser(mUser);
        binding.executePendingBindings();
        return binding.getRoot();
    }


    private long getOtherUserId(long user1Id, long user2Id){

        if(user1Id != mLoggedInUserId){
            return user2Id;
        }

        return user1Id;
    }


    private void executeUserUseCase(long userId){

        UseCaseHandler useCaseHandler = UseCaseHandler.getInstance();

        GetUser.RequestValues requestValue = new GetUser.RequestValues(userId);

        useCaseHandler.execute(mGetUser,requestValue,
                new UseCase.UseCaseCallback<GetUser.ResponseValue>() {
                    @Override
                    public void onSuccess(GetUser.ResponseValue response) {
                        mUser = response.getUser();

                    }

                    @Override
                    public void onError() {
                        /* TODO think about something useful to do on error
                        // The view may not be able to handle UI updates anymore
                        if (!mChatListView.isActive()) {
                            return;
                        }
                        mChatListView.showLoadingChatsError();
                        */
                        mUser = new User("No User", "noUserEmail");
                    }

                });
    }
}
