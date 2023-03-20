package com.tontine.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.tontine.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ChiTietHuiTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChiTietHui.class);
        ChiTietHui chiTietHui1 = new ChiTietHui();
        chiTietHui1.setId(1L);
        ChiTietHui chiTietHui2 = new ChiTietHui();
        chiTietHui2.setId(chiTietHui1.getId());
        assertThat(chiTietHui1).isEqualTo(chiTietHui2);
        chiTietHui2.setId(2L);
        assertThat(chiTietHui1).isNotEqualTo(chiTietHui2);
        chiTietHui1.setId(null);
        assertThat(chiTietHui1).isNotEqualTo(chiTietHui2);
    }
}
