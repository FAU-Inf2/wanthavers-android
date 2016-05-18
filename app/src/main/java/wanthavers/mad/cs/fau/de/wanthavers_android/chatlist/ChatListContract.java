package wanthavers.mad.cs.fau.de.wanthavers_android.chatlist;

import android.support.annotation.NonNull;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.Chat;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.BasePresenter;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.BaseView;

public interface ChatListContract {




    interface View extends BaseView<Presenter> {

        void showLoadingChatsError();

        boolean isActive();

        void setLoadingIndicator(final boolean active);

        void showChatDetailsUi(Chat chat);

        void showChats(List<Chat> chatList);

    }



    interface Presenter extends BasePresenter {

        void loadChats(boolean forceUpdate);

        void openChatDetails(@NonNull Chat chat);


    }


}
