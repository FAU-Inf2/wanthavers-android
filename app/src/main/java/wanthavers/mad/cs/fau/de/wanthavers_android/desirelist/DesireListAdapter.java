package wanthavers.mad.cs.fau.de.wanthavers_android.desirelist;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.Desire;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.DesireItemBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.DesireLogic;

public class DesireListAdapter extends BaseAdapter{

    private List<Desire> mDesireList;

    private DesireListContract.Presenter mUserActionsListener;
    private DesireListViewModel mDesireListViewModel;
    private DesireLogic mDesireLogic;

    public DesireListAdapter(List<Desire> tasks, DesireListContract.Presenter itemListener, DesireListViewModel desireListViewModel, DesireLogic desireLogic) {
        setList(tasks);
        mUserActionsListener = itemListener;
        mDesireListViewModel = desireListViewModel;
        mDesireLogic = desireLogic;
    }

    public void replaceData(List<Desire> desireList) {
        setList(desireList);
    }

    private void setList(List<Desire> desireList) {
        mDesireList = desireList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDesireList != null ? mDesireList.size() : 0;
    }

    @Override
    public Desire getItem(int i) {
        return mDesireList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Desire desire = getItem(i);
        DesireItemBinding binding;

        if (view == null) {
            // Inflate
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

            // Create the binding
            binding = DesireItemBinding.inflate(inflater, viewGroup, false);

        } else {
            binding = DataBindingUtil.getBinding(view);
            RatingBar itemRateBar = (RatingBar) view.findViewById(R.id.ItemRatingBar);
            itemRateBar.setRating(4.5f);  //TODO get rating from Server and insert here
        }

        // We might be recycling the binding for another task, so update it.
        // Create the action handler for the view
        DesireListItemActionHandler itemActionHandler =
                new DesireListItemActionHandler(mUserActionsListener);
        binding.setActionHandler(itemActionHandler);

        binding.setDesire(desire);
        binding.setDesires(mDesireListViewModel);
        binding.setDesireLogic(mDesireLogic);

        binding.executePendingBindings();
        return binding.getRoot();
    }

}

