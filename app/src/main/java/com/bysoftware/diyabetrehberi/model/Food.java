package com.bysoftware.diyabetrehberi.model;

public class Food {

    String isim;
    String birim_1;
    String birim_2;
    String birim_1_deger;
    String birim_2_deger;
    String gorsel;

    public Food(String isim, String birim_1, String birim_2, String birim_1_deger, String birim_2_deger, String gorsel) {
        this.isim = isim;
        this.birim_1 = birim_1;
        this.birim_2 = birim_2;
        this.birim_1_deger = birim_1_deger;
        this.birim_2_deger = birim_2_deger;
        this.gorsel = gorsel;
    }

    public  Food(){

    }

    public String getIsim() {
        return isim;
    }

    public void setIsim(String isim) {
        this.isim = isim;
    }

    public String getBirim_1() {
        return birim_1;
    }

    public void setBirim_1(String birim_1) {
        this.birim_1 = birim_1;
    }

    public String getBirim_2() {
        return birim_2;
    }

    public void setBirim_2(String birim_2) {
        this.birim_2 = birim_2;
    }

    public String getBirim_1_deger() {
        return birim_1_deger;
    }

    public void setBirim_1_deger(String birim_1_deger) {
        this.birim_1_deger = birim_1_deger;
    }

    public String getBirim_2_deger() {
        return birim_2_deger;
    }

    public void setBirim_2_deger(String birim_2_deger) {
        this.birim_2_deger = birim_2_deger;
    }

    public String getGorsel() {
        return gorsel;
    }

    public void setGorsel(String gorsel) {
        this.gorsel = gorsel;
    }
}
