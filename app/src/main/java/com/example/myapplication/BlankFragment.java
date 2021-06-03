package com.example.myapplication;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.LinkedList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BlankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlankFragment extends Fragment {


    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "text";
    public static final String COUNTER = "counter";
    List<String> trustedNr = new LinkedList<String>();



    private String mParam1;
    private String mParam2;

    public BlankFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BlankFragment newInstance(String param1, String param2) {
        BlankFragment fragment = new BlankFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_white, container, false);
        final SharedPreferences[] sharedPreferences = {getContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)};
        SharedPreferences.Editor editor = sharedPreferences[0].edit();
        TextInputEditText txtField = v.findViewById(R.id.txtField);
        Button btnAdd = v.findViewById(R.id.btnAdd);
        TableLayout tableLayout = v.findViewById(R.id.tableLayout);
        Button btnClear = v.findViewById(R.id.btnClear);
        final int[] i = {sharedPreferences[0].getInt(COUNTER, 0)};
        updateTables(tableLayout);

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.clear();
                editor.apply();
                tableLayout.removeAllViews();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = txtField.getText().toString();
                if(checkForDuplicate(number)){
                    createRow(tableLayout, number);
                    i[0] = i[0] +1;
                    editor.putInt(COUNTER, i[0]);
                    editor.putString(TEXT + i[0], number);
                    editor.apply();
                    Toast.makeText(getActivity(), "Data saved", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(), "You already have this number saved", Toast.LENGTH_SHORT).show();
                }
            }
        });




        return v;
    }

    private boolean checkForDuplicate(String number) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        int i = sharedPreferences.getInt(COUNTER, 0);
        while (i > 0){
            String nr = sharedPreferences.getString(TEXT + i, "");
            i--;
            if (nr.equals(number)){
                return false;
            }
        }
        return true;
    }

    private void createRow(TableLayout tableLayout, String number) {
        TableRow tableRow = new TableRow(getActivity());
        TableRow.LayoutParams lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
        lp.setMargins(4, 4, 4, 4);
        TextView t = new TextView(getActivity());
        t.setPadding(10, 10, 10, 10);
        t.setText(number);
        tableRow.addView(t, lp);
        tableLayout.addView(tableRow);
    }

    private void updateTables(TableLayout tableLayout){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        int i = sharedPreferences.getInt(COUNTER, 0);
        while(i > 0){
            String number = sharedPreferences.getString(TEXT + i, "");
            i--;
            createRow(tableLayout, number);
        }
    }
}