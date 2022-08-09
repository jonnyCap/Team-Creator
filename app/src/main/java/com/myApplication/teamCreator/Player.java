package com.myApplication.teamCreator;

public class Player {
    private String name;
    private int defaultStrength;
    private int specificStrength;


    public Player(String name, int defaultStrength){
        this.name = name;
        this.defaultStrength = defaultStrength;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getDefaultStrength() {
        return defaultStrength;
    }

    public void setDefaultStrength(int defaultStrength) {
        this.defaultStrength = defaultStrength;
    }

    public void setSpecificStrength(int specificStrength) {
        this.specificStrength = specificStrength;
    }
    public int getSpecificStrength(){
        return this.specificStrength;
    }
}