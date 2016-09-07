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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
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
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.DesiredetailAcceptDesirePopupBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.DesiredetailDeleteDesirePopupBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.DesiredetailFragBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.DesiredetailHaverCancelDesirePopupBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.DesiredetailReportPopupBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.DesiredetailUnacceptHaverPopupBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.desirecreate.OnSwipeTouchListener;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.DesireLogic;
import wanthavers.mad.cs.fau.de.wanthavers_android.maps.MapActivity;
import wanthavers.mad.cs.fau.de.wanthavers_android.rating.RatingActivity;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.CircularImageView;
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
    private Dialog mReportDialog, mDeleteDesireDialog, mAcceptDesireDialog, mDeleteHaverDialog, mUnacceptHaverDialog;
    private DesiredetailReportPopupBinding mDesiredetailReportPopupBinding;
    private DesiredetailAcceptDesirePopupBinding mDesiredetailAcceptDesirePopupBinding;
    private DesiredetailHaverCancelDesirePopupBinding mDesiredetailHaverCancelDesirePopupBinding;
    //private ProgressDialog mLoadingDialog;
    private Haver mHaver;
    private Menu mOptionsMenu;
    private boolean mLoadFinishedFlag;
    private boolean mIsHaver = false;

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

        mListAdapter = new DesireDetailAdapter(new ArrayList<Haver>(0), mPresenter, mDesireDetailActionHandler, mDesireLogic);
        mRecyclerView.setAdapter(mListAdapter);

        //S
        ScrollView sv = mDesireDetailFragBinding.desireDetailMainScreen;
        sv.setOnTouchListener(new OnSwipeTouchListener(getActivity(), mPresenter));

        return mDesireDetailFragBinding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        mLoadFinishedFlag = false;
        inflater.inflate(R.menu.desire_detail_menu, menu);
        mOptionsMenu = menu;
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu){

        if(mLoadFinishedFlag && getActivity() != null) {
            SharedPreferencesHelper sharedPreferencesHelper = SharedPreferencesHelper.getInstance(SharedPreferencesHelper.NAME_USER, getActivity().getApplicationContext());
            long loggedInUser = sharedPreferencesHelper.loadLong(SharedPreferencesHelper.KEY_USERID, 6L);

            Desire curDesire = mDesireDetailFragBinding.getDesire();

            MenuItem menReportDesire = menu.findItem(R.id.menu_report_desire);
            MenuItem menDeleteDesire = menu.findItem(R.id.menu_delete_desire);
            /*MenuItem menAcceptDesire = menu.findItem(R.id.menu_accept_desire);
            MenuItem menDeleteHaver = menu.findItem(R.id.menu_delete_haver);
            MenuItem menFinishDesire = menu.findItem(R.id.menu_finish_desire);*/

            if (mDesireDetailFragBinding.getDesire() != null && mDesireDetailFragBinding.getDesire().getCreator().getId() == loggedInUser) {

                if(curDesire.getStatus() == DesireStatus.STATUS_OPEN) {
                    menDeleteDesire.setVisible(true);
                }

                if(curDesire.getStatus() == DesireStatus.STATUS_IN_PROGRESS ){
                    //menFinishDesire.setVisible(true);
                    menDeleteDesire.setVisible(true);
                }

            } else if (mHaver != null && mHaver.getUser().getId() == loggedInUser) { //include haver here
                menReportDesire.setVisible(true);
                /*menDeleteHaver.setVisible(true);
                menAcceptDesire.setVisible(false);
                menFinishDesire.setVisible(false);*/
                //mDesireDetailFragBinding.cancelDesire.setVisibility(View.VISIBLE);
            } else if (mIsHaver) {
                menReportDesire.setVisible(true);
                /*menFinishDesire.setVisible(false);
                menAcceptDesire.setVisible(false);
                menDeleteHaver.setVisible(true);*/
                //mDesireDetailFragBinding.cancelDesire.setVisibility(View.VISIBLE);
            } else {
                menReportDesire.setVisible(true);
                /*menDeleteHaver.setVisible(false);
                menAcceptDesire.setVisible(true);
                menFinishDesire.setVisible(false);*/
                //mDesireDetailFragBinding.acceptDesire.setVisibility(View.VISIBLE);
            }

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
            /*case R.id.menu_accept_desire:
                //TODO: remove debug outputs
                if (mDesireDetailFragBinding.getDesire().isBiddingAllowed()) {
                    showAcceptDesirePopup(true);
                } else {
                    mPresenter.setHaver(false);
                    item.setVisible(false);
                }
                break;
            case R.id.menu_finish_desire:
                mPresenter.closeTransaction();
                break;
            case R.id.menu_delete_haver:
                showDeleteHaverPopup();
                break;*/
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
        mListAdapter.setDesire(desire);

        //show desire image
        Media mediaDesire = desire.getImage();
        if (mediaDesire != null) {
            final CircularImageView profileView = mDesireDetailFragBinding.desireDetailItemImage;
            Picasso.with(mDesireDetailFragBinding.getRoot().getContext()).load(mediaDesire.getLowRes()).into(profileView);
            //Picasso.with(mDesireDetailFragBinding.getRoot().getContext()).load(mediaDesire.getLowRes()).transform(new RoundedTransformation(1000,0)).into(profileView);
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
                final CircularImageView profileView = mDesireDetailFragBinding.imageWanter;
                Picasso.with(mDesireDetailFragBinding.getRoot().getContext()).load(mediaWanter.getLowRes()).into(profileView);
                //Picasso.with(mDesireDetailFragBinding.getRoot().getContext()).load(mediaWanter.getLowRes()).transform(new RoundedTransformation(1000,0)).into(profileView);
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
            if (!mDesireLogic.isDesireCreator(creator.getId()) && !mIsHaver) {
                mDesireDetailFragBinding.acceptDesire.setVisibility(View.VISIBLE);
                mDesireDetailFragBinding.cancelDesire.setVisibility(View.GONE);
            } else if (!mDesireLogic.isDesireCreator(creator.getId()) && mIsHaver) {
                mDesireDetailFragBinding.acceptDesire.setVisibility(View.GONE);
                mDesireDetailFragBinding.cancelDesire.setVisibility(View.VISIBLE);
            }
            mDesireDetailFragBinding.finishDesire.setVisibility(View.GONE);

            if (mDesireLogic.isDesireCreator(creator.getId())) {
                if(isAdded()) {
                    mDesireDetailFragBinding.desireHaverStatus.setText(getString(R.string.haver_status_open));
                }
            }
        } else if (desire.getStatus() == DesireStatus.STATUS_IN_PROGRESS) {
            mPresenter.getAcceptedHaver(desire.isBiddingAllowed());
            //haver cannot accept
            mDesireDetailFragBinding.acceptDesire.setVisibility(View.GONE);
            if (mDesireLogic.isDesireCreator(creator.getId())) {
                mDesireDetailFragBinding.finishDesire.setVisibility(View.VISIBLE);
                if(isAdded()) {
                    mDesireDetailFragBinding.desireHaverStatus.setText(getString(R.string.haver_status_in_progress));
                }
            }
        } else if (desire.getStatus() == DesireStatus.STATUS_DONE) {
            mPresenter.getAcceptedHaver(desire.isBiddingAllowed());
            //haver cannot accept & cancel
            mDesireDetailFragBinding.acceptDesire.setVisibility(View.GONE);
            mDesireDetailFragBinding.cancelDesire.setVisibility(View.GONE);
            mDesireDetailFragBinding.finishDesire.setVisibility(View.GONE);
            if (isAdded() && mDesireLogic.isDesireCreator(creator.getId())) {
                mDesireDetailFragBinding.desireHaverStatus.setText(getString(R.string.haver_status_done));
            }
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
        mDesireDetailFragBinding.acceptedHaverBar.setVisibility(View.GONE);
        mDesireDetailFragBinding.haverList.setVisibility(View.VISIBLE);
        endLoadingProgress();
    }

    public void showAcceptedHaver(Haver haver) {

        if(isActive()) {
            if (mDesireLogic.getLoggedInUserId() == haver.getUser().getId()
                    && mDesireDetailFragBinding.desireHaverStatus.getVisibility() == View.VISIBLE) {
                if (mDesireDetailFragBinding.getDesire().getStatus() == DesireStatus.STATUS_DONE) {
                    mDesireDetailFragBinding.desireHaverStatus.setText(getString(R.string.haver_status_done));
                } else {
                    mDesireDetailFragBinding.desireHaverStatus.setText(getString(R.string.haver_status_accepted));
                    mDesireDetailFragBinding.cancelDesire.setVisibility(View.VISIBLE);
                }
            } else if (mDesireLogic.getLoggedInUserId() != haver.getUser().getId()
                    && mDesireDetailFragBinding.desireHaverStatus.getVisibility() == View.VISIBLE
                    && !mDesireLogic.isDesireCreator(mDesireDetailFragBinding.getDesire().getCreator().getId())) {
                mDesireDetailFragBinding.desireHaverStatus.setText(getString(R.string.haver_status_rejected));
            }

            mDesireDetailFragBinding.haverList.setVisibility(View.GONE);
            mDesireDetailFragBinding.noHavers.setVisibility(View.GONE);
            mDesireDetailFragBinding.acceptedHaverBar.setVisibility(View.VISIBLE);

            Media mediaHaver = haver.getUser().getImage();
            if (mediaHaver != null) {
                final CircularImageView profileView = mDesireDetailFragBinding.imageAcceptedHaver;
                Picasso.with(mDesireDetailFragBinding.getRoot().getContext()).load(mediaHaver.getLowRes()).into(profileView);
                //Picasso.with(mDesireDetailFragBinding.getRoot().getContext()).load(mediaHaver.getLowRes()).transform(new RoundedTransformation(1000, 0)).into(profileView);
            } else {
                //else case is neccessary as the image is otherwise overwritten on scroll
                final ImageView profileView = mDesireDetailFragBinding.imageAcceptedHaver;
                profileView.setImageResource(R.drawable.no_pic);
            }

            showRatingButton(mDesireDetailFragBinding.getDesire());

        }

        endLoadingProgress();
    }

    private void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showLoadingHaversError() {

        if (isAdded()) {
            showMessage(getString(R.string.loading_havers_error));
        }
    }

    @Override
    public void showLoadingDesireError() {
        if (isAdded()) {
            showMessage(getString(R.string.loading_desire_error));
        }
    }

    @Override
    public void showSetHaverError() {
        if (isAdded()) {
            showMessage(getString(R.string.setting_haver_error));
        }
    }

    @Override
    public void showAcceptHaverError() {
        if (isAdded()) {
            showMessage(getString(R.string.accepting_haver_error));
        }
    }

    @Override
    public void showGetChatForDesireError() {
        if (isAdded()) {
            showMessage(getString(R.string.get_chat_for_desire_error));
        }
    }

    @Override
    public void showUpdateDesireStatusError() {
        if (isAdded()) {
            showMessage(getString(R.string.closing_transaction_error));
        }
    }

    @Override
    public void showDeleteDesireError() {
        if (isAdded()) {
            showMessage(getString(R.string.delete_desire_error));
        }
    }

    @Override
    public void showFlagDesireError() {
        if (isAdded()) {
            showMessage(getString(R.string.flag_desire_error));
        }
    }

    @Override
    public void showDeleteHaverError() {
        if (isAdded()) {
            showMessage(getString(R.string.desire_detail_delete_haver_error));
        }
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

    /*@Override
    public void showAcceptButton(List<Haver> havers) {
        if (isHaver(havers)) {
            MenuItem acceptDesireMitem =  mOptionsMenu.findItem(R.id.menu_accept_desire);
            if(acceptDesireMitem != null) {
                acceptDesireMitem.setVisible(false);
            }
            mDesireDetailFragBinding.desireHaverStatus.setText(R.string.haver_status_waiting);
        }
    }*/

    public void showHaverAcceptStatus() {
        if (isAdded()) {
            mDesireDetailFragBinding.desireHaverStatus.setText(R.string.haver_status_waiting);
        }
    }

    /*public boolean isHaver(List<Haver> havers) {
        long loggedInUserId = mDesireLogic.getLoggedInUserId();
        for (int i = 0; i < havers.size(); i++) {
            if (havers.get(i).getUser().getId() == loggedInUserId) {
                return true;
            }
        }
        return false;
    }*/

    @Override
    public void showUnacceptedHaverView(boolean active) {

        if(isActive()) {
            if (active) {
                mIsHaver = true;
                mDesireDetailFragBinding.desireHaverStatus.setText(getString(R.string.haver_status_waiting));
                if (mDesireDetailFragBinding.getDesire().isBiddingAllowed()) {
                    showBidderView(true);
                }
                mDesireDetailFragBinding.acceptDesire.setVisibility(View.GONE);
            } else {
                mIsHaver = false;
                mDesireDetailFragBinding.desireHaverStatus.setText(getString(R.string.haver_status_open));
                if (mDesireDetailFragBinding.getDesire().isBiddingAllowed()) {
                    showBidderView(false);
                }
                mDesireDetailFragBinding.acceptDesire.setVisibility(View.VISIBLE);
                mDesireDetailFragBinding.cancelDesire.setVisibility(View.GONE);
            }
        }
        onPrepareOptionsMenu(mOptionsMenu);
    }

    public void showBidderView(boolean active) {
        if (active) {
            if (isAdded()) {
                mDesireDetailFragBinding.desireDetailPriceTitle.setText(getString(R.string.accepted_desire_bid_title));
            }
        } else {
            if (isAdded()) {
                mDesireDetailFragBinding.desireDetailPriceTitle.setText(getString(R.string.create_desire_price_header));
                mDesireDetailFragBinding.setBidder(null);
            }
        }
    }

    @Override
    public void setBidder(Haver bidder) {
        if (bidder == null) {
            System.out.println("bidder input is null");
        }
        System.out.println(bidder);
        mDesireDetailFragBinding.setBidder(bidder);
        if (mDesireDetailFragBinding.getBidder() == null) {
            System.out.println("bidder output is null");
        }
    }

    @Override
    public double getBidInput() {
        EditText bidInputView = mDesiredetailAcceptDesirePopupBinding.acceptDesireBidInput;
        double bid = -1;
        if (!bidInputView.getText().toString().equals("")) {
            bid = Double.valueOf(bidInputView.getText().toString());
        }
        return bid;
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
    public void showAcceptDesirePopup(boolean initialCall) {
        mAcceptDesireDialog = new Dialog(getContext());

        mDesiredetailAcceptDesirePopupBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.desiredetail_accept_desire_popup, null, false);
        mAcceptDesireDialog.setContentView(mDesiredetailAcceptDesirePopupBinding.getRoot());

        mDesiredetailAcceptDesirePopupBinding.setActionHandler(mDesireDetailActionHandler);
        mDesiredetailAcceptDesirePopupBinding.setDesire(mDesireDetailFragBinding.getDesire());
        mDesiredetailAcceptDesirePopupBinding.setDesirelogic(mDesireLogic);

        if (!initialCall) {
            if (isAdded()) {
                mDesiredetailAcceptDesirePopupBinding.acceptDesireTitle.setText(getString(R.string.desire_modify_bid));
            }
            mDesiredetailAcceptDesirePopupBinding.buttonSubmitBid.setVisibility(View.GONE);
            mDesiredetailAcceptDesirePopupBinding.buttonSubmitModifiedBid.setVisibility(View.VISIBLE);
            mDesiredetailAcceptDesirePopupBinding.acceptDesireSubtitle.setVisibility(View.GONE);
            mDesiredetailAcceptDesirePopupBinding.acceptDesireBidInput.setHint(String.valueOf( (int) mDesireDetailFragBinding.getBidder().getRequestedPrice()));
        }

        mAcceptDesireDialog.show();
    }

    @Override
    public void closeAcceptDesirePopup() {
        mAcceptDesireDialog.dismiss();
    }

    @Override
    public void showDeleteHaverPopup() {
        mDeleteHaverDialog = new Dialog(getContext());

        mDesiredetailHaverCancelDesirePopupBinding = DataBindingUtil.inflate(
                LayoutInflater.from(getContext()), R.layout.desiredetail_haver_cancel_desire_popup, null, false
        );
        mDeleteHaverDialog.setContentView(mDesiredetailHaverCancelDesirePopupBinding.getRoot());

        mDesiredetailHaverCancelDesirePopupBinding.setActionHandler(mDesireDetailActionHandler);
        mDesiredetailHaverCancelDesirePopupBinding.setDesire(mDesireDetailFragBinding.getDesire());
        mDesiredetailHaverCancelDesirePopupBinding.setDesirelogic(mDesireLogic);
        mDesiredetailHaverCancelDesirePopupBinding.setHaver(mDesireDetailFragBinding.getHaver());

        mDeleteHaverDialog.show();
    }

    @Override
    public void closeDeleteHaverPopup() {
        mDeleteHaverDialog.dismiss();
    }

    @Override
    public void showReportPopup() {
        mReportDialog = new Dialog(getContext());

        mDesiredetailReportPopupBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.desiredetail_report_popup, null, false);
        mReportDialog.setContentView(mDesiredetailReportPopupBinding.getRoot());

        mDesiredetailReportPopupBinding.setActionHandler(mDesireDetailActionHandler);

        //Set up flag reason spinner
        Spinner mFlagReasonSpinner = mDesiredetailReportPopupBinding.reportReasonSpinner;
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.report_reasons, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mFlagReasonSpinner.setAdapter(adapter);
        //mFlagReasonSpinner.setAdapter(new ArrayAdapter<FlagReason>(getActivity(), android.R.layout.simple_spinner_item, R.array.report_reasons));

        mReportDialog.show();
    }

    @Override
    public DesireFlag getReport() {
        //get values
        String comment = mDesiredetailReportPopupBinding.reportComment.getText().toString();
        FlagReason flagReason = null;
        String flagString = (String) mDesiredetailReportPopupBinding.reportReasonSpinner.getSelectedItem();
        String[] flags = getResources().getStringArray(R.array.report_reasons);

        if (flagString.equals(flags[0])) {
            flagReason = FlagReason.INAPPROPRIATE;
        } else  if (flagString.equals(flags[1])) {
            flagReason = FlagReason.IMPOSSIBLE;
        } else  if (flagString.equals(flags[2])) {
            flagReason = FlagReason.SPAM;
        }

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
    public void endLoadingProgress() {
        mDesireDetailFragBinding.desireDetailLoadingScreen.setVisibility(View.GONE);
        mDesireDetailFragBinding.desireDetailMainScreen.setVisibility(View.VISIBLE);
        mLoadFinishedFlag = true;
        onPrepareOptionsMenu(mOptionsMenu);
    }

    @Override
    public void openDeletionDialog() {

        mDeleteDesireDialog = new Dialog(getContext());

        DesiredetailDeleteDesirePopupBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.desiredetail_delete_desire_popup, null, false);
        mDeleteDesireDialog.setContentView(mBinding.getRoot());

        mBinding.setActionHandler(mDesireDetailActionHandler);
        mBinding.setDesire(mDesireDetailFragBinding.getDesire());
        mBinding.setDesirelogic(mDesireLogic);

        mDeleteDesireDialog.show();
    }

    @Override
    public void closeDeletionDialog() {
        mDeleteDesireDialog.dismiss();
    }

    @Override
    public void showUnacceptHaverDialog() {
        mUnacceptHaverDialog = new Dialog(getContext());

        DesiredetailUnacceptHaverPopupBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.desiredetail_unaccept_haver_popup, null, false);
        mUnacceptHaverDialog.setContentView(mBinding.getRoot());

        mBinding.setActionHandler(mDesireDetailActionHandler);
        mBinding.setHaver(mDesireDetailFragBinding.getHaver());

        mUnacceptHaverDialog.show();
    }

    @Override
    public void closeUnacceptHaverDialog() {
        mUnacceptHaverDialog.dismiss();
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

    @Override
    public void hideFinishDesire(){
        /*MenuItem menFinishTransaction = mOptionsMenu.findItem(R.id.menu_finish_desire);
        menFinishTransaction.setVisible(false);*/
        mDesireDetailFragBinding.finishDesire.setVisibility(View.GONE);
        MenuItem menDeleteDesire = mOptionsMenu.findItem(R.id.menu_delete_desire);
        menDeleteDesire.setVisible(false);
    }

    @Override
    public void showTransactionSuccessMessage(){
        if (isAdded()) {
            showMessage(getString(R.string.closing_transaction_success));
        }
    }
}
