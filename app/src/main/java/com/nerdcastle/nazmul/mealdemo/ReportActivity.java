package com.nerdcastle.nazmul.mealdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Nazmul on 2/23/2016.
 */
public class ReportActivity extends AppCompatActivity {
    Spinner spinnerMonth;
    Spinner spinnerYear;
    TextView totalAmountTV;
    ArrayList<String>nameList;
    ArrayList<String>selfAmountList;
    ArrayList<String>officeAmountList;
    ArrayList<String>totalAmountList;
    ListView reportList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meal_report);
        initialize();
    }
    public void initialize() {
        totalAmountTV= (TextView) findViewById(R.id.totalAmountTV);
        reportList= (ListView) findViewById(R.id.totalReportListView);
        final int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        final int currentMonth = Calendar.getInstance().get(Calendar.MONTH)+1;
        spinnerMonth = (Spinner) findViewById(R.id.month);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getBaseContext(), R.array.month, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.post(new Runnable() {
            @Override
            public void run() {
                spinnerMonth.setSelection(currentMonth-1);
            }
        });
        spinnerMonth.setSelection(adapter.getPosition(String.valueOf(currentMonth)));
        spinnerMonth.setAdapter(adapter);
        spinnerYear = (Spinner) findViewById(R.id.year);
        ArrayAdapter<CharSequence> priceadapter = ArrayAdapter.createFromResource(
                getBaseContext(), R.array.year, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYear.post(new Runnable() {
            @Override
            public void run() {
                spinnerYear.setSelection(currentYear - 2015);
            }
        });
        spinnerYear.setAdapter(priceadapter);
    }
    public void getData(View view){
        String selectedYear=String.valueOf(spinnerYear.getSelectedItem());
        int selectedMonthAmount=spinnerMonth.getSelectedItemPosition()+1;
        String selectedMonth=String.valueOf(selectedMonthAmount);
        Toast.makeText(getBaseContext(), selectedMonth, Toast.LENGTH_LONG).show();
        getTotalAmount(selectedMonth, selectedYear);
        getReport(selectedMonth, selectedYear);
    }

    private void getReport(String selectedMonth, String selectedYear) {
        nameList=new ArrayList<>();
        selfAmountList=new ArrayList<>();
        officeAmountList=new ArrayList<>();
        totalAmountList=new ArrayList<>();
        String urlToGetTotalAmount="http://dotnet.nerdcastlebd.com/meal/api/meal/GetMonthlyBill?month="+selectedMonth+"&year="+selectedYear;
        JsonArrayRequest requestToGetReport=new JsonArrayRequest(Request.Method.GET, urlToGetTotalAmount, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i=0;i<response.length();i++){
                    try {
                        String name=response.getJSONObject(i).getString("EmployeeName");
                        String total=response.getJSONObject(i).getString("TotalAmount");
                        String selfAmount=response.getJSONObject(i).getString("SelfAmount");
                        String officeAmount=response.getJSONObject(i).getString("OfficeAmount");
                        nameList.add(name);
                        totalAmountList.add(total);
                        selfAmountList.add(selfAmount);
                        officeAmountList.add(officeAmount);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                AdapterForReport adapterForReport=new AdapterForReport(nameList,totalAmountList,selfAmountList,officeAmountList,getBaseContext());
                reportList.setAdapter(adapterForReport);
                Toast.makeText(getBaseContext(), response.toString(), Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        AppController.getInstance().addToRequestQueue(requestToGetReport);
    }

    private void getTotalAmount(String selectedMonth, String selectedYear) {
        String urlToGetTotalAmount="http://dotnet.nerdcastlebd.com/meal/api/meal/GetMonthlyTotalOfficeAmount?month="+selectedMonth+"&year="+selectedYear;
        StringRequest requestToGetTotalAmount=new StringRequest(Request.Method.GET, urlToGetTotalAmount, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                totalAmountTV.setText("Total Office Payable "+response+" tk");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        AppController.getInstance().addToRequestQueue(requestToGetTotalAmount);
    }
}
