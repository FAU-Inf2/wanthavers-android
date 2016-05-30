package wanthavers.mad.cs.fau.de.wanthavers_android.desiredetail;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.Haver;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.HaverItemBinding;

/**
 * Created by Oliver Lutz on 24.05.2016.
 */
public class DesireDetailAdapter extends RecyclerView.Adapter<DesireDetailAdapter.ViewHolder> {

    //private long mUserId;
    private List<Haver> mHaverList;

    private DesireDetailContract.Presenter mUserActionsListener;

    public DesireDetailAdapter(List<Haver> havers, DesireDetailContract.Presenter itemListener) {
        setList(havers);
        mUserActionsListener = itemListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private HaverItemBinding mhaverItemBinding;

        public ViewHolder(HaverItemBinding haverItemBinding) {
            super(haverItemBinding.getRoot());
            mhaverItemBinding = haverItemBinding;
            mhaverItemBinding.executePendingBindings();
        }

        public HaverItemBinding getHaverItemBinding() {
            return mhaverItemBinding;
        }

    }

    @Override
    public DesireDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        HaverItemBinding haverItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.getContext()), R.layout.haver_item, viewGroup, false);
        return new DesireDetailAdapter.ViewHolder(haverItemBinding);
    }

    @Override
    public void onBindViewHolder(DesireDetailAdapter.ViewHolder viewHolder, int position) {
        Haver haver = mHaverList.get(position);

        HaverItemBinding haverItemBinding = viewHolder.getHaverItemBinding();

        haverItemBinding.setHaver(haver);
    }

    public void replaceData(List<Haver> haverList) {
        setList(haverList);
    }

    private void setList(List<Haver> haverList) {
        mHaverList = haverList;
        notifyDataSetChanged();
    }

    public List<Haver> getList() {
        return mHaverList;
    }

    @Override
    public int getItemCount() {
        return mHaverList != null ? mHaverList.size() : 0;
    }

    /*public Haver getItem(int position) {
        return mHaverList.get(position);
    }*/

    @Override
    public long getItemId(int position) {
        return position;
    }

    /*public View getView(int position, View view, ViewGroup parent) {
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
    }*/
}
