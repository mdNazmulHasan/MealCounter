package com.nerdcastle.nazmul.mealdemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Nazmul on 2/28/2016.
 */
public class LoginActivity extends AppCompatActivity {
    EditText userIdET;
    EditText passwordET;
    String userId;
    String password;
    JSONObject loginObject;
    String urlToLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialize();
    }

    private void initialize() {
        userIdET= (EditText) findViewById(R.id.userIdET);
        passwordET= (EditText) findViewById(R.id.passwordET);
    }
    public void login(View view) throws JSONException {
        urlToLogin="http://dotnet.nerdcastlebd.com/meal/api/users/post";
        userId=userIdET.getText().toString();
        password=passwordET.getText().toString();
        loginObject=new JSONObject();
        loginObject.put("UserId",userId);
        loginObject.put("Password",password);
        JsonObjectRequest requestLogin=new JsonObjectRequest(Request.Method.POST, urlToLogin, loginObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Boolean result=response.getBoolean("ResultState");
                    String token=response.getString("Token");
                    //Toast.makeText(getApplicationContext(),result., Toast.LENGTH_LONG).show();
                    if(result){
                        Intent loginIntent=new Intent(getApplicationContext(),MealSubmissionActivity.class);
                        loginIntent.putExtra("Token",token);
                        startActivity(loginIntent);

                    }
                    else if(!result){
                        Toast.makeText(getApplicationContext(),"Incorrect UserId or Password", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    String msg = "Request Timed Out, Pls try again";
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                } else if (error instanceof NoConnectionError) {
                    String msg = "No internet Access, Check your internet connection.";
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                }

            }
        });
        AppController.getInstance().addToRequestQueue(requestLogin);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }
}
