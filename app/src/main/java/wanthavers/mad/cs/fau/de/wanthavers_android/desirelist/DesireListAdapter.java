package wanthavers.mad.cs.fau.de.wanthavers_android.desirelist;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.Media;
import wanthavers.mad.cs.fau.de.wanthavers_android.BR;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.DesireItemBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.DesireLogic;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.RoundedTransformation;

public class DesireListAdapter extends RecyclerView.Adapter<DesireListAdapter.ViewHolder>{

    private List<DesireItemViewModel> mDesireList;

    private DesireListContract.Presenter mUserActionsListener;
    private DesireListViewModel mDesireListViewModel;
    private DesireLogic mDesireLogic;

    public DesireListAdapter(List<DesireItemViewModel> desires, DesireListContract.Presenter itemListener, DesireListViewModel desireListViewModel, DesireLogic desireLogic) {
        setList(desires);
        mUserActionsListener = itemListener;
        mDesireListViewModel = desireListViewModel;
        mDesireLogic = desireLogic;
    }


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // initialise Databinding
        private DesireItemBinding mDesireItemBinding;


        public ViewHolder(DesireItemBinding desireBinding) {

            super(desireBinding.getRoot());
            mDesireItemBinding = desireBinding;
            mDesireItemBinding.executePendingBindings();
        }

        public DesireItemBinding getDesireItemBinding(){
            return mDesireItemBinding;
        }
    }


    // Create new views (invoked by the layout manager)
    @Override
    public DesireListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup,
                                                   int i) {

        DesireItemBinding desireItemBinding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.desire_item, viewGroup, false);

        return new DesireListAdapter.ViewHolder(desireItemBinding);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(DesireListAdapter.ViewHolder holder, int position) {

        DesireItemViewModel desireItemViewModel = mDesireList.get(position);


        //set new content with databinding
        DesireItemBinding desireItemBinding = holder.getDesireItemBinding();
        desireItemBinding.setVariable(BR.desire, desireItemViewModel);
        DesireListItemActionHandler itemActionHandler =
                new DesireListItemActionHandler(mUserActionsListener);
        desireItemBinding.setActionHandler(itemActionHandler);
        desireItemBinding.setDesireLogic(mDesireLogic);
        Media m = desireItemViewModel.getDesire().getImage();


        if (m != null) {
            final ImageView profileView = desireItemBinding.desireListImage;
            Picasso.with(desireItemBinding.getRoot().getContext()).load(m.getLowRes()).transform(new RoundedTransformation(200,0)).into(profileView);
        } else{
            //else case is neccessary as the image is otherwise overwritten on scroll
            final ImageView profileView = desireItemBinding.desireListImage;
            profileView.setImageResource(R.drawable.no_pic);
        }


    }

    public void replaceData(List<DesireItemViewModel> desireList) {
        setList(desireList);
    }

    private void setList(List<DesireItemViewModel> desireList) {
        mDesireList = desireList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount(){
        return mDesireList != null ? mDesireList.size() : 0;
    }


    @Override
    public long getItemId(int i) {
        return i;
    }

}

