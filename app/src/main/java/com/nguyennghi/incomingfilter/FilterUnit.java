package com.nguyennghi.incomingfilter;

/**
 * Created by nguyennghi on 3/8/18 5:29 PM
 */
public class FilterUnit {
    public int id;
   public String num;
    public UnitType unitType;
    public String provider;
    public  boolean blocking_incoming_calls;
    public  boolean blocking_incoming_mess;
    public  int incoming_call_action;
    public   boolean call_auto_sms;
    public   boolean mess_auto_sms;
    public   String auto_text_content;

    public   boolean enable;
    public FilterUnit()
    {

    }

    public FilterUnit(String num, UnitType unitType, String provider, boolean blocking_incoming_calls, boolean blocking_incoming_mess, int incoming_call_action,
                      boolean call_auto_sms, boolean mess_auto_sms, String auto_text_content, boolean enable) {
        this.num = num;
        this.unitType = unitType;
        this.provider = provider;
        this.blocking_incoming_calls = blocking_incoming_calls;
        this.blocking_incoming_mess = blocking_incoming_mess;
        this.incoming_call_action = incoming_call_action;
        this.call_auto_sms = call_auto_sms;
        this.mess_auto_sms = mess_auto_sms;
        this.auto_text_content = auto_text_content;
        this.enable = enable;
    }

    public FilterUnit(int id, String num, UnitType unitType, String provider, boolean blocking_incoming_calls, boolean blocking_incoming_mess, int incoming_call_action,
                      boolean call_auto_sms, boolean mess_auto_sms, String auto_text_content, boolean enable) {
        this.id = id;
        this.num = num;
        this.unitType = unitType;
        this.provider = provider;
        this.blocking_incoming_calls = blocking_incoming_calls;
        this.blocking_incoming_mess = blocking_incoming_mess;
        this.incoming_call_action = incoming_call_action;
        this.call_auto_sms = call_auto_sms;
        this.mess_auto_sms = mess_auto_sms;
        this.auto_text_content = auto_text_content;
        this.enable = enable;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public UnitType getUnitType() {
        return unitType;
    }

    public void setUnitType(UnitType unitType) {
        this.unitType = unitType;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public boolean isBlocking_incoming_calls() {
        return blocking_incoming_calls;
    }

    public void setBlocking_incoming_calls(boolean blocking_incoming_calls) {
        this.blocking_incoming_calls = blocking_incoming_calls;
    }

    public boolean isBlocking_incoming_mess() {
        return blocking_incoming_mess;
    }

    public void setBlocking_incoming_mess(boolean blocking_incoming_mess) {
        this.blocking_incoming_mess = blocking_incoming_mess;
    }

    public int getIncoming_call_action() {
        return incoming_call_action;
    }

    public void setIncoming_call_action(int incoming_call_action) {
        this.incoming_call_action = incoming_call_action;
    }

    public boolean isCall_auto_sms() {
        return call_auto_sms;
    }

    public void setCall_auto_sms(boolean call_auto_sms) {
        this.call_auto_sms = call_auto_sms;
    }

    public boolean isMess_auto_sms() {
        return mess_auto_sms;
    }

    public void setMess_auto_sms(boolean mess_auto_sms) {
        this.mess_auto_sms = mess_auto_sms;
    }

    public String getAuto_text_content() {
        return auto_text_content;
    }

    public void setAuto_text_content(String auto_text_content) {
        this.auto_text_content = auto_text_content;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}


