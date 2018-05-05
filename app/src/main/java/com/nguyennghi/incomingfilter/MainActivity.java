package com.nguyennghi.incomingfilter;

import android.content.Intent;
import android.os.Build;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import com.nguyennghi.incomingfilter.model.TaskDatabaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listViewItem;
    ImageView imgNewNumber;
    ListViewAdapter adapter = null;
    TaskDatabaseAdapter taskDatabaseAdapter;
    ArrayList<FilterUnit> listFilter;
    Button btnEnableAll, btnDisableAll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnDisableAll = (Button)findViewById(R.id.btnDisableAll);
        btnEnableAll = (Button)findViewById(R.id.btnEnableAll);

        taskDatabaseAdapter = new TaskDatabaseAdapter(this);
        taskDatabaseAdapter.open();

        listViewItem = (ListView) findViewById(R.id.list_item);

        listFilter = taskDatabaseAdapter.listUnits();
        Toast.makeText(this, String.valueOf(listFilter.size()), Toast.LENGTH_LONG).show();

        adapter = new ListViewAdapter(this,1, listFilter, taskDatabaseAdapter);
        listViewItem.setAdapter(adapter);

        imgNewNumber = (ImageView) findViewById(R.id.imgNewNumber);
        imgNewNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddNewNumber.class);
                intent.putExtra("NEW", "YES");
                startActivityForResult(intent, 1);

            }
        });

        btnEnableAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(FilterUnit filterUnit: listFilter)
                {
                    filterUnit.setEnable(true);
                    taskDatabaseAdapter.updateFilter(filterUnit);
                }

                adapter.notifyDataSetChanged();
            }
        });

        btnDisableAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(FilterUnit filterUnit: listFilter)
                {
                    filterUnit.setEnable(false);
                    taskDatabaseAdapter.updateFilter(filterUnit);
                }

                adapter.notifyDataSetChanged();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        adapter.notifyDataSetChanged();
        listViewItem.setAdapter(new ListViewAdapter(this,1, taskDatabaseAdapter.listUnits(), taskDatabaseAdapter));
        adapter.notifyDataSetChanged();
    }
}
