package com.example;

public class Ranking {
    private int rank;
    private String username;
    private int score;

    public Ranking(int rank, String username, int score) {
        this.rank = rank;
        this.username = username;
        this.score = score;
    }

    // Getters và setters cho các thuộc tính

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
