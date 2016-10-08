package wanthavers.mad.cs.fau.de.wanthavers_android.chatdetail;

import android.support.annotation.NonNull;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.Chat;
import de.fau.cs.mad.wanthavers.common.Message;
import de.fau.cs.mad.wanthavers.common.User;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.BasePresenter;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.BaseView;


public interface ChatDetailContract {

    interface View extends BaseView<Presenter> {

        void showMessages(List<Message> messageList, boolean loadOldMessages);

        void setLoadingIndicator(final boolean active);

        boolean isActive();

        void openFlagUserPopup(User otherUser);

        void closeFlagUserPopup();

        void showLoadingMessagesError();

        void showSendMessageError();

        void showUpdatedMessageListonSendSuccess(Message message);

        void showMessageEmptyError();

        void showFlaggingUserSuccess();

        void showFlaggingUserError();
    }


    interface Presenter extends BasePresenter {

        void loadMessages(boolean forceUpdate, boolean loadOldMessages);

        void sendMessage(Message message);

        void openFlagUserPopup(User otherUser);

        void closeFlagUserPopup();

        void flagUser(long otherUserId);
    }
}
