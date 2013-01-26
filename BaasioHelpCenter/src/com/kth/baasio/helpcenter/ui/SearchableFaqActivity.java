/**
 * 0. Project  : XXXX 프로젝트
 *
 * 1. FileName : SearchableActivity.java
 * 2. Package : com.kth.kanu.h3.sessioninfo
 * 3. Comment : 
 * 4. 작성자  : Brad
 * 5. 작성일  : 2012. 8. 31. 오후 3:46:24
 * 6. 변경이력 : 
 *                    이름     : 일자          : 근거자료   : 변경내용
 *                   ------------------------------------------------------
 *                    Brad : 2012. 8. 31. :            : 신규 개발.
 */

package com.kth.baasio.helpcenter.ui;

import com.kth.baasio.helpcenter.SimpleSinglePaneActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * <PRE>
 * 1. ClassName : 
 * 2. FileName  : SearchableActivity.java
 * 3. Package  : com.kth.kanu.h3.sessioninfo
 * 4. Comment  : 
 * 5. 작성자   : Brad
 * 6. 작성일   : 2012. 8. 31. 오후 3:46:24
 * </PRE>
 */
public class SearchableFaqActivity extends SimpleSinglePaneActivity {
    private SearchableFaqFragment mSearchableFaqFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSearchableFaqFragment = (SearchableFaqFragment)getFragment();
    }

    /*
     * (non-Javadoc)
     * @see com.kth.baasio.baassample.ui.SimpleSinglePaneActivity#onCreatePane()
     */
    @Override
    protected Fragment onCreatePane() {
        mSearchableFaqFragment = new SearchableFaqFragment();
        return mSearchableFaqFragment;
    }
}
