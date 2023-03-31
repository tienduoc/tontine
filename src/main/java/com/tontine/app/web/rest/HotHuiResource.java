package com.tontine.app.web.rest;

import com.tontine.app.domain.ChiTietHui;
import com.tontine.app.domain.HotHui;
import com.tontine.app.service.ChiTietHuiService;
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

    private final Logger log = LoggerFactory.getLogger(HotHuiResource.class);

    private final ChiTietHuiService chiTietHuiService;

    public HotHuiResource(ChiTietHuiService chiTietHuiService) {
        this.chiTietHuiService = chiTietHuiService;
    }

    @GetMapping("/hot-hui/{id}")
    public ResponseEntity<HotHui> getTinhTien(@PathVariable Long id) {
        Optional<ChiTietHui> chiTietHui = chiTietHuiService.findOne(id);
        HotHui hotHui = null;
        if (chiTietHui.isPresent()) {
            hotHui = new HotHui();
            hotHui.setTenHui(chiTietHui.get().getHui().getTenHui());
            hotHui.setNgayMo(chiTietHui.get().getHui().getNgayTao());
            hotHui.setNgayHot(chiTietHui.get().getNgayKhui());
            hotHui.setDayHui(chiTietHui.get().getHui().getDayHui());
            hotHui.setSoPhan(chiTietHui.get().getHui().getSoPhan());
            hotHui.setKhui(chiTietHui.get().getHui().getLoaiHui());
            hotHui.setHuiVien(chiTietHui.get().getHuiVien().getHoTen());
            hotHui.setSoKy(chiTietHui.get().getKy());
            hotHui.setThamKeu(chiTietHui.get().getThamKeu());
            long daHot = (chiTietHui.get().getKy() - 1) + chiTietHui.get().getHui().getDayHui();
            hotHui.setDaHot(daHot);
            // Fixme: check lai cong thuc
            long chuaHot = chiTietHui.get().getHui().getSoPhan() - chiTietHui.get().getKy() * chiTietHui.get().getHui().getDayHui();
            hotHui.setChuaHot(chuaHot);
            hotHui.setTienHui(daHot + chuaHot);
            long truThao = chiTietHui.get().getHui().getDayHui() / 2;
            hotHui.setTruThao(truThao);
            hotHui.setConLai(chiTietHui.get().getHui().getDayHui() - truThao);
        }
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(hotHui));
    }
}
