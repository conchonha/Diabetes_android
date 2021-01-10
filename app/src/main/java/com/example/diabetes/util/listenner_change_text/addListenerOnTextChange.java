package com.example.diabetes.util.listenner_change_text;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.example.diabetes.util.constant.Constant;
import com.example.diabetes.util.validations.Validations;
import com.google.android.material.textfield.TextInputLayout;

public class addListenerOnTextChange implements TextWatcher {
    private Context mContext;
    private TextInputLayout textInputLayout;
    private int mIndex;
    private EditText editText;

    public addListenerOnTextChange(Context context, TextInputLayout textInputLayout,int index,EditText editText) {
        super();
        this.mContext = context;
        this.textInputLayout = textInputLayout;
        this.mIndex = index;
        this.editText = editText;
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        switch (mIndex){
            case 1: // check email
                if(!Validations.isEmail(editText.getText().toString())){
                    textInputLayout.setError(Constant.mErrEmail);
                }else{
                    textInputLayout.setError(null);
                }
                break;
            case 2:
                if(!Validations.isPassword(editText.getText().toString())){
                    textInputLayout.setError(Constant.mErrPassword);
                }else {
                    textInputLayout.setError(null);
                }
                break;
            case 3:
                if(!Validations.isFullName(editText.getText().toString())){
                    editText.setError(Constant.mErrFullName);
                }else{
                    editText.setError(null);
                }
                break;
            case 4:
                if(!Validations.isAge(editText.getText().toString())){
                    editText.setError(Constant.mErrAge);
                }else{
                    editText.setError(null);
                }
                break;
            case 5:
                if(!Validations.isDateOfBirth(editText.getText().toString())){
                    editText.setError(Constant.mErrDateOfBirth);
                }else{
                    editText.setError(null);
                }
                break;
            case 6:
                if(!Validations.isEmail(editText.getText().toString())){
                    editText.setError(Constant.mErrEmail);
                }else{
                    editText.setError(null);
                }
                break;
            case 7:
                if(!Validations.isPassword(editText.getText().toString())){
                    editText.setError(Constant.mErrPassword);
                }else{
                    editText.setError(null);
                }
                break;
            case 8:
                if(!Validations.isHeightAndWeight(editText.getText().toString())){
                    editText.setError(Constant.mErrHeightAndWeight);
                }else{
                    editText.setError(null);
                }
                break;
        }
    }
}
