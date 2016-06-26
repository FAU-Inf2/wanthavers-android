package wanthavers.mad.cs.fau.de.wanthavers_android.desiredetail;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.fau.cs.mad.wanthavers.common.Chat;
import de.fau.cs.mad.wanthavers.common.DesireFlag;
import de.fau.cs.mad.wanthavers.common.DesireStatus;
import de.fau.cs.mad.wanthavers.common.FlagReason;
import de.fau.cs.mad.wanthavers.common.Haver;
import de.fau.cs.mad.wanthavers.common.Location;
import de.fau.cs.mad.wanthavers.common.Media;
import de.fau.cs.mad.wanthavers.common.User;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import de.fau.cs.mad.wanthavers.common.Desire;
import wanthavers.mad.cs.fau.de.wanthavers_android.chatdetail.ChatDetailActivity;
import wanthavers.mad.cs.fau.de.wanthavers_android.chatlist.ChatListActivity;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.DesiredetailDeleteDesirePopupBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.DesiredetailFragBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.DesiredetailReportPopupBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.DesireLogic;
import wanthavers.mad.cs.fau.de.wanthavers_android.maps.MapActivity;
import wanthavers.mad.cs.fau.de.wanthavers_android.rating.RatingActivity;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.RoundedTransformation;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.SharedPreferencesHelper;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * interface
 * to handle interaction events.
 * Use the {@link DesireDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DesireDetailFragment extends Fragment implements DesireDetailContract.View {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String DESIRE_ID = "DESIRE_ID";
    private DesiredetailFragBinding mDesireDetailFragBinding;
    private DesireDetailContract.Presenter mPresenter;
    private DesireDetailAdapter mListAdapter;
    private DesireLogic mDesireLogic;
    private RecyclerView mRecyclerView;
    private DesireDetailViewModel mDesireDetailViewModel;
    private DesireDetailActionHandler mDesireDetailActionHandler;
    private Dialog mReportDialog, mDeleteDesireDialog;
    private DesiredetailReportPopupBinding mDesiredetailReportPopupBinding;
    private ProgressDialog mLoadingDialog;
    private Haver mHaver;
    private Menu mOptionsMenu;

    public DesireDetailFragment() {
        //Requires empty public constructor
    }

    public static DesireDetailFragment newInstance(long desireId){
        Bundle arguments = new Bundle();
        arguments.putLong(DESIRE_ID, desireId);
        DesireDetailFragment fragment = new DesireDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void setPresenter(@NonNull DesireDetailContract.Presenter presenter){
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mDesireDetailFragBinding = DesiredetailFragBinding.inflate(inflater, container, false);

        mDesireDetailFragBinding.setHavers(mDesireDetailViewModel);

        //Set up havers view
        mRecyclerView = mDesireDetailFragBinding.haverList;

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mRecyclerView.setLayoutManager(layoutManager);

        setHasOptionsMenu(true);

        //set up action handler
        mDesireDetailActionHandler = new DesireDetailActionHandler(mDesireDetailFragBinding, mPresenter);
        mDesireDetailFragBinding.setActionHandler(mDesireDetailActionHandler);

        mListAdapter = new DesireDetailAdapter(new ArrayList<Haver>(0), mPresenter, mDesireDetailActionHandler);
        mRecyclerView.setAdapter(mListAdapter);

        return mDesireDetailFragBinding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.desire_detail_menu, menu);

        mOptionsMenu = menu;
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu){

        menu.clear();

        SharedPreferencesHelper sharedPreferencesHelper = SharedPreferencesHelper.getInstance(SharedPreferencesHelper.NAME_USER, getContext().getApplicationContext());
        long loggedInUser = sharedPreferencesHelper.loadLong(SharedPreferencesHelper.KEY_USERID, 6L);


        if(mDesireDetailFragBinding.getDesire() != null && mDesireDetailFragBinding.getDesire().getCreator().getId() == loggedInUser ){   //add haver here as well
            getActivity().getMenuInflater().inflate(R.menu.desire_detail_menu_creator, menu);
        } else if(mHaver != null && mHaver.getUser().getId() == loggedInUser) { //include haver here
            getActivity().getMenuInflater().inflate(R.menu.desire_detail_menu_haver, menu);
        } else {
            getActivity().getMenuInflater().inflate(R.menu.desire_detail_menu, menu);
        }

        super.onPrepareOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_report_desire:
                mPresenter.openReportPopup();
                break;
            case R.id.menu_delete_desire:
                mPresenter.openDeletionDialog();
                break;
            case R.id.menu_accept_desire:
                mPresenter.setHaver();
                item.setVisible(false);
                break;
            case R.id.menu_finish_desire:
                break;
                //TODO @Oliver Lutz - load finish desire here
        }
        return true;
    }

    public void setViewModel(DesireDetailViewModel viewModel) {mDesireDetailViewModel = viewModel;}

    public void showDesire(Desire desire, Haver haver) {

        if(mHaver == null){
            mHaver = haver;
            mDesireDetailFragBinding.setHaver(haver);
        }

        mDesireDetailFragBinding.setDesire(desire);
        mDesireDetailFragBinding.setDesirelogic(mDesireLogic);

        onPrepareOptionsMenu(mOptionsMenu);

        //show desire image
        Media mediaDesire = desire.getImage();
        if (mediaDesire != null) {
            final ImageView profileView = mDesireDetailFragBinding.desireDetailItemImage;
            Picasso.with(mDesireDetailFragBinding.getRoot().getContext()).load(mediaDesire.getLowRes()).transform(new RoundedTransformation(200,0)).into(profileView);
        } else{
            //else case is neccessary as the image is otherwise overwritten on scroll
            final ImageView profileView = mDesireDetailFragBinding.desireDetailItemImage;
            profileView.setImageResource(R.drawable.no_pic);
        }

        //show wanter image
        User creator = desire.getCreator();

        if (!mDesireLogic.isDesireCreator(creator.getId())) {
            Media mediaWanter = creator.getImage();
            if (mediaWanter != null) {
                final ImageView profileView = mDesireDetailFragBinding.imageWanter;
                Picasso.with(mDesireDetailFragBinding.getRoot().getContext()).load(mediaWanter.getLowRes()).transform(new RoundedTransformation(200,0)).into(profileView);
            } else{
                //else case is neccessary as the image is otherwise overwritten on scroll
                final ImageView profileView = mDesireDetailFragBinding.imageWanter;
                profileView.setImageResource(R.drawable.no_pic);
            }
            mDesireDetailFragBinding.desireHaverStatus.setVisibility(View.VISIBLE);
        }

        //Show havers
        if (desire.getStatus() == DesireStatus.STATUS_OPEN) {
            mPresenter.loadHavers(false);
        } else if (desire.getStatus() == DesireStatus.STATUS_IN_PROGRESS) {
            mPresenter.getAcceptedHaver();
            //haver cannot accept
            //mDesireDetailFragBinding.buttonAcceptDesire.setVisibility(View.GONE);
            mDesireDetailFragBinding.buttonCloseTransaction.setVisibility(View.VISIBLE);
        } else if (desire.getStatus() == DesireStatus.STATUS_DONE) {
            mPresenter.getAcceptedHaver();
            //haver cannot accept
            //mDesireDetailFragBinding.buttonAcceptDesire.setVisibility(View.GONE);
        }

    }

    public void showRatingButton(Desire desire) {
        /*
        if (!desire.getCreatorHasRated() && mDesireLogic.isDesireCreator(desire.getCreator().getId())) {
            mDesireDetailFragBinding.buttonRating.setVisibility(View.VISIBLE);
        } else if (!desire.getHaverHasRated() && (mHaver != null && mHaver.getUser().getId() == mDesireLogic.getLoggedInUserId())) {
            mDesireDetailFragBinding.buttonRating.setVisibility(View.VISIBLE);
        }
        */
    }

    public void showHavers(List<Haver> havers) {
        mListAdapter.replaceData(havers);
        mDesireDetailViewModel.setWanterListSize(havers.size());

        endLoadingProgress();
    }

    public void showAcceptedHaver(Haver haver) {

        if (mDesireLogic.getLoggedInUserId() == haver.getUser().getId()
                && mDesireDetailFragBinding.desireHaverStatus.getVisibility() == View.VISIBLE) {
            mDesireDetailFragBinding.desireHaverStatus.setText(getString(R.string.haver_status_accepted));
        } else if (mDesireLogic.getLoggedInUserId() != haver.getUser().getId()
                && mDesireDetailFragBinding.desireHaverStatus.getVisibility() == View.VISIBLE) {
            mDesireDetailFragBinding.desireHaverStatus.setText(getString(R.string.haver_status_rejected));
        }

        mDesireDetailFragBinding.haverList.setVisibility(View.GONE);
        mDesireDetailFragBinding.noHavers.setVisibility(View.GONE);
        mDesireDetailFragBinding.acceptedHaverBar.setVisibility(View.VISIBLE);

        Media mediaHaver = haver.getUser().getImage();
        if (mediaHaver != null) {
            final ImageView profileView = mDesireDetailFragBinding.imageAcceptedHaver;
            Picasso.with(mDesireDetailFragBinding.getRoot().getContext()).load(mediaHaver.getLowRes()).transform(new RoundedTransformation(200,0)).into(profileView);
        } else{
            //else case is neccessary as the image is otherwise overwritten on scroll
            final ImageView profileView = mDesireDetailFragBinding.imageAcceptedHaver;
            profileView.setImageResource(R.drawable.no_pic);
        }

        showRatingButton(mDesireDetailFragBinding.getDesire());

        endLoadingProgress();

    }

    private void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showLoadingHaversError() {
        showMessage(getString(R.string.loading_havers_error));
    }

    @Override
    public void showLoadingDesireError() {
        showMessage(getString(R.string.loading_desire_error));
    }

    @Override
    public void showSetHaverError() {
        showMessage(getString(R.string.setting_haver_error));
    }

    @Override
    public void showAcceptHaverError() {
        showMessage(getString(R.string.accepting_haver_error));
    }

    @Override
    public void showGetChatForDesireError() {
        showMessage(getString(R.string.get_chat_for_desire_error));
    }

    @Override
    public void showUpdateDesireStatusError() {
        showMessage(getString(R.string.closing_transaction_error));
    }

    @Override
    public void showDeleteDesireError() {
        showMessage(getString(R.string.delete_desire_error));
    }

    @Override
    public void showFlagDesireError() {
        showMessage(getString(R.string.flag_desire_error));
    }

    public void setDesireLogic(DesireLogic desireLogic){mDesireLogic = desireLogic;}

    //may be modified
    @Override
    public void setLoadingIndicator(final boolean active) {
        if (getView() == null) {
            return;
        }
        /*final SwipeRefreshLayout srl = (SwipeRefreshLayout) getView().findViewById(R.id.refresh_layout);

        // Make sure setRefreshing() is called after the layout is done with everything else.
        srl.post(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(active);
            }
        });*/
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showAcceptButton(List<Haver> havers) {
        if (isHaver(havers)) {
            MenuItem acceptDesireMitem =  mOptionsMenu.findItem(R.id.menu_accept_desire);
            if(acceptDesireMitem != null) {
                acceptDesireMitem.setVisible(false);
            }
        }
    }

    public boolean isHaver(List<Haver> havers) {
        long loggedInUserId = mDesireLogic.getLoggedInUserId();
        for (int i = 0; i < havers.size(); i++) {
            if (havers.get(i).getUser().getId() == loggedInUserId) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void showChatList(long userid){

        //TODO change back to going to chat overview

        Intent intent = new Intent(getContext(), ChatListActivity.class);
        intent.putExtra(ChatListActivity.USER_ID, userid);
        startActivity(intent);
    }

    @Override
    public void showChatDetailsUi(Chat chat) {
        //TODO open chatDetailsUI
        Intent intent = new Intent(getContext(), ChatDetailActivity.class);
        intent.putExtra(ChatDetailActivity.EXTRA_CHAT_ID, chat.getObjectId());
        intent.putExtra("ChatOjbect", chat);
        startActivity(intent);
    }

    @Override
    public void showRating(long desireId) {
        Intent intent = new Intent(getContext(), RatingActivity.class);
        intent.putExtra(RatingActivity.EXTRA_DESIRE_ID, desireId);
        startActivity(intent);
    }

    @Override
    public void showReportPopup() {
        mReportDialog = new Dialog(getContext());

        mDesiredetailReportPopupBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.desiredetail_report_popup, null, false);
        mReportDialog.setContentView(mDesiredetailReportPopupBinding.getRoot());

        mDesiredetailReportPopupBinding.setActionHandler(mDesireDetailActionHandler);

        //Set up flag reason spinner
        Spinner mFlagReasonSpinner = mDesiredetailReportPopupBinding.reportReasonSpinner;
        mFlagReasonSpinner.setAdapter(new ArrayAdapter<FlagReason>(getActivity(), android.R.layout.simple_spinner_item, FlagReason.values()));

        mReportDialog.show();
    }

    @Override
    public DesireFlag getReport() {
        //get values
        String comment = mDesiredetailReportPopupBinding.reportComment.getText().toString();
        FlagReason flagReason = (FlagReason) mDesiredetailReportPopupBinding.reportReasonSpinner.getSelectedItem();

        DesireFlag desireFlag = new DesireFlag();
        if (!comment.equals("")) {
            desireFlag.setComment(comment);
        }
        desireFlag.setFlagReason(flagReason);

        return desireFlag;
    }

    @Override
    public void closeReportPopup() {
        mReportDialog.dismiss();
    }

    @Override
    public void showLoadingProgress() {
        mLoadingDialog = new ProgressDialog(getActivity());
        mLoadingDialog.setTitle(getString(R.string.loading_desire_title));
        //mLoadingDialog.setMessage("Wait while loading...");
        mLoadingDialog.show();
    }

    @Override
    public void endLoadingProgress() {
        mLoadingDialog.dismiss();
    }

    @Override
    public void openDeletionDialog() {

        mDeleteDesireDialog = new Dialog(getContext());

        DesiredetailDeleteDesirePopupBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.desiredetail_delete_desire_popup, null, false);
        mDeleteDesireDialog.setContentView(mBinding.getRoot());

        mBinding.setActionHandler(mDesireDetailActionHandler);

        mDeleteDesireDialog.show();
    }

    @Override
    public void closeDeletionDialog() {
        mDeleteDesireDialog.dismiss();
    }

    @Override
    public void closeView() {
        getActivity().finish();
    }

    @Override
    public void showMap(Location location){
        Intent intent = new Intent(getContext(), MapActivity.class);
        intent.putExtra("location", location);
        intent.putExtra("calledAct", "2"); //for distinguishing which activity started the map
        startActivity(intent);

    }
}
