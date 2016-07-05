package wanthavers.mad.cs.fau.de.wanthavers_android.desirecreate;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import wanthavers.mad.cs.fau.de.wanthavers_android.R;

public class CustomTextWatcher implements TextWatcher {

    private EditText mEditText;

    public CustomTextWatcher(EditText et) {
        mEditText = et;
    }

    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        //do nothing
    }

    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        //do nothing
    }

    public void afterTextChanged(Editable edt) {

        switch(mEditText.getId()){
            case R.id.create_desire_Title:
                if (edt.length() == 1 && edt.toString().equals(" "))
                    mEditText.setText("");
                break;

            case R.id.create_desire_description:
                if (edt.length() == 1 && edt.toString().equals(" ")){
                    mEditText.setText("");
                }
                break;

            case R.id.create_desire_price:
                //allows 0 for price but not more than one at the beginning of a price
                if (edt.length() == 2 && edt.toString().equals("00"))
                    mEditText.setText("0");
                break;
        }

    }

}
