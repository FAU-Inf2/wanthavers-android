package wanthavers.mad.cs.fau.de.wanthavers_android.licenses;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static com.google.common.base.Preconditions.checkNotNull;

import wanthavers.mad.cs.fau.de.wanthavers_android.R;

public class LicenseAdapter extends RecyclerView.Adapter<LicenseAdapter.ViewHolder>{

    private String[] mLicenseTitleList;
    private String[] mLicenseDescriptionList;
    private Context mContext;

    public LicenseAdapter(String[] licenseTitleList, @NonNull String[] licenseDescriptionList, Context context){
        mLicenseTitleList = licenseTitleList;
        mLicenseDescriptionList = checkNotNull(licenseDescriptionList);
        mContext = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView mDescriptionView;
        public TextView mTitleView;

        public ViewHolder(View view){
            super(view);
            mDescriptionView = (TextView) view.findViewById(R.id.license_description);
            mTitleView = (TextView) view.findViewById(R.id.license_title);
        }

        public TextView getDescriptionView() {
            return mDescriptionView;
        }

        public TextView getTitleView() {
            return mTitleView;
        }

    }

    @Override
    public LicenseAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View view = LayoutInflater.from(mContext).inflate(R.layout.license_item, viewGroup, false);
        return new LicenseAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        if (mLicenseTitleList != null) {
            holder.getTitleView().setVisibility(View.VISIBLE);
            holder.getTitleView().setText(mLicenseTitleList[position]);
        }
        holder.getDescriptionView().setText(mLicenseDescriptionList[position]);
    }

    @Override
    public int getItemCount(){
        return mLicenseDescriptionList.length;
    }

}
