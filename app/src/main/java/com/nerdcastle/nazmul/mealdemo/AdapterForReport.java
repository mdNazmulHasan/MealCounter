package com.nerdcastle.nazmul.mealdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Nazmul on 2/22/2016.
 */
public class AdapterForReport extends ArrayAdapter<ReportModel> {
    private ArrayList<ReportModel> reportList;
    private Context context;

    public AdapterForReport(ArrayList<ReportModel> reportList, Context context) {
        super(context, R.layout.custom_row_for_report,reportList);
        this.reportList=reportList;
        this.context=context;
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
            view = inflater.inflate(R.layout.custom_row_for_report,null);
            holder = new ViewHolder();
            holder.nameTV = (TextView) view.findViewById(R.id.nameTV);
            holder.selfAmountTV = (TextView) view.findViewById(R.id.selfAmountTV);
            holder.officeAmountTV = (TextView) view.findViewById(R.id.officeAmountTV);
            holder.totalTV = (TextView) view.findViewById(R.id.totalTV);
            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.nameTV.setText(reportList.get(position).getName());
        holder.selfAmountTV.setText(reportList.get(position).getSelfAmount());
        holder.totalTV.setText(reportList.get(position).getTotal());
        holder.officeAmountTV.setText(reportList.get(position).getOfficeAmount());
        return view;
    }

}