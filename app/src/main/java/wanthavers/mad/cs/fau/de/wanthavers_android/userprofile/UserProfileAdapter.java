package wanthavers.mad.cs.fau.de.wanthavers_android.userprofile;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.common.Media;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.DesireHistoryItemBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.CircularImageView;

public class UserProfileAdapter extends RecyclerView.Adapter<UserProfileAdapter.ViewHolder> {

    private List<Desire> mDesireList;

    public UserProfileAdapter(List<Desire> desireList) {
        setList(desireList);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private DesireHistoryItemBinding mDesireHistoryItemBinding;

        public ViewHolder(DesireHistoryItemBinding desireHistoryItemBinding) {
            super(desireHistoryItemBinding.getRoot());
            mDesireHistoryItemBinding = desireHistoryItemBinding;
            mDesireHistoryItemBinding.executePendingBindings();
        }

        public DesireHistoryItemBinding getDesireHistoryItemBinding() {
            return mDesireHistoryItemBinding;
        }
    }

    @Override
    public UserProfileAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        DesireHistoryItemBinding desireHistoryItemBinding = DataBindingUtil
                .inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.desire_history_item, viewGroup, false);
        return new UserProfileAdapter.ViewHolder(desireHistoryItemBinding);
    }

    @Override
    public void onBindViewHolder(UserProfileAdapter.ViewHolder viewHolder, int position) {
        Desire desire = mDesireList.get(position);

        DesireHistoryItemBinding desireHistoryItemBinding = viewHolder.getDesireHistoryItemBinding();

        desireHistoryItemBinding.setDesire(desire);

        Media media = desireHistoryItemBinding.getDesire().getImage();

        if (media != null) {
            final CircularImageView profileView = desireHistoryItemBinding.desireHistoryImage;
            Picasso.with(desireHistoryItemBinding.getRoot().getContext()).load(media.getLowRes()).into(profileView);
            //Picasso.with(desireHistoryItemBinding.getRoot().getContext()).load(media.getLowRes()).transform(new RoundedTransformation(1000,0)).into(profileView);
        } else{
            //else case is neccessary as the image is otherwise overwritten on scroll
            final ImageView profileView = desireHistoryItemBinding.desireHistoryImage;
            profileView.setImageResource(R.drawable.no_pic);
        }

    }

    public void replaceData(List<Desire> desireList) {
        setList(desireList);
    }

    private void setList(List<Desire> desireList) {
        mDesireList = desireList;
        notifyDataSetChanged();
    }

    public List<Desire> getList() {
        return mDesireList;
    }

    @Override
    public int getItemCount() {
        return mDesireList != null ? mDesireList.size() : 0;
    }

    /*public Desire getItem(int position) {
        return mDesireList.get(position);
    }*/

    @Override
    public long getItemId(int position) {
        return position;
    }

}
