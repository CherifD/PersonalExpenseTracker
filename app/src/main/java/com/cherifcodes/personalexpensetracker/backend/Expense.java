package com.cherifcodes.personalexpensetracker.backend;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.text.TextUtils;

import java.util.Date;

@Entity
public class Expense {
    @PrimaryKey (autoGenerate = true)
    private int id;
    private String category;
    private double amount;
    private String businessName;
    private Date date;

    public Expense(String category, String businessName, double amount, Date date) {
        this.setCategory(category);
        this.setBusinessName(businessName);
        this.setAmount(amount);
        this.setDate(date);
    }

    public int getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }

    public String getBusinessName() {
        return businessName;
    }

    public Date getDate() {
        return date;
    }

    public void setCategory(String category) {
        if (TextUtils.isEmpty(category))
            throw new IllegalArgumentException("Invalid category.");
        this.category = category;
    }

    public void setAmount(double amount) {
        if (amount <= 0)
            throw new IllegalArgumentException("Invalid expense amount.");
        this.amount = amount;
    }

    public void setBusinessName(String businessName) {
        if (TextUtils.isEmpty(businessName))
            throw new IllegalArgumentException("Invalid business name.");
        this.businessName = businessName;
    }

    public void setDate(Date date) {
        if (date == null || date.compareTo(new Date()) < 0)
            throw new IllegalArgumentException("Invalid expense date.");
        this.date = date;
    }
}
