package wanthavers.mad.cs.fau.de.wanthavers_android.chatdetail;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;


public class ChatDetailViewModel extends BaseObservable {

    int mChatListSize = 0;

    private final ChatDetailContract.Presenter mPresenter;

    private Context mContext;

    public ChatDetailViewModel(Context context, ChatDetailContract.Presenter presenter) {
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
