package com.example.RowMatch.service;

import com.example.RowMatch.Models.Team;
import com.example.RowMatch.Models.User;
import com.example.RowMatch.repository.teamRepository;
import com.example.RowMatch.repository.userRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class teamServiceTest {

    @Mock
    private teamRepository teamRepository;
    @Mock
    private userRepository userRepository;
    private teamService underTest;

    @BeforeEach
    void setUp(){
        underTest = new teamService(teamRepository,userRepository);
    }

    @Test
    public void testCreateTeamRequestSuccess() {
        final String teamName = "Team 1";
        final int userId = 1;
        final User user = new User();
        user.setCoins(1500);

        when(teamRepository.findByName(eq(teamName))).thenReturn(Optional.empty());
        when(userRepository.findById(eq(userId))).thenReturn(Optional.of(user));
        when(userRepository.save(eq(user))).thenReturn(user);
        when(teamRepository.save(any(Team.class))).thenReturn(new Team(teamName, Collections.singletonList(userId)));

        ResponseEntity<String> response = underTest.createTeamRequest(new Team(teamName,new ArrayList<>())  , userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Team " + teamName + " was created successfully!", response.getBody());
    }

    @Test
    public void testCreateTeamRequestTeamAlreadyExists() {
        final String teamName = "Existing Team";
        final int userId = 2;

        when(teamRepository.findByName(eq(teamName))).thenReturn(Optional.of(new Team(teamName,new ArrayList<>())));

        ResponseEntity<String> response = underTest.createTeamRequest(new Team(teamName,new ArrayList<>()), userId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Team name was already taken!", response.getBody());
    }

    @Test
    public void testCreateTeamRequestNotEnoughCoins() {
        final String teamName = "Team 2";
        final int userId = 3;
        final User user = new User();
        user.setCoins(500);

        when(teamRepository.findByName(eq(teamName))).thenReturn(Optional.empty());
        when(userRepository.findById(eq(userId))).thenReturn(Optional.of(user));

        ResponseEntity<String> response = underTest.createTeamRequest(new Team(teamName,new ArrayList<>()), userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("You don't have enough coins!!", response.getBody());
    }

    @Test
    public void testJoinTeamRequest_whenTeamExistsAndNotFullAndUserNotInTeam_returnsSuccess() {
        // arrange
        int userId = 1;
        String teamName = "team1";
        List<Integer> members = new ArrayList<>(Arrays.asList(2, 3, 4));
        Team existingTeam = new Team(teamName, members);
        when(teamRepository.findByName(anyString())).thenReturn(Optional.of(existingTeam));

        // act
        ResponseEntity<String> response = underTest.joinTeamRequest(existingTeam, userId);

        // assert
        verify(teamRepository, times(1)).save(existingTeam);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(teamName + " team has been updated. Members: " + members.toString() + "," + userId, response.getBody());
    }

    @Test
    public void testJoinTeamRequest_whenTeamDoesNotExist_returnsBadRequest() {
        // arrange
        int userId = 1;
        String teamName = "team1";
        when(teamRepository.findByName(anyString())).thenReturn(Optional.empty());

        // act
        ResponseEntity<String> response = underTest.joinTeamRequest(new Team(teamName, new ArrayList<>()), userId);

        // assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Team doesn't exist!!", response.getBody());
    }

    @Test
    public void testJoinTeamRequest_whenUserAlreadyInTeam_returnsBadRequest() {
        // arrange
        int userId = 1;
        String teamName = "team1";
        List<Integer> members = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
        Team existingTeam = new Team(teamName, members);
        when(teamRepository.findByName(anyString())).thenReturn(Optional.of(existingTeam));

        // act
        ResponseEntity<String> response = underTest.joinTeamRequest(existingTeam, userId);

        // assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("You are already in this team", response.getBody());
    }

    @Test
    public void testJoinTeamRequest_whenTeamIsFull() {
        // arrange
        int userId = 1;
        String teamName = "team1";
        List<Integer> members = new ArrayList<>(Arrays.asList(2, 3, 4, 5, 6, 7, 8, 9, 10, 11));
        Team existingTeam = new Team(teamName, members);
        when(teamRepository.findByName(anyString())).thenReturn(Optional.of(existingTeam));

        // act
        ResponseEntity<String> response = underTest.joinTeamRequest(existingTeam, userId);

        // assert
        verify(teamRepository, times(0)).save(existingTeam);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("This team is full!", response.getBody());
    }

    @Test
    public void leaveTeamRequestExistingTeamSuccess() {
        // arrange
        Team team = new Team("Team A", new ArrayList<>(List.of(1, 2, 3)));
        int userId = 2;
        Optional<Team> existingTeam = Optional.of(team);
        when(teamRepository.findByName(team.getName())).thenReturn(existingTeam);

        // act
        ResponseEntity<String> result = underTest.leaveTeamRequest(team, userId);

        // assert
        assertEquals(200, result.getStatusCodeValue());
        assertEquals("You have successfully left the team", result.getBody());
        verify(teamRepository, times(1)).findByName(team.getName());
        verify(teamRepository, times(1)).save(team);
        assertEquals(List.of(1, 3), team.getMembers());
    }

    @Test
    public void leaveTeamRequestNonExistingTeam() {
        // arrange
        Team team = new Team("Team A", new ArrayList<>(List.of(1, 2, 3)));
        int userId = 4;
        when(teamRepository.findByName(team.getName())).thenReturn(Optional.empty());

        // act
        ResponseEntity<String> result = underTest.leaveTeamRequest(team, userId);

        // assert
        assertEquals(400, result.getStatusCodeValue());
        assertEquals("Team doesn't exist!!", result.getBody());
        verify(teamRepository, times(1)).findByName(team.getName());
    }

    @Test
    public void testGetTeamsReq() {
        // mock repository response
        List<Team> teams = new ArrayList<>();
        teams.add(new Team("Team 1", List.of(new User().getId(), new User().getId())));
        teams.add(new Team("Team 2", List.of(new User().getId(), new User().getId())));
        teams.add(new Team("Team 3", List.of(new User().getId(), new User().getId())));
        when(teamRepository.findAll()).thenReturn(teams);

        // test method
        ResponseEntity<List<Team>> response = underTest.getTeamsReq();

        // check response
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(3, response.getBody().size());
    }

    @Test
    void canSaveTeam() {
        //given
        Team team = new Team(
                "TeamTest",
                new ArrayList<>()
        );
        //when
        underTest.saveTeam(team);

        //then
        ArgumentCaptor<Team> teamArgumentCaptor=
                ArgumentCaptor.forClass(Team.class);

        verify(teamRepository)
                .save(teamArgumentCaptor.capture());
        Team capturedTeam = teamArgumentCaptor.getValue();

        assertThat(capturedTeam).isEqualTo(team);
    }

    @Test
    void willThrowWhenTeamNameIsTaken() {
        //given
        Team team = new Team(
                "TeamTest",
                new ArrayList<>()
        );
        given(teamRepository.selectExistsName(team.getName()))
                .willReturn(true);

        //then
        assertThatThrownBy(() -> underTest.saveTeam(team))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Team name " + team.getName() + " taken");

        verify(teamRepository, never()).save(any());
    }

    @Test
    void canGetAllTeams() {
        //when
        underTest.getTeams();
        //then
        verify(teamRepository).findAll();
    }

    @Test
    void throwExceptionWhenTeamIdNotFound() {
        //given
        int id = anyInt();
        //when
        when(teamRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.getTeamById(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(String.format("Team does not exist for id = %s!", id));

        verify(teamRepository).findById(id);
    }

    @Test
    void canDeleteTeam() {
        int testId=42;

        // perform the call
        underTest.deleteTeam(testId);

        // verify the mocks
        verify(teamRepository, times(1)).deleteById(eq(testId));
    }

    @Test
    public void testUpdateTeam() {
        Team existingTeam = new Team(1, "Team A", new ArrayList<>());
        Team updatedTeam = new Team(1, "Team B", new ArrayList<>());
        when(teamRepository.findById(1)).thenReturn(Optional.of(existingTeam));
        when(teamRepository.save(existingTeam)).thenReturn(updatedTeam);

        // Act
        Team result = underTest.updateTeam(updatedTeam);

        // Assert
        assertEquals("Team B", result.getName());
        assertEquals(existingTeam.getMembers(), result.getMembers());
    }

    @Test()
    public void testUpdateTeamNotFound() throws EntityNotFoundException{
        // Arrange
        Team updatedTeam = new Team(1, "Team B", new ArrayList<>());
        when(teamRepository.findById(1)).thenReturn(Optional.empty());

        // Act
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            underTest.updateTeam(updatedTeam);
        });

        assertEquals("Team doesnt exist for id = 1!", exception.getMessage());
    }
}