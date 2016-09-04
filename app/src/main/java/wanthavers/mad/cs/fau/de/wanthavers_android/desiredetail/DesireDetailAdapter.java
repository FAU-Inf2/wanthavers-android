package wanthavers.mad.cs.fau.de.wanthavers_android.desiredetail;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.common.Haver;
import de.fau.cs.mad.wanthavers.common.Media;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.HaverItemBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.DesireLogic;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.CircularImageView;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.RoundedTransformation;

public class DesireDetailAdapter extends RecyclerView.Adapter<DesireDetailAdapter.ViewHolder> {

    private List<Haver> mHaverList;

    private DesireDetailContract.Presenter mUserActionsListener;
    private DesireDetailActionHandler mDesireDetailActionHandler;
    private DesireLogic mDesireLogic;
    private Desire mDesire;
    private HaverItemBinding mHaverItemBinding;

    public DesireDetailAdapter(List<Haver> havers, DesireDetailContract.Presenter itemListener, DesireDetailActionHandler desireDetailActionHandler,
                               DesireLogic desireLogic) {
        setList(havers);
        mUserActionsListener = itemListener;
        mDesireDetailActionHandler = desireDetailActionHandler;
        mDesireLogic = desireLogic;
    }

    public void setDesire(Desire desire) {
        mDesire = desire;
        //mHaverItemBinding.setDesire(mDesire);
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

        mHaverItemBinding = viewHolder.getHaverItemBinding();

        mHaverItemBinding.setHaver(haver);

        mHaverItemBinding.setActionHandler(mDesireDetailActionHandler);
        mHaverItemBinding.setDesirelogic(mDesireLogic);
        mHaverItemBinding.setDesire(mDesire);

        Media m = mHaverItemBinding.getHaver().getUser().getImage();


        if (m != null) {
            final CircularImageView profileView = mHaverItemBinding.imageHaver;
            Picasso.with(mHaverItemBinding.getRoot().getContext()).load(m.getLowRes()).into(profileView);
            //Picasso.with(mHaverItemBinding.getRoot().getContext()).load(m.getLowRes()).transform(new RoundedTransformation(1000,0)).into(profileView);
        } else{
            //else case is neccessary as the image is otherwise overwritten on scroll
            final ImageView profileView = mHaverItemBinding.imageHaver;
            profileView.setImageResource(R.drawable.no_pic);
        }
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
}
