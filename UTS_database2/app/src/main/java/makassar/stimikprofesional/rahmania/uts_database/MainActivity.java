package makassar.stimikprofesional.rahmania.uts_database;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    private Button btnTR;
    private ListView listBank;
    String[] daftarBank;
    DataHelper dbHelper;
    protected Cursor cursor;
    public static MainActivity HalamanUtama;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnTR = (Button)findViewById(R.id.btInput);

        btnTR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createMhs = new Intent(MainActivity.this,inputData.class);
                startActivity(createMhs);
            }
        });
        HalamanUtama = this;
        dbHelper = new DataHelper(this);
        RefreshList();
    }

    public void RefreshList(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM databanknya", null);
        daftarBank = new String[cursor.getCount()];
        cursor.moveToFirst();
        for (int posRec = 0; posRec < cursor.getCount(); posRec++){
            cursor.moveToPosition(posRec);
            daftarBank[posRec] = cursor.getString(1).toString();
        }
        listBank = (ListView)findViewById(R.id.listBank);
        listBank.setAdapter(new ArrayAdapter(this,android.R.layout.simple_expandable_list_item_1,daftarBank));
        listBank.setSelected(true);

        listBank.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String pilihan = daftarBank[position];
                final CharSequence[] itemPilihan = {"Lihat Data Bank","Update Data Bank","Hapus Data Bank"};

                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Pilihan");
                builder.setItems(itemPilihan, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                Intent viewBank = new Intent(getApplicationContext(), lihatData.class);
                                viewBank.putExtra("nama", pilihan);
                                startActivity(viewBank);
                                break;
                            case 1:
                                Intent updateBank = new Intent(getApplicationContext(), updateData.class);
                                updateBank.putExtra("nama", pilihan);
                                startActivity(updateBank);
                                break;
                            case 2:


                                AlertDialog.Builder ikan = new AlertDialog.Builder(MainActivity.this);

                                ikan.setTitle("Hapus");
                                ikan.setMessage("Hapus  data base "+pilihan+" ?");
                                ikan.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        SQLiteDatabase db = dbHelper.getReadableDatabase();
                                        db.execSQL("DELETE FROM databanknya WHERE nomor_rekening = '"+pilihan+"'");
                                        RefreshList();
                                    }
                                })
                                        .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        });
                                ikan.show();
                                break;
                        }
                    }
                });
                builder.create().show();
            }

        });
    }


}



