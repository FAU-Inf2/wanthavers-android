package wanthavers.mad.cs.fau.de.wanthavers_android.chatdetail;

import android.support.annotation.NonNull;

import java.util.List;

import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.BasePresenter;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.BaseView;
import wanthavers.mad.cs.fau.de.wanthavers_android.chatlist.Chat;

public interface ChatDetailContract {

    interface View extends BaseView<Presenter> {

        void showMessages(List<Chat> chatList);

        void showLoadingChatsError();

        void setLoadingIndicator(final boolean active);

    }



    interface Presenter extends BasePresenter {

        void loadMessages(boolean forceUpdate);

    }
}
