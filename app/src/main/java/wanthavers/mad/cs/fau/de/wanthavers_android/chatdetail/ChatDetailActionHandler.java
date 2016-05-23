package wanthavers.mad.cs.fau.de.wanthavers_android.chatdetail;

import de.fau.cs.mad.wanthavers.common.Message;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.ChatdetailFragBinding;

public class ChatDetailActionHandler {



    private ChatDetailContract.Presenter mListener;
    private ChatdetailFragBinding mChatDetailFragBinding;
    private long mChatUserId;

    public ChatDetailActionHandler(long chatUserId, ChatdetailFragBinding chatDetailFragment, ChatDetailContract.Presenter listener) {
        mListener = listener;
        mChatDetailFragBinding = chatDetailFragment;
        mChatUserId = chatUserId;

    }

    public void sendMessage(){
        Message message = new Message();
        message.setFrom(mChatUserId);
        message.setBody(mChatDetailFragBinding.etMessage.getText().toString());
        mListener.sendMessage(message);
    }
}
