package com.tontine.app.web.rest;

import java.util.Comparator;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import com.tontine.app.domain.ChiTietHui;
import com.tontine.app.domain.Hui;
import com.tontine.app.domain.HuiVien;
import com.tontine.app.service.HuiService;

public class HuiHelper {

    static long getKyHienTai( Optional<Hui> hui ) {
        return (long) hui
            .map( h -> h.getChiTietHuis().stream()
                .filter( e -> e.getKy() != null )
                .max( Comparator.comparing( ChiTietHui::getKy ) )
                .map( ChiTietHui::getKy )
                .orElse( 0 ) )
            .orElse( 0 );
    }

    static void calculateTongHui( HuiVien huiVien, HuiService huiService ) {
        AtomicLong tongHuiSong = new AtomicLong();
        AtomicLong tongHuiChet = new AtomicLong();

        huiVien.getChiTietHuis().forEach(chiTietHui -> {
            huiService.findOne(chiTietHui.getHui().getId()).ifPresent(hui -> {
                long kyHienTai = getKyHienTai(Optional.of(hui));
                long dayHui = chiTietHui.getHui().getDayHui();

                if (chiTietHui.getThamKeu() == null) {
                    long huiSong = dayHui * kyHienTai;
                    chiTietHui.setHuiSong(huiSong);
                    tongHuiSong.addAndGet(huiSong);
                } else {
                    long huiChet = dayHui * (chiTietHui.getHui().getSoPhan() - kyHienTai);
                    chiTietHui.setHuiChet(huiChet);
                    tongHuiChet.addAndGet(huiChet);
                }
            });
        });

        huiVien.setTongHuiSong(tongHuiSong.get());
        huiVien.setTongHuiChet(tongHuiChet.get());
    }
}
