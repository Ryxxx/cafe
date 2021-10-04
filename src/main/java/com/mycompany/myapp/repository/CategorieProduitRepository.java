package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.CategorieProduit;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CategorieProduit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CategorieProduitRepository extends JpaRepository<CategorieProduit, Long> {}
