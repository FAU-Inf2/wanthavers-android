package wanthavers.mad.cs.fau.de.wanthavers_android.desirecreate;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import de.fau.cs.mad.wanthavers.common.Category;
import de.fau.cs.mad.wanthavers.common.Desire;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.WantHaversApplication;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.DesirecreateFragBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.WantHaversTextView;

public class DesireCreateFragment extends Fragment implements DesireCreateContract.View {
    private DesirecreateFragBinding mViewDataBinding;
    private DesireCreateContract.Presenter mPresenter;
    private DesireCreateActionHandler mDesireCreateActionHandler;

    private EditText mDesireTitle;
    private EditText mDesireDescription;
    private TextView mDesireDescriptionCounter;
    private TimePicker mTimePicker;//= (TimePicker) getActivity().findViewById(R.id.DesireTimePicker);
    private DatePicker mDatePicker;// = (DatePicker) getActivity().findViewById(R.id.DesireDatePicker);
    private Date mExpirationDate = null;
    private TextView mDate;
    String mStringDate = " ";
    private final long HOURINMILLISECONDS = 3600000;

    public DesireCreateFragment(){
        //Requires empty public constructor
    }
    public static DesireCreateFragment newInstance(){ return new DesireCreateFragment();}

    @Override
    public void setPresenter(@NonNull DesireCreateContract.Presenter presenter) {
        //mPresenter = checkNotNull(presenter);
        mPresenter = presenter;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewDataBinding.setPresenter(mPresenter);

    }
    @Override
    public void onResume()  {
        super.onResume();
        mPresenter.start();
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        setRetainInstance(true);

        mViewDataBinding = DesirecreateFragBinding.inflate(inflater, container, false);
        mViewDataBinding.setPresenter(mPresenter);

        mDesireCreateActionHandler = new DesireCreateActionHandler(mPresenter);
        mViewDataBinding.setActionHandler(mDesireCreateActionHandler);

        mViewDataBinding.getRoot().setOnTouchListener(new OnSwipeTouchListener(getActivity(), mPresenter, mDesireCreateActionHandler));

        mDesireTitle  = mViewDataBinding.createDesireTitle;
        mDesireDescription  =  mViewDataBinding.createDesireDescription;
        mDesireDescriptionCounter = mViewDataBinding.createDesireDescriptionCounter;
        //mDate = mViewDataBinding.noDateSelected;

        CustomTextWatcher myWatcher = new CustomTextWatcher(mDesireTitle);
        CustomTextWatcher myWatcher2 = new CustomTextWatcher(mDesireDescription);
        mDesireDescriptionCounter.setText("0/300");
        mDesireDescription.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int aft ){
            }

