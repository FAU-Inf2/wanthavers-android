package wanthavers.mad.cs.fau.de.wanthavers_android.chatlist;

import android.databinding.BaseObservable;
import android.content.Context;
import android.databinding.Bindable;

import de.fau.cs.mad.wanthavers.common.User;

public class ChatListViewModel extends BaseObservable {



    int mChatListSize = 0;

    private final ChatListContract.Presenter mPresenter;

    private Context mContext;

    public ChatListViewModel(Context context, ChatListContract.Presenter presenter) {
        mContext = context;
        mPresenter = presenter;

    }

    @Bindable
    public boolean isNotEmpty() { return mChatListSize > 0; }

    public void setChatListSize(int chatListSize) {
        mChatListSize = chatListSize;
        //notifyPropertyChanged(BR.notEmpty);

    }

}
