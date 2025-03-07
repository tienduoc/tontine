package com.tontine.app.web.rest;

import com.tontine.app.domain.Hui;
import com.tontine.app.response.DayHui;
import com.tontine.app.response.PhieuDongHuiResponse;
import com.tontine.app.service.HuiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.ResponseUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ds-hui-khui")
public class HuiKhuiResource {

    private final HuiService huiService;

    public HuiKhuiResource(HuiService huiService) {
        this.huiService = huiService;
    }

    @GetMapping("")
    public ResponseEntity<List<Hui>> getAllHuis() {
        List<Hui> huis = huiService.findAll();
        return ResponseEntity.ok().body(huis);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Hui> getHui(@PathVariable Long id) {
        Optional<Hui> hui = huiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(hui);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<PhieuDongHuiResponse> getPhieuDongHui(@PathVariable Long userId) {
        PhieuDongHuiResponse phieuDongHuiResponse = new PhieuDongHuiResponse();
        phieuDongHuiResponse.setHuiVien("C Ngân q7");
        phieuDongHuiResponse.setSoDay(8);
        phieuDongHuiResponse.setSoChan(8);
        phieuDongHuiResponse.setChet(3);
        phieuDongHuiResponse.setSong(5);
        phieuDongHuiResponse.setHuiSong(43801000);
        phieuDongHuiResponse.setHuiChet(69000000);
        phieuDongHuiResponse.setCanBangAmDuong(-25190000);
        phieuDongHuiResponse.setLoiNhuan(14210000);
        phieuDongHuiResponse.setDayHuis(getDayHuis());
        phieuDongHuiResponse.setTongSoTienBoTham(990000);
        phieuDongHuiResponse.setTongSoTienDong(5010000);
        return ResponseEntity.ok().body(phieuDongHuiResponse);
    }

    private List<DayHui> getDayHuis() {
        List<DayHui> listDayHui = new ArrayList<>();

        DayHui day10 = new DayHui();
        day10.setTenDayHui("Dây 10");
        day10.setSoTien(3000000);
        day10.setNgayMo(LocalDate.of(2024, 12, 23));
        day10.setKyHui(3);
        day10.setSoKyDong(15);
        day10.setNgayHot(LocalDate.of(2025, 1, 6));
        day10.setSoTienBoTham(420000);
        day10.setSong(1);
        day10.setSoTienDong(2580000);
        day10.setSoTienHot(1234567890L);
        listDayHui.add(day10);

        DayHui day8 = new DayHui();
        day8.setTenDayHui("Dây 8");
        day8.setSoTien(3000000);
        day8.setNgayMo(LocalDate.of(2024, 12, 23));
        day8.setKyHui(2);
        day8.setSoKyDong(19);
        day8.setNgayHot(LocalDate.of(2025, 1, 6));
        day8.setSoTienBoTham(570000);
        day8.setSong(1);
        day8.setSoTienDong(2430000);
        day8.setSoTienHot(1234567890L);
        listDayHui.add(day8);

        return listDayHui;
    }
}
