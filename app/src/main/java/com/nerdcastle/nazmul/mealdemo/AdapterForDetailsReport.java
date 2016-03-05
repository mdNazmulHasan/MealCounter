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
public class AdapterForDetailsReport extends ArrayAdapter<DetailsReportModel> {
    private ArrayList<DetailsReportModel> detailsReportModelArrayList;
    private Context context;

    public AdapterForDetailsReport(ArrayList<DetailsReportModel> detailsReportModelArrayList, Context context) {
        super(context, R.layout.custom_row_for_details_report,detailsReportModelArrayList);
        this.detailsReportModelArrayList=detailsReportModelArrayList;
        this.context=context;
    }

    private static class ViewHolder {
        public TextView dateTV;
        public TextView amountTV;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custom_row_for_details_report,null);
            holder = new ViewHolder();
            holder.dateTV = (TextView) view.findViewById(R.id.dateTV);
            holder.amountTV = (TextView) view.findViewById(R.id.amountTV);
            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.dateTV.setText(detailsReportModelArrayList.get(position).getDate());
        holder.amountTV.setText(detailsReportModelArrayList.get(position).getAmount());
        notifyDataSetChanged();
        return view;
    }

}