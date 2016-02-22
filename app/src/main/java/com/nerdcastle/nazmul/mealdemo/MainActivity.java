package com.nerdcastle.nazmul.mealdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //generate list
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
        list.add("item2");

        //instantiate custom adapter
        MyCustomAdapter adapter = new MyCustomAdapter(list, this);

        //handle listview and assign adapter
        ListView lView = (ListView)findViewById(R.id.sampleList);
        lView.setAdapter(adapter);
    }
}
