package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.AdresseUtilisateur;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the AdresseUtilisateur entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AdresseUtilisateurRepository extends JpaRepository<AdresseUtilisateur, Long> {}
