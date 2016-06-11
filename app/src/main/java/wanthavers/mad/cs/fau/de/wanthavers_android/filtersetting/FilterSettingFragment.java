package wanthavers.mad.cs.fau.de.wanthavers_android.filtersetting;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.FiltersettingFragBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.desirelist.DesireListActivity;

import static com.google.common.base.Preconditions.checkNotNull;

public class FilterSettingFragment extends Fragment implements FilterSettingContract.View {
    private FilterSettingContract.Presenter mPresenter;
    private FiltersettingFragBinding mFilterSettingFragBinding;

    public FilterSettingFragment() {
        //Requires empty public constructor
    }

    public static FilterSettingFragment newInstance() {
        return new FilterSettingFragment();
    }

    @Override
    public void setPresenter(@NonNull FilterSettingContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.filtersetting_frag, container, false);
        //mFilterSettingFragBinding = FiltersettingFragBinding.bind(view);
        //mFilterSettingFragBinding = FiltersettingFragBinding.inflate(inflater, container, false);
        //return mFilterSettingFragBinding.getRoot();
        setHasOptionsMenu(true);

        mFilterSettingFragBinding  = FiltersettingFragBinding.inflate(inflater,container,false);

        mFilterSettingFragBinding.setPresenter(mPresenter);
        return mFilterSettingFragBinding.getRoot();
    }


    @Override
    public void showDesireList(){
        Intent intent = new Intent(getContext(), DesireListActivity.class);
        startActivity(intent);
    }
}
