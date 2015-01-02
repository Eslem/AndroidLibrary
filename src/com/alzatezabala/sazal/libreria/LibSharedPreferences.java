package com.alzatezabala.sazal.libreria;

import android.content.Context;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;
import java.util.Map.Entry;

public class LibSharedPreferences {
    private Context _ctx;
    private String _name;
    private android.content.SharedPreferences sharedpreferences;

    public LibSharedPreferences(Context context, String name) {
        this._ctx = context;
        this._name = name;
    }

    public Context get_ctx() {
        return _ctx;
    }

    public void set_ctx(Context _ctx) {
        this._ctx = _ctx;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public void saveMultipleData(HashMap<String, String> values) {
        sharedpreferences = _ctx.getSharedPreferences(_name,
                Context.MODE_PRIVATE);
        Editor editor = sharedpreferences.edit();
        for (Entry<String, String> e : values.entrySet()) {
            editor.putString(e.getKey(), e.getValue());
        }
        editor.commit();
    }

    public void setValue(String key, String val) {
        sharedpreferences = _ctx.getSharedPreferences(_name,
                Context.MODE_PRIVATE);
        Editor editor = sharedpreferences.edit();
        editor.putString(key, val);
        editor.commit();
    }

    public String getValue(String key) {
        sharedpreferences = _ctx.getSharedPreferences(_name,
                Context.MODE_PRIVATE);
        if (sharedpreferences.contains(key)) {
            return sharedpreferences.getString(key, "");

        } else
            return "";
    }

    public void empty() {
        sharedpreferences = _ctx.getSharedPreferences(_name,
                Context.MODE_PRIVATE);
        Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.commit();
    }
}
