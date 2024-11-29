package com.sabora.server.Repositories;

import com.sabora.server.Models.DataAnalyst;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataAnalystRepository  extends JpaRepository<DataAnalyst, Long> {
}