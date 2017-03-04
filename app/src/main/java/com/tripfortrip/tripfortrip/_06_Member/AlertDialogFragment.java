package com.tripfortrip.tripfortrip._06_Member;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.tripfortrip.tripfortrip.R;

import java.util.Collection;
import java.util.List;

/**
 * Created by wei-tzutseng on 2017/2/23.
 */

public class AlertDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {
    private  String title,message,positiveButton,negativeButton,neutralButton;
    private  int icon ;
    List list;
    FirebaseAuth auth;

    public AlertDialogFragment(String title, String message, String positiveButton, String negativeButton, String neutralButton,FirebaseAuth auth,List list) {
        this.title = title;
        this.message = message;
        this.positiveButton = positiveButton;
        this.negativeButton = negativeButton;
        this.neutralButton = neutralButton;
        this.auth =auth;
        this.list=list;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setIcon(icon)
                .setMessage(message)
                .setPositiveButton(positiveButton, this)
                .setNegativeButton(negativeButton, this)
                .setNeutralButton(neutralButton,this)
                .create();
    }
    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                //密碼錯誤重新輸入
                if(title.equals(getString(R.string.wrongPasswd))){

                    ((EditText)list.get(1)).setFocusable(true);
                    ((EditText)list.get(1)).setText("");
                    break;
                }else {
//
                }


            case DialogInterface.BUTTON_NEGATIVE:
                break;
            case DialogInterface.BUTTON_NEUTRAL:
                if(title.equals(getString(R.string.wrongPasswd))) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("確定重設密碼？")
                            .setMessage("按下確定後系統會自動發出重設密碼認證到您的信箱")
                            .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            auth.sendPasswordResetEmail(((EditText) list.get(0)).getText().toString());

                                            ((EditText) list.get(0)).setFocusable(true);
                                            ((EditText) list.get(1)).setText("");
//                                            Toast.makeText(getActivity(), "密碼更改信已寄出，請至信箱確認", Toast.LENGTH_LONG).show();
                                        }
                                    }
                            )
                            .show();
                }
                break;
            default:
                break;
        }

    }

}
