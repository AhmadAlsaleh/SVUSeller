package com.svu_test.svusellerapp;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class SellerInfoActivity extends AppCompatActivity {

    private ImageView infoIV;
    private Spinner infoSP;
    private TextView infoRegionTV;
    private ArrayList<SellerModel> sellerModels;

    private Button calcComBTN, saveComBTN;
    private TextView infoComTV;
    private EditText yearET, monthET, coastET, northET, southET, eastET, lebanonET;
    private int selectedSeller = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_info);

        findViews();

        sellerModels = new DBHelper(this).getAllSellers();
        ArrayList<String> sellers = new ArrayList<>();
        for (SellerModel sellerModel: sellerModels) {
            sellers.add(sellerModel.getName());
        }
        final ArrayAdapter aa = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                sellers
        );
        infoSP.setAdapter(aa);
        infoSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                infoIV.setImageBitmap(sellerModels.get(position).getImage());
                infoRegionTV.setText("Region: " + sellerModels.get(position).getRegion());
                selectedSeller = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        calcComBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInput()) {
                    Pair<Integer, ArrayList<Integer>> p = getSales(selectedSeller);
                    int com = 0;
                    if (p.first > 10000000) {
                        com += (int) ((10000000 * 0.05) + ((p.first - 10000000) * 0.07));
                    } else {
                        com += (int) (p.first * 0.05);
                    }
                    for (int i: p.second) {
                        if (i > 10000000) {
                            com += (int) ((10000000 * 0.03) + ((i - 10000000) * 0.04));
                        } else {
                            com += (int) (i * 0.03);
                        }
                    }
                    infoComTV.setText(String.valueOf(com));

                }
            }
        });

    }

    private void findViews() {
        infoIV = findViewById(R.id.infoIV);
        infoSP = findViewById(R.id.infoSellersSP);
        infoRegionTV = findViewById(R.id.infoRegionTV);

        calcComBTN = findViewById(R.id.calcComBTN);
        saveComBTN = findViewById(R.id.saveComBTN);
        infoComTV = findViewById(R.id.infoComTV);

        yearET = findViewById(R.id.infoYearET);
        monthET = findViewById(R.id.infoMonthET);
        coastET = findViewById(R.id.coastET);
        northET = findViewById(R.id.northET);
        southET = findViewById(R.id.southET);
        eastET = findViewById(R.id.eastET);
        lebanonET = findViewById(R.id.lebanonET);
    }

    private Pair<Integer, ArrayList<Integer>> getSales(int i) {
        int c = Integer.parseInt(coastET.getText().toString());
        int n = Integer.parseInt(northET.getText().toString());
        int s = Integer.parseInt(southET.getText().toString());
        int e = Integer.parseInt(eastET.getText().toString());
        int l = Integer.parseInt(lebanonET.getText().toString());
        ArrayList<Integer> al = new ArrayList<>();
        switch (sellerModels.get(i).getRegion().toLowerCase()) {
            case "coast":
                al.add(e);
                al.add(n);
                al.add(s);
                al.add(l);
                return new Pair<>(c, al);
            case "north":
                al.add(c);
                al.add(e);
                al.add(s);
                al.add(l);
                return new Pair<>(n, al);
            case "south":
                al.add(c);
                al.add(n);
                al.add(e);
                al.add(l);
                return new Pair<>(s, al);
            case "east":
                al.add(c);
                al.add(n);
                al.add(s);
                al.add(l);
                return new Pair<>(e, al);
            default:
                al.add(c);
                al.add(n);
                al.add(s);
                al.add(e);
                return new Pair<>(l, al);
        }
    }

    private boolean checkInput() {
        return !yearET.getText().toString().isEmpty() &&
                !monthET.getText().toString().isEmpty() &&
                !coastET.getText().toString().isEmpty() &&
                !northET.getText().toString().isEmpty() &&
                !southET.getText().toString().isEmpty() &&
                !eastET.getText().toString().isEmpty() &&
                !lebanonET.getText().toString().isEmpty();
    }

}
