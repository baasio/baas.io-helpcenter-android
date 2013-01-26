
package com.kth.baasio.helpcenter.ui.dialog;

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.kth.baasio.helpcenter.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

public class DefaultDialogFragment extends SherlockDialogFragment {

    private String mBody;

    private String mTitle;

    private boolean hasNegativeButton = true;

    public static DefaultDialogFragment newInstance() {
        DefaultDialogFragment frag = new DefaultDialogFragment();
        return frag;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public void setBody(String body) {
        this.mBody = body;
    }

    public void setNeedNegativeButton(boolean need) {
        hasNegativeButton = need;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setStyle(SherlockDialogFragment.STYLE_NORMAL, R.style.Theme_Sherlock_Light_Dialog);

        AlertDialog dialog = null;

        if (hasNegativeButton) {
            dialog = new AlertDialog.Builder(getActivity())
                    .setTitle(mTitle)
                    .setMessage(mBody)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            if (mListener != null) {
                                mListener.onPositiveButtonSelected(getTag());
                            }
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton(android.R.string.cancel,
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (mListener != null) {
                                        mListener.onNegativeButtonSelected(getTag());
                                    }
                                    dialog.dismiss();
                                }
                            }).create();
        } else {
            dialog = new AlertDialog.Builder(getActivity()).setTitle(mTitle).setMessage(mBody)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            if (mListener != null) {
                                mListener.onPositiveButtonSelected(getTag());
                            }
                            dialog.dismiss();
                        }
                    }).create();
        }

        return dialog;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        if (mListener != null) {
            mListener.onDismiss(getTag(), dialog);
        }
    }

    private DialogResultListener mListener;

    public interface DialogResultListener {
        public boolean onPositiveButtonSelected(String tag);

        public boolean onNegativeButtonSelected(String tag);

        public boolean onDismiss(String tag, DialogInterface dialog);
    }

    public void setDialogResultListener(DialogResultListener listener) {
        mListener = listener;
    }
}
