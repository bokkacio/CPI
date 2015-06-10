package ru.iate.cpi.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import de.greenrobot.event.EventBus;
import ru.iate.cpi.R;
import ru.iate.cpi.db.table.Region;
import ru.iate.cpi.event.*;
import ru.iate.cpi.ui.OptionMenuCodes;
import ru.iate.cpi.ui.containers.SpinnerElement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, OptionMenuCodes.DATA_INPUT_ACTIVITY, 0, OptionMenuCodes.DATA_INPUT_ACTIVITY_STR);
        menu.add(0, OptionMenuCodes.CPI_CALCULATION, 1, OptionMenuCodes.CPI_CALCULATION_STR);
        menu.add(0, OptionMenuCodes.EXIT, 2, OptionMenuCodes.EXIT_STR);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case OptionMenuCodes.DATA_INPUT_ACTIVITY:
            {
                //start DataInput activity
                Intent intent = new Intent(this, DataInput.class);
                startActivityForResult(intent, OptionMenuCodes.DATA_INPUT_ACTIVITY);
                finish();
                break;
            }
            case OptionMenuCodes.CPI_CALCULATION:
            {
                //start CpiResult activity
                Intent intent = new Intent(this, CpiResult.class);
                startActivityForResult(intent, OptionMenuCodes.CPI_CALCULATION);
                finish();
                break;
            }
            case OptionMenuCodes.EXIT:
            {
                setResult(OptionMenuCodes.EXIT);
                finish();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
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
                        new SimpleDateFormat("dd.MM.yyyy").format(settings.GetWorkingPeriod()));
        currentSettings.setText(result);
    }

    public void onSetPeriodClick(View view) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, workingPeriodPicker.getDayOfMonth());
        cal.set(Calendar.MONTH, workingPeriodPicker.getMonth());
        cal.set(Calendar.YEAR, workingPeriodPicker.getYear());

        SpinnerElement selectedItem = (SpinnerElement)spinnerRegionCode.getSelectedItem();
        EventBus.getDefault().post(new AddSettingEvent(new ru.iate.cpi.db.table.Settings(selectedItem.Id, cal.getTime())));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == OptionMenuCodes.EXIT)
        {
            setResult(OptionMenuCodes.EXIT);
            finish();
        }
    }
}