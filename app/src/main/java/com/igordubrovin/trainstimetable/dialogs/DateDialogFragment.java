package com.igordubrovin.trainstimetable.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import com.igordubrovin.trainstimetable.R;
import com.igordubrovin.trainstimetable.utils.DateHelper;

import java.util.List;

/**
 * Created by Игорь on 03.03.2017.
 */

public class DateDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    ActionListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        List<Integer> date = DateHelper.getListCurrentDate();
        DatePickerDialog picker = new DatePickerDialog(getActivity(), R.style.StyleDatePicker, this, date.get(2), date.get(1)-1, date.get(0));
        picker.setOnCancelListener(this);
        picker.setTitle("Выбор даты");
        return picker;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        listener.clickPositiveButton(year, month, dayOfMonth);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        listener.cancelWithoutDate();
        super.onCancel(dialog);
    }

    public void setActionListener(ActionListener l){
        listener = l;
    }

    public interface ActionListener{
        void clickPositiveButton(int year, int month, int dayOfMonth);
        void cancelWithoutDate();
    }

}
