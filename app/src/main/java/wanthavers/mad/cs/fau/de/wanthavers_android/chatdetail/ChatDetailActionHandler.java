package wanthavers.mad.cs.fau.de.wanthavers_android.chatdetail;

import de.fau.cs.mad.wanthavers.common.Message;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.ChatdetailFragBinding;

public class ChatDetailActionHandler {



    private ChatDetailContract.Presenter mListener;
    private ChatdetailFragBinding mChatDetailFragBinding;
    private long mChatUserId;
    private ChatDetailFragment mChatDetailFragment;

    public ChatDetailActionHandler(long chatUserId, ChatdetailFragBinding chatDetailFragBind, ChatDetailContract.Presenter listener, ChatDetailFragment chatDetailFragment) {
        mListener = listener;
        mChatDetailFragBinding = chatDetailFragBind;
        mChatUserId = chatUserId;
        mChatDetailFragment = chatDetailFragment;

    }

    public void sendMessage(){

        //check if message body is not empty
        String messageBody = mChatDetailFragBinding.etMessage.getText().toString();
        String messageBodyTestString = messageBody.replaceAll("\\s+","");

        if(messageBodyTestString.length() == 0 ) {
            mChatDetailFragment.showMessageEmptyError();
            return;
        }

        Message message = new Message();
        message.setFrom(mChatUserId);
        message.setBody(messageBody);
        mChatDetailFragBinding.etMessage.setText("");
        mListener.sendMessage(message);
    }


    public void setLoggedInUser(long userId){
        mChatUserId = userId;
    }
}
