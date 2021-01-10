package com.example.diabetes.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.example.diabetes.R;

public class Helpers {
    public static void showAlertDialog(String msg, Context context) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setTitle("dialog box");
        dialogBuilder.setIcon(R.mipmap.ic_launcher);
        dialogBuilder.setMessage(msg);
        dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dialogBuilder.setCancelable(false);
        dialogBuilder.show();
    }
}
