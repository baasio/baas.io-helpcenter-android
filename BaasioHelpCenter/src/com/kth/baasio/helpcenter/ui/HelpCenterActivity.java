
package com.kth.baasio.helpcenter.ui;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.kth.baasio.help.data.Question;
import com.kth.baasio.helpcenter.BaseActivity;
import com.kth.baasio.helpcenter.R;
import com.kth.baasio.helpcenter.ui.SendQuestionFragment.OnQuestionSentListener;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

public class HelpCenterActivity extends BaseActivity implements OnPageChangeListener, TabListener,
        OnQuestionSentListener {
    private ViewPager mViewPager;

    private FaqFragment mFaqFragment;

    private SendQuestionFragment mSendQuestionFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mViewPager = (ViewPager)findViewById(R.id.pager);
        if (mViewPager != null) {
            // Phone setup
            mViewPager.setAdapter(new HelpPagerAdapter(getSupportFragmentManager()));
            mViewPager.setOnPageChangeListener(this);
            mViewPager.setPageMarginDrawable(R.drawable.grey_border_inset_lr);
            mViewPager.setPageMargin(getResources()
                    .getDimensionPixelSize(R.dimen.page_margin_width));

            final ActionBar actionBar = getSupportActionBar();
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
            actionBar.addTab(actionBar.newTab().setText(R.string.title_help).setTabListener(this));
            actionBar.addTab(actionBar.newTab().setText(R.string.title_send_question)
                    .setTabListener(this));

        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getSupportMenuInflater().inflate(R.menu.activity_helpcenter, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_faq_search) {
            startSearch(null, false, null, false);
        }

        return super.onOptionsItemSelected(item);
    }

    private class HelpPagerAdapter extends FragmentPagerAdapter {
        public HelpPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    mFaqFragment = new FaqFragment();
                    return mFaqFragment;

                case 1:
                    mSendQuestionFragment = new SendQuestionFragment();
                    return mSendQuestionFragment;

            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    /*
     * (non-Javadoc)
     * @see android.support.v4.view.ViewPager.OnPageChangeListener#
     * onPageScrollStateChanged(int)
     */
    @Override
    public void onPageScrollStateChanged(int arg0) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see
     * android.support.v4.view.ViewPager.OnPageChangeListener#onPageScrolled
     * (int, float, int)
     */
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see
     * android.support.v4.view.ViewPager.OnPageChangeListener#onPageSelected
     * (int)
     */
    @Override
    public void onPageSelected(int position) {
        getSupportActionBar().setSelectedNavigationItem(position);
    }

    /*
     * (non-Javadoc)
     * @see com.actionbarsherlock.app.ActionBar.TabListener#onTabSelected(com.
     * actionbarsherlock.app.ActionBar.Tab,
     * android.support.v4.app.FragmentTransaction)
     */
    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    /*
     * (non-Javadoc)
     * @see com.actionbarsherlock.app.ActionBar.TabListener#onTabUnselected(com.
     * actionbarsherlock.app.ActionBar.Tab,
     * android.support.v4.app.FragmentTransaction)
     */
    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see com.actionbarsherlock.app.ActionBar.TabListener#onTabReselected(com.
     * actionbarsherlock.app.ActionBar.Tab,
     * android.support.v4.app.FragmentTransaction)
     */
    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see
     * com.kth.baasio.baassample.ui.help.SendQuestionFragment.OnQuestionSentListener
     * #OnQuestionSent(com.kth.baasio.help.data.Question)
     */
    @Override
    public void OnQuestionSent(Question question) {
        mViewPager.setCurrentItem(0);
    }
}
