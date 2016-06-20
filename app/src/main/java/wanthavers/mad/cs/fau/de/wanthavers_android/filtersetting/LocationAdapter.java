package wanthavers.mad.cs.fau.de.wanthavers_android.filtersetting;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.Location;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.LocationItemBinding;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {

    private List<Location> mLocationList;

    private FilterSettingActionHandler mActionHandler;

    public LocationAdapter(List<Location> locationList, FilterSettingActionHandler actionHandler) {
        setList(locationList);
        mActionHandler = actionHandler;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private LocationItemBinding mLocationItemBinding;

        public ViewHolder(LocationItemBinding locationItemBinding) {
            super(locationItemBinding.getRoot());
            mLocationItemBinding = locationItemBinding;
            mLocationItemBinding.executePendingBindings();
        }

        public LocationItemBinding getLocationItemBinding () {
            return mLocationItemBinding;
        }
    }

    public void replaceData(List<Location> locationList) {
        setList(locationList);
    }

    private void setList(List<Location> locationList) {
        mLocationList = locationList;
        notifyDataSetChanged();
    }

    public List<Location> getList() {
        return mLocationList;
    }


    @Override
    public LocationAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LocationItemBinding locationItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.getContext()), R.layout.location_item, viewGroup, false);
        return new LocationAdapter.ViewHolder(locationItemBinding);
    }

    @Override
    public void onBindViewHolder(LocationAdapter.ViewHolder viewHolder, int position) {
        Location location = mLocationList.get(position);
        LocationItemBinding locationItemBinding = viewHolder.getLocationItemBinding();
        locationItemBinding.setLocation(location);
        locationItemBinding.setActionHandler(mActionHandler);
    }

    @Override
    public int getItemCount() {
        return mLocationList != null ? mLocationList.size() : 0;
    }

}
