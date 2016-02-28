package com.nerdcastle.nazmul.mealdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Nazmul on 2/22/2016.
 */
public class AdapterForMealSubmission extends BaseAdapter implements ListAdapter {
    private ArrayList<String> employeeNameList = new ArrayList<String>();
    private Context context;
    int count = 0;


    public AdapterForMealSubmission(ArrayList<String> employeeNameList, Context context) {
        this.employeeNameList = employeeNameList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return employeeNameList.size();
    }

    @Override
    public Object getItem(int pos) {
        return employeeNameList.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
    }

    private static class ViewHolder {
        public TextView nameTV;
        public Button deleteBtn;
        public Button addBtn;
        public TextView mealCounterTV;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final ViewHolder holder;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custom_row_for_meal_submission, null);
            holder = new ViewHolder();
            holder.nameTV = (TextView) view.findViewById(R.id.nameTV);
            holder.deleteBtn = (Button) view.findViewById(R.id.minus_btn);
            holder.addBtn = (Button) view.findViewById(R.id.add_btn);
            holder.mealCounterTV = (TextView) view.findViewById(R.id.counterTV);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.nameTV.setText(employeeNameList.get(position));
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count = Integer.parseInt(holder.mealCounterTV.getText().toString());
                if (count > 0) {
                    count--;
                }
                holder.mealCounterTV.setText(String.valueOf(count));
                notifyDataSetChanged();
            }
        });
        holder.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count = Integer.parseInt(holder.mealCounterTV.getText().toString());
                count++;
                holder.mealCounterTV.setText(String.valueOf(count));
                notifyDataSetChanged();
            }
        });

        return view;
    }
}