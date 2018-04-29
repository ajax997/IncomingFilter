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
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskDatabaseAdapter = new TaskDatabaseAdapter(this);
        taskDatabaseAdapter.open();

        listViewItem = (ListView) findViewById(R.id.list_item);

        ArrayList<FilterUnit> listFilter = taskDatabaseAdapter.listUnits();
        Toast.makeText(this, String.valueOf(listFilter.size()), Toast.LENGTH_LONG).show();

        adapter = new ListViewAdapter(this,1, listFilter);
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


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (!Telephony.Sms.getDefaultSmsPackage(getApplicationContext()).equals(getApplicationContext().getPackageName())) {
                Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
                intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME,
                        getApplicationContext().getPackageName());
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        adapter.notifyDataSetChanged();
      // Toast.makeText(this, "Received!", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, JsonHelper.list(), Toast.LENGTH_LONG).show();
    }
}
