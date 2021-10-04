package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AdresseUtilisateurTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AdresseUtilisateur.class);
        AdresseUtilisateur adresseUtilisateur1 = new AdresseUtilisateur();
        adresseUtilisateur1.setId(1L);
        AdresseUtilisateur adresseUtilisateur2 = new AdresseUtilisateur();
        adresseUtilisateur2.setId(adresseUtilisateur1.getId());
        assertThat(adresseUtilisateur1).isEqualTo(adresseUtilisateur2);
        adresseUtilisateur2.setId(2L);
        assertThat(adresseUtilisateur1).isNotEqualTo(adresseUtilisateur2);
        adresseUtilisateur1.setId(null);
        assertThat(adresseUtilisateur1).isNotEqualTo(adresseUtilisateur2);
    }
}
