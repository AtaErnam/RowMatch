package com.example.RowMatch.repository;


import com.example.RowMatch.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface userRepository extends JpaRepository<User, Integer> {

    @Query("" +
            "SELECT CASE WHEN COUNT(s) > 0 THEN " +
            "TRUE ELSE FALSE END " +
            "FROM User s " +
            "WHERE s.coins = ?1"
    )
    Boolean selectExistsCoins(Integer coins);

}
