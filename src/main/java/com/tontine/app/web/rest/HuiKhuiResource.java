package com.tontine.app.web.rest;

import com.tontine.app.domain.ChiTietHui;
import com.tontine.app.domain.HuiVien;
import com.tontine.app.response.HuiKhuiResponse;
import com.tontine.app.response.PhieuDongHuiResponse;
import com.tontine.app.service.ChiTietHuiService;
import com.tontine.app.service.HuiService;
import com.tontine.app.service.HuiVienService;
import com.tontine.app.service.mapper.HuiKhuiMapper;
import com.tontine.app.util.DateUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ds-hui-khui")
public class HuiKhuiResource {

    private final HuiService huiService;
    private final HuiVienService huiVienService;
    private final HuiKhuiMapper huiKhuiMapper;
    private final ChiTietHuiService chiTietHuiService;

    public HuiKhuiResource(HuiService huiService, HuiVienService huiVienService, HuiKhuiMapper huiKhuiMapper, ChiTietHuiService chiTietHuiService, ChiTietHuiService chiTietHuiService1) {
        this.huiService = huiService;
        this.huiVienService = huiVienService;
        this.huiKhuiMapper = huiKhuiMapper;
        this.chiTietHuiService = chiTietHuiService1;
    }

    @GetMapping
    public ResponseEntity<List<HuiVien>> getAllHuis(@RequestParam String date) {
        return DateUtils.parseDate(date)
            .map(huiService::getHuisByNgayKhui)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.badRequest().body(Collections.emptyList()));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<PhieuDongHuiResponse> getHui(@PathVariable Long userId, @RequestParam String date) {
        Optional<HuiVien> huiVienOptional = huiVienService.findOne(userId);
        Optional<LocalDate> ngayOptional = DateUtils.parseDate(date);

        if (huiVienOptional.isEmpty() || ngayOptional.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }


        HuiVien huiVien = huiVienOptional.get();
        LocalDate ngay = ngayOptional.get();

        List<HuiKhuiResponse> huiKhuiResponses = huiKhuiMapper.mapToHuiKhuiResponses(huiVien, date);

        PhieuDongHuiResponse response = new PhieuDongHuiResponse();
        response.setHuiVien(huiVien.getHoTen());
        response.setNgay(ngay);
        huiKhuiResponses.sort(Comparator.comparing(HuiKhuiResponse::getDayHui));
        response.setHuiKhuiResponseList(huiKhuiResponses);

        return ResponseEntity.ok(response);
    }
}
