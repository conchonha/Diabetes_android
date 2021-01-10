package com.example.diabetes.util.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

import com.example.diabetes.R;

public class DialogShow {
    public static Dialog showdialog(Context context){
        Dialog dialog = new Dialog(context);
        dialog.setContentView(View.inflate(context,R.layout.layout_loading_dialog,null));
        return dialog;
    }
    public static void showDialogAcount(Context context,String title,String Message){
        AlertDialog.Builder dialog=new AlertDialog.Builder(context);
        dialog.setMessage(Message);
        dialog.setTitle(title);
        dialog.setIcon(R.drawable.ic_help);
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        dialog.show();
    }
}
