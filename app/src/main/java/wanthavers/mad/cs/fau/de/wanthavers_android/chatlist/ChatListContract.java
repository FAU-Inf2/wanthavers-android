package wanthavers.mad.cs.fau.de.wanthavers_android.chatlist;

import android.support.annotation.NonNull;

import de.fau.cs.mad.wanthavers.common.Desire;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.BasePresenter;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.BaseView;

public interface ChatListContract {




    interface View extends BaseView<Presenter> {

        //void showChats(List<Chat> chatList);

        void showLoadingChatsError();

        void setLoadingIndicator(final boolean active);

        public void showChatDetailsUi(long chatId);

        //TODO void showNoTasks();
    }



    interface Presenter extends BasePresenter {

        void loadChats(boolean forceUpdate);

        void openChatDetails(@NonNull Chat chat);


    }


}
