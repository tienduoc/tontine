package com.tontine.app.web.rest;

import java.util.Comparator;
import java.util.Optional;

import com.tontine.app.domain.ChiTietHui;
import com.tontine.app.domain.Hui;

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
}
