package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.myapplication.adapter.CoffeeAdapter;
import com.example.myapplication.model.Coffee;
import com.example.myapplication.model.CoffeeType;
import com.example.myapplication.model.CoffeeTypeButtons;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "PhotoUrl";
    private DatabaseReference db;
    List<Coffee> coffeeList= new ArrayList<Coffee>();
    List<Coffee> coffeeUseList= new ArrayList<Coffee>();
    StorageReference storageRef;
    RecyclerView coffeeRecycler;
    CoffeeAdapter coffeeAdapter;
    List<CoffeeTypeButtons> coffeeTypeButtons = new ArrayList<>();
    int coffeeTypeSelected =0;
    EditText searchCoffeeText;


//ДОБАВЛЕНИЯ КОФЯ
    int cCoffeeTypeId;
    String cName;
    String cDescription;
    String cImgPath;
    float cCost;
    ImageView imageView;
    String tempImgPath;
    Uri uploadUri;
//--------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db=FirebaseDatabase.getInstance().getReference("Coffee");

        getDataFromDB();


//        setContentView(R.layout.add_cofee);  //если нужно добавитть кофе - можно поменять активити
        //разкоментировать для добавления нового кофя
//        storageRef = FirebaseStorage.getInstance().getReference("imgDb");





    }

    private void initialise() {
        coffeeRecycler = findViewById(R.id.coffeeRecycler);
        RecyclerView.LayoutManager gridLayoutManager= new GridLayoutManager(getApplicationContext(),2);
        coffeeRecycler.setLayoutManager(gridLayoutManager);
        coffeeAdapter= new CoffeeAdapter(getApplicationContext(),coffeeUseList );
        coffeeRecycler.setAdapter(coffeeAdapter);
        coffeeTypeButtons.add(new CoffeeTypeButtons(findViewById(R.id.coffeeType_button0), findViewById(R.id.coffeeType_Text0)));
        coffeeTypeButtons.add(new CoffeeTypeButtons(findViewById(R.id.coffeeType_button1), findViewById(R.id.coffeeType_Text1)));
        coffeeTypeButtons.add(new CoffeeTypeButtons(findViewById(R.id.coffeeType_button2), findViewById(R.id.coffeeType_Text2)));
        searchCoffeeText= findViewById(R.id.coffeeSearch);



    }

//main
    public void StartApp(View view) {
        setContentView(R.layout.activity_store);
        initialise();



    }

    public void AddPicture(View view) {
        getImg();

    }

    private void getImg() {
        Intent intent= new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1&&data!=null && data.getData()!=null)
        {
            if (resultCode==RESULT_OK )
            {
                Log.d(TAG, data.getData().toString());
                tempImgPath= data.getData().toString();
                imageView= findViewById(R.id.imageView2);
                imageView.setImageURI(data.getData());
            }
        }
    }

    public void saveCoffee(View view) {
        initialiseCoffee();


    }


    private void uploadImg() {
        Bitmap bitmap= ((BitmapDrawable) imageView.getDrawable() ).getBitmap();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100, baos);
        byte[] byteArr= baos.toByteArray();
        StorageReference mRef=storageRef.child(System.currentTimeMillis()+"my_img");
        UploadTask up= mRef.putBytes(byteArr);
        Task<Uri> tast= up.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                return mRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                cImgPath= task.getResult().toString();
                Toast.makeText(getApplicationContext(),getString(R.string.img_was_uploaded), Toast.LENGTH_SHORT ).show();
