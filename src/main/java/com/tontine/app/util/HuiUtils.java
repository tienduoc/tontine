package com.tontine.app.util;

import com.tontine.app.domain.ChiTietHui;
import com.tontine.app.domain.Hui;
import com.tontine.app.domain.HuiVien;
import com.tontine.app.service.HuiService;

import java.util.Comparator;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Utility class for Hui-related operations.
 * Provides utility methods for calculating Hui statistics and values.
 */
public class HuiUtils {

    /**
     * Calculates the "tienHotHui" value for a ChiTietHui.
     *
     * @param cth the ChiTietHui entity
     * @return the calculated tienHotHui value
     */
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

    /**
     * Gets the current period (ky) of a Hui.
     *
     * @param hui the Hui entity
     * @return the current period as a long value, or 0 if not found
     */
    public static long getKyHienTai(Hui hui) {
        if (hui == null) {
            return 0;
        }

        return hui.getChiTietHuis().stream()
            .filter(e -> e.getKy() != null)
            .max(Comparator.comparing(ChiTietHui::getKy))
            .map(ChiTietHui::getKy)
            .orElse(0);
    }

    /**
     * Calculates the total Hui values (huiSong and huiChet) for a HuiVien.
     * Updates the HuiVien entity with the calculated values.
     *
     * @param huiVien the HuiVien entity to calculate totals for
     * @param huiService the HuiService to retrieve Hui data
     */
    public static void calculateTongHui(HuiVien huiVien, HuiService huiService) {
        if (huiVien == null || huiService == null) {
            return;
        }

        AtomicLong tongHuiSong = new AtomicLong();
        AtomicLong tongHuiChet = new AtomicLong();

        huiVien.getChiTietHuis().forEach(chiTietHui -> {
            if (chiTietHui.getHui() != null && chiTietHui.getHui().getId() != null) {
                huiService.findOne(chiTietHui.getHui().getId()).ifPresent(hui -> {
                    long kyHienTai = getKyHienTai(hui);

                    if (chiTietHui.getThamKeu() == null) {
                        calculateAndSetHuiSong(hui, chiTietHui, kyHienTai, tongHuiSong);
                    } else {
                        calculateAndSetHuiChet(chiTietHui, kyHienTai, tongHuiChet);
                    }
                });
            }
        });

        huiVien.setTongHuiSong(tongHuiSong.get());
        huiVien.setTongHuiChet(tongHuiChet.get());
    }

    /**
     * Calculates and sets the "huiSong" value for a ChiTietHui.
     *
     * @param hui the Hui entity
     * @param chiTietHui the ChiTietHui entity to update
     * @param kyHienTai the current period
     * @param tongHuiSong the accumulator for total huiSong
     */
    private static void calculateAndSetHuiSong(Hui hui, ChiTietHui chiTietHui, long kyHienTai, AtomicLong tongHuiSong) {
        if (chiTietHui.getHui() == null) {
            return;
        }

        long dayHui = chiTietHui.getHui().getDayHui();
        long totalThamKeu = hui.getChiTietHuis().stream()
            .filter(ch -> ch.getThamKeu() != null)
            .mapToLong(ChiTietHui::getThamKeu)
            .sum();

        long huiSong = dayHui * kyHienTai - totalThamKeu;
        chiTietHui.setHuiSong(huiSong);
        tongHuiSong.addAndGet(huiSong);
    }

    /**
     * Calculates and sets the "huiChet" value for a ChiTietHui.
     *
     * @param chiTietHui the ChiTietHui entity to update
     * @param kyHienTai the current period
     * @param tongHuiChet the accumulator for total huiChet
     */
    private static void calculateAndSetHuiChet(ChiTietHui chiTietHui, long kyHienTai, AtomicLong tongHuiChet) {
        if (chiTietHui.getHui() == null) {
            return;
        }

        long dayHui = chiTietHui.getHui().getDayHui();
        Integer soPhan = chiTietHui.getHui().getSoPhan();

        if (soPhan != null) {
            long huiChet = dayHui * (soPhan - kyHienTai);
            chiTietHui.setHuiChet(huiChet);
            tongHuiChet.addAndGet(huiChet);
        }
    }
}
