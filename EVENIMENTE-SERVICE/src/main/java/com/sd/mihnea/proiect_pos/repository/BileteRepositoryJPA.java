package com.sd.mihnea.proiect_pos.repository;


import com.sd.mihnea.proiect_pos.model.Bilete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BileteRepositoryJPA extends JpaRepository<Bilete, UUID> {
}
