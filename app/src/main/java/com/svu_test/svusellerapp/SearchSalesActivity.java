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

public class SearchSalesActivity extends AppCompatActivity {

    private ImageView salesIV;
    private Spinner salesSP, salesMonthSP;
    private EditText salesYearET;
    private Button salesSearchBTN;
    private TextView salesTV;
    private int selectedSeller = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_sales);

        findViews();

        DBHelper dbHelper = new DBHelper(this);

        salesMonthSP.setAdapter(new ArrayAdapter<>(this,
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
                salesIV.setImageBitmap(sellerModels.get(position).getImage());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        salesSearchBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!salesYearET.getText().toString().isEmpty()) {
                    DBHelper helper = new DBHelper(SearchSalesActivity.this);
                    ArrayList<SaleModel> saleModels = helper.selectSales(sellerModels.get(selectedSeller).getId(),
                            salesYearET.getText().toString(),
                            salesMonthSP.getSelectedItem().toString());
                    StringBuilder sales = new StringBuilder();
                    for (SaleModel model: saleModels) {
                        sales.append(model.getRegion()).append("  ").append(model.getAmount()).append("\n");
                    }
                    salesTV.setText(sales);
                }
            }
        });

    }

    private void findViews() {
        salesIV = findViewById(R.id.salesIV);
        salesSP = findViewById(R.id.salesSellersSP);
        salesMonthSP = findViewById(R.id.salesMonthSP);
        salesYearET = findViewById(R.id.salesYearET);
        salesSearchBTN = findViewById(R.id.salesSearchBTN);
        salesTV = findViewById(R.id.salesTV);
    }

}
