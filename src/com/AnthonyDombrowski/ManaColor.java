package com.AnthonyDombrowski;

enum ManaColor {

    RED("MOUNTAIN"),

    GREEN("FOREST"),

    BLUE("ISLAND"),

    White("PLAINS"),

    BLACK("SWAMP");


    private String typeName;

    public String getTypeName() {
        return this.typeName;
    }
    ManaColor(String typeName) {
        this.typeName = typeName;
    }
}
