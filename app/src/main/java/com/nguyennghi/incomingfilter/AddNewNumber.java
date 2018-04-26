package com.nguyennghi.incomingfilter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnewnumber);


        sp_blocking_type = (Spinner) findViewById(R.id.spinner);
        sp_provider = (Spinner)findViewById(R.id.spinner2);
        lbMess = (TextView)findViewById(R.id.lbMess);
        txtNum = (EditText)findViewById(R.id.txtNum);
        btnSave = (Button)findViewById(R.id.btnSave);
        btnCancel = (Button)findViewById(R.id.btnCancel);
        chk_Blocking_incoming_calls = (CheckBox)findViewById(R.id.chk_blocking_incoming_calls);
        chk_blocking_incoming_mess = (CheckBox)findViewById(R.id.chk_blocking_incoming_mess);
        radioGroup = (RadioGroup)findViewById(R.id.rb_group);
        phoneAutoSms = (CheckBox)findViewById(R.id.chkPhoneAutoSMS);
        messAutoSms = (CheckBox)findViewById(R.id.chkMessAutoSMS);
        txtMess = (EditText)findViewById(R.id.txtMess);


        sp_provider.setEnabled(false);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sp_blocking_type.setAdapter(adapter);
        sp_blocking_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (sp_blocking_type.getSelectedItemPosition())
                {
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

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.providers_array, android.R.layout.simple_spinner_item);

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sp_provider.setAdapter(adapter2);


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
                JSONObject jsonObject = new JSONObject();

                try {
                    jsonObject.accumulate("blocking_by", blockingBy(sp_blocking_type.getSelectedItemPosition()).toString());
                    jsonObject.accumulate("provider", sp_provider.getSelectedItem().toString());
                    jsonObject.accumulate("number", txtNum.getText());
                    jsonObject.accumulate("blocking_incoming_calls", chk_Blocking_incoming_calls.isSelected());
                    jsonObject.accumulate("blocking_incoming_mess", chk_blocking_incoming_mess.isSelected());
                    jsonObject.accumulate("incoming_call_action",getAction(radioGroup.getCheckedRadioButtonId()));
                    jsonObject.accumulate("call_auto_sms",phoneAutoSms.isSelected());
                    jsonObject.accumulate("mess_auto_sms",messAutoSms.isSelected());
                    jsonObject.accumulate("auto_text_content", txtMess.getText().toString());

                    JsonHelper.writeJson(jsonObject, timeStamp, "//data//");
                    JsonHelper.list();
                } catch (JSONException e) {
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

    private BlockingBy blockingBy(int position)
    {
        switch (position)
        {
            case 0:
                return BlockingBy.PHONE_NUMBER;
            case 1:
                return BlockingBy.PHONE_START_WITH;
            case 2:
                return BlockingBy.PHONE_END_WITH;
            case 3:
                return BlockingBy.PHONE_END_WITH;
                default:
                    return null;
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
