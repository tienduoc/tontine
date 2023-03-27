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
            hotHui.setSoKy(1);
            hotHui.setThamKeu(chiTietHui.get().getThamKeu());
            hotHui.setDaHot(2L);
            hotHui.setChuaHot(3L);
            hotHui.setTienHui(4L);
            hotHui.setTruThao(5L);
            hotHui.setConLai(6L);
        }
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(hotHui));
    }
}
