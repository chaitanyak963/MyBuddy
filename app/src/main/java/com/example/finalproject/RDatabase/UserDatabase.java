package com.example.finalproject.RDatabase;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "UserDatabase")
public class UserDatabase {

    @PrimaryKey
    @NonNull
    int id;

    String name,mail,num,en1,en2,en3;

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getEn1() {
        return en1;
    }

    public void setEn1(String en1) {
        this.en1 = en1;
    }

    public String getEn2() {
        return en2;
    }

    public void setEn2(String en2) {
        this.en2 = en2;
    }

    public String getEn3() {
        return en3;
    }

    public void setEn3(String en3) {
        this.en3 = en3;
    }
}
