
package com.kth.baasio.helpcenter.ui.pulltorefresh;

import com.kth.baasio.helpcenter.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Date;

public class LoadingLayout extends FrameLayout {

    static final int DEFAULT_ROTATION_ANIMATION_DURATION = 150;

    private ImageView mHeaderImage;

    private ProgressBar mHeaderProgress;

    private TextView mHeaderText;

    private TextView mHeaderTimeText;

    private String mPullLabel;

    private String mRefreshingLabel;

    private String mReleaseLabel;

    private Animation mRotateAnimation, mResetRotateAnimation;

    public LoadingLayout(Context context) {
        super(context);
    }

    public LoadingLayout(Context context, final int mode, String releaseLabel, String pullLabel,
            String refreshingLabel, TypedArray attrs) {
        super(context);
        ViewGroup header = (ViewGroup)LayoutInflater.from(context).inflate(
                R.layout.pull_to_refresh_header, this);
        LinearLayout linearText = (LinearLayout)header.findViewById(R.id.linearLayoutText2);
        mHeaderText = (TextView)linearText.findViewById(R.id.pull_to_refresh_text);
        // 05.25 유미형님. 시간 보여주지 말자.
        mHeaderTimeText = (TextView)linearText.findViewById(R.id.pull_to_refresh_updated_at);
        mHeaderTimeText.setVisibility(View.GONE);

        mHeaderImage = (ImageView)header.findViewById(R.id.pull_to_refresh_image);
        mHeaderProgress = (ProgressBar)header.findViewById(R.id.pull_to_refresh_progress);

        final Interpolator interpolator = new LinearInterpolator();
        mRotateAnimation = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateAnimation.setInterpolator(interpolator);
        mRotateAnimation.setDuration(DEFAULT_ROTATION_ANIMATION_DURATION);
        mRotateAnimation.setFillAfter(true);

        mResetRotateAnimation = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        mResetRotateAnimation.setInterpolator(interpolator);
        mResetRotateAnimation.setDuration(DEFAULT_ROTATION_ANIMATION_DURATION);
        mResetRotateAnimation.setFillAfter(true);

        mReleaseLabel = releaseLabel;
        mPullLabel = pullLabel;
        mRefreshingLabel = refreshingLabel;

        switch (mode) {
            case PullToRefreshBase.MODE_PULL_UP_TO_REFRESH:
                mHeaderImage.setImageResource(R.drawable.pulltorefresh_up_arrow);
                break;
            case PullToRefreshBase.MODE_PULL_DOWN_TO_REFRESH:
            default:
                mHeaderImage.setImageResource(R.drawable.updating_arrow);
                break;
        }

        if (attrs.hasValue(R.styleable.PullToRefresh_Helpcenter_ptrHeaderTextColor)) {
            final int color = attrs.getColor(
                    R.styleable.PullToRefresh_Helpcenter_ptrHeaderTextColor, Color.BLACK);
            setTextColor(color);
        }
    }

    public void reset() {
        mHeaderText.setText(mPullLabel);
        mHeaderImage.setVisibility(View.VISIBLE);
        mHeaderProgress.setVisibility(View.GONE);
        // 05.25 유미형님. 시간 보여주지 말자.
        mHeaderTimeText.setVisibility(View.GONE);
        // mHeaderTimeText.setVisibility(View.VISIBLE);
    }

    public void releaseToRefresh() {
        mHeaderText.setText(mReleaseLabel);
        mHeaderImage.clearAnimation();
        mHeaderImage.startAnimation(mRotateAnimation);
    }

    public void setPullLabel(String pullLabel) {
        mPullLabel = pullLabel;
    }

    public void refreshing() {
        mHeaderText.setText(mRefreshingLabel);
        mHeaderImage.clearAnimation();
        mHeaderImage.setVisibility(View.GONE);
        mHeaderProgress.setVisibility(View.VISIBLE);
        // 05.25 유미형님. 시간 보여주지 말자.
        // mHeaderTimeText.setVisibility(View.GONE);
        // setTimeText();
    }

    public void setRefreshingLabel(String refreshingLabel) {
        mRefreshingLabel = refreshingLabel;
    }

    public void setReleaseLabel(String releaseLabel) {
        mReleaseLabel = releaseLabel;
    }

    public void pullToRefresh() {
        if (mPullLabel != null) {
            mHeaderText.setText(mPullLabel);
            mHeaderText.setVisibility(View.VISIBLE);
        } else {
            mHeaderText.setVisibility(View.GONE);
        }
        // 05.25 유미형님. 시간 보여주지 말자.
        mHeaderTimeText.setVisibility(View.GONE);
        // mHeaderTimeText.setVisibility(View.VISIBLE);
        mHeaderImage.clearAnimation();
        mHeaderImage.startAnimation(mResetRotateAnimation);
    }

    public void setTextColor(int color) {
        mHeaderText.setTextColor(color);
    }

    public void setTimeText() {
        mHeaderTimeText.setText(new Date(System.currentTimeMillis()).toLocaleString());
    }

}
