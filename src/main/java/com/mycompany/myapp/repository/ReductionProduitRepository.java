package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ReductionProduit;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ReductionProduit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReductionProduitRepository extends JpaRepository<ReductionProduit, Long> {}
