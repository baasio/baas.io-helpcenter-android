
package com.kth.baasio.helpcenter.prefereces;

import com.kth.common.PlatformSpecificImplementationFactory;
import com.kth.common.preference.SharedPreferenceSaver;

import android.content.Context;
import android.content.SharedPreferences;

public class HelpCenterPreferences {
    private static final String DEFAULT_PREFERENCES_NAME = "HelpDeskPreferences";

    private static final String SHARED_PREFERENCE_NAME_HELPDESK_EMAIL = "baasio_helpdesk_email";

    private static SharedPreferences mPreferences;

    private static SharedPreferences getPreference(Context context) {
        if (mPreferences == null)
            mPreferences = context.getSharedPreferences(DEFAULT_PREFERENCES_NAME,
                    Context.MODE_PRIVATE);

        return mPreferences;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Clone is not allowed.");
    }

    public static void clear(Context context) {
        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.clear();

        SharedPreferenceSaver saver = PlatformSpecificImplementationFactory
                .getSharedPreferenceSaver(context);
        saver.savePreferences(editor, false);
    }

    public static void setHelpDeskEmail(Context context, String string) {
        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.putString(SHARED_PREFERENCE_NAME_HELPDESK_EMAIL, string);

        SharedPreferenceSaver saver = PlatformSpecificImplementationFactory
                .getSharedPreferenceSaver(context);
        saver.savePreferences(editor, false);
    }

    public static String getHelpDeskEmail(Context context) {
        SharedPreferences prefs = getPreference(context);
        String result = prefs.getString(SHARED_PREFERENCE_NAME_HELPDESK_EMAIL, "");

        return result;
    }
}
