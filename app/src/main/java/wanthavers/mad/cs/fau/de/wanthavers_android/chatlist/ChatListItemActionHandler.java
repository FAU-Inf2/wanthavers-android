package wanthavers.mad.cs.fau.de.wanthavers_android.chatlist;

import de.fau.cs.mad.wanthavers.common.Chat;

public class ChatListItemActionHandler {



    private ChatListContract.Presenter mListener;

    public ChatListItemActionHandler(ChatListContract.Presenter listener) {
        mListener = listener;
    }


    /**
     * Called by the Data Binding library when the row is clicked.
     */
    public void chatClicked(Chat chat) {
        mListener.openChatDetails(chat);
    }
}
