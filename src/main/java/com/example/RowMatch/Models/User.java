package com.example.RowMatch.Models;

import jakarta.persistence.*;

@Entity
@Table(name = "user_tbl")
public class User {

    @Id
    @GeneratedValue
    private int id;
    @Column(nullable = false)
    private Integer coins;
    @Column(nullable = false)
    private Integer level;

    public User() {
    }

    public User(Integer coins, Integer level) {
        this.coins = coins;
        this.level = level;
    }

    public Integer getCoins() {
        return coins;
    }

    public void setCoins(Integer coins) {
        this.coins = coins;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", coins=" + coins +
                ", level=" + level +
                '}';
    }
}
