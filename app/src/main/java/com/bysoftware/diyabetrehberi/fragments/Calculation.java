package com.bysoftware.diyabetrehberi.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.bysoftware.diyabetrehberi.R;

public class Calculation extends Fragment {

    EditText kan, hedefkan, duyarlilik, karbonhidrat, oran, et;
    Switch yag;
    Button hesapla;
    View root;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_calculation, container, false);
        kan = root.findViewById(R.id.kan);
        hedefkan = root.findViewById(R.id.hedef_kan);
        duyarlilik = root.findViewById(R.id.duyarlilik);
        karbonhidrat = root.findViewById(R.id.karbonhidrat);
        oran = root.findViewById(R.id.oran);
        et = root.findViewById(R.id.et);
        yag = root.findViewById(R.id.yag);
        hesapla = root.findViewById(R.id.hesapla);

        hesapla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_kan = kan.getText().toString();
                String txt_hedefkan = hedefkan.getText().toString();
                String txt_duyarlilik = duyarlilik.getText().toString();
                String txt_karbonhidrat = karbonhidrat.getText().toString();
                String txt_oran = oran.getText().toString();
                String txt_et = et.getText().toString();
                if (TextUtils.isEmpty(txt_kan) || TextUtils.isEmpty(txt_hedefkan) || TextUtils.isEmpty(txt_duyarlilik)
                        || TextUtils.isEmpty(txt_karbonhidrat) || TextUtils.isEmpty(txt_oran)){
                    Toast.makeText(getContext(), "Lütfen işaretlenmiş alanları doldurun", Toast.LENGTH_SHORT).show();
                } else {
                    int kansekeri = Integer.parseInt(txt_kan) - Integer.parseInt(txt_hedefkan);
                    int yemek_bolusu = Integer.parseInt(txt_karbonhidrat) / Integer.parseInt(txt_oran);
                    int duzeltme_bolusu = kansekeri / Integer.parseInt(txt_duyarlilik);
                    int insulin = yemek_bolusu + duzeltme_bolusu;

                    Toast.makeText(getContext(),  insulin + " ünite", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return root;
    }
}