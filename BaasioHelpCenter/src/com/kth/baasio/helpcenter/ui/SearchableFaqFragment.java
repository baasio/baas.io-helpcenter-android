
package com.kth.baasio.helpcenter.ui;

import static com.kth.common.utils.LogUtils.LOGV;
import static com.kth.common.utils.LogUtils.makeLogTag;

import com.actionbarsherlock.app.SherlockFragment;
import com.kth.baasio.callback.BaasioCallback;
import com.kth.baasio.exception.BaasioException;
import com.kth.baasio.help.BaasioHelp;
import com.kth.baasio.help.data.Faq;
import com.kth.baasio.help.data.FaqCategory;
import com.kth.baasio.helpcenter.R;
import com.kth.baasio.helpcenter.utils.EtcUtils;
import com.kth.baasio.helpcenter.utils.actionmodecompat.ActionMode;
import com.kth.baasio.helpcenter.utils.actionmodecompat.ActionMode.Callback;
import com.kth.baasio.utils.ObjectUtils;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SearchableFaqFragment extends SherlockFragment implements Callback {

    private static final String TAG = makeLogTag(SearchableFaqFragment.class);

    private ViewGroup mRootView;

    private ListView mList;

    private TextView mEmptyList;

    private EntityListAdapter mListAdapter;

    private SparseArray<String> mCategoryDirectory;

    private List<Faq> mFaqs;

    private ActionMode mActionMode;

    private View mLongClickedView;

    private Integer mLongClickedPosition;

    public SearchableFaqFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mListAdapter = new EntityListAdapter(getActivity());

        mFaqs = new ArrayList<Faq>();
        mCategoryDirectory = new SparseArray<String>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setRetainInstance(true);

        mRootView = (ViewGroup)inflater.inflate(R.layout.fragment_helpcenter_list, null);

        mEmptyList = (TextView)mRootView.findViewById(R.id.textEmptyList);
        mEmptyList.setText(getString(R.string.empty_search_faq_list));

        mList = (ListView)mRootView.findViewById(R.id.list);
        mList.setAdapter(mListAdapter);

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Faq help = mFaqs.get(position - mList.getHeaderViewsCount());
                String title = mCategoryDirectory.get(Integer.valueOf(help.getClassificationId()));

                Intent intent = new Intent(getActivity(), FaqDetailActivity.class);
                intent.putExtra(Intent.EXTRA_TITLE, title);
                intent.putExtra(FaqDetailFragment.FAQ_DETAIL, help.toString());
                startActivity(intent);
            }
        });
        String query = getActivity().getIntent().getStringExtra(SearchManager.QUERY);

        getSherlockActivity().getSupportActionBar().setTitle(
                getResources().getString(R.string.title_search_faqs, query));

        getEntities(query);
        return mRootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void getEntities(String query) {

        BaasioHelp.searchHelpsInBackground(query, new BaasioCallback<List<FaqCategory>>() {

            @Override
            public void onException(BaasioException e) {
                Toast.makeText(getActivity(), getString(R.string.error_fail_search_faqs),
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(List<FaqCategory> response) {

                if (response != null) {
                    mFaqs.clear();
                    mCategoryDirectory.clear();

                    List<FaqCategory> faqcategories = response;
                    for (FaqCategory faqCategory : faqcategories) {
                        mCategoryDirectory.put(faqCategory.getId(), faqCategory.getName());
                        mFaqs.addAll(faqCategory.getFaqs());
                    }

                    mListAdapter.notifyDataSetChanged();

                    if (mFaqs.isEmpty()) {
                        mEmptyList.setVisibility(View.VISIBLE);
                    } else {
                        mEmptyList.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    public class EntityViewHolder {
        public TextView mTextCategory;

        public TextView mTextQuestion;
    }

    private class EntityListAdapter extends BaseAdapter {
        private Context mContext;

        private LayoutInflater mInflater;

        public EntityListAdapter(Context context) {
            super();

            mContext = context;

            mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            if (!ObjectUtils.isEmpty(mFaqs)) {
                if (mFaqs.size() > 0) {
                    return mFaqs.size();
                }
            }
            return 0;
        }

        @Override
        public Faq getItem(int position) {
            return mFaqs.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        /*
         * (non-Javadoc)
         * @see android.widget.Adapter#getView(int, android.view.View,
         * android.view.ViewGroup)
         */
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            EntityViewHolder view = null;

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.listview_item_search_faq, parent, false);

                view = new EntityViewHolder();

                view.mTextCategory = (TextView)convertView.findViewById(R.id.textCategory);
                view.mTextQuestion = (TextView)convertView.findViewById(R.id.textName);

                if (view != null) {
                    convertView.setTag(view);
                }
            } else {
                view = (EntityViewHolder)convertView.getTag();
            }

            Faq faq = mFaqs.get(position);

            if (!ObjectUtils.isEmpty(faq)) {
                String title = mCategoryDirectory.get(Integer.valueOf(faq.getClassificationId()));
                view.mTextCategory.setText("<" + title + ">");

                view.mTextQuestion.setText(faq.getTitle());
            }
            return convertView;
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

        LOGV(TAG,
                "onActionItemClicked: position=" + mLongClickedPosition + " title="
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

}
