package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StockProduitTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockProduit.class);
        StockProduit stockProduit1 = new StockProduit();
        stockProduit1.setId(1L);
        StockProduit stockProduit2 = new StockProduit();
        stockProduit2.setId(stockProduit1.getId());
        assertThat(stockProduit1).isEqualTo(stockProduit2);
        stockProduit2.setId(2L);
        assertThat(stockProduit1).isNotEqualTo(stockProduit2);
        stockProduit1.setId(null);
        assertThat(stockProduit1).isNotEqualTo(stockProduit2);
    }
}
