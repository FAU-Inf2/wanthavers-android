package wanthavers.mad.cs.fau.de.wanthavers_android.chatdetail;

import de.fau.cs.mad.wanthavers.common.Chat;
import de.fau.cs.mad.wanthavers.common.Message;
import wanthavers.mad.cs.fau.de.wanthavers_android.chatlist.ChatListContract;

public class ChatDetailItemActionHandler {



    private ChatDetailContract.Presenter mListener;

    public ChatDetailItemActionHandler(ChatDetailContract.Presenter listener) {
        mListener = listener;
    }


    /**
     * Called by the Data Binding library when the row is clicked.
     */
    public void chatClicked(Chat chat) {/*do nothing as as not applicable here */}

}
