package com.sd.mihnea.proiect_pos.repository;

import com.sd.mihnea.proiect_pos.model.Pachete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PacheteRepositoryJPA extends JpaRepository<Pachete, Integer> {
}
