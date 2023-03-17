package com.tontine.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HuiMapperTest {

    private HuiMapper huiMapper;

    @BeforeEach
    public void setUp() {
        huiMapper = new HuiMapperImpl();
    }
}
