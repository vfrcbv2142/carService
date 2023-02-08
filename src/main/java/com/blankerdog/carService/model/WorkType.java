package com.blankerdog.carService.model;

public enum WorkType {
    PAINTING ("painting"),
    SOLDERING ("soldering"),
    DISASSEMBLING ("disassembling"),
    STRAIGHTENING ("straightening"),
    PREPARINGALUMINUM ("preparingAluminum"),
    PREPARINGPLASTIC ("preparingPlastic"),
    PREPARINGIRON ("preparingIron");

    private String name;

    WorkType(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
