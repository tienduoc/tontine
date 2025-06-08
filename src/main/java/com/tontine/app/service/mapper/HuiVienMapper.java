package com.tontine.app.service.mapper;

import com.tontine.app.domain.HuiVien;
import com.tontine.app.service.dto.HuiVienDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface HuiVienMapper extends EntityMapper<HuiVienDTO, HuiVien> {}
