package wanthavers.mad.cs.fau.de.wanthavers_android.desirelist;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.common.Rating;
import wanthavers.mad.cs.fau.de.wanthavers_android.BR;

public class DesireItemViewModel extends BaseObservable {

    @Bindable
    private Desire mDesire;

    public DesireItemViewModel(Desire desire){
        mDesire = desire;
    }

    public Desire getDesire(){
        return mDesire;
    }

    public void setDesire(Desire desire){
        mDesire = desire;
        notifyPropertyChanged(BR.desire);
    }

}
