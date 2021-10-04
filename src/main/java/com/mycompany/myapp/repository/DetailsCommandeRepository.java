package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.DetailsCommande;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DetailsCommande entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DetailsCommandeRepository extends JpaRepository<DetailsCommande, Long> {}