            @Override
            public void afterTextChanged(Editable s) {
                mDesireDescriptionCounter.setText(s.toString().length() + "/300");
                if(s.toString().length() == 300){
                    mDesireDescriptionCounter.setTextColor(Color.RED);
                }else{
                    mDesireDescriptionCounter.setTextColor(Color.GRAY);
                }
            }
        });

        //Set up timespan seekbar
        SeekBar seekBar = (SeekBar) mViewDataBinding.timeSpanSeekbar;
        final WantHaversTextView timeSpanStatus = (WantHaversTextView) mViewDataBinding.timeSpanStatus;
        final WantHaversTextView timeSpanUnit = (WantHaversTextView) mViewDataBinding.timeSpanUnit;
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            Integer mProgress = 12;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mProgress = progress + 1;
                //String curTimeSpan = mProgress.toString();
                //timeSpanStatus.setText(curTimeSpan);

                String[] timeSpan = getEntryForProgress(mProgress);
                timeSpanStatus.setText(timeSpan[0]);
                timeSpanUnit.setText(timeSpan[1]);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                /*String curTimeSpan = mProgress.toString();
                timeSpanStatus.setText(curTimeSpan);*/

                String[] timeSpan = getEntryForProgress(mProgress);
                timeSpanStatus.setText(timeSpan[0]);
                timeSpanUnit.setText(timeSpan[1]);

            }
        });

        //set default RadioButtonHours
        //mViewDataBinding.radioButtonHours.toggle();

        mDesireTitle.addTextChangedListener(myWatcher);
        mDesireDescription.addTextChangedListener(myWatcher2);

        return mViewDataBinding.getRoot();
    }


    public String[] getEntryForProgress(Integer progress){

        String timeSpan[] = new String[2];
        switch (progress) {
            //1h 2h 3h 5h 10h 15h 1d 2d 3d 5d 1w 2w 1w
            case 1:
                //1h
                timeSpan[0] = "1";
                timeSpan[1] = getString(R.string.desire_create_hour);
                return timeSpan;
            case 2:
                //2h
                timeSpan[0] = "2";
                timeSpan[1] = getString(R.string.desire_create_hours);
                return timeSpan;
            case 3:
                //3h
                timeSpan[0] = "3";
                timeSpan[1] = getString(R.string.desire_create_hours);
                return timeSpan;
            case 4:
                //5h
                timeSpan[0] = "5";
                timeSpan[1] = getString(R.string.desire_create_hours);
                return timeSpan;
            case 5:
                //10h
                timeSpan[0] = "10";
                timeSpan[1] = getString(R.string.desire_create_hours);
                return timeSpan;
            case 6:
                //15h
                timeSpan[0] = "15";
                timeSpan[1] = getString(R.string.desire_create_hours);
                return timeSpan;
            case 7:
                //1d
                timeSpan[0] = "1";
                timeSpan[1] = getString(R.string.desire_create_day);
                return timeSpan;
            case 8:
                //2d
                timeSpan[0] = "2";
                timeSpan[1] = getString(R.string.desire_create_days);
                return timeSpan;
            case 9:
                //3d
                timeSpan[0] = "3";
                timeSpan[1] = getString(R.string.desire_create_days);
                return timeSpan;
            case 10:
                //5d
                timeSpan[0] = "5";
                timeSpan[1] = getString(R.string.desire_create_days);
                return timeSpan;
            case 11:
                //1w
                timeSpan[0] = "1";
                timeSpan[1] = getString(R.string.desire_create_week);
                return timeSpan;
            case 12:
                //2w
                timeSpan[0] = "2";
                timeSpan[1] = getString(R.string.desire_create_weeks);
                return timeSpan;
            case 13:
                //3w
                timeSpan[0] = "3";
                timeSpan[1] = getString(R.string.desire_create_weeks);
                return timeSpan;
            default:
                timeSpan[0] = "3";
                timeSpan[1] = getString(R.string.desire_create_weeks);
                return timeSpan;
        }
    }

    @Override
    public void showNextDesireCreateStep() {


        if(mDesireTitle.getText().toString().isEmpty() || mDesireDescription.getText().toString().isEmpty() ){
            showMessage( getString(R.string.createDesire_Empty_Text));
            return;
        }

        Intent intent = new Intent(getContext(), DesireCreateActivity2ndStep.class);
        intent.putExtra("desireTitle", mDesireTitle.getText().toString());
        intent.putExtra("desireDescription", mDesireDescription.getText().toString());
        //intent.putExtra("desireExpirationDate", mExpirationDate);
        intent.putExtra("desireTimeSpan", getDesireTimeSpan());
        startActivity(intent);
    }

    public long getDesireTimeSpan() {
        long timeSpan = 0L;
        /*if (mViewDataBinding.radioButtonHours.isChecked()){
            timeSpan = ((mViewDataBinding.timeSpanSeekbar.getProgress() + 1)* 3600000);
        }else if (mViewDataBinding.radioButtonDays.isChecked()){
            timeSpan = ((mViewDataBinding.timeSpanSeekbar.getProgress() + 1)* 3600000 * 24);
        }*/

        switch((mViewDataBinding.timeSpanSeekbar.getProgress() + 1)){
            //1h 2h 3h 5h 10h 15h 1d 2d 3d 5d 1w 2w 1w
            case 1:
                //1h
                timeSpan = HOURINMILLISECONDS;
                break;
            case 2:
                //2h
                timeSpan = 2 * HOURINMILLISECONDS;
                break;
            case 3:
                //3h
                timeSpan = 3 * HOURINMILLISECONDS;
                break;
            case 4:
                //5h
                timeSpan = 5 * HOURINMILLISECONDS;
                break;
            case 5:
                //10h
                timeSpan = 10 * HOURINMILLISECONDS;
                break;
            case 6:
                //15h
                timeSpan = 15 * HOURINMILLISECONDS;
                break;
            case 7:
                //1d
                timeSpan = 24 * HOURINMILLISECONDS;
                break;
            case 8:
                //2d
                timeSpan = 2 * 24 * HOURINMILLISECONDS;
                break;
            case 9:
                //3d
                timeSpan = 3 * 24 * HOURINMILLISECONDS;
                break;
            case 10:
                //5d
                timeSpan = 5 * 24 * HOURINMILLISECONDS;
                break;
            case 11:
                //1w
                timeSpan = 7 * 24 * HOURINMILLISECONDS;
                break;
            case 12:
                //2w
                timeSpan = 2 * 7 * 24 * HOURINMILLISECONDS;
                break;
            case 13:
                //3w
                timeSpan = 3 * 7 * 24 * HOURINMILLISECONDS;
                break;
        }

        Log.d("TimeSpan in ms: ",Long.toString(timeSpan));
        return timeSpan;
    }


    @Override
    public void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showMedia(Desire m){
        //no Medias in this Step
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.create_desire_step_one, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.next_step:
                mPresenter.createNextDesireCreateStep();
        }

        return true;
    }

    @Override
    public void showCategorySelection() {
        //no Category Selection in this Step
    }

    @Override
    public void showCategory(Category category) {
        //no Category Selection in this Step
    }

    @Override
    public void showGetCategoriesError() {
        //no Category Selection in this Step
    }

    @Override
    public void showCategories(List<Category> categories) {
        //no Category Selection in this Step
    }

    public void selectExpirationDate(){

       // new DatePickerDialog(this, myDateListener, year, month, day);



        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());



        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.date_time_dialog, null);
        mTimePicker = (TimePicker) dialogView.findViewById(R.id.DesireTimePicker);
        mTimePicker.setIs24HourView(true);
        mDatePicker = (DatePicker) dialogView.findViewById(R.id.DesireDatePicker);
        mDatePicker.setMinDate(System.currentTimeMillis() - 1000); // only future dates allowed

        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle(getString(R.string.select_date));
        dialogBuilder.setCancelable(false);
        dialogBuilder.setIcon(R.drawable.ic_clock);
        dialogBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {

               mExpirationDate = new Date(mDatePicker.getYear() - 1900, mDatePicker.getMonth(), mDatePicker.getDayOfMonth(),
                        mTimePicker.getCurrentHour(), mTimePicker.getCurrentMinute ());

                if (mExpirationDate.after(new Date())){
                    mStringDate = DateFormat.getDateTimeInstance().format(mExpirationDate);
                    mDate.setText(mStringDate);
                    dialog.dismiss();
                }else{
                    showMessage(getString(R.string.select_date_error));
                }

            }

        });
        dialogBuilder.setNegativeButton("Cancel", null);

        dialogBuilder.show();


    }

    public void toggleHoursRadioButton(){
        mViewDataBinding.timeSpanUnit.setText(getString(R.string.desire_create_hours));
    }

    public void toggleDaysRadioButton(){
        mViewDataBinding.timeSpanUnit.setText(getString(R.string.desire_create_days));
    }

    /*public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        WantHaversTextView date = mViewDataBinding.noDateSelected;
        if (mStringDate != " "){
            date.setText(mStringDate);
        }
    }*/
}
