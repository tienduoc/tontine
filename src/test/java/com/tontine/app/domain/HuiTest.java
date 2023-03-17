package com.tontine.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.tontine.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HuiTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Hui.class);
        Hui hui1 = new Hui();
        hui1.setId(1L);
        Hui hui2 = new Hui();
        hui2.setId(hui1.getId());
        assertThat(hui1).isEqualTo(hui2);
        hui2.setId(2L);
        assertThat(hui1).isNotEqualTo(hui2);
        hui1.setId(null);
        assertThat(hui1).isNotEqualTo(hui2);
    }
}
