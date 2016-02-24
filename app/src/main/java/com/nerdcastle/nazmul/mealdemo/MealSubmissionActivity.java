package com.nerdcastle.nazmul.mealdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MealSubmissionActivity extends AppCompatActivity {
    TextView dayTV;
    TextView dateTV;
    TextView rateTV;
    String day;
    String date;
    String rate;
    String formattedDate;
    ListView lView;
    ArrayList<String> countervalueList;
    ArrayList<String> employeeList;
    ArrayList<String> idList;
    JSONArray dataToBeSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_submission);
        initialize();
        getDateAndRate();
        getAllEmployees();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_for_order, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.report:
                Intent intent = new Intent(getApplicationContext(),
                        ReportActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getAllEmployees() {
        String urlToGetAllEmployees = "http://dotnet.nerdcastlebd.com/meal/api/meal/GetAllEmployees";
        JsonArrayRequest requestToGetAllEmployees = new JsonArrayRequest(Request.Method.GET, urlToGetAllEmployees, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                employeeList = new ArrayList<>();
                idList = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject employyeeObject = response.getJSONObject(i);
                        String employeeName = employyeeObject.getString("Name");
                        String employeeId = employyeeObject.getString("Id");
                        idList.add(employeeId);
                        employeeList.add(employeeName);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                MyCustomAdapter adapter = new MyCustomAdapter(employeeList, getBaseContext());

                //handle listview and assign adapter

                lView.setAdapter(adapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error instanceof TimeoutError) {
                    String msg = "Request Timed Out, Pls try again";
                    Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
                }
                else if(error instanceof NoConnectionError) {
                    String msg = "No internet Access, Check your internet connection.";
                    Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
                }

            }
        });
        AppController.getInstance().addToRequestQueue(requestToGetAllEmployees);
    }

    private void initialize() {
        dayTV = (TextView) findViewById(R.id.dayTV);
        dateTV = (TextView) findViewById(R.id.dateTV);
        rateTV = (TextView) findViewById(R.id.rateTV);
        lView = (ListView) findViewById(R.id.sampleList);

    }

    public String dateFormatterforLukka(String date) {
        String inputDate = date;
        String inputFormat = "MM/dd/yyyy";
        String outputFormat = "d ' ' MMMM ' ' yyyy";

        Date parsed = null;
        String outputDate = "";
        try {
            SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, new Locale("en", "US"));
            SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, new Locale("en", "US"));
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);
        } catch (Exception e) {
            outputDate = inputDate;
        }
        return outputDate;
    }

    public void getDateAndRate() {
        String urltoGetDateAndRate = "http://dotnet.nerdcastlebd.com/meal/api/meal/GetDateAndRate";
        JsonObjectRequest requestToGetDateAndRate = new JsonObjectRequest(Request.Method.GET, urltoGetDateAndRate, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    day = response.getString("Day");
                    date = response.getString("Date");
                    rate = response.getString("Rate");
                    formattedDate = dateFormatterforLukka(date);
                    dayTV.setText(day);
                    dateTV.setText(formattedDate);
                    rateTV.setText("Rate: " + rate + " tk");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        AppController.getInstance().addToRequestQueue(requestToGetDateAndRate);
    }

    public void submit(View view) throws JSONException {
        countervalueList = getAllValues();
        dataToBeSend = new JSONArray();


        for (int i = 0; i < countervalueList.size(); i++) {
            JSONObject everyEmployeeData = new JSONObject();
            everyEmployeeData.put("EmployeeId", idList.get(i));
            everyEmployeeData.put("NumberOfMeal", countervalueList.get(i));
            dataToBeSend.put(everyEmployeeData);
        }
       //submitData(dataToBeSend);
        String urlToSubmitData = "http://dotnet.nerdcastlebd.com/meal/api/meal/PostDailyBill";
        final JsonArrayRequest requestToSubmitData=new JsonArrayRequest(Request.Method.POST, urlToSubmitData, dataToBeSend, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    boolean data=response.getJSONObject(0).getBoolean("ResultState");
                    if(data){
                        Toast.makeText(getBaseContext(),"Succesfully Saved",Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error instanceof TimeoutError) {
                    String msg = "Request Timed Out, Pls try again";
                    Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
                }
                else if(error instanceof NoConnectionError) {
                    String msg = "No internet Access, Check your internet connection.";
                    Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
                }

            }
        });
        AppController.getInstance().addToRequestQueue(requestToSubmitData);

        //Toast.makeText(getBaseContext(), dataToBeSend.toString(), Toast.LENGTH_LONG).show();

    }

    public ArrayList<String> getAllValues() {
        countervalueList = new ArrayList<>();
        View parentView = null;
        String number = "";
        for (int i = 0; i < lView.getCount(); i++) {
            parentView = getViewByPosition(i, lView);
            number = ((TextView) parentView
                    .findViewById(R.id.counter)).getText().toString();

            countervalueList.add(number);

        }
        return countervalueList;

    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition
                + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }
}
