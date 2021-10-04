package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.SessionPanier;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SessionPanier entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SessionPanierRepository extends JpaRepository<SessionPanier, Long> {}
