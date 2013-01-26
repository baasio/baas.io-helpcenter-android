
package com.kth.baasio.helpcenter.ui.dialog;

import com.kth.common.utils.LogUtils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

public class DialogUtils {

    public static DefaultDialogFragment showDefaultDialog(Fragment fragment, String tag,
            String title, String message, boolean needNegativeButton) {

        // DialogFragment.show() will take care of adding the fragment
        // in a transaction. We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = fragment.getFragmentManager().beginTransaction();
        Fragment prev = fragment.getFragmentManager().findFragmentByTag(tag);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        DefaultDialogFragment defaultDialog = DefaultDialogFragment.newInstance();
        defaultDialog.setTitle(title);
        defaultDialog.setBody(message);
        defaultDialog.setNeedNegativeButton(needNegativeButton);
        // defaultDialog.setDialogResultListener(this);
        defaultDialog.show(ft, tag);

        return defaultDialog;
    }

    public static void dissmissDefaultDialog(Fragment fragment, String tag) {
        DefaultDialogFragment defaultDialog = (DefaultDialogFragment)fragment.getFragmentManager()
                .findFragmentByTag(tag);

        if (defaultDialog != null) {
            defaultDialog.dismiss();
        }
    }

    public static DefaultDialogFragment showDefaultDialog(FragmentActivity fragmentActivity,
            String tag, String title, String message, boolean needNegativeButton) {

        // DialogFragment.show() will take care of adding the fragment
        // in a transaction. We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = fragmentActivity.getSupportFragmentManager().beginTransaction();
        Fragment prev = fragmentActivity.getSupportFragmentManager().findFragmentByTag(tag);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        DefaultDialogFragment defaultDialog = DefaultDialogFragment.newInstance();
        defaultDialog.setTitle(title);
        defaultDialog.setBody(message);
        defaultDialog.setNeedNegativeButton(needNegativeButton);
        defaultDialog.show(ft, tag);

        return defaultDialog;
    }

    public static void dissmissDefaultDialog(FragmentActivity fragmentActivity, String tag) {
        DefaultDialogFragment defaultDialog = (DefaultDialogFragment)fragmentActivity
                .getSupportFragmentManager().findFragmentByTag(tag);

        if (defaultDialog != null) {
            defaultDialog.dismiss();
        }
    }

    public static ProgressDialogFragment showProgressDialog(Fragment fragment, String tag,
            String body, int style) {
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction. We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = fragment.getFragmentManager().beginTransaction();
        Fragment prev = fragment.getFragmentManager().findFragmentByTag(tag);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        ProgressDialogFragment progress = ProgressDialogFragment.newInstance();
        progress.setBody(body);
        progress.setStyle(style);
        progress.show(ft, tag);

        return progress;
    }

    public static ProgressDialogFragment showProgressDialog(Fragment fragment, String tag,
            String body) {
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction. We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = fragment.getFragmentManager().beginTransaction();
        Fragment prev = fragment.getFragmentManager().findFragmentByTag(tag);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        ProgressDialogFragment progress = ProgressDialogFragment.newInstance();
        progress.setBody(body);
        progress.show(ft, tag);

        return progress;
    }

    public static void setProgress(Fragment fragment, String tag, int progressValue) {
        ProgressDialogFragment progress = (ProgressDialogFragment)fragment.getFragmentManager()
                .findFragmentByTag(tag);

        if (progress != null) {
            progress.setProgress(progressValue);
        }
    }

    public static void dissmissProgressDialog(Fragment fragment, String tag) {
        ProgressDialogFragment progress = (ProgressDialogFragment)fragment.getFragmentManager()
                .findFragmentByTag(tag);

        if (progress != null) {
            progress.dismiss();
        }
    }

    public static ProgressDialogFragment showProgressDialog(FragmentActivity fragmentActivity,
            String tag, String body) {
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction. We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = fragmentActivity.getSupportFragmentManager().beginTransaction();
        Fragment prev = fragmentActivity.getSupportFragmentManager().findFragmentByTag(tag);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        ProgressDialogFragment progress = ProgressDialogFragment.newInstance();
        progress.setBody(body);
        progress.setCancelable(false);
        progress.show(ft, tag);

        return progress;
    }

    public static ProgressDialogFragment showProgressDialog(FragmentActivity fragmentActivity,
            String tag, String body, int style) {
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction. We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = fragmentActivity.getSupportFragmentManager().beginTransaction();
        Fragment prev = fragmentActivity.getSupportFragmentManager().findFragmentByTag(tag);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        ProgressDialogFragment progress = ProgressDialogFragment.newInstance();
        progress.setBody(body);
        progress.setStyle(style);
        progress.setCancelable(false);
        progress.show(ft, tag);
        LogUtils.LOGE(tag, "show");

        return progress;
    }

    public static void setProgress(FragmentActivity fragmentActivity, String tag, int progressValue) {
        ProgressDialogFragment progress = (ProgressDialogFragment)fragmentActivity
                .getSupportFragmentManager().findFragmentByTag(tag);

        if (progress != null) {
            progress.setProgress(progressValue);
        }
    }

    public static void dissmissProgressDialog(FragmentActivity fragmentActivity, String tag) {
        ProgressDialogFragment progress = (ProgressDialogFragment)fragmentActivity
                .getSupportFragmentManager().findFragmentByTag(tag);

        if (progress != null) {
            progress.dismiss();
            LogUtils.LOGE(tag, "hide");
        } else {
            LogUtils.LOGE(tag, "hide failed(null)");
        }
    }
}
