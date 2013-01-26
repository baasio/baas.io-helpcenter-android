
package com.kth.baasio.helpcenter.ui;

import com.actionbarsherlock.app.SherlockFragment;
import com.kth.baasio.Baas;
import com.kth.baasio.callback.BaasioCallback;
import com.kth.baasio.entity.user.BaasioUser;
import com.kth.baasio.exception.BaasioException;
import com.kth.baasio.help.BaasioHelp;
import com.kth.baasio.help.data.Question;
import com.kth.baasio.helpcenter.R;
import com.kth.baasio.helpcenter.prefereces.HelpCenterPreferences;
import com.kth.baasio.helpcenter.ui.dialog.DefaultDialogFragment;
import com.kth.baasio.helpcenter.ui.dialog.DefaultDialogFragment.DialogResultListener;
import com.kth.baasio.helpcenter.ui.dialog.DialogUtils;
import com.kth.baasio.helpcenter.utils.EtcUtils;
import com.kth.baasio.utils.ObjectUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class SendQuestionFragment extends SherlockFragment {

    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private ViewGroup mRootView;

    private EditText mEmail;

    private EditText mBody;

    private TextView mCounter;

    private Button mPost;

    private int mMaxLength;

    private OnQuestionSentListener mListener;

    public interface OnQuestionSentListener {
        void OnQuestionSent(Question question);
    }

    public SendQuestionFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setRetainInstance(true);

        mRootView = (ViewGroup)inflater.inflate(R.layout.fragment_send_question, null);

        mEmail = (EditText)mRootView.findViewById(R.id.textEmail);

        BaasioUser current = Baas.io().getSignedInUser();
        String email = HelpCenterPreferences.getHelpDeskEmail(getActivity());

        if (!ObjectUtils.isEmpty(current)) {
            if (!ObjectUtils.isEmpty(current.getEmail())) {
                mEmail.setText(current.getEmail());
            } else {
                if (!ObjectUtils.isEmpty(email)) {
                    mEmail.setText(email);
                }
            }
        } else {
            if (!ObjectUtils.isEmpty(email)) {
                mEmail.setText(email);
            }
        }

        mBody = (EditText)mRootView.findViewById(R.id.textBody);

        mMaxLength = getResources().getInteger(R.integer.question_max_length);

        mBody.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCounter.setText(s.toString().length() + "/" + mMaxLength);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mCounter = (TextView)mRootView.findViewById(R.id.textCounter);
        mCounter.setText(0 + "/" + mMaxLength);

        mPost = (Button)mRootView.findViewById(R.id.buttonPost);
        mPost.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final String email = mEmail.getText().toString().trim();

                Pattern pattern = Pattern.compile(EMAIL_PATTERN);
                if (!pattern.matcher(email).matches()) {
                    Toast.makeText(getActivity(),
                            getActivity().getResources().getString(R.string.error_invalid_email),
                            Toast.LENGTH_LONG).show();
                    return;
                }

                final String body = mBody.getText().toString().trim();

                if (ObjectUtils.isEmpty(body)) {
                    Toast.makeText(getActivity(),
                            getActivity().getResources().getString(R.string.error_empty_question),
                            Toast.LENGTH_LONG).show();
                    return;
                }

                DefaultDialogFragment dialog = DialogUtils.showDefaultDialog(getActivity(),
                        "confirm_send", getString(R.string.label_question_post_title),
                        getString(R.string.label_question_post_body), true);
                dialog.setDialogResultListener(new DialogResultListener() {

                    @Override
                    public boolean onPositiveButtonSelected(String tag) {
                        new Handler().post(new Runnable() {

                            @Override
                            public void run() {
                                DialogUtils.showProgressDialog(getActivity(), "sending_progress",
                                        getString(R.string.label_question_posting),
                                        ProgressDialog.STYLE_SPINNER);
                            }
                        });

                        BaasioHelp.sendQuestionInBackground(getActivity(), email, body,
                                new BaasioCallback<Question>() {

                                    @Override
                                    public void onException(BaasioException e) {
                                        DialogUtils.dissmissProgressDialog(getActivity(),
                                                "sending_progress");

                                        // FIXME: 인증 실패일 경우 어떻게 처리할지 대응필요
                                        DefaultDialogFragment dialog = DialogUtils
                                                .showDefaultDialog(
                                                        getActivity(),
                                                        "question_sent",
                                                        getString(R.string.label_question_post_fail_title),
                                                        getString(R.string.label_question_post_fail_body),
                                                        false);

                                        dialog.setDialogResultListener(new DialogResultListener() {

                                            @Override
                                            public boolean onPositiveButtonSelected(String tag) {
                                                return false;
                                            }

                                            @Override
                                            public boolean onNegativeButtonSelected(String tag) {
                                                return false;
                                            }

                                            @Override
                                            public boolean onDismiss(String tag,
                                                    DialogInterface dialog) {
                                                return false;
                                            }
                                        });
                                    }

                                    @Override
                                    public void onResponse(Question response) {
                                        DialogUtils.dissmissProgressDialog(getActivity(),
                                                "sending_progress");

                                        if (!ObjectUtils.isEmpty(response)) {
                                            onSent(response);
                                        }
                                    }
                                });
                        return false;
                    }

                    @Override
                    public boolean onNegativeButtonSelected(String tag) {
                        // TODO Auto-generated method stub
                        return false;
                    }

                    @Override
                    public boolean onDismiss(String tag, DialogInterface dialog) {
                        // TODO Auto-generated method stub
                        return false;
                    }
                });

            }
        });

        if (ObjectUtils.isEmpty(Baas.io().getSignedInUser())) {
            Toast.makeText(getActivity(), getString(R.string.error_need_login), Toast.LENGTH_LONG)
                    .show();

            if (mPost != null) {
                mPost.setEnabled(false);
            }
        }
        return mRootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnQuestionSentListener)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString());
        }

    }

    private void onSent(final Question question) {
        if (ObjectUtils.isEmpty(question)) {
            return;
        }

        String dateString = "";

        try {
            String acceptedNumber = question.getAcceptedNumber();
            SimpleDateFormat formatter = new SimpleDateFormat("yyMMddkkmmssSSS");
            Date date = formatter.parse(acceptedNumber);

            dateString = EtcUtils.getSimpleDateString(date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String result = getString(R.string.msg_send_question_success, dateString,
                question.getEmail());

        DefaultDialogFragment dialog = DialogUtils.showDefaultDialog(getActivity(),
                "question_sent", "접수 완료", result, false);
        dialog.setDialogResultListener(new DialogResultListener() {

            @Override
            public boolean onPositiveButtonSelected(String tag) {
                return false;
            }

            @Override
            public boolean onNegativeButtonSelected(String tag) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean onDismiss(String tag, DialogInterface dialog) {
                mBody.setText("");

                HelpCenterPreferences.setHelpDeskEmail(getActivity(), question.getEmail());

                mListener.OnQuestionSent(question);
                return false;
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

}
