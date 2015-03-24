package ru.iate.cpi.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import ru.iate.cpi.R;

/**
 * Created by sanea on 24.03.15.
 */
public class Settings extends Activity {
    private DatePicker workingPeriodPicker;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        initComponents();
    }

    private void initComponents(){
        workingPeriodPicker = (DatePicker)findViewById(R.id.datePicker_workingPeriod);

        //disable writing inside a date picker using keyboard
        workingPeriodPicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
    }
}