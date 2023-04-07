package com.tontine.app.web.rest;

import com.tontine.app.domain.ChiTietHui;
import com.tontine.app.domain.HotHui;
import com.tontine.app.service.ChiTietHuiService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
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

    private final CacheManager cacheManager;

    public HotHuiResource(ChiTietHuiService chiTietHuiService, CacheManager cacheManager) {
        this.chiTietHuiService = chiTietHuiService;
        this.cacheManager = cacheManager;
    }

    @Cacheable(cacheNames = "hotHuiById")
    @GetMapping("/hot-hui/{id}")
    public ResponseEntity<HotHui> getTinhTien(@PathVariable Long id) {
        Optional<ChiTietHui> cthOpt = chiTietHuiService.findOne(id);
        HotHui hotHui = null;
        if (cthOpt.isPresent()) {
            ChiTietHui cth = cthOpt.get();
            hotHui = new HotHui();
            hotHui.setTenHui(cth.getHui().getTenHui());
            hotHui.setNgayMo(cth.getHui().getNgayTao());
            hotHui.setNgayHot(cth.getNgayKhui());
            hotHui.setDayHui(cth.getHui().getDayHui());
            hotHui.setSoPhan(cth.getHui().getSoPhan());
            hotHui.setKhui(cth.getHui().getLoaiHui());
            hotHui.setHuiVien(cth.getHuiVien().getHoTen());
            hotHui.setSoKy(cth.getKy());
            hotHui.setThamKeu(cth.getThamKeu());
            long daHot = (cth.getKy() - 1) * cth.getHui().getDayHui();
            hotHui.setDaHot(daHot);
            long chuaHot = (cth.getHui().getSoPhan() - cth.getKy()) * (cth.getHui().getDayHui() - cth.getThamKeu());
            hotHui.setChuaHot(chuaHot);
            long tienHui = daHot + chuaHot;
            hotHui.setTienHui(tienHui);
            long truThao = cth.getHui().getDayHui() / 2;
            hotHui.setTruThao(truThao);
            hotHui.setConLai(tienHui - truThao);
        }
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(hotHui));
    }
}
