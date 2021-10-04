package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReductionProduitTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReductionProduit.class);
        ReductionProduit reductionProduit1 = new ReductionProduit();
        reductionProduit1.setId(1L);
        ReductionProduit reductionProduit2 = new ReductionProduit();
        reductionProduit2.setId(reductionProduit1.getId());
        assertThat(reductionProduit1).isEqualTo(reductionProduit2);
        reductionProduit2.setId(2L);
        assertThat(reductionProduit1).isNotEqualTo(reductionProduit2);
        reductionProduit1.setId(null);
        assertThat(reductionProduit1).isNotEqualTo(reductionProduit2);
    }
}
