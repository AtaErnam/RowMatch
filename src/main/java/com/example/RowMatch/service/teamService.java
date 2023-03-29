package com.example.RowMatch.service;


import com.example.RowMatch.Models.Team;
import com.example.RowMatch.Models.User;
import com.example.RowMatch.repository.teamRepository;
import com.example.RowMatch.repository.userRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class teamService {

    private final teamRepository repository;
    private final userRepository userRepository;

    public teamService(teamRepository repository, userRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    // USER SERVICE

    public ResponseEntity<String> createTeamRequest(Team team, int userId) {
        if (repository.findByName(team.getName()).orElse(null) != null) {
            return ResponseEntity.status(400).body("Team name was already taken!");
        }

        User existingUser = userRepository.findById(userId).orElse(null);
        if (existingUser.getCoins() < 1000) {
            return ResponseEntity.status(200).body("You don't have enough coins!!");
        }
        existingUser.setCoins(existingUser.getCoins() - 1000);

        List<Integer> newTeamList = new ArrayList<>();
        newTeamList.add(userId);
        Team createdTeam = new Team(team.getName(), newTeamList);

        userRepository.save(existingUser);
        repository.save(createdTeam);
        return ResponseEntity.status(200).body("Team " + team.getName() + " was created successfully!");
    }

    public ResponseEntity<String> joinTeamRequest(Team team, int userId) {
        Team existingTeam = repository.findByName(team.getName()).orElse(null);
        if (existingTeam == null) {
            return ResponseEntity.status(400).body("Team doesn't exist!!");
        }

        List<Integer> updatedTeam = null;
        if (existingTeam.getMembers() == null){
            updatedTeam = new ArrayList<>();
        }
        else {
            updatedTeam = existingTeam.getMembers();
        }

        if (updatedTeam.contains(Integer.valueOf(userId))) {
            return ResponseEntity.status(400).body("You are already in this team");
        } else if (updatedTeam.size() >= 10) {
            return ResponseEntity.status(200).body("This team is full!");
        } else {
            updatedTeam.add(userId);
            existingTeam.setMembers(updatedTeam);

            repository.save(existingTeam);
            return ResponseEntity.status(200).body(team.getName() + " team has been updated. Members: " + existingTeam.getMembers().toString() + "," + userId);
        }
    }

    public ResponseEntity<String> leaveTeamRequest(Team team, int userId) {
        Team existingTeam = repository.findByName(team.getName()).orElse(null);
        if (existingTeam == null) {
            return ResponseEntity.status(400).body("Team doesn't exist!!");
        }
        List<Integer> updatedTeam = existingTeam.getMembers();

        updatedTeam.remove(Integer.valueOf(userId));
        existingTeam.setMembers(updatedTeam);

        repository.save(existingTeam);
        return ResponseEntity.status(200).body("You have successfully left the team");
    }

    public ResponseEntity<List<Team>> getTeamsReq() {
        List<Team> availableTeams = repository.findAll();
        Random rand = new Random();
        availableTeams.removeIf(availableTeam -> availableTeam.getMembers().size() >= 10);
        while (availableTeams.size() > 10) {
            int randomIndex = rand.nextInt(availableTeams.size());
            availableTeams.remove(randomIndex);
        }
        if (availableTeams.size() > 0) {
            return ResponseEntity.status(200).body(availableTeams);
        }
        return ResponseEntity.status(200).body(null);
    }

    // ADMIN SERVICE
    public Team saveTeam(Team team) {
        boolean existsTeam = repository
                .selectExistsName(team.getName());
        if (existsTeam) {
            throw new RuntimeException(
                    "Team name " + team.getName() + " taken"
            );
        }
        return repository.save(team);
    }

    public List<Team> getTeams() {
        return repository.findAll();
    }

    public Team getTeamById(int id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format("Team does not exist for id = %s!", id)));
    }

    public String deleteTeam(int id) {
        repository.deleteById(id);
        return "Team with id: " + id + " is removed!";
    }

    public Team updateTeam(Team team) {
        Team existingTeam = repository.findById(team.getId()).orElseThrow(() -> new EntityNotFoundException(String.format("Team doesnt exist for id = %s!", team.getId())));
        existingTeam.setName(team.getName());
        existingTeam.setMembers(team.getMembers());

        return repository.save(existingTeam);
    }
}
