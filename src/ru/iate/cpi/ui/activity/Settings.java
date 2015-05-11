package ru.iate.cpi.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import de.greenrobot.event.EventBus;
import ru.iate.cpi.R;
import ru.iate.cpi.db.table.Region;
import ru.iate.cpi.event.GetRegionsEvent;
import ru.iate.cpi.event.RegionsSourceEvent;
import ru.iate.cpi.service.CpiService;
import ru.iate.cpi.ui.LogTags;
import ru.iate.cpi.ui.containers.SpinnerElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sanea on 24.03.15.
 */
public class Settings extends Activity {
    private DatePicker workingPeriodPicker;
    private Spinner spinnerRegionTitle, spinnerRegionCode;
    private List<Region> Regions = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        EventBus.getDefault().register(this);

        if(Regions == null)
            EventBus.getDefault().post(new GetRegionsEvent());

        initComponents();
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    private void initComponents(){
        workingPeriodPicker = (DatePicker)findViewById(R.id.datePicker_workingPeriod);
        spinnerRegionCode = (Spinner)findViewById(R.id.spinner_regionCode);
        spinnerRegionTitle = (Spinner)findViewById(R.id.spinner_regionTitle);

        //disable writing inside a date picker using keyboard
        workingPeriodPicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
    }

    public void onEventMainThread(RegionsSourceEvent event){
        Log.d(LogTags.ERROR_PREFIX, "CpiService_onEventBackgroundThread - GetRegionsEvent" + event.Regions.size());

        List<SpinnerElement> codeList = new ArrayList<SpinnerElement>();
        List<SpinnerElement> titleList = new ArrayList<SpinnerElement>();

        for (int i = 0; i < event.Regions.size(); i++){
            codeList.add(new SpinnerElement(event.Regions.get(i).GetId(), event.Regions.get(i).GetCode()));
            titleList.add(new SpinnerElement(event.Regions.get(i).GetId(), event.Regions.get(i).GetTitle()));
        }

        setSpinnerAdapter(codeList, spinnerRegionCode);
        setSpinnerAdapter(titleList, spinnerRegionTitle);
    }

    private void setSpinnerAdapter(List<SpinnerElement> elements, Spinner spinner){
        ArrayAdapter<SpinnerElement> dataAdapter = new ArrayAdapter<SpinnerElement>(this,
                android.R.layout.simple_spinner_item, elements);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }
}