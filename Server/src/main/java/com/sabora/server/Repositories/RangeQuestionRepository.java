package com.sabora.server.Repositories;

import com.sabora.server.Entities.RangeQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RangeQuestionRepository extends JpaRepository<RangeQuestion, Long> {
}
