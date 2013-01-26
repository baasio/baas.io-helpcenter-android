
package com.kth.baasio.helpcenter.ui.pulltorefresh;

import com.kth.baasio.helpcenter.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

public class PullToRefreshExpandableListView extends
        PullToRefreshAdapterViewBase<ExpandableListView> {

    private LoadingLayout mHeaderLoadingView;

    private LoadingLayout mFooterLoadingView;

    private FrameLayout mLvFooterLoadingFrame;

    private boolean mAddedLvFooter = false;

    public class InternalListView extends ExpandableListView implements EmptyViewMethodAccessor {
        public InternalListView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        public void setAdapter(ListAdapter adapter) {
            // Add the Footer View at the last possible moment
            if (!mAddedLvFooter && null != mLvFooterLoadingFrame) {
                addFooterView(mLvFooterLoadingFrame, null, false);
                mAddedLvFooter = true;
            }

            super.setAdapter(adapter);
        }

        @Override
        public void setEmptyView(View emptyView) {
            PullToRefreshExpandableListView.this.setEmptyView(emptyView);
        }

        @Override
        public void setEmptyViewInternal(View emptyView) {
            super.setEmptyView(emptyView);
        }

        public ContextMenuInfo getContextMenuInfo() {
            return super.getContextMenuInfo();
        }

        @Override
        public boolean performItemClick(View view, int position, long id) {
            return super.performItemClick(view, position, id);
        }

    }

    public PullToRefreshExpandableListView(Context context) {
        super(context);
        setDisableScrollingWhileRefreshing(false);
    }

    public PullToRefreshExpandableListView(Context context, int mode) {
        super(context, mode);
        setDisableScrollingWhileRefreshing(false);
    }

    public PullToRefreshExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDisableScrollingWhileRefreshing(false);
    }

    @Override
    public ContextMenuInfo getContextMenuInfo() {
        return ((InternalListView)getRefreshableView()).getContextMenuInfo();
    }

    public void setReleaseLabel(String releaseLabel) {
        super.setReleaseLabel(releaseLabel);

        if (null != mHeaderLoadingView) {
            mHeaderLoadingView.setReleaseLabel(releaseLabel);
        }
        if (null != mFooterLoadingView) {
            mFooterLoadingView.setReleaseLabel(releaseLabel);
        }
    }

    // 05.25 유미형님. 시간 보여주지 말자.
    public void setTimeLable() {
        super.setTimeLabel();

        // if (null != mHeaderLoadingView) {
        // mHeaderLoadingView.setTimeText();
        // }
        // if (null != mFooterLoadingView) {
        // mFooterLoadingView.setTimeText();
        // }
    }

    public void setPullLabel(String pullLabel) {
        super.setPullLabel(pullLabel);

        if (null != mHeaderLoadingView) {
            mHeaderLoadingView.setPullLabel(pullLabel);
        }
        if (null != mFooterLoadingView) {
            mFooterLoadingView.setPullLabel(pullLabel);
        }
    }

    public void setRefreshingLabel(String refreshingLabel) {
        super.setRefreshingLabel(refreshingLabel);

        if (null != mHeaderLoadingView) {
            mHeaderLoadingView.setRefreshingLabel(refreshingLabel);
        }
        if (null != mFooterLoadingView) {
            mFooterLoadingView.setRefreshingLabel(refreshingLabel);
        }
    }

    @Override
    protected final ExpandableListView createRefreshableView(Context context, AttributeSet attrs) {
        ExpandableListView lv = new InternalListView(context, attrs);

        final int mode = getMode();

        // Loading View Strings
        String pullLabel = context.getString(R.string.pull_to_refresh_pull_label);
        String refreshingLabel = null;// context.getString(R.string.pull_to_refresh_refreshing_label);
        String releaseLabel = context.getString(R.string.pull_to_refresh_release_label);

        // Get Styles from attrs
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PullToRefresh);

        // Add Loading Views
        if (mode == MODE_PULL_DOWN_TO_REFRESH || mode == MODE_BOTH) {
            FrameLayout frame = new FrameLayout(context);
            mHeaderLoadingView = new LoadingLayout(context, MODE_PULL_DOWN_TO_REFRESH,
                    releaseLabel, pullLabel, refreshingLabel, a);
            frame.addView(mHeaderLoadingView, FrameLayout.LayoutParams.FILL_PARENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT);
            mHeaderLoadingView.setVisibility(View.GONE);
            lv.addHeaderView(frame, null, false);

            mLvFooterLoadingFrame = new FrameLayout(context);
            mFooterLoadingView = new LoadingLayoutFooter(context);
            mLvFooterLoadingFrame.addView(mFooterLoadingView, FrameLayout.LayoutParams.FILL_PARENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT);
            mFooterLoadingView.setVisibility(View.GONE);

        }
        if (mode == MODE_PULL_UP_TO_REFRESH || mode == MODE_BOTH) {
            mLvFooterLoadingFrame = new FrameLayout(context);
            mFooterLoadingView = new LoadingLayout(context, MODE_PULL_UP_TO_REFRESH, releaseLabel,
                    pullLabel, refreshingLabel, a);
            mLvFooterLoadingFrame.addView(mFooterLoadingView, FrameLayout.LayoutParams.FILL_PARENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT);
            mFooterLoadingView.setVisibility(View.GONE);
        }

        a.recycle();

        // Set it to this so it can be used in ListActivity/ListFragment
        lv.setId(android.R.id.list);
        return lv;
    }

    @Override
    protected void setRefreshingInternal(boolean doScroll) {

        // If we're not showing the Refreshing view, or the list is empty, then
        // the header/footer views won't show so we use the
        // normal method
        ListAdapter adapter = mRefreshableView.getAdapter();
        if (!getShowViewWhileRefreshing() || null == adapter || adapter.isEmpty()) {
            super.setRefreshingInternal(doScroll);
            return;
        }

        super.setRefreshingInternal(false);

        final LoadingLayout originalLoadingLayout, listViewLoadingLayout;
        final int selection, scrollToY;

        switch (getCurrentMode()) {
            case MODE_PULL_UP_TO_REFRESH:
                originalLoadingLayout = getFooterLayout();
                listViewLoadingLayout = mFooterLoadingView;
                selection = mRefreshableView.getCount() - 1;
                scrollToY = getScrollY() - getHeaderHeight();
                break;
            case MODE_PULL_DOWN_TO_REFRESH:
            default:
                originalLoadingLayout = getHeaderLayout();
                listViewLoadingLayout = mHeaderLoadingView;
                selection = 0;
                scrollToY = getScrollY() + getHeaderHeight();
                break;
        }

        if (doScroll) {
            // We scroll slightly so that the ListView's header/footer is at the
            // same Y position as our normal header/footer
            setHeaderScroll(scrollToY);
        }

        // Hide our original Loading View
        originalLoadingLayout.setVisibility(View.INVISIBLE);

        // Show the ListView Loading View and set it to refresh
        listViewLoadingLayout.setVisibility(View.VISIBLE);
        listViewLoadingLayout.refreshing();

        if (doScroll) {
            // Make sure the ListView is scrolled to show the loading
            // header/footer
            mRefreshableView.setSelection(selection);

            // Smooth scroll as normal
            smoothScrollTo(0);
        }
    }

    @Override
    protected void resetHeader() {

        // If we're not showing the Refreshing view, or the list is empty, then
        // the header/footer views won't show so we use the
        // normal method
        ListAdapter adapter = mRefreshableView.getAdapter();
        if (!getShowViewWhileRefreshing() || null == adapter || adapter.isEmpty()) {
            super.resetHeader();
            return;
        }

        LoadingLayout originalLoadingLayout;
        LoadingLayout listViewLoadingLayout;

        // int scrollToHeight = getHeaderHeight();
        int selection;

        switch (getCurrentMode()) {
            case MODE_PULL_UP_TO_REFRESH:
                originalLoadingLayout = getFooterLayout();
                listViewLoadingLayout = mFooterLoadingView;

                selection = mRefreshableView.getCount() - 1;
                break;
            case MODE_PULL_DOWN_TO_REFRESH:
            default:
                originalLoadingLayout = getHeaderLayout();
                listViewLoadingLayout = mHeaderLoadingView;
                // scrollToHeight *= -1;
                selection = 0;
                break;
        }

        originalLoadingLayout.setVisibility(View.VISIBLE);

        mRefreshableView.dispatchTouchEvent(MotionEvent.obtain(0, 0, MotionEvent.ACTION_CANCEL,
                100.0f, 100.0f, 0));
        if (getState() != MANUAL_REFRESHING && mRefreshableView.getFirstVisiblePosition() == 0) {
            mRefreshableView.setSelection(selection);
            setHeaderScroll(-mRefreshableView.getChildAt(0).getBottom());
        } else if (mRefreshableView.getFirstVisiblePosition() != 0) {
            mRefreshableView.setSelection(selection);
        }

        // Hide the ListView Header/Footer
        listViewLoadingLayout.setVisibility(View.GONE);

        super.resetHeader();
    }

    protected int getNumberInternalHeaderViews() {
        int count = 0;
        if (mRefreshableView instanceof ListView) {
            count = mRefreshableView.getHeaderViewsCount();
        }
        return null != mHeaderLoadingView ? count++ : 0;
    }

    protected int getNumberInternalFooterViews() {
        int count = 0;
        if (mRefreshableView instanceof ListView) {
            count = mRefreshableView.getFooterViewsCount();
        }
        return null != mFooterLoadingView ? count++ : 0;
    }

    private boolean mHasMoreData = false;

    private boolean mIsLoadingData = false;

    public void setHasMoreData(boolean hasMoreData) {
        mHasMoreData = hasMoreData;
    }

    public boolean hasMoreData() {
        return mHasMoreData;
    }

    public void setIsLoading(boolean isLoading) {
        mIsLoadingData = isLoading;
    }

    public boolean getIsLoading() {
        return mIsLoadingData;
    }

    int mFirstVisibleItem = -1;

    int mCurrentTopChild = -1;

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
            int totalItemCount) {
        int currentChildTop = 0;
        if (visibleItemCount > 0) {
            currentChildTop = view.getChildAt(0).getTop();
            if (mFirstVisibleItem == 0 && currentChildTop == 0) {
                if (mOnFirstItemVisibleListener != null)
                    mOnFirstItemVisibleListener.onFirstItemVisible();
            } else {
                if (mOnDirectionListener != null) {
                    if (mFirstVisibleItem > firstVisibleItem) {
                        mOnDirectionListener.onUp();

                    } else if (mFirstVisibleItem == firstVisibleItem) {
                        if (mCurrentTopChild > currentChildTop
                                && mCurrentTopChild - currentChildTop > 5) {
                            // before data
                            mOnDirectionListener.onDown();
                        } else if (mCurrentTopChild < currentChildTop
                                && currentChildTop - mCurrentTopChild > 5) {
                            // loading data
                            mOnDirectionListener.onUp();
                        }
                    } else if (mFirstVisibleItem < firstVisibleItem) {
                        mOnDirectionListener.onDown();
                    }
                }
            }
        }

        int lastInScreen = firstVisibleItem + visibleItemCount;
        if (visibleItemCount > 0 && (lastInScreen == totalItemCount)) {
            // only process first event
            if (mHasMoreData && !mIsLoadingData) {
                mIsLoadingData = true;
                mOnRefreshListener.onUpdate();
                if (null != mFooterLoadingView) {
                    mFooterLoadingView.setVisibility(View.VISIBLE);
                }
            } else if (!mHasMoreData && !mIsLoadingData) {
                if (mOnLastItemVisibleListener != null) {
                    mOnLastItemVisibleListener.onLastItemVisible();
                }
            }
        }

        mFirstVisibleItem = firstVisibleItem;
        mCurrentTopChild = currentChildTop;
    }

    public void setFooterGone() {
        if (null != mFooterLoadingView) {
            mFooterLoadingView.setVisibility(View.GONE);
            mFooterLoadingView.invalidate();
        }
    }

    public void setFooterVisible() {
        if (null != mFooterLoadingView) {
            mFooterLoadingView.setVisibility(View.VISIBLE);
            mFooterLoadingView.invalidate();
        }
    }
}
