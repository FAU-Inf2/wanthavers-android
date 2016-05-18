package wanthavers.mad.cs.fau.de.wanthavers_android.chatlist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.databinding.DataBindingUtil;

import de.fau.cs.mad.wanthavers.common.Chat;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.ChatItemBinding;
import java.util.List;

public class ChatListAdapter extends BaseAdapter {


    private List<Chat> mChatList;

    private ChatListContract.Presenter mUserActionsListener;
    private ChatListViewModel mChatListViewModel;

    public ChatListAdapter(List<Chat> chats, ChatListContract.Presenter itemListener, ChatListViewModel chatListViewModel) {
        setList(chats);
        mUserActionsListener = itemListener;
        mChatListViewModel = chatListViewModel;
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

        binding.executePendingBindings();
        return binding.getRoot();
    }
}
