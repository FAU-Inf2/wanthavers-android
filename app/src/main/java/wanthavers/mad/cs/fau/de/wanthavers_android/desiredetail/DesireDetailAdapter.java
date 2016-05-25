package wanthavers.mad.cs.fau.de.wanthavers_android.desiredetail;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.Haver;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.HaverItemBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.DesireLogic;

/**
 * Created by Oliver Lutz on 24.05.2016.
 */
public class DesireDetailAdapter extends BaseAdapter{

    private List<Haver> mHaverList;
    //private DesireDetailViewModel mDesireDetailViewModel;
    //private DesireLogic mDesireLogic;

    public DesireDetailAdapter(List<Haver> havers/*, DesireDetailViewModel desireDetailViewModel, DesireLogic desireLogic*/) {
        setList(havers);
        //mDesireDetailViewModel = desireDetailViewModel;
        //mDesireLogic = desireLogic;
    }

    public void replaceData(List<Haver> haverList) {
        setList(haverList);
    }

    private void setList(List<Haver> haverList) {
        mHaverList = haverList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mHaverList != null ? mHaverList.size() : 0;
    }

    @Override
    public Haver getItem(int position) {
        return mHaverList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Haver haver = getItem(position);
        HaverItemBinding binding;

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());

            binding = HaverItemBinding.inflate(inflater, parent, false);
        } else {
            binding = DataBindingUtil.getBinding(view);
            RatingBar itemRateBar = (RatingBar) view.findViewById(R.id.HaverRatingBar);
            itemRateBar.setRating(4.5f); //TODO rating from server
        }

        binding.setHaver(haver);
        binding.executePendingBindings();

        return binding.getRoot();
    }
}
