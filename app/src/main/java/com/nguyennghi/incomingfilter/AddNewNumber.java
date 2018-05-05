package com.nguyennghi.incomingfilter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.nguyennghi.incomingfilter.model.TaskDatabaseAdapter;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddNewNumber extends AppCompatActivity {

    Spinner sp_provider;
    Spinner sp_blocking_type;
    TextView lbMess;
    EditText txtNum;
    Button btnSave, btnCancel;
    CheckBox chk_Blocking_incoming_calls, chk_blocking_incoming_mess;
    RadioGroup radioGroup;
    CheckBox phoneAutoSms, messAutoSms;
    EditText txtMess;
    TaskDatabaseAdapter taskDatabaseAdapter;

    RadioButton rb1, rb2, rb3, rb4;

    boolean editMode = false;
    int position = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnewnumber);
        Intent intent = getIntent();
        taskDatabaseAdapter = new TaskDatabaseAdapter(this);
        taskDatabaseAdapter.open();

        Toast.makeText(this, intent.getStringExtra("NEW"), Toast.LENGTH_LONG).show();

        sp_blocking_type = (Spinner) findViewById(R.id.spinner);
        sp_provider = (Spinner) findViewById(R.id.spinner2);
        lbMess = (TextView) findViewById(R.id.lbMess);
        txtNum = (EditText) findViewById(R.id.txtNum);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        chk_Blocking_incoming_calls = (CheckBox) findViewById(R.id.chk_blocking_incoming_calls);
        chk_blocking_incoming_mess = (CheckBox) findViewById(R.id.chk_blocking_incoming_mess);
        radioGroup = (RadioGroup) findViewById(R.id.rb_group);
        phoneAutoSms = (CheckBox) findViewById(R.id.chkPhoneAutoSMS);
        messAutoSms = (CheckBox) findViewById(R.id.chkMessAutoSMS);
        txtMess = (EditText) findViewById(R.id.txtMess);

        rb1 = (RadioButton) findViewById(R.id.rbHangUp);
        rb2 = (RadioButton) findViewById(R.id.rbPickUp);
        rb3 = (RadioButton) findViewById(R.id.rbSilent);
        rb4 = (RadioButton) findViewById(R.id.rbVibrate);

        sp_provider.setEnabled(false);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.providers_array, android.R.layout.simple_spinner_item);

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sp_provider.setAdapter(adapter2);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sp_blocking_type.setAdapter(adapter);

        if(intent.getStringExtra("NEW").equals("NO"))
        {
            editMode =true;
            position =Integer.valueOf(intent.getStringExtra("POSITION"));
            FilterUnit filterUnit = taskDatabaseAdapter.listUnits().get(position);
            //debug
            Toast.makeText(this, String.valueOf(filterUnit.enable), Toast.LENGTH_SHORT).show();

            int by = blockingBy(filterUnit.unitType);

            int proviID = getProvider(filterUnit.provider);


            sp_blocking_type.setSelection(by,true);
            sp_provider.setSelection(proviID, true);
            txtNum.setText(filterUnit.num);
            chk_Blocking_incoming_calls.setChecked(filterUnit.blocking_incoming_calls);
            chk_blocking_incoming_mess.setChecked(filterUnit.blocking_incoming_mess);
            setCheck(filterUnit.incoming_call_action);
            phoneAutoSms.setChecked(filterUnit.call_auto_sms);
            messAutoSms.setChecked(filterUnit.mess_auto_sms);
            txtMess.setText(filterUnit.auto_text_content);

            if(proviID == 3)
            {
                sp_provider.setEnabled(true);
                txtNum.setEnabled(false);
            }
            else {
                sp_provider.setEnabled(false);
                txtNum.setEnabled(true);
            }
        }

        sp_blocking_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (sp_blocking_type.getSelectedItemPosition()) {
                    case 0:
                        sp_provider.setEnabled(false);
                        lbMess.setText("Block Phone Number");
                        txtNum.setEnabled(true);
                        break;
                    case 1:
                        sp_provider.setEnabled(false);
                        lbMess.setText("Block Phone Number Starts With:");
                        txtNum.setEnabled(true);
                        break;
                    case 2:
                        sp_provider.setEnabled(false);
                        lbMess.setText("Block Phone Number Ends With:");
                        txtNum.setEnabled(true);
                        break;
                    case 3:
                        sp_provider.setEnabled(true);
                        lbMess.setText("Block Phone Number Ends With:");
                        txtNum.setEnabled(false);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
                FilterUnit filterUnit = new FilterUnit();

                try {
                    filterUnit.setUnitType((blockingBy(sp_blocking_type.getSelectedItemPosition())));
                    filterUnit.setProvider(sp_provider.getSelectedItem().toString());
                    filterUnit.setNum(txtNum.getText().toString());
                    filterUnit.setBlocking_incoming_calls(chk_Blocking_incoming_calls.isChecked());
                    filterUnit.setBlocking_incoming_mess(chk_blocking_incoming_mess.isChecked());

                    filterUnit.setIncoming_call_action(rb1.isChecked() ? 0 : (rb2.isChecked() ? 1 : (rb3.isChecked() ? 2 : 3)));
                    filterUnit.setCall_auto_sms(phoneAutoSms.isChecked());
                    filterUnit.setMess_auto_sms(messAutoSms.isChecked());
                    filterUnit.setAuto_text_content(txtMess.getText().toString());
                    if (editMode) {
                        filterUnit.setId(position);
                        taskDatabaseAdapter.updateFilter(filterUnit);
                    } else {
                        filterUnit.setEnable(true);
                        taskDatabaseAdapter.addNewFilter(filterUnit);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Intent resultIntent = new Intent();
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent resultIntent = new Intent();
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    private void setCheck(int position)
    {
        switch (position)
        {
            case 0:
                rb1.setChecked(true);
                break;
            case 1:
                rb2.setChecked(true);
                break;
            case 2:
                rb3.setChecked(true);
                break;
            case 3:
                rb4.setChecked(true);
                break;
        }
    }

    private int getProvider(String provider) {
        return provider.equals("Viettel") ? 0 : (provider.equals("Mobifone") ? 1 :
                (provider.equals("Vinaphone") ? 2 : (provider.equals("Vietnamobile") ? 3 : 4)));
    }
    private UnitType blockingBy(int position)
    {
        switch (position)
        {
            case 0:
                return UnitType.NUM;
            case 1:
                return UnitType.START_NUM;
            case 2:
                return UnitType.END_NUM;
            case 3:
                return UnitType.PROVIDER;
                default:
                    return null;
        }
    }
    private int blockingBy(UnitType unitType)
    {
       switch (unitType)
       {
           case NUM:
               return 0;
           case START_NUM:
               return 1;
           case END_NUM:
               return 2;
           case PROVIDER:
               return 3;
               default:return -1;
       }
    }


    private InComingCallActions getAction(int position)
    {
        switch (position)
        {
            case 0:
                return InComingCallActions.HANG_UP;
            case 1:
                return InComingCallActions.PICK_UP;
            case 2:
                return InComingCallActions.SILENT;
            case 3:
                return InComingCallActions.VIBRATE;
            default:
                return null;
        }
    }
}
