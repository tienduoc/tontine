package com.tontine.app.web.rest;

import java.time.LocalDate;
import java.util.List;

import com.tontine.app.domain.ChiTietHui;
import com.tontine.app.service.ChiTietHuiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.ResponseUtil;

@RestController
@RequestMapping( "/api" )
public class ThongKeResource {

    private final Logger log = LoggerFactory.getLogger( ThongKeResource.class );

    private final ChiTietHuiService chiTietHuiService;

    public ThongKeResource( ChiTietHuiService chiTietHuiService ) {
        this.chiTietHuiService = chiTietHuiService;
    }

    @GetMapping( "/thong-ke" )
    public ResponseEntity<List<ChiTietHui>> getHuiKeu(
        @RequestParam( required = false )
        @DateTimeFormat( pattern = "yyyyMMdd" )
        LocalDate date
    ) {
        var allByNgayKhui = chiTietHuiService.findAllByNgayKhui( date );
        return ResponseUtil.wrapOrNotFound( allByNgayKhui);
    }
}
