package wanthavers.mad.cs.fau.de.wanthavers_android.filtersetting;

import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.FiltersettingFragBinding;

public class FilterSettingActionHandler {

    private FilterSettingContract.Presenter mListener;
    private FiltersettingFragBinding mFilterSettingFragBinding;

    public FilterSettingActionHandler(FilterSettingContract.Presenter listener, FiltersettingFragBinding filtersettingFragBinding) {
        mListener = listener;
        mFilterSettingFragBinding = filtersettingFragBinding;
    }

}
