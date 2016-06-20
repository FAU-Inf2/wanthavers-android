package wanthavers.mad.cs.fau.de.wanthavers_android.chatdetail;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.Message;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.MessageItemBinding;

public class ChatDetailAdapter extends RecyclerView.Adapter<ChatDetailAdapter.ViewHolder> {

    private long mUserId;
    private List<Message> mMessageList;

    private ChatDetailContract.Presenter mUserActionsListener;

    public ChatDetailAdapter(List<Message> messages, long userId, ChatDetailContract.Presenter itemListener) {
        setList(messages);
        mUserId = userId;
        mUserActionsListener = itemListener;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private MessageItemBinding mMessageItemBinding;

        public ViewHolder(MessageItemBinding chatItemBinding) {

            super(chatItemBinding.getRoot());
            mMessageItemBinding = chatItemBinding;
            mMessageItemBinding.executePendingBindings();
        }

        public MessageItemBinding getMessageItemBinding(){
            return mMessageItemBinding;
        }
    }


    // Create new views (invoked by the layout manager)
    @Override
    public ChatDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        MessageItemBinding messageItemBinding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.message_item, viewGroup, false);
        return new ChatDetailAdapter.ViewHolder(messageItemBinding);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ChatDetailAdapter.ViewHolder holder, int position) {

        Message message = mMessageList.get(position);

        //set new content with databinding
        MessageItemBinding messageItemBinding = holder.getMessageItemBinding();

        // set messages and layout according to user

        final boolean isMe = (message.getFrom() == mUserId) ? true : false;  //TODO change to something useful

        if(isMe){
            
            messageItemBinding.tvBody.setBackground(messageItemBinding.getRoot().getResources().getDrawable(R.drawable.chat_border_me));
            messageItemBinding.tvBody.setTextColor(messageItemBinding.getRoot().getResources().getColor(R.color.colorSecondary));
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)messageItemBinding.tvBody.getLayoutParams();
            params.setMargins(200, 0, 0, 0);
            messageItemBinding.tvBody.setLayoutParams(params);

        }else {
            messageItemBinding.tvBody.setBackground(messageItemBinding.getRoot().getResources().getDrawable(R.drawable.chat_border_other));
            messageItemBinding.tvBody.setTextColor(messageItemBinding.getRoot().getResources().getColor(R.color.colorPrimary));
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)messageItemBinding.tvBody.getLayoutParams();
            params.setMargins(0, 0, 200, 0);
            messageItemBinding.tvBody.setLayoutParams(params);

        }


        messageItemBinding.tvBody.setText(message.getBody());
    }


    public void replaceData(List<Message> messageList) {
        setList(messageList);
    }

    private void setList(List<Message> messageList) {
        mMessageList = messageList;
        notifyDataSetChanged();
    }

    public List<Message> getList(){
        return mMessageList;
    }

    @Override
    public int getItemCount() {
        return mMessageList != null ? mMessageList.size() : 0;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void setLoggedInUser(long userId){
        mUserId = userId;
    }

}
