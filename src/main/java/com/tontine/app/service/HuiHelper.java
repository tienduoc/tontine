package com.tontine.app.service;

import com.tontine.app.domain.ChiTietHui;

public class HuiHelper {

    public static Long calculateTienHotHui(ChiTietHui chiTietHui) {
        Long thamKeu = chiTietHui.getThamKeu();
        Integer ky = chiTietHui.getKy();
        if (thamKeu == null || ky == null) {
            return null;
        }

        //        Hui hui = chiTietHui.getHui();
        //        Integer soPhan = hui.getSoPhan();
        //        Long dayHui = hui.getDayHui();
        //
        //        if (ky == 1) { // Hot hui dau
        //            return (soPhan - 1) * (dayHui - thamKeu) - (dayHui / 2);
        //        } else if (Objects.equals(ky, soPhan)) { // Hot hui chot
        //            return dayHui * ky - (dayHui / 2);
        //        } else {
        //            return (ky - 1) * dayHui + (soPhan - ky) * (dayHui - thamKeu) - (dayHui / 2);
        //        }

        return chiTietHui.getTienHot();
    }
}
