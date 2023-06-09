package com.tontine.app.service;

import com.tontine.app.domain.ChiTietHui;
import com.tontine.app.domain.Hui;
import java.util.Objects;

public class HuiHelper {

    public static Long calculateTienHotHui(ChiTietHui chiTietHui) {
        Long thamKeu = chiTietHui.getThamKeu();
        Hui hui = chiTietHui.getHui();
        Integer soPhan = hui.getSoPhan();
        Long dayHui = hui.getDayHui();
        Integer ky = chiTietHui.getKy();

        if (thamKeu == null || ky == null) {
            return null;
        }

        if (ky == 1) { // Hot hui dau
            return (soPhan - 1) * (dayHui - thamKeu) - (dayHui / 2);
        } else if (Objects.equals(ky, soPhan)) { // Hot hui chot
            return dayHui * ky - (dayHui / 2);
        } else {
            return (ky - 1) * dayHui + (soPhan - ky) * (dayHui - thamKeu) - (dayHui / 2);
        }
    }
}
