package wanthavers.mad.cs.fau.de.wanthavers_android.desirelist;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.Desire;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.DesireItemBinding;

public class DesireListAdapter extends BaseAdapter{

    private List<Desire> mDesireList;

    private DesireListContract.Presenter mUserActionsListener;

    public DesireListAdapter(List<Desire> tasks, DesireListContract.Presenter itemListener) {
        setList(tasks);
        mUserActionsListener = itemListener;
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
        }

        // We might be recycling the binding for another task, so update it.
        // Create the action handler for the view
        DesireListItemActionHandler itemActionHandler =
                new DesireListItemActionHandler(mUserActionsListener);
        binding.setActionHandler(itemActionHandler);
        binding.setDesire(desire);
        binding.executePendingBindings();
        return binding.getRoot();
    }
}

