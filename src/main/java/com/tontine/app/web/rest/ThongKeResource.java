package com.tontine.app.web.rest;

import static com.tontine.app.web.rest.HuiHelper.getKyHienTai;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.tontine.app.domain.ChiTietHui;
import com.tontine.app.domain.Hui;
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
        @RequestParam( value = "date" ) @DateTimeFormat( pattern = "yyyyMMdd" ) LocalDate date ) {

        List<ChiTietHui> chiTietHuis = chiTietHuiService.findByNgayKhui( date );

        List<HuiKeuNgayResponse> groupedResponses = chiTietHuis.stream()
            .collect( Collectors.groupingBy( chiTietHui -> chiTietHui.getHuiVien().getHoTen() ) )
            .entrySet()
            .stream()
            .map( entry -> toHuiKeuNgayResponse( entry.getKey(), entry.getValue() ) )
            .collect( Collectors.toList() );

        return ResponseUtil.wrapOrNotFound( Optional.of( groupedResponses ) );
    }

    private HuiKeuNgayResponse toHuiKeuNgayResponse( String tenHuiVien, List<ChiTietHui> chiTietHuis ) {
        List<ChiTietHuiKeuResponse> chiTietResponses = chiTietHuis.stream()
            .map( this::toChiTietHuiKeu )
            .collect( Collectors.toList() );

        HuiKeuNgayResponse response = new HuiKeuNgayResponse();
        response.setTenHuiVien( tenHuiVien );
        response.setChiTiets( chiTietResponses );
        return response;
    }

    private ChiTietHuiKeuResponse toChiTietHuiKeu( ChiTietHui chiTietHui ) {
        ChiTietHuiKeuResponse chiTiet = new ChiTietHuiKeuResponse();

        Hui hui = chiTietHui.getHui();
        chiTiet.setTenHui( hui.getTenHui() );
        chiTiet.setHuiHot( chiTietHui.getTienHot() );


        long kyHienTai = getKyHienTai( Optional.of( hui ) );
        long dayHui = hui.getDayHui();
        if ( chiTietHui.getThamKeu() == null ) {
            chiTiet.setHuiSong( dayHui * kyHienTai );
            chiTiet.setHuiChet( 0L );
        } else {
            chiTiet.setHuiChet( dayHui * ( hui.getSoPhan() - kyHienTai ) );
            chiTiet.setHuiSong( 0L );
        }

        chiTiet.setConLai( chiTietHui.getTienHot() - ( chiTiet.getHuiSong() + chiTiet.getHuiChet() ) );
        return chiTiet;
    }
}
