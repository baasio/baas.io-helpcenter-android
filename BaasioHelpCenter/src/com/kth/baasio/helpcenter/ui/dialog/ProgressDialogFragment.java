
package com.kth.baasio.helpcenter.ui.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;

public class ProgressDialogFragment extends DialogFragment {
    private String mBody;

    private int mStyle = ProgressDialog.STYLE_SPINNER;

    private int mProgressMax = 100;

    private ProgressDialog mDialog;

    public static ProgressDialogFragment newInstance() {
        ProgressDialogFragment frag = new ProgressDialogFragment();
        return frag;
    }

    public void setBody(String body) {
        mBody = body;
    }

    public void setStyle(int style) {
        mStyle = style;
    }

    public void setMax(int progressMax) {
        mProgressMax = progressMax;
    }

    public void setProgress(int progress) {
        if (mDialog != null) {
            mDialog.setProgress(progress);
        }
    }

    public int getProgress() {
        if (mDialog != null) {
            return mDialog.getProgress();
        }

        return -1;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mDialog = new ProgressDialog(getActivity());
        if (mBody != null && mBody.length() > 0) {
            mDialog.setMessage(mBody);
        }

        mDialog.setCancelable(false);

        mDialog.setProgressStyle(mStyle);
        if (mStyle != ProgressDialog.STYLE_SPINNER) {
            mDialog.setMax(mProgressMax);
            mDialog.setIndeterminate(false);
            mDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(android.R.string.cancel),
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (mListener != null) {
                                mListener.onNegativeButtonSelected(getTag());
                            }
                            dialog.dismiss();
                        }
                    });
        } else {
            mDialog.setIndeterminate(true);
        }
        mDialog.setProgress(50);
        // Disable the back button
        OnKeyListener keyListener = new OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                }
                return false;
            }
        };
        mDialog.setOnKeyListener(keyListener);
        return mDialog;
    }

    private DialogResultListener mListener;

    public interface DialogResultListener {
        public boolean onNegativeButtonSelected(String tag);
    }

    public void setDialogResultListener(DialogResultListener listener) {
        mListener = listener;
    }
}
