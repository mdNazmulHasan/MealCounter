package com.nerdcastle.nazmul.mealdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
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

public class MainActivity extends AppCompatActivity {
    TextView dayTV;
    TextView dateTV;
    TextView rateTV;
    String day;
    String date;
    String rate;
    String formattedDate;
    ListView lView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
        getDateAndRate();
        getAllEmployees();

        /*//generate list
        ArrayList<String> list = new ArrayList<String>();
        list.add("item1");
        list.add("item2");
        list.add("item2");
        list.add("item2");
        list.add("item2");
        list.add("item2");
        list.add("item2");
        list.add("item2");
        list.add("item2");
        list.add("item2");
        list.add("item2");
        list.add("item2");
        list.add("item2");
        list.add("item2");
        list.add("item2");
        list.add("item2");*/

      /*  //instantiate custom adapter
        MyCustomAdapter adapter = new MyCustomAdapter(list, this);

        //handle listview and assign adapter
        ListView lView = (ListView)findViewById(R.id.sampleList);
        lView.setAdapter(adapter);*/
    }

    private void getAllEmployees() {
        String urlToGetAllEmployees="http://dotnet.nerdcastlebd.com/meal/api/meal/GetAllEmployees";
        JsonArrayRequest requestToGetAllEmployees=new JsonArrayRequest(Request.Method.GET, urlToGetAllEmployees, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                ArrayList<String>employeeList=new ArrayList<>();
                for(int i=0;i<response.length();i++){
                    try {
                        JSONObject employyeeObject=response.getJSONObject(i);
                        String employeeName=employyeeObject.getString("Name");
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

            }
        });
        AppController.getInstance().addToRequestQueue(requestToGetAllEmployees);
    }

    private void initialize() {
        dayTV= (TextView) findViewById(R.id.dayTV);
        dateTV= (TextView) findViewById(R.id.dateTV);
        rateTV= (TextView) findViewById(R.id.rateTV);
        lView = (ListView)findViewById(R.id.sampleList);

    }
    public String dateFormatterforLukka(String date){
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
        String urltoGetDateAndRate="http://dotnet.nerdcastlebd.com/meal/api/meal/GetDateAndRate";
        JsonObjectRequest requestToGetDateAndRate=new JsonObjectRequest(Request.Method.GET, urltoGetDateAndRate, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    day=response.getString("Day");
                    date=response.getString("Date");
                    rate=response.getString("Rate");
                    formattedDate=dateFormatterforLukka(date);
                    dayTV.setText(day);
                    dateTV.setText(formattedDate);
                    rateTV.setText("Rate: "+rate+" tk");
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
}
