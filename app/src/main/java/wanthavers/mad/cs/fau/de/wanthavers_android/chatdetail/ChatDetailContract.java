package wanthavers.mad.cs.fau.de.wanthavers_android.chatdetail;

import android.support.annotation.NonNull;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.Chat;
import de.fau.cs.mad.wanthavers.common.Message;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.BasePresenter;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.BaseView;


public interface ChatDetailContract {

    interface View extends BaseView<Presenter> {

        void showMessages(List<Message> messageList);

        void setLoadingIndicator(final boolean active);

        boolean isActive();

        void showLoadingMessagesError();

        void showSendMessageError();

        void showUpdatedMessageListonSendSuccess(Message message);
    }


    interface Presenter extends BasePresenter {

        void loadMessages(boolean forceUpdate);

        void sendMessage(Message message);
    }
}