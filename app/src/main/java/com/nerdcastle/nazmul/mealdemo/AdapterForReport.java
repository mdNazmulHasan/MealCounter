package com.nerdcastle.nazmul.mealdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Nazmul on 2/22/2016.
 */
public class AdapterForReport extends BaseAdapter implements ListAdapter {
    private ArrayList<String> nameList = new ArrayList<String>();
    private ArrayList<String> selfAmountList = new ArrayList<String>();
    private ArrayList<String> officeAmountList = new ArrayList<String>();
    private ArrayList<String> totalAmountList = new ArrayList<String>();
    private Context context;


    public AdapterForReport(ArrayList<String> nameList, ArrayList<String> totalAmountList, ArrayList<String> selfAmountList, ArrayList<String> officeAmountList, Context context) {
        this.nameList = nameList;
        this.selfAmountList = selfAmountList;
        this.officeAmountList = officeAmountList;
        this.totalAmountList = totalAmountList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return officeAmountList.size();
    }

    @Override
    public Object getItem(int pos) {
        return officeAmountList.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
    }

    private static class ViewHolder {
        public TextView nameTV;
        public TextView selfAmountTV;
        public TextView officeAmountTV;
        public TextView totalTV;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custom_row_for_report, null);
            holder = new ViewHolder();

            holder.nameTV = (TextView) view.findViewById(R.id.nameTV);
            holder.selfAmountTV = (TextView) view.findViewById(R.id.selfAmountTV);
            holder.officeAmountTV = (TextView) view.findViewById(R.id.officeAmountTV);
            holder.totalTV = (TextView) view.findViewById(R.id.totalTV);
            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.nameTV.setText(nameList.get(position));
        holder.selfAmountTV.setText(selfAmountList.get(position));
        holder.totalTV.setText(totalAmountList.get(position));
        holder.officeAmountTV.setText(officeAmountList.get(position));
        return view;
    }
}