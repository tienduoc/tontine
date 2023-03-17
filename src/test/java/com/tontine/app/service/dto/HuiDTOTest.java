package com.tontine.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.tontine.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HuiDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HuiDTO.class);
        HuiDTO huiDTO1 = new HuiDTO();
        huiDTO1.setId(1L);
        HuiDTO huiDTO2 = new HuiDTO();
        assertThat(huiDTO1).isNotEqualTo(huiDTO2);
        huiDTO2.setId(huiDTO1.getId());
        assertThat(huiDTO1).isEqualTo(huiDTO2);
        huiDTO2.setId(2L);
        assertThat(huiDTO1).isNotEqualTo(huiDTO2);
        huiDTO1.setId(null);
        assertThat(huiDTO1).isNotEqualTo(huiDTO2);
    }
}
