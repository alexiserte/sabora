package com.sabora.server.Repositories;

import com.sabora.server.Entities.AmbientMusic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AmbientMusicRepository extends JpaRepository<AmbientMusic, Integer>
{
}
