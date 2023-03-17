package com.tontine.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.tontine.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HuiVienTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HuiVien.class);
        HuiVien huiVien1 = new HuiVien();
        huiVien1.setId(1L);
        HuiVien huiVien2 = new HuiVien();
        huiVien2.setId(huiVien1.getId());
        assertThat(huiVien1).isEqualTo(huiVien2);
        huiVien2.setId(2L);
        assertThat(huiVien1).isNotEqualTo(huiVien2);
        huiVien1.setId(null);
        assertThat(huiVien1).isNotEqualTo(huiVien2);
    }
}
