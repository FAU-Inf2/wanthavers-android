package wanthavers.mad.cs.fau.de.wanthavers_android.licenses;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.LicensesFragBinding;

public class LicensesFragment extends Fragment{

    public LicensesFragment(){
        //Requires empty public constructor
    }
    public static LicensesFragment newInstance(){ return new LicensesFragment();}

    @Override
    public void onResume()  {
        super.onResume();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        LicensesFragBinding licensesFragBinding = LicensesFragBinding.inflate(inflater, container, false);

        //android licenses
        final String[] androidLicense = getResources().getStringArray(R.array.android_licenses);
        RecyclerView.LayoutManager androidLayoutManager = new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        licensesFragBinding.androidLicensesList.setLayoutManager(androidLayoutManager);
        LicenseAdapter androidLicenseAdapter = new LicenseAdapter(null, androidLicense, getContext());
        licensesFragBinding.androidLicensesList.setAdapter(androidLicenseAdapter);

        //server licenses
        final String[] serverLicense = getResources().getStringArray(R.array.server_licenses);
        RecyclerView.LayoutManager serverLayoutManager = new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        licensesFragBinding.serverLicensesList.setLayoutManager(serverLayoutManager);
        LicenseAdapter serverLicenseAdapter = new LicenseAdapter(null, serverLicense, getContext());
        licensesFragBinding.serverLicensesList.setAdapter(serverLicenseAdapter);

        //common licenses
        final String[] commonLicense = getResources().getStringArray(R.array.common_licenses);
        RecyclerView.LayoutManager commonLayoutManager = new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        licensesFragBinding.commonLicensesList.setLayoutManager(commonLayoutManager);
        LicenseAdapter commonLicenseAdapter = new LicenseAdapter(null, commonLicense, getContext());
        licensesFragBinding.commonLicensesList.setAdapter(commonLicenseAdapter);

        //other licenses
        final String[] otherLicense = getResources().getStringArray(R.array.other_licenses);
        String[][] dividedLicenses = divideTitleDescription(otherLicense);
        RecyclerView.LayoutManager otherLayoutManager = new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        licensesFragBinding.otherLicensesList.setLayoutManager(otherLayoutManager);
        LicenseAdapter otherLicenseAdapter = new LicenseAdapter(dividedLicenses[0], dividedLicenses[1], getContext());
        licensesFragBinding.otherLicensesList.setAdapter(otherLicenseAdapter);

        return licensesFragBinding.getRoot();
    }

    private String[][] divideTitleDescription(String[] licenses) {
        String[] licensesTitle = new String[licenses.length];
        String[] licensesDescription = new String[licenses.length];
        for (int i = 0; i < licenses.length; i++) {
            String[] tmp = licenses[i].split("#description#");
            licensesTitle[i] = tmp[0];
            licensesDescription[i] = tmp[1];
        }
        String[][] dividedLicenses = new String[2][licenses.length];
        dividedLicenses[0] = licensesTitle;
        dividedLicenses[1] = licensesDescription;
        return dividedLicenses;
    }

}
