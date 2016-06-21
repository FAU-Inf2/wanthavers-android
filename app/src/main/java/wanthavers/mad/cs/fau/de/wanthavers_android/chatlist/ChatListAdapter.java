package wanthavers.mad.cs.fau.de.wanthavers_android.chatlist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.databinding.DataBindingUtil;

import com.squareup.picasso.Picasso;

import de.fau.cs.mad.wanthavers.common.Media;
import wanthavers.mad.cs.fau.de.wanthavers_android.BR;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCaseHandler;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.ChatItemBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.ChatLogic;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.RoundedTransformation;

import java.util.List;

public class ChatListAdapter  extends RecyclerView.Adapter<ChatListAdapter.ViewHolder>{


    private List<ChatItemViewModel> mChatList;

    private ChatListContract.Presenter mUserActionsListener;
    private ChatListViewModel mChatListViewModel;
    private UseCaseHandler mUseCaseHandler;

    public ChatListAdapter(List<ChatItemViewModel> chats, ChatListContract.Presenter itemListener,
                           ChatListViewModel chatListViewModel) {
        setList(chats);
        mUserActionsListener = itemListener;
        mChatListViewModel = chatListViewModel;
        mUseCaseHandler = UseCaseHandler.getInstance();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ChatItemBinding mChatItemBinding;

        public ViewHolder(ChatItemBinding chatItemBinding) {

            super(chatItemBinding.getRoot());
            mChatItemBinding = chatItemBinding;
            mChatItemBinding.executePendingBindings();
        }

        public ChatItemBinding getChatItemBinding(){
            return mChatItemBinding;
        }
    }


    // Create new views (invoked by the layout manager)
    @Override
    public ChatListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        ChatItemBinding chatItemBinding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.chat_item, viewGroup, false);
        return new ChatListAdapter.ViewHolder(chatItemBinding);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ChatListAdapter.ViewHolder holder, int position) {

        ChatItemViewModel chatItemViewModel = mChatList.get(position);

        //set new content with databinding
        ChatItemBinding chatItemBinding = holder.getChatItemBinding();
        chatItemBinding.setChatLogic(new ChatLogic());
        chatItemBinding.setVariable(BR.chat, chatItemViewModel);
        ChatListItemActionHandler itemActionHandler = new ChatListItemActionHandler(mUserActionsListener);
        chatItemBinding.setActionHandler(itemActionHandler);
        Media m = null;
        if(chatItemViewModel.getOtherUser() != null) {
            m = chatItemViewModel.getOtherUser().getImage();
        }

        if (m != null) {
            final ImageView profileView = chatItemBinding.ivProfileOther;
            Picasso.with(chatItemBinding.getRoot().getContext()).load(m.getLowRes()).transform(new RoundedTransformation(200,0)).into(profileView);
        } else{
            final ImageView profileView = chatItemBinding.ivProfileOther;
            profileView.setImageResource(R.drawable.no_pic);
        }

    }

    public void replaceData(List<ChatItemViewModel> chatList) {
        setList(chatList);
    }

    private void setList(List<ChatItemViewModel> chatList) {
        mChatList = chatList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount(){
        return mChatList != null ? mChatList.size() : 0;
    }


    @Override
    public long getItemId(int i) {
        return i;
    }

}
