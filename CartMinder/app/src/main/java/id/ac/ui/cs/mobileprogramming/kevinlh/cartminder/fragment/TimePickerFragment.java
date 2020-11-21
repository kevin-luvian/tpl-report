package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class TimePickerFragment extends DialogFragment {
    private TimePickerDialog.OnTimeSetListener listener;
    private int hour;
    private int minute;

    public TimePickerFragment(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public TimePickerDialog.OnTimeSetListener getListener() {
        return listener;
    }

    public void setListener(TimePickerDialog.OnTimeSetListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//        return super.onCreateDialog(savedInstanceState);
        return new TimePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT, listener, hour, minute, true);
    }
}
