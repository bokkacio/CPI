package ru.iate.cpi.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import ru.iate.cpi.R;
import ru.iate.cpi.calc.CpiCalculation;
import ru.iate.cpi.calc.CpiElement;
import ru.iate.cpi.ui.FormatHelper;
import ru.iate.cpi.ui.OptionMenuCodes;

import java.util.List;

/**
 * Created by sanea on 15.04.15.
 */
public class CpiResult extends Activity {
    private TableLayout cpiResultTable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cpi_result);
        cpiResultTable = (TableLayout)findViewById(R.id.table_cpiResult);
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
            cpiResultTable.removeAllViews();
            CpiCalculation calculation = new CpiCalculation(this);
            calculation.initDbData();

            //draw result CPI
            CpiElement element = calculation.getCpi();
            drawHeaderTableRow();
            drawCpiTableRow(element);

            //draw CPI for categories
            List<CpiElement> categoriesCpi = calculation.getAggregateFirstLevels();
            for (CpiElement cpi : categoriesCpi)
                drawCpiTableRow(cpi);
        }
        catch(Exception ex){
            Toast.makeText(this, "CPI calculation error " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void drawHeaderTableRow(){
        View rowData = View.inflate(this, R.layout.cpi_table_row, null);
        View rowSeparator = View.inflate(this, R.layout.cpi_table_row_separator, null);

        TextView code = (TextView)rowData.findViewById(R.id.txt_cpiRowCode);
        TextView title = (TextView)rowData.findViewById(R.id.txt_cpiRowTitle);
        TextView index = (TextView)rowData.findViewById(R.id.txt_cpiRowIndex);
        TextView weight = (TextView)rowData.findViewById(R.id.txt_cpiRowWeight);

        code.setText(getResources().getString(R.string.lbl_cpiCodeTitle));
        title.setText(getResources().getString(R.string.lbl_cpiCategoryTitle));
        index.setText(getResources().getString(R.string.lbl_cpiIndex));
        weight.setText(getResources().getString(R.string.lbl_cpiWeight));

        cpiResultTable.addView(rowData);
        cpiResultTable.addView(rowSeparator);
    }

    private void drawCpiTableRow(CpiElement cpi){
        View rowData = View.inflate(this, R.layout.cpi_table_row, null);
        View rowSeparator = View.inflate(this, R.layout.cpi_table_row_separator, null);

        TextView code = (TextView)rowData.findViewById(R.id.txt_cpiRowCode);
        TextView title = (TextView)rowData.findViewById(R.id.txt_cpiRowTitle);
        TextView index = (TextView)rowData.findViewById(R.id.txt_cpiRowIndex);
        TextView weight = (TextView)rowData.findViewById(R.id.txt_cpiRowWeight);

        code.setText(cpi.code);
        title.setText(cpi.title);
        index.setText(FormatHelper.GetDouble(cpi.index));
        weight.setText(FormatHelper.GetFloat(cpi.weight));

        cpiResultTable.addView(rowData);
        cpiResultTable.addView(rowSeparator);
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