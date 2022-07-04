package com.bysoftware.diyabetrehberi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bysoftware.diyabetrehberi.fragments.AddFood;
import com.bysoftware.diyabetrehberi.fragments.Calculation;
import com.bysoftware.diyabetrehberi.fragments.FoodCalculation;
import com.bysoftware.diyabetrehberi.model.Food;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class FoodAddActivity extends AppCompatActivity {

    private static final int IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageTask uploadTask;
    StorageReference storageReference;
    DatabaseReference reference;
    FirebaseUser fuser;
    ImageView imageView;
    FirebaseAuth mAuth;
    Button add;
    String txt_name;
    TextInputEditText name, birim1, birim2, birim1deger, birim2deger;

    public final int ID_FOOD_CALCULATION =1 ;
    public final int ID_CALCULATION =2;
    public final int ID_ADD_FOOD = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food__add);
        add = findViewById(R.id.add);
        name = findViewById(R.id.editTextName);
        birim1 = findViewById(R.id.editTextBirim1);
        birim2 = findViewById(R.id.editTextBirim2);
        birim2deger = findViewById(R.id.editTextBirim2deger);
        birim1deger = findViewById(R.id.editTextBirim1deger);
        imageView = findViewById(R.id.addimage);

        mAuth = FirebaseAuth.getInstance();
        signInAnonymously();
        storageReference = FirebaseStorage.getInstance().getReference("uploads");

        if (ActivityCompat.shouldShowRequestPermissionRationale(FoodAddActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(FoodAddActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        } else {
            ActivityCompat.requestPermissions(FoodAddActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_name = name.getText().toString();
                String txt_birim1 = birim1.getText().toString();
                String txt_birim2 = birim2.getText().toString();
                String txt_birim1deger = birim1deger.getText().toString();
                String txt_birim2deger = birim2deger.getText().toString();

                if (TextUtils.isEmpty(txt_name)) {
                    Toast.makeText(FoodAddActivity.this, "Yemek adı zorunludur!", Toast.LENGTH_SHORT).show();
                } else {
                    if (imageView.getDrawable() == null || imageUri == null) {
                        Toast.makeText(FoodAddActivity.this, "Lütfen Yemeği Temsil Eden Bir Görsel Seçin", Toast.LENGTH_SHORT).show();
                    } else {
                        addCategory(txt_name, txt_birim1, txt_birim2, txt_birim1deger, txt_birim2deger);
                        Intent intent = new Intent(FoodAddActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });

        MeowBottomNavigation meowBottomNavigation = findViewById(R.id.bottomNavigation);

        meowBottomNavigation.add(new MeowBottomNavigation.Model(ID_FOOD_CALCULATION, R.drawable.dish1));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(ID_CALCULATION, R.drawable.siringa));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(ID_ADD_FOOD, R.drawable.ic_baseline_post_add_24));

        meowBottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                Fragment fragment = null;
                switch (item.getId()) {

                    case ID_FOOD_CALCULATION:
                        //fragment = new FoodCalculation();
                        Intent intent = new Intent(getApplicationContext(), FoodCalculation.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        break;
                    case ID_CALCULATION:

                        Intent intent1 = new Intent(getApplicationContext(), FoodAddActivity.class);
                        startActivity(intent1);
                        overridePendingTransition(0, 0);
                        fragment = new Calculation();
                        break;
                    case ID_ADD_FOOD:
                        Intent intent2 = new Intent(getApplicationContext(), AddFood.class);
                      //  startActivity(intent2);
                        overridePendingTransition(0, 0);
                        fragment = new AddFood();
                        break;

                }
                //loadFragment(fragment);
            }
        });
        meowBottomNavigation.setCount(1, "10");
        meowBottomNavigation.show(2, true);

        meowBottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {

            }
        });
    }
    public void addCategory(String name, String birim_1,String birim_2, String birim_1_deger,String birim_2_deger){
        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance("https://diyabet-rehberi-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Foods").child(txt_name);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Food food = snapshot.getValue(Food.class);
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("isim", name);
                hashMap.put("birim_1", birim_1);
                hashMap.put("birim_2", birim_2);
                hashMap.put("birim_1_deger", birim_1_deger);
                hashMap.put("birim_2_deger", birim_2_deger);
                reference.updateChildren(hashMap);
                if (uploadTask != null && uploadTask.isInProgress()) {
                    Toast.makeText(FoodAddActivity.this, "Upload in preogress", Toast.LENGTH_SHORT).show();
                } else {
                    uploadImage();
                    // finish();
                }
                // Glide.with(CategoryAddActivity.this).load(category.getImage()).into(imageView);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void openImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }
    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = this.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage() {
        //final ProgressDialog pd = new ProgressDialog(this);
        //pd.setMessage("Uploading");
        // pd.show();

        if (imageUri != null) {
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(imageUri));

            uploadTask = fileReference.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();

                        reference = FirebaseDatabase.getInstance("https://diyabet-rehberi-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Foods").child(txt_name);
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("gorsel", "" + mUri);
                        reference.updateChildren(map);
                        uploadTask.cancel();

                        // pd.dismiss();
                        //  finish();
                    } else {
                        Toast.makeText(FoodAddActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                        //pd.dismiss();
                    }


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(FoodAddActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    // pd.dismiss();
                }
            });
        } else {
            Toast.makeText(FoodAddActivity.this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                Bitmap imageToStore = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imageView.setImageBitmap(imageToStore);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (uploadTask != null && uploadTask.isInProgress()) {
                Toast.makeText(FoodAddActivity.this, "Upload in preogress", Toast.LENGTH_SHORT).show();
            } else {
                // uploadImage();
            }
        }
    }
    private void signInAnonymously() {
        mAuth.signInAnonymously().addOnSuccessListener(this, new  OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                // do your stuff
            }
        })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e(TAG, "signInAnonymously:FAILURE", exception);
                    }
                });
    }
}