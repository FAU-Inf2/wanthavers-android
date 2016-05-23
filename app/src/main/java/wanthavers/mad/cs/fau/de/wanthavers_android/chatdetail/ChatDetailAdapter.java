package wanthavers.mad.cs.fau.de.wanthavers_android.chatdetail;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.List;

import de.fau.cs.mad.wanthavers.common.Message;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.MessageItemBinding;

public class ChatDetailAdapter extends BaseAdapter {

    private long mUserId;
    private List<Message> mMessageList;

    private ChatDetailContract.Presenter mUserActionsListener;
    private ChatDetailViewModel mChatDetailViewModel;

    public ChatDetailAdapter(List<Message> messages, long userId, ChatDetailContract.Presenter itemListener, ChatDetailViewModel chatDetailViewModel) {
        setList(messages);
        mUserId = userId;
        mUserActionsListener = itemListener;
        mChatDetailViewModel = chatDetailViewModel;
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
    public int getCount() {
        return mMessageList != null ? mMessageList.size() : 0;
    }

    @Override
    public Message getItem(int i) {
        return mMessageList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        Message message = getItem(position);
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


        binding.executePendingBindings();

        //setup rest of ParserChat
        final boolean isMe = (message.getFrom() == mUserId) ? true : false;  //TODO change to something useful

        if(isMe){
            //binding.ivProfileMe.setVisibility(View.VISIBLE);
            //binding.ivProfileOther.setVisibility(View.GONE);

            //binding.tvBody.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
            binding.tvBody.setBackground(viewGroup.getResources().getDrawable(R.drawable.chat_border_me));
            binding.tvBody.setTextColor(viewGroup.getResources().getColor(R.color.colorMainText));
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)binding.tvBody.getLayoutParams();
            params.setMargins(200, 0, 0, 0);
            binding.tvBody.setLayoutParams(params);

        }else {
            //binding.ivProfileMe.setVisibility(View.GONE);
            //binding.ivProfileOther.setVisibility(View.VISIBLE);
            //binding.tvBody.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            binding.tvBody.setBackground(viewGroup.getResources().getDrawable(R.drawable.chat_border_other));
            binding.tvBody.setTextColor(viewGroup.getResources().getColor(R.color.colorPrimary));
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)binding.tvBody.getLayoutParams();
            params.setMargins(0, 0, 200, 0);
            binding.tvBody.setLayoutParams(params);

        }

       // final ImageView profileView = isMe ? binding.ivProfileMe :  binding.ivProfileOther;
        //Picasso.with(viewGroup.getContext()).load(getProfileUrl(message.getUserId())).into(profileView);
        binding.tvBody.setText(message.getBody());

        //TODO set rest of bindings if needed


        return binding.getRoot();
    }


    // Create a gravatar image based on the hash value obtained from userId
    private static String getProfileUrl(final String userId) {
        String hex = "";
        try {
            final MessageDigest digest = MessageDigest.getInstance("MD5");
            final byte[] hash = digest.digest(userId.getBytes());
            final BigInteger bigInt = new BigInteger(hash);
            hex = bigInt.abs().toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "http://www.gravatar.com/avatar/" + hex + "?d=identicon";
    }
}
