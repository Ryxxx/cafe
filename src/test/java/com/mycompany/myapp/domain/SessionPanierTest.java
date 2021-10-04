package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SessionPanierTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SessionPanier.class);
        SessionPanier sessionPanier1 = new SessionPanier();
        sessionPanier1.setId(1L);
        SessionPanier sessionPanier2 = new SessionPanier();
        sessionPanier2.setId(sessionPanier1.getId());
        assertThat(sessionPanier1).isEqualTo(sessionPanier2);
        sessionPanier2.setId(2L);
        assertThat(sessionPanier1).isNotEqualTo(sessionPanier2);
        sessionPanier1.setId(null);
        assertThat(sessionPanier1).isNotEqualTo(sessionPanier2);
    }
}
