package com.example.projektdb;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.projektdb.Objekti;

import java.util.ArrayList;
import java.util.List;


public class ObjektiAdapterSpinner extends ArrayAdapter<Objekti> {

    ArrayList<Objekti> arrayListObjekti = new ArrayList<>();
    private String objekatId;
    private String title;

    public ObjektiAdapterSpinner(Context context, List<Objekti> objektiList) {
        super(context, 0, objektiList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return createView(position, convertView, parent);
    }

    private View createView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }

        // Customize the view based on your Objekti class
        TextView textView = convertView.findViewById(android.R.id.text1);
        Objekti objekti = getItem(position);
        if (objekti != null) {
            String title = objekti.getTitle(); // Assuming getTitle() method exists in Objekti class
            textView.setText(title);
            Log.e("proba1", "Setting text1: " + title);
        }

        return convertView;
    }

    public String getTitle() {
        return title;
    }
}
