package com.sd.mihnea.proiect_pos.repository;

import com.sd.mihnea.proiect_pos.model.Evenimente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EvenimenteRepositoryJPA extends JpaRepository<Evenimente, Integer> {
}
