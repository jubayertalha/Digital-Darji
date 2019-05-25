package com.appincubator.digitaldarji.Model;

public class Notifications {
    int _id;
    String email;
    String msg;
    String type;

    public Notifications(int _id, String email, String msg, String type) {
        this._id = _id;
        this.email = email;
        this.msg = msg;
        this.type = type;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
