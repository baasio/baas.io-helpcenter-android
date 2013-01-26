
package com.kth.baasio.helpcenter.ui;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.MenuItem;
import com.kth.baasio.callback.BaasioCallback;
import com.kth.baasio.exception.BaasioException;
import com.kth.baasio.help.BaasioHelp;
import com.kth.baasio.help.data.Faq;
import com.kth.baasio.help.data.FaqCategory;
import com.kth.baasio.helpcenter.R;
import com.kth.baasio.helpcenter.ui.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.kth.baasio.helpcenter.ui.pulltorefresh.PullToRefreshExpandableListView;
import com.kth.baasio.helpcenter.utils.EtcUtils;
import com.kth.baasio.helpcenter.utils.actionmodecompat.ActionMode;
import com.kth.baasio.helpcenter.utils.actionmodecompat.ActionMode.Callback;
import com.kth.baasio.utils.ObjectUtils;
import com.kth.common.utils.LogUtils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class FaqFragment extends SherlockFragment implements Callback, OnRefreshListener {

    private static final String TAG = LogUtils.makeLogTag(FaqFragment.class);

    private ViewGroup mRootView;

    private PullToRefreshExpandableListView mPullToRefreshList;

    private ExpandableListView mFaqList;

    private FaqListAdapter mListAdapter;

    private List<FaqCategory> mFaqCategories;

    private ActionMode mActionMode;

    private View mLongClickedView;

    private Integer mLongClickedPosition;

    public FaqFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mListAdapter = new FaqListAdapter(getActivity());

        getEntities(false);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setRetainInstance(true);

        mRootView = (ViewGroup)inflater.inflate(R.layout.fragment_faq, null);

        mPullToRefreshList = (PullToRefreshExpandableListView)mRootView.findViewById(R.id.list);
        mPullToRefreshList.setOnRefreshListener(this);
        mPullToRefreshList.setFooterGone();

        mFaqList = mPullToRefreshList.getRefreshableView();
        mFaqList.setAdapter(mListAdapter);

        mFaqList.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                    int childPosition, long id) {
                FaqCategory category = mFaqCategories.get(groupPosition);
                Faq help = category.getFaqs().get(childPosition);

                Intent intent = new Intent(getActivity(), FaqDetailActivity.class);
                intent.putExtra(Intent.EXTRA_TITLE, category.getName());
                intent.putExtra(FaqDetailFragment.FAQ_DETAIL, help.toString());
                startActivity(intent);
                return false;
            }
        });

        return mRootView;
    }

    private void getEntities(final boolean isRefresh) {

        if (!isRefresh) {
            if (mPullToRefreshList != null) {
                mPullToRefreshList.setRefreshing();
            }
        }

        BaasioHelp.getHelpsInBackground(new BaasioCallback<List<FaqCategory>>() {

            @Override
            public void onException(BaasioException e) {
                new Handler().post(new Runnable() {

                    @Override
                    public void run() {
                        if (mPullToRefreshList != null) {
                            if (mPullToRefreshList.isRefreshing())
                                mPullToRefreshList.onRefreshComplete();
                        }
                    }
                });

                Toast.makeText(getActivity(), getString(R.string.error_fail_get_faqs),
                        Toast.LENGTH_LONG).show();
            }

            @Override
            @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            public void onResponse(List<FaqCategory> response) {
                new Handler().post(new Runnable() {

                    @Override
                    public void run() {
                        if (mPullToRefreshList != null) {
                            if (mPullToRefreshList.isRefreshing())
                                mPullToRefreshList.onRefreshComplete();
                        }
                    }
                });

                if (response != null) {
                    ArrayList<FaqCategory> list = new ArrayList<FaqCategory>();
                    for (int i = 0; i < response.size(); i++) {
                        FaqCategory category = response.get(i);
                        if (category.getFaqs().size() != 0) {
                            list.add(category);
                        }
                    }
                    response = list;
                }

                mFaqCategories = response;

                if (mListAdapter.getGroupCount() > 0) {
                    if (Build.VERSION.SDK_INT < 14) {
                        mFaqList.expandGroup(0);
                    } else {
                        mFaqList.expandGroup(0, true);
                    }
                }

                mListAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu,
            com.actionbarsherlock.view.MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(com.actionbarsherlock.view.Menu menu) {
        // TODO Auto-generated method stub
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    public class GroupViewHolder {
        public TextView mTextName;

        public ImageView mImageGroupOpened;

        public ImageView mImageGroupClosed;
    }

    public class ChildViewHolder {
        public TextView mTextQuestion;
    }

    private class FaqListAdapter extends BaseExpandableListAdapter {
        private Context mContext;

        private LayoutInflater mInflater;

        public FaqListAdapter(Context context) {
            super();

            mContext = context;

            mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        /*
         * (non-Javadoc)
         * @see android.widget.ExpandableListAdapter#getChild(int, int)
         */
        @Override
        public Faq getChild(int groupPosition, int childPosition) {
            return mFaqCategories.get(groupPosition).getFaqs().get(childPosition);
        }

        /*
         * (non-Javadoc)
         * @see android.widget.ExpandableListAdapter#getChildId(int, int)
         */
        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return mFaqCategories.get(groupPosition).getFaqs().get(childPosition).getId();
        }

        /*
         * (non-Javadoc)
         * @see android.widget.ExpandableListAdapter#getChildView(int, int,
         * boolean, android.view.View, android.view.ViewGroup)
         */
        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                View convertView, ViewGroup parent) {
            ChildViewHolder view = null;

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.listview_item_faq, parent, false);

                view = new ChildViewHolder();

                view.mTextQuestion = (TextView)convertView.findViewById(R.id.textName);

                if (view != null) {
                    convertView.setTag(view);
                }
            } else {
                view = (ChildViewHolder)convertView.getTag();
            }

            Faq help = mFaqCategories.get(groupPosition).getFaqs().get(childPosition);
            if (!ObjectUtils.isEmpty(help)) {
                view.mTextQuestion.setText(help.getTitle());
            }

            return convertView;
        }

        /*
         * (non-Javadoc)
         * @see android.widget.ExpandableListAdapter#getChildrenCount(int)
         */
        @Override
        public int getChildrenCount(int groupPosition) {
            if (getGroupCount() > groupPosition) {
                if (!ObjectUtils.isEmpty(mFaqCategories.get(groupPosition).getFaqs())) {
                    return mFaqCategories.get(groupPosition).getFaqs().size();
                }
            }
            return 0;
        }

        /*
         * (non-Javadoc)
         * @see android.widget.ExpandableListAdapter#getGroup(int)
         */
        @Override
        public FaqCategory getGroup(int groupPosition) {
            return mFaqCategories.get(groupPosition);
        }

        /*
         * (non-Javadoc)
         * @see android.widget.ExpandableListAdapter#getGroupCount()
         */
        @Override
        public int getGroupCount() {
            if (!ObjectUtils.isEmpty(mFaqCategories)) {
                return mFaqCategories.size();
            }
            return 0;
        }

        /*
         * (non-Javadoc)
         * @see android.widget.ExpandableListAdapter#getGroupId(int)
         */
        @Override
        public long getGroupId(int groupPosition) {
            return mFaqCategories.get(groupPosition).getId();
        }

        /*
         * (non-Javadoc)
         * @see android.widget.ExpandableListAdapter#getGroupView(int, boolean,
         * android.view.View, android.view.ViewGroup)
         */
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                ViewGroup parent) {
            GroupViewHolder view = null;

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.listview_item_faqcategory, parent, false);

                view = new GroupViewHolder();

                view.mTextName = (TextView)convertView.findViewById(R.id.textName);
                view.mImageGroupClosed = (ImageView)convertView.findViewById(R.id.imageGroupClosed);
                view.mImageGroupOpened = (ImageView)convertView.findViewById(R.id.imageGroupOpened);

                if (view != null) {
                    convertView.setTag(view);
                }
            } else {
                view = (GroupViewHolder)convertView.getTag();
            }

            FaqCategory category = mFaqCategories.get(groupPosition);
            if (!ObjectUtils.isEmpty(category)) {
                view.mTextName.setText(category.getName() + " (" + category.getFaqs().size() + ")");
            }

            if (isExpanded) {
                view.mImageGroupOpened.setVisibility(View.VISIBLE);
                view.mImageGroupClosed.setVisibility(View.GONE);
            } else {
                view.mImageGroupOpened.setVisibility(View.GONE);
                view.mImageGroupClosed.setVisibility(View.VISIBLE);
            }
            return convertView;
        }

        /*
         * (non-Javadoc)
         * @see android.widget.ExpandableListAdapter#hasStableIds()
         */
        @Override
        public boolean hasStableIds() {
            // TODO Auto-generated method stub
            return false;
        }

        /*
         * (non-Javadoc)
         * @see android.widget.ExpandableListAdapter#isChildSelectable(int, int)
         */
        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

    }

    /*
     * (non-Javadoc)
     * @see com.kth.kanu.baassample.utils.actionmodecompat.ActionMode.Callback#
     * onCreateActionMode
     * (com.kth.kanu.baassample.utils.actionmodecompat.ActionMode,
     * android.view.Menu)
     */
    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        if (mLongClickedView == null) {
            return true;
        }

        return true;
    }

    /*
     * (non-Javadoc)
     * @see com.kth.kanu.baassample.utils.actionmodecompat.ActionMode.Callback#
     * onPrepareActionMode
     * (com.kth.kanu.baassample.utils.actionmodecompat.ActionMode,
     * android.view.Menu)
     */
    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     * @see com.kth.kanu.baassample.utils.actionmodecompat.ActionMode.Callback#
     * onActionItemClicked
     * (com.kth.kanu.baassample.utils.actionmodecompat.ActionMode,
     * android.view.MenuItem)
     */
    @Override
    public boolean onActionItemClicked(ActionMode mode, android.view.MenuItem item) {
        boolean handled = false;
        switch (item.getItemId()) {

        }

        LogUtils.LOGV(TAG, "onActionItemClicked: position=" + mLongClickedPosition + " title="
                + item.getTitle());
        mActionMode.finish();
        return handled;
    }

    /*
     * (non-Javadoc)
     * @see com.kth.kanu.baassample.utils.actionmodecompat.ActionMode.Callback#
     * onDestroyActionMode
     * (com.kth.kanu.baassample.utils.actionmodecompat.ActionMode)
     */
    @Override
    public void onDestroyActionMode(ActionMode mode) {
        mActionMode = null;
        if (mLongClickedView != null) {
            EtcUtils.setActivatedCompat(mLongClickedView, false);
            mLongClickedPosition = null;
            mLongClickedView = null;
        }
    }

    /*
     * (non-Javadoc)
     * @see com.kth.baasio.baassample.view.pulltorefresh.PullToRefreshBase.
     * OnRefreshListener#onRefresh()
     */
    @Override
    public void onRefresh() {
        getEntities(true);
    }

    /*
     * (non-Javadoc)
     * @see com.kth.baasio.baassample.view.pulltorefresh.PullToRefreshBase.
     * OnRefreshListener#onUpdate()
     */
    @Override
    public void onUpdate() {
        // TODO Auto-generated method stub

    }

}
