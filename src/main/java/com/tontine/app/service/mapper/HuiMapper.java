package com.tontine.app.service.mapper;

import com.tontine.app.domain.Hui;
import com.tontine.app.service.dto.HuiDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface HuiMapper extends EntityMapper<HuiDTO, Hui> {}
