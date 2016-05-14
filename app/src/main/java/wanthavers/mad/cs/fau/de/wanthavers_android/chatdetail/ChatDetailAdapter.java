package wanthavers.mad.cs.fau.de.wanthavers_android.chatdetail;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;

import java.util.List;

import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.chatlist.Chat;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.ChatItemBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.MessageItemBinding;

public class ChatDetailAdapter extends BaseAdapter {


    private List<Chat> mChatList;

    private ChatDetailContract.Presenter mUserActionsListener;
    private ChatDetailViewModel mChatDetailViewModel;

    public ChatDetailAdapter(List<Chat> chats, ChatDetailContract.Presenter itemListener, ChatDetailViewModel chatDetailViewModel) {
        setList(chats);
        mUserActionsListener = itemListener;
        mChatDetailViewModel = chatDetailViewModel;
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
        MessageItemBinding binding;

        if (view == null) {
            // Inflate
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

            // Create the binding
            binding = MessageItemBinding.inflate(inflater, viewGroup, false);

        } else {
            binding = DataBindingUtil.getBinding(view);
        }

        // We might be recycling the binding for another task, so update it.
        // Create the action handler for the view
        ChatDetailItemActionHandler itemActionHandler = new ChatDetailItemActionHandler(mUserActionsListener);
        binding.setActionHandler(itemActionHandler);

        binding.executePendingBindings();
        return binding.getRoot();
    }
}
