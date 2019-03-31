package com.example.quizapp;

public class Rank {
    String username;
    int Score,Order;
    public Rank(){

    }

    public Rank(String username, int score, int order) {
        this.username = username;
        Score = score;
        Order = order;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getScore() {
        return Score;
    }

    public void setScore(int score) {
        Score = score;
    }

    public int getOrder() {
        return Order;
    }

    public void setOrder(int order) {
        Order = order;
    }
}
