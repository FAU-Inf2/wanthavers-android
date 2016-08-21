package wanthavers.mad.cs.fau.de.wanthavers_android.desirecreate;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import de.fau.cs.mad.wanthavers.common.Category;
import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.common.Media;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCaseHandler;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.SelectImageLogic;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetCategory;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetSubcategories;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.SetDesire;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.SetImage;

public class DesireCreatePresenter implements DesireCreateContract.Presenter {
    private final DesireCreateContract.View mDesireCreateView;
    private final UseCaseHandler mUseCaseHandler;
    private final SetDesire mSetDesire;
    private final SetImage mSetImage;
    private final DesireCreateActivity3rdStep mDesireCreateActivity3rdStep;
    private final DesireCreateActivity2ndStep mDesireCreateActivity2ndStep;
    //private Media mMedia;
    private Desire mDesire;
    private final SelectImageLogic mImageLogic;
    private final GetSubcategories mGetSubcategories;
    private final GetCategory mGetCategory;

    public DesireCreatePresenter(@NonNull UseCaseHandler ucHandler, @NonNull DesireCreateContract.View view,
                                 DesireCreateActivity3rdStep desireCreateActivity3rdStep, DesireCreateActivity2ndStep desireCreateActivity2ndStep,
                                 SetDesire setDesire, SetImage setImage, GetSubcategories getSubcategories, GetCategory getCategory) {

        mUseCaseHandler = ucHandler;
        mDesireCreateView = view;
        mSetDesire = setDesire;
        mSetImage = setImage;
        mDesireCreateActivity3rdStep = desireCreateActivity3rdStep;
        mDesireCreateActivity2ndStep = desireCreateActivity2ndStep;
        mImageLogic = new SelectImageLogic(desireCreateActivity2ndStep);
        mGetSubcategories = getSubcategories;
        mGetCategory = getCategory;

        mDesireCreateView.setPresenter(this);

    }

    public SelectImageLogic getImageLogic(){
        return mImageLogic;
    }

    public void start() {
    }

    @Override
    public void createNextDesireCreateStep() {
        mDesireCreateView.showNextDesireCreateStep();
    }

    @Override
    public void selectImageFromDevice(){
        /*if(mDesireCreateView.isStoragePermissionGranted()){
            mDesireCreateView.selectImageForDesire();
        }*/
        if(mImageLogic.isStoragePermissionGranted()){
            mImageLogic.selectImageForDesire();
        }

    }

    @Override
    public void openCategorySelection() {
        mDesireCreateView.showCategorySelection();
    }

    public void setDesire(Desire desire){


        SetDesire.RequestValues requestValue = new SetDesire.RequestValues(desire);

        mUseCaseHandler.execute(mSetDesire, requestValue,
                new UseCase.UseCaseCallback<SetDesire.ResponseValue>() {
                    @Override
                    public void onSuccess(SetDesire.ResponseValue response) {

                        Desire desire = response.getDesire();

                    }

                    @Override
                    public void onError() {
                        mDesireCreateView.showMessage(mDesireCreateActivity3rdStep.getResources().getString(R.string.desire_create_failed));
                    }
                }
        );
    }

    public void setImage(File file,final Desire desire){

        SetImage.RequestValues requestValue = new SetImage.RequestValues(file);

        mUseCaseHandler.execute(mSetImage, requestValue,
                new UseCase.UseCaseCallback<SetImage.ResponseValue>() {
                    @Override
                    public void onSuccess(SetImage.ResponseValue response) {

                        desire.setImage(response.getMedia());
                        mDesireCreateView.showMedia(desire);

                    }


                    @Override
                    public void onError() {
                        mDesireCreateView.showMessage(mDesireCreateActivity3rdStep.getResources().getString(R.string.desire_image_upload_failed));
                    }

                });

    }

    public void loadCategories() {

        GetSubcategories.RequestValues requestValues = new GetSubcategories.RequestValues(0, true);

        mUseCaseHandler.execute(mGetSubcategories, requestValues, new UseCase.UseCaseCallback<GetSubcategories.ResponseValue>() {

            @Override
            public void onSuccess(GetSubcategories.ResponseValue response) {
                List<Category> categories = response.getCategories();
                mDesireCreateView.showCategories(categories);
                //System.out.println("Server sent " + categories.size() + " categories");
            }

            @Override
            public void onError() {
                mDesireCreateView.showGetCategoriesError();
            }

        });
    }

    public void getCategory(long categoryId) {
        GetCategory.RequestValues requestValues = new GetCategory.RequestValues(categoryId);

        mUseCaseHandler.execute(mGetCategory, requestValues, new UseCase.UseCaseCallback<GetCategory.ResponseValue>() {
            @Override
            public void onSuccess(GetCategory.ResponseValue response) {
                Category category = response.getCategory();
                setCurFilterCategory(category);
            }

            @Override
            public void onError() {
                mDesireCreateView.showGetCategoriesError();
            }
        });
    }

    public void setCurFilterCategory(Category category) {
        mDesireCreateView.showCategory(category);
    }

    public void selectExpirationDate(){
        mDesireCreateView.selectExpirationDate();
    }

    public void showInfo(){
        //show revers bidding info
        ((TextView) new AlertDialog.Builder(mDesireCreateActivity2ndStep)
                .setTitle(mDesireCreateActivity2ndStep.getString(R.string.desire_create_reverse_bidding_popup_title))
                .setIcon(android.R.drawable.ic_dialog_info)
                .setMessage(Html.fromHtml("<p>" + mDesireCreateActivity2ndStep.getString(R.string.desire_create_reverse_bidding_popup_text)
                        +"\n"+ "<a href=\"http://wanthaver.com\">www.wanthaver.com</a>.</p>"))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // close dialog
                }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show()
                .findViewById(android.R.id.message))
                .setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void toggleHoursRadioButton(){
        mDesireCreateView.toggleHoursRadioButton();
    }

    public void toggleDaysRadioButton(){
        mDesireCreateView.toggleDaysRadioButton();
    }
}

