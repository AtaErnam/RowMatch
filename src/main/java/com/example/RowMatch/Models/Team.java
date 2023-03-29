package com.example.RowMatch.Models;

import jakarta.persistence.*;


import java.util.Arrays;
import java.util.List;


@Entity
@Table(name = "TEAM_TBL")
public class Team{

    @Id
    @GeneratedValue
    private int id;
    private String name;

    @ElementCollection
    private List<Integer> members;

    public Team() {
    }

    public Team(String name, List<Integer> members) {
        this.name = name;
        this.members = members;
    }

    public Team(int id,String name, List<Integer> members) {
        this.id = id;
        this.name = name;
        this.members = members;
    }

    public String getInfo() {
        StringBuilder retVal = new StringBuilder("Name:" + name + "\n");
        retVal.append("Members\n");

        for (Integer member : members) {
            retVal.append(member);
        }

        return retVal.toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getMembers() {
        return members;
    }

    public void setMembers(List<Integer> members) {
        this.members = members;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
            return "Team{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", members=" + Arrays.toString(members.toArray()) +
                    '}';
    }
}
