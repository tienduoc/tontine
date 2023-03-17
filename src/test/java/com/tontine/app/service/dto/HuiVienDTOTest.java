package com.tontine.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.tontine.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HuiVienDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HuiVienDTO.class);
        HuiVienDTO huiVienDTO1 = new HuiVienDTO();
        huiVienDTO1.setId(1L);
        HuiVienDTO huiVienDTO2 = new HuiVienDTO();
        assertThat(huiVienDTO1).isNotEqualTo(huiVienDTO2);
        huiVienDTO2.setId(huiVienDTO1.getId());
        assertThat(huiVienDTO1).isEqualTo(huiVienDTO2);
        huiVienDTO2.setId(2L);
        assertThat(huiVienDTO1).isNotEqualTo(huiVienDTO2);
        huiVienDTO1.setId(null);
        assertThat(huiVienDTO1).isNotEqualTo(huiVienDTO2);
    }
}
