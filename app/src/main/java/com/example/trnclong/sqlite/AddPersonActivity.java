package com.example.trnclong.sqlite;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddPersonActivity extends Activity {
    EditText edtName,edtPhone,edtAddress;
    RadioGroup radioGender;
    RadioButton rbGender;
    Button btnAdd,btnCancel;
    Database db = new Database(this);
    Bundle b;
    ModelPerson mModelPerson  = new ModelPerson();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);
        addControls();
        addEvents();
    }
    private void addControls() {
        edtName = findViewById(R.id.edtName);
        edtAddress = findViewById(R.id.edtAddress);
        edtPhone = findViewById(R.id.edtPhone);
        radioGender = findViewById(R.id.radioGender);
        btnAdd = findViewById(R.id.btnAdd);
        btnCancel = findViewById(R.id.btnCancel);

        b = getIntent().getExtras();
        if (b!=null){
            mModelPerson.setmId(b.getInt("ID"));
            mModelPerson = db.getContact(mModelPerson.getmId());
            setData();
        }
    }

    private void setData() {
        btnAdd.setText("UPDATE");
        edtName.setText(mModelPerson.getmName());
        edtAddress.setText(mModelPerson.getmAddress());
        edtPhone.setText(mModelPerson.getmPhone());
        if (mModelPerson.getmGender().equals("Male")) {
            radioGender.check(R.id.radioMale);
        } else radioGender.check(R.id.radioFemale);
    }
    private void addEvents() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mModelPerson.setmName(edtName.getText().toString());
                mModelPerson.setmPhone(edtPhone.getText().toString());
                mModelPerson.setmAddress(edtAddress.getText().toString());
                long day = System.currentTimeMillis();
                String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date(day));
                mModelPerson.setmDate(date);
                String h = new SimpleDateFormat("hh:mm:ss").format(new Date(day));
                mModelPerson.setmTime(h);
                int idrad = radioGender.getCheckedRadioButtonId();
                rbGender = (RadioButton) findViewById(idrad);
                mModelPerson.setmGender(rbGender.getText().toString());

                if (b!=null){
                    db.updateContact(mModelPerson);
                } else {
                    mModelPerson.setmId(db.addContact(mModelPerson));
                }

                db.close();
                Intent intent = new Intent();
                Bundle b = new Bundle();
                b.putSerializable("RETURN", (Serializable) mModelPerson);
                intent.putExtras(b);
                setResult(RESULT_OK,intent);

                finish();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
