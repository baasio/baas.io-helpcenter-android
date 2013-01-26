
package com.kth.baasio.helpcenter.ui;

import com.actionbarsherlock.app.SherlockFragment;
import com.kth.baasio.callback.BaasioCallback;
import com.kth.baasio.exception.BaasioException;
import com.kth.baasio.help.BaasioHelp;
import com.kth.baasio.help.data.Faq;
import com.kth.baasio.helpcenter.R;
import com.kth.baasio.utils.JsonUtils;
import com.kth.baasio.utils.ObjectUtils;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class FaqDetailFragment extends SherlockFragment {

    public static final String FAQ_DETAIL = "faq_detail";

    private ViewGroup mRootView;

    private TextView mTextQuestion;

    private TextView mTextAnswer;

    private Faq mFaq;

    public FaqDetailFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setRetainInstance(true);

        mRootView = (ViewGroup)inflater.inflate(R.layout.fragment_faqdetail, null);
        mTextQuestion = (TextView)mRootView.findViewById(R.id.textQuestion);
        mTextAnswer = (TextView)mRootView.findViewById(R.id.textAnswer);

        if (getActivity().getIntent() != null) {
            String faq = getActivity().getIntent().getStringExtra(FAQ_DETAIL);
            if (!ObjectUtils.isEmpty(faq)) {
                mFaq = JsonUtils.parse(faq, Faq.class);

                refreshView();

                getEntities(mFaq.getUuid());
            }
        }

        return mRootView;
    }

    private void refreshView() {
        if (!ObjectUtils.isEmpty(mFaq.getTitle())) {
            mTextQuestion.setText(mFaq.getTitle());
        }

        if (!ObjectUtils.isEmpty(mFaq.getContent())) {
            String result = mFaq.getContent().replaceAll("[\n]{2,}", "\n\n");
            mTextAnswer.setText(result);
        }
    }

    private void getEntities(String uuid) {
        BaasioHelp.getHelpDetailInBackground(uuid, new BaasioCallback<Faq>() {

            @Override
            public void onException(BaasioException e) {
                Toast.makeText(getActivity(), getString(R.string.error_fail_get_faqdetail),
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(Faq response) {
                if (response != null) {
                    mFaq = response;
                }

                refreshView();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

}
