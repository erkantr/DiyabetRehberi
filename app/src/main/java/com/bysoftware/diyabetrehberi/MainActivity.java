package com.bysoftware.diyabetrehberi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Switch;

import com.bysoftware.diyabetrehberi.fragments.AddFood;
import com.bysoftware.diyabetrehberi.fragments.Calculation;
import com.bysoftware.diyabetrehberi.fragments.FoodCalculation;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    public final int ID_FOOD_CALCULATION =1 ;
    public final int ID_CALCULATION =2;
    public final int ID_ADD_FOOD = 3;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        signInAnonymously();

        MeowBottomNavigation meowBottomNavigation = findViewById(R.id.bottomNavigation);

        meowBottomNavigation.add(new MeowBottomNavigation.Model(ID_FOOD_CALCULATION, R.drawable.dish1));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(ID_CALCULATION,R.drawable.siringa));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(ID_ADD_FOOD,R.drawable.ic_baseline_post_add_24));

        meowBottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                Fragment fragment = null;
                switch(item.getId()){

                    case ID_FOOD_CALCULATION:
                        fragment = new FoodCalculation();
                       // Intent intent = new Intent(getApplicationContext(), Foods.class);
                       // startActivity(intent);
                        //loadFragment(fragment);
                      // overridePendingTransition(0,0);
                        break;
                    case  ID_CALCULATION:

                        Intent intent1 = new Intent(getApplicationContext(), Foods.class);
                       // startActivity(intent1);
                        overridePendingTransition(0,0);
                        fragment = new Calculation();
                        break;
                    case ID_ADD_FOOD:
                        Intent intent2 = new Intent(getApplicationContext(), Foods.class);
                       // startActivity(intent2);
                        overridePendingTransition(0,0);
                        fragment = new AddFood();
                        break;

                }
                loadFragment(fragment);
            }
        });
        meowBottomNavigation.setCount(1, "10");
        meowBottomNavigation.show(2,true);

        meowBottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {

            }
        });
    }
    private void loadFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,fragment).commit();
    }
    private void signInAnonymously() {
        mAuth.signInAnonymously().addOnSuccessListener( this, new  OnSuccessListener<AuthResult>() {
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