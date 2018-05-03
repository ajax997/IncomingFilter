package com.nguyennghi.incomingfilter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.nguyennghi.incomingfilter.model.TaskDatabaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nguyennghi on 3/8/18 5:05 PM
 */
public class ListViewAdapter extends ArrayAdapter<FilterUnit>  {


    List<FilterUnit> listItem;
    Context context;
    TaskDatabaseAdapter taskDatabaseAdapter;

    public ListViewAdapter(Context context, int resource, List<FilterUnit> objects,TaskDatabaseAdapter taskDatabaseAdapter) {
        super(context, resource, objects);
        listItem = objects;
        this.context = context;
        this.taskDatabaseAdapter = taskDatabaseAdapter;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_row, parent, false);
            holder = new Holder();
            holder.num = (TextView) convertView.findViewById(R.id.txtNumber);
            holder.aSwitch = (Switch) convertView.findViewById(R.id.S_enable);
            holder.edit = (ImageView) convertView.findViewById(R.id.btn_edit);
            holder.delete = (ImageView) convertView.findViewById(R.id.btn_del);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        //TODO

        final FilterUnit filterUnit;
            filterUnit = listItem.get(position);


        holder.num.setText(filterUnit.num);
        holder.aSwitch.setChecked(filterUnit.enable);


        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                Toast.makeText(context, "Edit", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(context, AddNewNumber.class);
                intent.putExtra("NEW", "NO");
                intent.putExtra("POSITION",String.valueOf(position));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Delete", Toast.LENGTH_LONG).show();
                taskDatabaseAdapter.deleteFilter(filterUnit.id+1);
                listItem.remove(position);
                notifyDataSetChanged();
            }
        });

        holder.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(context, String.valueOf(isChecked), Toast.LENGTH_LONG).show();
                filterUnit.setEnable(isChecked);
                taskDatabaseAdapter.updateFilter(filterUnit);
            }
        });

        return convertView;
    }

    public static class Holder{
        public TextView num;
        public ImageView edit, delete;
        public Switch aSwitch;
    }

}
