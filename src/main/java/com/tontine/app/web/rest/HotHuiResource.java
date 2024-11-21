package com.tontine.app.web.rest;

import com.tontine.app.domain.ChiTietHui;
import com.tontine.app.domain.HotHui;
import com.tontine.app.domain.Hui;
import com.tontine.app.service.ChiTietHuiService;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.ResponseUtil;

@RestController
@RequestMapping("/api")
public class HotHuiResource {

    private final ChiTietHuiService chiTietHuiService;

    public HotHuiResource(ChiTietHuiService chiTietHuiService) {
        this.chiTietHuiService = chiTietHuiService;
    }

    @GetMapping("/hot-hui/{id}")
    public synchronized ResponseEntity<HotHui> getTinhTien(@PathVariable Long id) {
        Optional<ChiTietHui> cthOpt = chiTietHuiService.findOne(id);
        HotHui hotHui = null;
        if (cthOpt.isPresent()) {
            ChiTietHui cth = cthOpt.get();
            hotHui = new HotHui();
            tinhTienHui(cth, hotHui);

            hotHui.setTenHui(cth.getHui().getTenHui());
            hotHui.setNgayMo(cth.getHui().getNgayTao());
            hotHui.setNgayHot(cth.getNgayKhui());
            hotHui.setDayHui(cth.getHui().getDayHui());
            hotHui.setSoPhan(cth.getHui().getSoPhan());
            hotHui.setKhui(cth.getHui().getLoaiHui());
            hotHui.setHuiVien(cth.getHuiVien().getHoTen());
            hotHui.setSoKy(cth.getKy());
            hotHui.setThamKeu(cth.getThamKeu());
        }
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(hotHui));
    }

    private void tinhTienHui(ChiTietHui cth, HotHui hotHui) {
        Hui hui = cth.getHui();
        int ky = cth.getKy();
        Long dayHui = hui.getDayHui();
        long truThao = dayHui / 2;
        int kyHienTai = ky - 1;
        long daHot = kyHienTai * dayHui;
        Integer soPhan = hui.getSoPhan();
        Long thamKeu = cth.getThamKeu();

        if (Objects.equals(soPhan, ky)) {
            // Ky cuoi
            cth.setThamKeu(0L);
            hotHui.setChuaHot(0L);
            hotHui.setConLai(dayHui * kyHienTai - truThao);
        } else {
            // Ky binh thuong
            long chuaHot = (soPhan - ky) * (dayHui - thamKeu);
            hotHui.setChuaHot((soPhan - ky) * (dayHui - thamKeu));
            hotHui.setTienHui(daHot + chuaHot);
            hotHui.setConLai(daHot + chuaHot - truThao);
        }

        hotHui.setDaHot(daHot);
        hotHui.setTruThao(truThao);
    }
}
