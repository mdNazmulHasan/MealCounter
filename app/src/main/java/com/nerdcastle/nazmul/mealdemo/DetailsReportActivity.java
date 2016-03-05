package com.nerdcastle.nazmul.mealdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Nazmul on 3/5/2016.
 */
public class DetailsReportActivity extends AppCompatActivity {
    TextView titleTV;
    String token;
    ListView personalLV;
    String name;
    String employeeId;
    String month;
    String year;
    String urlOfDetailsReport;
    DetailsReportModel detailsReportModel;
    ArrayList<DetailsReportModel>detailsReportModelArrayList;
    AdapterForDetailsReport adapterForDetailsReport;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_report);
        name=getIntent().getStringExtra("Name");
        employeeId=getIntent().getStringExtra("EmployeeId");
        month=getIntent().getStringExtra("Month");
        year=getIntent().getStringExtra("Year");
        token=getIntent().getStringExtra("token");
        initialize();
        getDetailsReport();
    }

    private void getDetailsReport() {
        detailsReportModelArrayList=new ArrayList<>();
        urlOfDetailsReport="http://dotnet.nerdcastlebd.com/meal/api/meal/GetDetailsMonthlyBillOfAnEmployee?employeeId="+employeeId+"&month="+month+"&year="+year;
        JsonArrayRequest requestToGetDetailsReport=new JsonArrayRequest(Request.Method.GET, urlOfDetailsReport, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i=0;i<response.length();i++){
                    try {
                        String date=response.getJSONObject(i).getString("ExpenseDate");
                        String amount=response.getJSONObject(i).getString("Amount");
                        detailsReportModel=new DetailsReportModel(date,amount);
                        detailsReportModelArrayList.add(detailsReportModel);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                adapterForDetailsReport=new AdapterForDetailsReport(detailsReportModelArrayList,getApplicationContext());
                personalLV.setAdapter(adapterForDetailsReport);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        AppController.getInstance().addToRequestQueue(requestToGetDetailsReport);

    }

    private void initialize() {
        titleTV= (TextView) findViewById(R.id.titleTV);
        titleTV.setText("Monthly Report of "+name);
        personalLV= (ListView) findViewById(R.id.personalLV);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_for_details_report, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.home:
                Intent homeIntent = new Intent(getApplicationContext(),
                        MealSubmissionActivity.class);
                homeIntent.putExtra("Token", token);
                startActivity(homeIntent);
                return true;
            case R.id.report:
                Intent reportIntent = new Intent(getApplicationContext(),
                        ReportActivity.class);
                reportIntent.putExtra("Token", token);
                startActivity(reportIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
