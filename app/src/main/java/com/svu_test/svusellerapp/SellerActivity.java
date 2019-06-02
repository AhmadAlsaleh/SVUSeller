package com.svu_test.svusellerapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class SellerActivity extends AppCompatActivity {

    private static final int REQ_IMAGE_CODE = 111;
    private ImageView sellerIV;
    private Button addSellerBTN;
    private EditText addSellerNameET, addSellerNumberET;
    private Spinner addSellerRegionSP;
    private RecyclerView sellerRV;
    private Bitmap selectedBM;
    private LinearLayout sellerEditLL;
    private Button sellerEditBTN, sellerEditCancelBTN;
    private LinearLayout addSellerLL;
    private FloatingActionButton addSellerFAB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller);

        addSellerFAB = findViewById(R.id.addSellerFAB);
        addSellerLL = findViewById(R.id.addSellerLL);
        sellerIV = findViewById(R.id.addSellerIV);
        addSellerBTN = findViewById(R.id.addSellerBTN);
        addSellerNameET = findViewById(R.id.addSellerNameET);
        addSellerNumberET = findViewById(R.id.addSellerNumberET);
        addSellerRegionSP = findViewById(R.id.addSellerSP);
        sellerEditLL = findViewById(R.id.sellerEditLL);
        sellerEditCancelBTN = findViewById(R.id.sellerEditCancelBTN);
        sellerEditBTN = findViewById(R.id.sellerEditBTN);

        final ArrayAdapter aa = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{"Coast", "North", "South", "East", "Lebanon"}
        );
        addSellerRegionSP.setAdapter(aa);

        sellerIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQ_IMAGE_CODE);
            }
        });

        sellerEditCancelBTN.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                sellerEditLL.setVisibility(View.GONE);
                addSellerBTN.setVisibility(View.VISIBLE);

                addSellerNameET.setText("");
                addSellerNumberET.setText("");
                sellerIV.setImageDrawable(getDrawable(R.drawable.ic_launcher_background));
                selectedBM = null;
            }
        });

        addSellerBTN.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if (checkData()) {
                    SellerModel sellerModel = new SellerModel("", addSellerNameET.getText().toString(),
                            addSellerRegionSP.getSelectedItem().toString(),
                            addSellerNumberET.getText().toString(),
                            selectedBM);

                    DBHelper dbHelper = new DBHelper(SellerActivity.this);
                    dbHelper.insertSeller(sellerModel);

                    sellerIV.setImageDrawable(getDrawable(R.drawable.ic_launcher_background));
                    addSellerNameET.setText("");
                    addSellerNumberET.setText("");

                    SellerActivity.this.recreate();
                }
            }
        });

        addSellerFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSellerLL.setVisibility(View.VISIBLE);
            }
        });

        ArrayList<SellerModel> sellerModels = new DBHelper(this).getAllSellers();
        sellerRV = findViewById(R.id.sellersRV);
        sellerRV.setHasFixedSize(true);
        sellerRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        sellerRV.setAdapter(new SellersRVAdapter(this, this, sellerModels));

    }

    private boolean checkData() {
        if (addSellerNameET.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter name", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (addSellerNumberET.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter number", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (selectedBM == null) {
            Toast.makeText(this, "Select Picture", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public void setEditDate(final SellerModel sellerModel) throws NullPointerException {
        addSellerBTN.setVisibility(View.GONE);
        sellerEditLL.setVisibility(View.VISIBLE);
        addSellerNameET.setText(sellerModel.getName());
        addSellerNumberET.setText(sellerModel.getNumber());
        sellerIV.setImageBitmap(sellerModel.getImage());
        selectedBM = sellerModel.getImage();

        sellerEditBTN.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if (checkData()) {
                    SellerModel model = new SellerModel(sellerModel.getId(), addSellerNameET.getText().toString(),
                            addSellerRegionSP.getSelectedItem().toString(),
                            addSellerNumberET.getText().toString(),
                            selectedBM);

                    DBHelper dbHelper = new DBHelper(SellerActivity.this);
                    dbHelper.updateSeller(model);
                    SellerActivity.this.recreate();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (addSellerLL.getVisibility() == View.VISIBLE) {
            addSellerLL.setVisibility(View.GONE);
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_IMAGE_CODE) {
            try {
                final Uri imageUri = data != null ? data.getData() : null;
                assert imageUri != null;
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                sellerIV.setImageBitmap(selectedImage);
                selectedBM = selectedImage;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }
    }
}
