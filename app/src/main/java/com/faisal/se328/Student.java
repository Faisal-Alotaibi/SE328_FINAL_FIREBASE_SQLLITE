package com.faisal.se328;

public class Student {

    String  name , surname , fathname , nid , dob , gender;

    public Student() {

    }
    public Student(String name, String surname, String fathname, String nid, String dob, String gender) {

        this.name = name;
        this.surname = surname;
        this.fathname = fathname;
        this.nid = nid;
        this.dob = dob;
        this.gender = gender;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFathname() {
        return fathname;
    }

    public void setFathname(String fathname) {
        this.fathname = fathname;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return " " +
                "\nname='" + name + '\'' +
                " \nsurname='" + surname + '\'' +
                " \nfathname='" + fathname + '\'' +
                " \nnid='" + nid + '\'' +
                " \ndob='" + dob + '\'' +
                " \ngender='" + gender + '\''
                ;
    }
}
