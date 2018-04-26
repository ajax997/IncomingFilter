package com.nguyennghi.incomingfilter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nguyennghi on 3/8/18 5:05 PM
 */
public class ListViewAdapter extends ArrayAdapter<FilterUnit> {

    List<FilterUnit> listItem;
    Context context;
    public ListViewAdapter(Context context, int resource, List<FilterUnit> objects) {
        super(context, resource, objects);
        listItem = objects;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_row,parent,false);
            holder = new Holder();
           holder.num = (TextView)convertView.findViewById(R.id.txtNumber);
            convertView.setTag(holder);
        }
        else {
            holder = (Holder)convertView.getTag();
        }

        holder.num.setText(listItem.get(position).num);

        return convertView;
    }

    public static class Holder{

        public TextView num;


    }

}
