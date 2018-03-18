package com.example.trnclong.sqlite;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private AdapterPerson mAdapter;
    private Database db;
    private List<ModelPerson> modelPersonList;
    private  int posClick = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControls();
    }
    private void addControls() {
        mRecyclerView = findViewById(R.id.rvContact);
        mRecyclerView.setHasFixedSize(true);

        db = new Database(this);
        modelPersonList = new ArrayList<>();
        modelPersonList = db.getAllContacts();

        mAdapter = new AdapterPerson(modelPersonList,MainActivity.this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickedListener(new AdapterPerson.OnItemClickedListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getBaseContext(), DetailActivity.class);
                intent.putExtra("ID",modelPersonList.get(position).getmId());
                startActivityForResult(intent, 2);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.add_person,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch (id){
            case R.id.add:
                Intent intent = new Intent(MainActivity.this, AddPersonActivity.class);
                startActivityForResult(intent,1);
                break;
            case R.id.deleteAll:
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
                alertBuilder.setTitle("DROP ALL DATA");
                alertBuilder.setMessage("Are you want you delete all ?");
                alertBuilder.setCancelable(false);
                alertBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteAll();
                    }
                });
                alertBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog alertDialog = alertBuilder.create();
                alertDialog.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAll() {
        db.deleteAllContact();
        modelPersonList.clear();
        mAdapter.notifyDataSetChanged();
        db.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK){
            ModelPerson mModelPerson = (ModelPerson) data.getExtras().getSerializable("RETURN");
            modelPersonList.add(mModelPerson);
            mAdapter.notifyDataSetChanged();
        }
        if (requestCode == 2 && resultCode == RESULT_OK){
            String contactID = data.getStringExtra("ID");
            int id = Integer.parseInt(contactID);
            int pos = 0;
            for (int i=0;i<modelPersonList.size();i++){
                if (modelPersonList.get(i).getmId()==id) {
                    pos = i;
                    break;
                }
            }
            modelPersonList.remove(pos);
            mAdapter.notifyDataSetChanged();
        }
        if (requestCode == 2 && resultCode == 1){
            String editID = data.getStringExtra("ID");
            int id = Integer.parseInt(editID);
            int pos=0;
            for (int i=0;i<modelPersonList.size();i++){
                if (modelPersonList.get(i).getmId()==id) {
                    pos = i;
                    break;
                }
            }
            modelPersonList.get(pos).setmName(db.getContact(id).getmName());
            db.close();
            mAdapter.notifyDataSetChanged();
        }
    }

}
