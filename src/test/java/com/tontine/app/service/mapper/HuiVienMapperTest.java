package com.tontine.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HuiVienMapperTest {

    private HuiVienMapper huiVienMapper;

    @BeforeEach
    public void setUp() {
        huiVienMapper = new HuiVienMapperImpl();
    }
}
