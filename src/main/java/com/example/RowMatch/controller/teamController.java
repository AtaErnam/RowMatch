package com.example.RowMatch.controller;

import com.example.RowMatch.Models.Team;
import com.example.RowMatch.service.teamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class teamController {

    @Autowired
    private teamService service;

    // USER CONTROLS

    @PostMapping("/createTeamRequest/{userId}")
    public ResponseEntity<String> createTeamRequest(@PathVariable int userId,@RequestBody Team team) {
        return service.createTeamRequest(team,userId);
    }

    @GetMapping("/joinTeamRequest/{userId}")
    public ResponseEntity<String> joinTeamRequest(@RequestBody Team team, @PathVariable int userId){
        return service.joinTeamRequest(team,userId);
    }

    @PutMapping("/leaveTeamRequest/{userId}")
    public ResponseEntity<String> leaveTeamRequest(@RequestBody Team team, @PathVariable int userId){
        return service.leaveTeamRequest(team,userId);
    }

    @GetMapping("/getTeamsReq")
    public ResponseEntity<List<Team>> getTeamsReq(){
        return service.getTeamsReq();
    }

    // ADMIN CONTROLS

    @PostMapping("/addTeam")
    public Team addTeam(@RequestBody Team team){
        return service.saveTeam(team);
    }

    @GetMapping("/teams")
    public List<Team> findAllTeams(){
        return service.getTeams();
    }

    @GetMapping("/team/{id}")
    public Team findTeam(@PathVariable int id){
        return service.getTeamById(id);
    }

    @PutMapping("/updateTeam")
    public Team updateTeam(@RequestBody Team team){
        return service.updateTeam(team);
    }

    @DeleteMapping("/deleteTeam/{id}")
    public String deleteTeam(@PathVariable int id){
        return service.deleteTeam(id);
    }
}
