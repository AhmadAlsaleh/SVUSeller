package com.svu_test.svusellerapp;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
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
import android.widget.Toast;

import java.util.ArrayList;

public class SellerInfoActivity extends AppCompatActivity {

    private ImageView infoIV;
    private Spinner infoSP, monthSP;
    private TextView infoRegionTV;
    private ArrayList<SellerModel> sellerModels;

    private Button calcComBTN;
    private EditText yearET, coastET, northET, southET, eastET, lebanonET;
    private int selectedSeller = 0;
    private int finalCom = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_info);

        findViews();

        monthSP = findViewById(R.id.infoMonthSP);
        monthSP.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                StaticData.months()));

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
            @SuppressLint("SetTextI18n")
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
                    finalCom = com;

                    new AlertDialog.Builder(SellerInfoActivity.this)
                            .setMessage("Your Commission is " + finalCom)
                            .setNegativeButton("Cancel", null)
                            .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    saveData();
                                }
                            })
                            .create()
                            .show();

                }
            }
        });
    }

    private void saveData() {
        if (!checkInput()) {
            Toast.makeText(SellerInfoActivity.this, "Check your Input", Toast.LENGTH_SHORT).show();
            return;
        }

        final DBHelper dbHelper = new DBHelper(SellerInfoActivity.this);
        if (dbHelper.checkCom(sellerModels.get(selectedSeller).getId(),
                yearET.getText().toString(), monthSP.getSelectedItem().toString())) {

            dbHelper.insertSale(new SaleModel("", sellerModels.get(selectedSeller).getId(),
                    yearET.getText().toString(), monthSP.getSelectedItem().toString(),
                    Integer.parseInt(coastET.getText().toString()), "Coast"));

            dbHelper.insertSale(new SaleModel("", sellerModels.get(selectedSeller).getId(),
                    yearET.getText().toString(), monthSP.getSelectedItem().toString(),
                    Integer.parseInt(northET.getText().toString()), "North"));

            dbHelper.insertSale(new SaleModel("", sellerModels.get(selectedSeller).getId(),
                    yearET.getText().toString(), monthSP.getSelectedItem().toString(),
                    Integer.parseInt(southET.getText().toString()), "South"));

            dbHelper.insertSale(new SaleModel("", sellerModels.get(selectedSeller).getId(),
                    yearET.getText().toString(), monthSP.getSelectedItem().toString(),
                    Integer.parseInt(eastET.getText().toString()), "East"));

            dbHelper.insertSale(new SaleModel("", sellerModels.get(selectedSeller).getId(),
                    yearET.getText().toString(), monthSP.getSelectedItem().toString(),
                    Integer.parseInt(lebanonET.getText().toString()), "Lebanon"));

            dbHelper.insertCommission(new CommissionModel("", sellerModels.get(selectedSeller).getId(),
                    yearET.getText().toString(), monthSP.getSelectedItem().toString(),
                    finalCom));

            clearData();

        } else {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
            new AlertDialog.Builder(SellerInfoActivity.this)
                    .setMessage("This Commission is already exist,\nWe will replace it, Are you agree?")
                    .setNegativeButton("No", null)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dbHelper.updateSale(new SaleModel("", sellerModels.get(selectedSeller).getId(),
                                    yearET.getText().toString(), monthSP.getSelectedItem().toString(),
                                    Integer.parseInt(coastET.getText().toString()), "Coast"));

                            dbHelper.updateSale(new SaleModel("", sellerModels.get(selectedSeller).getId(),
                                    yearET.getText().toString(), monthSP.getSelectedItem().toString(),
                                    Integer.parseInt(northET.getText().toString()), "North"));

                            dbHelper.updateSale(new SaleModel("", sellerModels.get(selectedSeller).getId(),
                                    yearET.getText().toString(), monthSP.getSelectedItem().toString(),
                                    Integer.parseInt(southET.getText().toString()), "South"));

                            dbHelper.updateSale(new SaleModel("", sellerModels.get(selectedSeller).getId(),
                                    yearET.getText().toString(), monthSP.getSelectedItem().toString(),
                                    Integer.parseInt(eastET.getText().toString()), "East"));

                            dbHelper.updateSale(new SaleModel("", sellerModels.get(selectedSeller).getId(),
                                    yearET.getText().toString(), monthSP.getSelectedItem().toString(),
                                    Integer.parseInt(lebanonET.getText().toString()), "Lebanon"));

                            dbHelper.updateCommission(new CommissionModel("", sellerModels.get(selectedSeller).getId(),
                                    yearET.getText().toString(), monthSP.getSelectedItem().toString(),
                                    finalCom));

                            clearData();
                        }
                    })
                    .create()
                    .show();
        }
    }

    private void findViews() {
        infoIV = findViewById(R.id.infoIV);
        infoSP = findViewById(R.id.infoSellersSP);
        infoRegionTV = findViewById(R.id.infoRegionTV);

        calcComBTN = findViewById(R.id.calcComBTN);

        yearET = findViewById(R.id.infoYearET);
        coastET = findViewById(R.id.coastET);
        northET = findViewById(R.id.northET);
        southET = findViewById(R.id.southET);
        eastET = findViewById(R.id.eastET);
        lebanonET = findViewById(R.id.lebanonET);
    }


    private void clearData() {
        yearET.setText("");
        coastET.setText("");
        northET.setText("");
        southET.setText("");
        eastET.setText("");
        lebanonET.setText("");
        finalCom = 0;
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
                !coastET.getText().toString().isEmpty() &&
                !northET.getText().toString().isEmpty() &&
                !southET.getText().toString().isEmpty() &&
                !eastET.getText().toString().isEmpty() &&
                !lebanonET.getText().toString().isEmpty();
    }

}
