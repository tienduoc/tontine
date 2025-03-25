package com.tontine.app.web.rest;

import com.tontine.app.domain.ChiTietHui;
import com.tontine.app.domain.Hui;
import com.tontine.app.domain.HuiVien;
import com.tontine.app.response.DayHuiResponse;
import com.tontine.app.response.PhieuDongHuiResponse;
import com.tontine.app.service.HuiService;
import com.tontine.app.service.HuiVienService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.ResponseUtil;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ds-hui-khui")
public class HuiKhuiResource {

    private final HuiService huiService;
    private final HuiVienService huiVienService;

    public HuiKhuiResource(HuiService huiService, HuiVienService huiVienService) {
        this.huiService = huiService;
        this.huiVienService = huiVienService;
    }

    @GetMapping
    public ResponseEntity<List<Hui>> getAllHuis(@RequestParam String date) {
        try {
            LocalDate ngayKhui = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyyMMdd"));
            List<Hui> huis = huiService.getHuisByNgayKhui(ngayKhui);
            return ResponseEntity.ok(huis);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Hui> getHui(@PathVariable Long id) {
        return ResponseUtil.wrapOrNotFound(huiService.findOne(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<PhieuDongHuiResponse> getPhieuDongHui(@PathVariable long userId) {
        return huiVienService.findOne(userId)
            .map(huiVien -> ResponseEntity.ok(buildPhieuDongHuiResponse(huiVien)))
            .orElse(ResponseEntity.ok(new PhieuDongHuiResponse()));
    }

    private PhieuDongHuiResponse buildPhieuDongHuiResponse(HuiVien huiVien) {
        PhieuDongHuiResponse response = new PhieuDongHuiResponse();
        response.setHuiVien(huiVien.getHoTen());
        List<DayHuiResponse> dayHuis = mapDayHuis(huiVien);
        response.setDayHuis(dayHuis);
        return response;
    }

    private List<DayHuiResponse> mapDayHuis(HuiVien huiVien) {
        Set<Long> seenHuiIds = new HashSet<>();
        return huiVien.getChiTietHuis().stream()
            .map(ChiTietHui::getHui)
            .filter(Objects::nonNull)
            .filter(hui -> seenHuiIds.add(hui.getId()))
            .map(hui -> mapToDayHui(hui, huiVien.getId()))
            .sorted(Comparator.comparing(DayHuiResponse::getNgayHot).reversed()
                .thenComparing(DayHuiResponse::getHuiId))
            .collect(Collectors.toList());
    }


    private DayHuiResponse mapToDayHui(Hui hui, long userId) {
        int song = getChanSong(hui, userId);
        int chet = getChanChet(hui, userId);

        DayHuiResponse dayHui = new DayHuiResponse();
        dayHui.setHuiId(hui.getId());
        dayHui.setTenDayHui(hui.getTenHui());
        dayHui.setSoTien(hui.getDayHui());
        dayHui.setNgayMo(hui.getNgayTao());
        dayHui.setSong(getChanSong(hui, userId));
        dayHui.setChet(chet);
        dayHui.setSoKyDong(hui.getSoPhan());

        hui.getChiTietHuis().stream()
            .filter(chiTietHui -> chiTietHui.getHuiVien().getId() == userId)
            .filter(chiTietHui -> chiTietHui.getKy() != null)
            .max(Comparator.comparing(ChiTietHui::getKy))
            .ifPresent(chiTietHui -> dayHui.setSoTienHot(chiTietHui.getTienHot()));


        hui.getChiTietHuis().stream()
            .filter(chiTietHui -> chiTietHui.getKy() != null)
            .max(Comparator.comparing(ChiTietHui::getKy))
            .ifPresent(chiTietHui -> {
                dayHui.setSoTienBoTham(chiTietHui.getThamKeu());
                dayHui.setKyHui(chiTietHui.getKy());
                dayHui.setNgayHot(chiTietHui.getNgayKhui());
                dayHui.setSoTienDong(getSoTienDong(hui, chiTietHui.getThamKeu(), song, chet));
            });

        return dayHui;
    }

    private int getChanChet(Hui hui, long userId) {
        return (int) hui.getChiTietHuis().stream()
            .filter(e -> e.getHuiVien().getId() == userId && e.getThamKeu() != null)
            .count();
    }

    private int getChanSong(Hui hui, long userId) {
        return (int) hui.getChiTietHuis().stream()
            .filter(e -> e.getHuiVien().getId() == userId && e.getThamKeu() == null)
            .count();
    }

    private long getSoTienDong(Hui hui, Long thamKeu, int song, int chet) {
        long soTienDong = hui.getDayHui() - thamKeu;
        long soTienDongSong = (song != 0) ? soTienDong * song : 0;
        long soTienDongChet = (chet != 0) ? soTienDong * chet : 0;
        return soTienDongSong + soTienDongChet;
    }
}
