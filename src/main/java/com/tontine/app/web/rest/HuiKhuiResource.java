package com.tontine.app.web.rest;

import com.tontine.app.domain.ChiTietHui;
import com.tontine.app.domain.Hui;
import com.tontine.app.domain.HuiVien;
import com.tontine.app.response.DayHuiResponse;
import com.tontine.app.response.PhieuDongHuiResponse;
import com.tontine.app.service.HuiService;
import com.tontine.app.service.HuiVienService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.ResponseUtil;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ds-hui-khui")
public class HuiKhuiResource {

    private final HuiService huiService;
    private final HuiVienService huiVienService;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    public HuiKhuiResource(HuiService huiService, HuiVienService huiVienService) {
        this.huiService = huiService;
        this.huiVienService = huiVienService;
    }

    @GetMapping
    public ResponseEntity<List<Hui>> getAllHuis(
            @RequestParam String date
    ) {
        return parseDate(date).map(huiService::getHuisByNgayKhui)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest()
                        .body(Collections.emptyList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Hui> getHui(@PathVariable Long id) {
        return ResponseUtil.wrapOrNotFound(huiService.findOne(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<PhieuDongHuiResponse> getPhieuDongHui(@PathVariable long userId, @RequestParam String date) {
        return huiVienService.findOne(userId)
                .map(huiVien -> ResponseEntity.ok(buildPhieuDongHuiResponse(huiVien, date)))
                .orElse(ResponseEntity.ok(new PhieuDongHuiResponse()));
    }

    private Optional<LocalDate> parseDate(String date) {
        try {
            return Optional.of(LocalDate.parse(date, DATE_FORMATTER));
        } catch (DateTimeParseException e) {
            return Optional.empty();
        }
    }

    private PhieuDongHuiResponse buildPhieuDongHuiResponse(HuiVien huiVien, String date) {
        PhieuDongHuiResponse response = new PhieuDongHuiResponse();
        response.setHuiVien(huiVien.getHoTen());
        response.setDayHuis(mapDayHuis(huiVien, date));
        return response;
    }

    private List<DayHuiResponse> mapDayHuis(HuiVien huiVien, String date) {
        LocalDate selectedDate = parseDate(date).orElseThrow(
                () -> new IllegalArgumentException("Invalid date format yyyyMMdd: " + date));

        Set<Long> seenHuiIds = new HashSet<>();
        return huiVien.getChiTietHuis()
                .stream()
                .filter(chiTietHui -> selectedDate.equals(chiTietHui.getNgayKhui()))
                .map(ChiTietHui::getHui)
                .filter(hui -> hui != null && seenHuiIds.add(hui.getId()))
                .map(hui -> mapToDayHui(hui, huiVien.getId()))
                .sorted(Comparator.comparing(DayHuiResponse::getNgayHot)
                        .reversed()
                        .thenComparing(DayHuiResponse::getHuiId))
                .collect(Collectors.toList());
    }

    private DayHuiResponse mapToDayHui(Hui hui, long userId) {
        int song = countHuiInstances(hui, userId, true);
        int chet = countHuiInstances(hui, userId, false);

        DayHuiResponse dayHui = new DayHuiResponse();
        dayHui.setHuiId(hui.getId());
        dayHui.setTenDayHui(hui.getTenHui());
        dayHui.setSoTien(hui.getDayHui());
        dayHui.setNgayMo(hui.getNgayTao());
        dayHui.setSong(song);
        dayHui.setChet(chet);
        dayHui.setSoKyDong(hui.getSoPhan());

        hui.getChiTietHuis()
                .stream()
                .filter(chiTietHui -> chiTietHui.getHuiVien()
                        .getId() == userId)
                .filter(chiTietHui -> chiTietHui.getKy() != null)
                .max(Comparator.comparing(ChiTietHui::getKy))
                .ifPresent(chiTietHui -> {
                    dayHui.setSoTienHot(chiTietHui.getTienHot());
                    dayHui.setSoTienBoTham(chiTietHui.getThamKeu());
                    dayHui.setKyHui(chiTietHui.getKy());
                    dayHui.setNgayHot(chiTietHui.getNgayKhui());
                    dayHui.setSoTienDong(calculateSoTienDong(hui, chiTietHui.getThamKeu(), song, chet));
                });

        return dayHui;
    }

    private int countHuiInstances(Hui hui, long userId, boolean isSong) {
        return (int) hui.getChiTietHuis()
                .stream()
                .filter(e -> e.getHuiVien()
                        .getId() == userId && (isSong == (e.getThamKeu() == null)))
                .count();
    }

    private long calculateSoTienDong(Hui hui, Long thamKeu, int song, int chet) {
        long soTienDong = hui.getDayHui() - Optional.ofNullable(thamKeu)
                .orElse(0L);
        return soTienDong * (song + chet);
    }
}
