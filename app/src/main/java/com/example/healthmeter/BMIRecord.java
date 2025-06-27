package com.example.healthmeter;

public class BMIRecord {
    public String name;
    public int age;
    public float bmi;
    public String category;
    public int weight;
    public int height;

    public BMIRecord() {
        // Required empty constructor for Firebase
    }

    public BMIRecord(String name, int age, float bmi, String category) {
        this.name = name;
        this.age = age;
        this.bmi = bmi;
        this.category = category;
        this.weight = weight;
        this.height = height;
    }

    @Override
    public String toString() {
        return "Name: " + name + "\nAge: " + age + "\nBMI: " + bmi + "\nCategory: " + category + "\nWeight: "+weight + "\n Height: "+height;
    }
}

