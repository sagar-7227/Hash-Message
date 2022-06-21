package com.meicode.hashmessage.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.meicode.hashmessage.R;
import com.meicode.hashmessage.databinding.FragmentPowBinding;
import com.meicode.hashmessage.managers.SharedPreferenceManager;


public class PowFragment extends DialogFragment implements View.OnClickListener {

    private FragmentPowBinding viewBinding;
    private Context mcontext;
    private SharedPreferenceManager prefs;


    public PowFragment() {
        // Required empty public constructor
    }  // PowFragment

    public static PowFragment newInstance() {

        return new PowFragment();
    } // PowFragment newInstance

    @Override
    public void onAttach(@NonNull Context context) {

        super.onAttach(context);
        mcontext = context.getApplicationContext();
    } // onAttach

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }                // onCreateView

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        prefs = new SharedPreferenceManager(mcontext);
        viewBinding.edtSetPow.setText(String.valueOf(prefs.getPowValue()));
        viewBinding.btnClose.setOnClickListener(this);
        viewBinding.btnContinue.setOnClickListener(this);
    } // onViewCreated

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        }
        return dialog;

    }  // onCreateDialog

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_close:
                dismiss();
                break;   // case 1

            case R.id.btn_continue:
                if (viewBinding.edtSetPow.getText() != null) {
                    String pow = viewBinding.edtSetPow.getText().toString();
                    prefs.setPowValue(Integer.parseInt(pow));

                    if (getActivity() != null) {
                        Intent intent = mcontext.getPackageManager().getLaunchIntentForPackage(mcontext.getPackageName());
                        startActivity(intent);
                        getActivity().finish();
                    } else {
                        dismiss();
                    }

                } // if
                break;
        } // switch
    } // on click


        @Override
        public void onDetach(){
            super.onDetach();
            viewBinding= null;
            mcontext=null;
        } // onDetach

    }  // PowFragment
