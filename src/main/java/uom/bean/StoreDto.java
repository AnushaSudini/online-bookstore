/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uom.bean;

import java.io.Serializable;

/**
 *
 * @author Anusha
 */
public class StoreDto implements Serializable {

    private int bookstoreID;
    private String address;
    private long phoneNumber;
    private String emailID;
    private int userID;
   
    private int locationID;

    public int getLocationID() {
        return locationID;
    }

    public void setLocationID(int locationID) {
        this.locationID = locationID;
    }

    public int getBookstoreID() {
        return bookstoreID;
    }

    public void setBookstoreID(int bookstoreID) {
        this.bookstoreID = bookstoreID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

   
}
