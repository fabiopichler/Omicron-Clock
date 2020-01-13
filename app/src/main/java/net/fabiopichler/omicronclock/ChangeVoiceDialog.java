package net.fabiopichler.omicronclock;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class ChangeVoiceDialog extends DialogFragment
        implements RadioGroup.OnCheckedChangeListener,
        View.OnClickListener {

    public interface ChangedEventListener {
        void onVoiceChanged(String index);
    }

    private ChangedEventListener mChangedEventListener;
    private Context mContext;
    private String mVoicePath;

    ChangeVoiceDialog(Context context, String voicePath) {
        mContext = context;
        mVoicePath = voicePath;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_change_voice, null);

        RadioGroup voiceRadioGroup = view.findViewById(R.id.voiceRadioGroup);

        if (mVoicePath.equals("01"))
            voiceRadioGroup.check(R.id.radio_01);
        else if (mVoicePath.equals("02"))
            voiceRadioGroup.check(R.id.radio_02);

        voiceRadioGroup.setOnCheckedChangeListener(this);
        view.findViewById(R.id.okButton).setOnClickListener(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(view);

        return builder.create();
    }

    public void setChangedEventListener(ChangedEventListener evt) {
        mChangedEventListener = evt;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.okButton)
            dismiss();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch(checkedId) {
            case R.id.radio_01:
                mChangedEventListener.onVoiceChanged("01");
                break;

            case R.id.radio_02:
                mChangedEventListener.onVoiceChanged("02");
                break;
        }
    }
}
