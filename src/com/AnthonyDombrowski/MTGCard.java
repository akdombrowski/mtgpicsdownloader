package com.AnthonyDombrowski;

import java.util.List;

public class MTGCard {
    private String name;
    private int power;
    private int toughness;
    private int type;
    private int numColors;
    private List<ManaColor> colors;

    public MTGCard(String name, int power, int toughness, int type, int numColors, List<ManaColor> colors) {
        this.name = name;
        this.power = power;
        this.toughness = toughness;
        this.type = type;
        this.numColors = numColors;
        this.colors = colors;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getToughness() {
        return toughness;
    }

    public void setToughness(int toughness) {
        this.toughness = toughness;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getNumColors() {
        return numColors;
    }

    public void setNumColors(int numColors) {
        this.numColors = numColors;
    }

    public List<ManaColor> getColors() {
        return colors;
    }

    public void setColors(List<ManaColor> colors) {
        this.colors = colors;
    }
}
