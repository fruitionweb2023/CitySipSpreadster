package com.arp.citysipspreadster.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.arp.citysipspreadster.R;
import com.arp.citysipspreadster.model.countryList.Country;

import java.util.ArrayList;
import java.util.List;


public class CountryListAdapter extends ArrayAdapter<Country> {

    LayoutInflater layoutInflater;
    ArrayList<Country> cuisines;

    public CountryListAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull ArrayList<Country> cuisines) {
        super(context, resource, textViewResourceId, cuisines);

        this.cuisines = cuisines;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return rowview(convertView, position);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return rowview(convertView, position);
    }

    private View rowview(View convertView, int position) {

        Country rowItem = getItem(position);

        viewHolder holder;
        View rowview = convertView;
        if (rowview == null) {

            holder = new viewHolder();
            layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowview = layoutInflater.inflate(R.layout.raw_recyclear_view_drop_down, null, false);

            holder.txtTitle = (TextView) rowview.findViewById(R.id.tvName);
            rowview.setTag(holder);
        } else {
            holder = (viewHolder) rowview.getTag();
        }
        holder.txtTitle.setText(rowItem.getName());

        return rowview;
    }

    private class viewHolder {
        TextView txtTitle ;

    }

    public int getItemIndexById(String id) {
        for (Country item : cuisines) {
            if(item.getId().toString().equals(id.toString())){
                return this.cuisines.indexOf(item);
            }
        }
        return 0;
    }
}