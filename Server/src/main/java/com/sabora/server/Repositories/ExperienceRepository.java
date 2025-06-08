package com.sabora.server.Repositories;

import com.sabora.server.Entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExperienceRepository extends JpaRepository<Experience, Integer>{
    Experience findById(int id);

    @Query("""
    SELECT e FROM Experience e 
    JOIN e.food f 
    WHERE e.client = :client 
      AND e.scenario = :scenario 
      AND e.sound = :sound 
      AND f IN :foods
    GROUP BY e
    HAVING COUNT(DISTINCT f) = :foodCount
    """)
    List<Experience> findByClientAndScenarioAndSoundAndAllFoods(
            @Param("client") Cliente client,
            @Param("scenario") Scenario scenario,
            @Param("sound") Sound sound,
            @Param("foods") List<Food> foods,
            @Param("foodCount") long foodCount
    );

    List<Experience> findByClientAndScenarioAndSoundAndFood(Cliente client, Scenario scenario, Sound sound, List<Food> food);
}
