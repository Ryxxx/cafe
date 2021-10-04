package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DetailsCommandeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DetailsCommande.class);
        DetailsCommande detailsCommande1 = new DetailsCommande();
        detailsCommande1.setId(1L);
        DetailsCommande detailsCommande2 = new DetailsCommande();
        detailsCommande2.setId(detailsCommande1.getId());
        assertThat(detailsCommande1).isEqualTo(detailsCommande2);
        detailsCommande2.setId(2L);
        assertThat(detailsCommande1).isNotEqualTo(detailsCommande2);
        detailsCommande1.setId(null);
        assertThat(detailsCommande1).isNotEqualTo(detailsCommande2);
    }
}
