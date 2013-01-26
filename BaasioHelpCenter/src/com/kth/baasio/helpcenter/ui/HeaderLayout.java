
package com.kth.baasio.helpcenter.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class HeaderLayout extends LinearLayout {

    SizeChangedListener mListener = null;

    public interface SizeChangedListener {
        void onSizeChanged(int w, int h, int oldw, int oldh);
    }

    public void setOnSizeChangedListener(SizeChangedListener listener) {
        mListener = listener;
    }

    public HeaderLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    public HeaderLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public HeaderLayout(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // TODO Auto-generated method stub
        super.onSizeChanged(w, h, oldw, oldh);

        if (mListener != null) {
            mListener.onSizeChanged(w, h, oldw, oldh);
        }
    }
}
