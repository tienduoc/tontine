package com.tontine.app.service;

import com.tontine.app.domain.ChiTietHui;
import java.util.Objects;

public class HuiHelper {

    public static synchronized Long calculateTienHotHui(ChiTietHui cth) {
        Integer soPhan = cth.getHui().getSoPhan();
        Long dayHui = cth.getHui().getDayHui();
        int ky = cth.getKy();
        int kyHienTai = ky - 1;
        Long thamKeu = cth.getThamKeu();
        long truThao = dayHui / 2;

        if (Objects.equals(soPhan, kyHienTai)) {
            cth.setThamKeu(0L);
            return (dayHui * kyHienTai) - truThao;
        } else {
            return (kyHienTai * dayHui) + (soPhan - ky) * (dayHui - thamKeu) - truThao;
        }
    }
}
