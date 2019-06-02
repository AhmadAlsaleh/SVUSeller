package com.svu_test.svusellerapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class CommissionActivity extends AppCompatActivity {

    private ImageView comIV;
    private Spinner salesSP, comMonthSP;
    private EditText comYearET;
    private Button comSearchBTN;
    private TextView comTV;
    private int selectedSeller = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commission);

        findViews();

        DBHelper dbHelper = new DBHelper(this);

        comMonthSP.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                StaticData.months()));
        ArrayList<String> sellers = new ArrayList<>();
        final ArrayList<SellerModel> sellerModels = dbHelper.getAllSellers();
        for (SellerModel model: sellerModels) {
            sellers.add(model.getName());
        }
        salesSP.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                sellers));
        salesSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSeller = position;
                comIV.setImageBitmap(sellerModels.get(position).getImage());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        comSearchBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!comYearET.getText().toString().isEmpty()) {
                    DBHelper helper = new DBHelper(CommissionActivity.this);
                    ArrayList<CommissionModel> commissionModels = helper.selectCommission(sellerModels.get(selectedSeller).getId(),
                            comYearET.getText().toString(),
                            comMonthSP.getSelectedItem().toString());
                    StringBuilder com = new StringBuilder();
                    for (CommissionModel model: commissionModels) {
                        com.append(model.getAmount()).append("S.P.\n");
                    }
                    comTV.setText(com.toString());
                }
            }
        });

    }

    private void findViews() {
        comIV = findViewById(R.id.comIV);
        salesSP = findViewById(R.id.comSellersSP);
        comMonthSP = findViewById(R.id.comMonthSP);
        comYearET = findViewById(R.id.comYearET);
        comSearchBTN = findViewById(R.id.comSearchBTN);
        comTV = findViewById(R.id.comTV);
    }

}
