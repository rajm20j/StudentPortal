package com.example.studentportal;

public class UserInfo {
    String name,email,branch,hostel,roll,contact;

    public UserInfo()
    {

    }

    public UserInfo(String name, String email, String branch, String hostel, String roll, String contact) {
        this.name = name;
        this.email = email;
        this.branch = branch;
        this.hostel = hostel;
        this.roll = roll;
        this.contact = contact;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getBranch() {
        return branch;
    }

    public String getHostel() {
        return hostel;
    }

    public String getRoll() {
        return roll;
    }

    public String getContact() {
        return contact;
    }
}
