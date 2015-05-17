package ru.iate.cpi.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import de.greenrobot.event.EventBus;
import ru.iate.cpi.R;
import ru.iate.cpi.db.table.Region;
import ru.iate.cpi.event.*;
import ru.iate.cpi.ui.LogTags;
import ru.iate.cpi.ui.containers.SpinnerElement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by sanea on 24.03.15.
 */
public class Settings extends Activity implements AdapterView.OnItemSelectedListener {
    private TextView currentSettings;
    private DatePicker workingPeriodPicker;
    private Spinner spinnerRegionTitle, spinnerRegionCode;
    private List<Region> regions;
    private ru.iate.cpi.db.table.Settings settings;
    private Region settingsRegion;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        EventBus.getDefault().register(this);

        initComponents();
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    private void initComponents(){
        currentSettings = (TextView)findViewById(R.id.textView_currentSettings);
        workingPeriodPicker = (DatePicker)findViewById(R.id.datePicker_workingPeriod);
        spinnerRegionCode = (Spinner)findViewById(R.id.spinner_regionCode);
        spinnerRegionTitle = (Spinner)findViewById(R.id.spinner_regionTitle);

        spinnerRegionCode.setOnItemSelectedListener(this);
        spinnerRegionTitle.setOnItemSelectedListener(this);

        //disable writing inside a date picker using keyboard
        workingPeriodPicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);

        //extract regions
        if(regions == null)
            EventBus.getDefault().post(new GetRegionsEvent());
        else
            initSpinners();

        //extract settings
        if(settings == null)
            EventBus.getDefault().post(new GetSettingsEvent());
        else
            initSettings();
    }

    //Extract regions from DB
    public void onEventMainThread(RegionsSourceEvent event){
        regions = event.Regions;
        initSpinners();
    }

    //Extract settings from DB
    public void onEventMainThread(SettingsSourceEvent event) {
        settings = event.settings;
        settingsRegion = event.region;
        initSettings();
    }

    private void initSpinners(){
        List<SpinnerElement> codeList = new ArrayList<SpinnerElement>();
        List<SpinnerElement> titleList = new ArrayList<SpinnerElement>();

        for (int i = 0; i < regions.size(); i++){
            codeList.add(new SpinnerElement(regions.get(i).GetId(), regions.get(i).GetCode()));
            titleList.add(new SpinnerElement(regions.get(i).GetId(), regions.get(i).GetTitle()));
        }

        setSpinnerAdapter(codeList, spinnerRegionCode);
        setSpinnerAdapter(titleList, spinnerRegionTitle);
    }

    private void initSettings(){

        String result = settings == null || settingsRegion == null ? "Текущие настройки не установлены" :
                String.format("Текущий регион: %s\nТекущий рабочий период: %s", settingsRegion.GetTitle(),
                        String.format("%s.%d", new SimpleDateFormat("dd.MM").format(settings.GetWorkingPeriod()), settings.GetWorkingPeriod().getYear()));
        currentSettings.setText(result);
    }

    public void onSetPeriodClick(View view) {
        Date pickerDate = new Date(workingPeriodPicker.getYear(), workingPeriodPicker.getMonth(), workingPeriodPicker.getDayOfMonth());
        SpinnerElement selectedItem = (SpinnerElement)spinnerRegionCode.getSelectedItem();
        EventBus.getDefault().post(new AddSettingEvent(new ru.iate.cpi.db.table.Settings(selectedItem.Id, pickerDate)));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
        spinnerRegionCode.setSelection(pos);
        spinnerRegionTitle.setSelection(pos);
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    //TODO: need to create new static class
    private void setSpinnerAdapter(List<SpinnerElement> elements, Spinner spinner){
        ArrayAdapter<SpinnerElement> dataAdapter = new ArrayAdapter<SpinnerElement>(this,
                android.R.layout.simple_spinner_item, elements);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }
}