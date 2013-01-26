
package com.kth.baasio.helpcenter.sample;

import com.kth.baasio.Baas;
import com.kth.common.utils.LogUtils;

import android.app.Application;

public class BaasioApplication extends Application {
    private static final String TAG = LogUtils.makeLogTag(BaasioApplication.class);

    @Override
    public void onCreate() {
        super.onCreate();

        Baas.io().init(this, BaasioConfig.BAASIO_URL, BaasioConfig.BAASIO_ID,
                BaasioConfig.APPLICATION_ID);
    }

    @Override
    public void onTerminate() {
        Baas.io().uninit(this);
        super.onTerminate();
    }
}
