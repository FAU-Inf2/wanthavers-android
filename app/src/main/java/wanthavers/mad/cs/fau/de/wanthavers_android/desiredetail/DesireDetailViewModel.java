package wanthavers.mad.cs.fau.de.wanthavers_android.desiredetail;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import wanthavers.mad.cs.fau.de.wanthavers_android.BR;

public class DesireDetailViewModel extends BaseObservable {

    int mHaverListSize = 0;

    private final DesireDetailContract.Presenter mPresenter;

    private Context mContext;

    public DesireDetailViewModel(Context context, DesireDetailContract.Presenter presenter) {
        mContext = context;
        mPresenter = presenter;
    }

    @Bindable
    public boolean isNotEmpty() {return mHaverListSize > 0;}

    public void setWanterListSize(int haverListSize) {
        mHaverListSize = haverListSize;
        notifyPropertyChanged(BR.notEmpty);
    }

}
