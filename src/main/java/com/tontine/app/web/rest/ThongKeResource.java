package com.tontine.app.web.rest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.tontine.app.domain.ChiTietHui;
import com.tontine.app.response.ChiTietHuiKeuResponse;
import com.tontine.app.response.HuiKeuNgayResponse;
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
    public ResponseEntity<List<HuiKeuNgayResponse>> getHuiKeu(
        @RequestParam( value = "date" )
        @DateTimeFormat( pattern = "yyyyMMdd" )
        LocalDate date
    ) {
        List<ChiTietHui> allByNgayKhui = chiTietHuiService.findByNgayKhui( date );

        // Grouping by TenHuiVien
        var groupedResponses = allByNgayKhui.stream()
            .collect( Collectors.groupingBy( chiTietHui -> chiTietHui.getHuiVien().getHoTen() ) )
            .entrySet()
            .stream()
            .map( entry -> {
                String tenHuiVien = entry.getKey();
                List<ChiTietHuiKeuResponse> chiTietResponses = entry.getValue().stream()
                    .map( this::toChiTietHuiKeu )
                    .collect( Collectors.toList() );
                return toHuiKeuNgayResponse( tenHuiVien, chiTietResponses );
            } )
            .collect( Collectors.toList() );

        return ResponseUtil.wrapOrNotFound( Optional.of( groupedResponses ) );
    }

    private HuiKeuNgayResponse toHuiKeuNgayResponse( String tenHuiVien, List<ChiTietHuiKeuResponse> chiTietResponses ) {
        HuiKeuNgayResponse response = new HuiKeuNgayResponse();
        response.setTenHuiVien( tenHuiVien );
        response.setChiTiets( chiTietResponses );
        return response;
    }

    private ChiTietHuiKeuResponse toChiTietHuiKeu( ChiTietHui chiTietHui ) {
        var chiTiet = new ChiTietHuiKeuResponse();
        chiTiet.setTenHui( chiTietHui.getHui().getTenHui() );
        chiTiet.setHuiHot( 10000000L );
        chiTiet.setHuiSong( 20000000L );
        chiTiet.setHuiChet( 30000000L );
        chiTiet.setConLai( 40000000L );
        return chiTiet;
    }

}
