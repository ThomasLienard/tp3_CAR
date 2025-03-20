package com.example.TP3_CAR.Dto;

public class ReducerDTO {
    
    private String word;
    private int count;

    public ReducerDTO(String word) {
        this.word = word;
    }
    
    public String getWord() {
        return word;
    }
    public void setWord(String word) {
        this.word = word;
    }
    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }
}
