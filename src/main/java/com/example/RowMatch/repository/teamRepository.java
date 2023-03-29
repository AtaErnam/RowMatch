package com.example.RowMatch.repository;

import com.example.RowMatch.Models.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface teamRepository extends JpaRepository<Team, Integer> {
    Optional<Team> findByName(String name);

    @Query("" +
            "SELECT CASE WHEN COUNT(t) > 0 THEN " +
            "TRUE ELSE FALSE END " +
            "FROM Team t " +
            "WHERE t.name = ?1"
    )
    Boolean selectExistsName(String name);
}
