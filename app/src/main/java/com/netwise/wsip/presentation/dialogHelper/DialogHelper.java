package com.netwise.wsip.presentation.dialogHelper;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.netwise.wsip.R;

/**
 * Created by dawido on 28.03.2018.
 */

public class DialogHelper {

    public static void displayErrorDialog(Context context, String msg){
        new AlertDialog.Builder(context,  R.style.Theme_AppCompat_DayNight_Dialog)
                .setTitle(context.getResources().getString(R.string.error_header))
                .setMessage(msg)
                .setPositiveButton(context.getResources().getString(R.string.ok_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create().show();
    }

    public static void displaySuccessDialog(Context context, String msg, Runnable func){
        new AlertDialog.Builder(context, R.style.Theme_AppCompat_DayNight_Dialog)
                .setTitle(context.getResources().getString(R.string.success_header))
                .setMessage(msg)
                .setPositiveButton(context.getResources().getString(R.string.ok_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        func.run();
                    }
                }).create().show();
    }
    public static void displayDialog(Context context, String title, String msg){
        new AlertDialog.Builder(context,  R.style.Theme_AppCompat_DayNight_Dialog)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(context.getResources().getString(R.string.ok_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create().show();
    }
}
