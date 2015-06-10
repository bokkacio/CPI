package ru.iate.cpi.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import ru.iate.cpi.R;
import ru.iate.cpi.calc.CpiCalculation;
import ru.iate.cpi.ui.OptionMenuCodes;

/**
 * Created by sanea on 15.04.15.
 */
public class CpiResult extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cpi_result);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, OptionMenuCodes.SETTINGS_ACTIVITY, 1, OptionMenuCodes.SETTINGS_ACTIVITY_STR);
        menu.add(0, OptionMenuCodes.EXIT, 2, OptionMenuCodes.EXIT_STR);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case OptionMenuCodes.SETTINGS_ACTIVITY:
            {
                //start DataInput activity
                Intent intent = new Intent(this, Settings.class);
                startActivityForResult(intent, OptionMenuCodes.SETTINGS_ACTIVITY);
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

    public void OnCpiCalcClick(View view){
        try{
            CpiCalculation calculation = new CpiCalculation(this);
            calculation.initDbData();
            Toast.makeText(this, "CPI result " + calculation.getCpi(), Toast.LENGTH_SHORT).show();
        }
        catch(Exception ex){
            Toast.makeText(this, "CPI calculation error " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
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