//                cImgPath= uploadUri.toString();
//                Log.d(TAG, uploadUri.toString());


            }
        });


    }


    private void initialiseCoffee() {

       EditText editText= findViewById(R.id.addName);
        if (editText.getText().toString().equals("")) return;
           cName= editText.getText().toString();


        editText= findViewById(R.id.addDescription);
        if (editText.getText().toString().equals("")) return;
        cDescription=editText.getText().toString();

        editText= findViewById(R.id.addCoast);
        if (editText.getText().toString().equals("")) return;
        try {
            cCost= Float.parseFloat(editText.getText().toString());
        }catch (NumberFormatException e) {
            return;
        }

        editText= findViewById(R.id.addCoffeeTypeId);
        if (editText.getText().toString().equals("")) return;
        try {
            cCoffeeTypeId= Integer.parseInt(editText.getText().toString());
        }catch (NumberFormatException e) {
            return;
        }

       Log.d(TAG, String.valueOf(cCoffeeTypeId));

        uploadImg();

        addCoffeToDataBase();
    }

    private void addCoffeToDataBase() {
        String id= db.push().getKey();
        Coffee nCoffe= new Coffee(id,cCoffeeTypeId,cName,cDescription,cImgPath,cCost );

        if (id!=null)
        {
            db.child(id).setValue(nCoffe);
            Toast.makeText(this, getString(R.string.saved), Toast.LENGTH_SHORT ).show();
        }
    }

    private void getDataFromDB()
    {

        ValueEventListener vListener = new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (coffeeList.size()>0)coffeeList.clear();
                for (DataSnapshot ds: snapshot.getChildren())
                {
                    Coffee coffee= ds.getValue(Coffee.class);
                    assert coffeeList!=null;
                    coffeeList.add(coffee);
                    coffeeUseList = new ArrayList<>(coffeeList);

                }
//                coffeeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        db.addValueEventListener(vListener);
    }

    public void addToCart(View view) {


    }

    public void coffeeTypeSelect0(View view) {
        coffeeTypeButtonUnSelected();

        coffeeTypeSelected=0;
        coffeeTypeButtonSelected(coffeeTypeSelected);
    }

    public void coffeeTypeSelect1(View view) {
        coffeeTypeButtonUnSelected();

        coffeeTypeSelected=1;
        coffeeTypeButtonSelected(coffeeTypeSelected);

    }

    public void coffeeTypeSelect2(View view) {
        coffeeTypeButtonUnSelected();

        coffeeTypeSelected=2;
        coffeeTypeButtonSelected(coffeeTypeSelected);
    }





    public void coffeeTypeButtonSelected(int id)
    {
        coffeeTypeButtons.get(id).getCoffeeButton().setImageResource(R.drawable.pressed_choose_coffe_btn);
        coffeeTypeButtons.get(id).getCoffeeName().setTextColor(Color.WHITE);
        coffeeUseList.clear();
        for ( Coffee coffee:coffeeList)
        {
            if (coffee.getCoffeeTypeId()==id)
            {
                coffeeUseList.add(coffee);
            }
        }
       coffeeAdapter.notifyDataSetChanged();


    }


    public void coffeeTypeButtonUnSelected()
    {
        for (int i = 0; i <coffeeTypeButtons.size(); i++) {
           coffeeTypeButtons.get(i).getCoffeeButton().setImageResource(R.drawable.unpressed_choose_coffe_btn);
           coffeeTypeButtons.get(i).getCoffeeName().setTextColor(ContextCompat.getColor(this, R.color.unpressed_button_coffee_type));
        }
    }

    public void searchCoffee(View view) {
        String coffeeName = searchCoffeeText.getText().toString();
        coffeeUseList.clear();
        if (!coffeeName.equals(""))
        {
            for (Coffee coffee:coffeeList) {
                if (coffee.getName().toLowerCase().contains(coffeeName.toLowerCase()))
                {
                    coffeeUseList.add(coffee);
                }

            }
        }
        else
        {
            for (Coffee coffee:coffeeList) {
                    coffeeUseList.add(coffee);
            }
            Toast.makeText(getApplicationContext(), "сброс настроек, показываю все кофе", Toast.LENGTH_LONG).show();
        }


        coffeeAdapter.notifyDataSetChanged();
    }
}