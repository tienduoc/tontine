package com.tontine.app.service;

import com.tontine.app.domain.ChiTietHui;
import com.tontine.app.domain.Hui;

public class HuiHelper {

    public static long calculateTienHotHui(ChiTietHui chiTietHui) {
        long tienHot = 0;
        Long thamKeu = chiTietHui.getThamKeu();
        if (thamKeu != null) {
            Hui hui = chiTietHui.getHui();
            Integer soPhan = hui.getSoPhan();
            Long dayHui = hui.getDayHui();
            Integer ky = chiTietHui.getKy();
            if (ky == 1) { // Hot hui dau
                tienHot = (soPhan - 1) * (dayHui - thamKeu) - (dayHui / 2);
            } else if (ky == soPhan - 1) { // Hot hui chot
                tienHot = dayHui * ky - (dayHui / 2);
            } else {
                tienHot = (ky - 1) * dayHui + (soPhan - ky) * (dayHui - thamKeu) - (dayHui / 2);
            }
        }
        return tienHot;
    }
}
