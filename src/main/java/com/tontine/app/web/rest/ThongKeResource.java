package com.tontine.app.web.rest;

import static com.tontine.app.web.rest.HuiHelper.getKyHienTai;

import com.tontine.app.domain.ChiTietHui;
import com.tontine.app.domain.Hui;
import com.tontine.app.domain.HuiVien;
import com.tontine.app.response.ChiTietHuiKeuResponse;
import com.tontine.app.response.HuiKeuNgayResponse;
import com.tontine.app.service.ChiTietHuiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.ResponseUtil;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ThongKeResource {

    private static final Logger log = LoggerFactory.getLogger(ThongKeResource.class);
    private final ChiTietHuiService chiTietHuiService;
    private LocalDate selectedDate = null;

    public ThongKeResource(ChiTietHuiService chiTietHuiService) {
        this.chiTietHuiService = chiTietHuiService;
    }

    @GetMapping("/thong-ke")
    public ResponseEntity<List<HuiKeuNgayResponse>> getHuiKeu(@RequestParam(required = false) String date) {
        if (date != null) {
            if (!parseDate(date)) {
                return ResponseEntity.badRequest().body(null);
            }
        }

        List<HuiVien> huiViens = chiTietHuiService.findByNgayKhui(selectedDate).stream()
            .map(ChiTietHui::getHuiVien)
            .collect(Collectors.toList());

        List<HuiKeuNgayResponse> response = huiViens.stream()
            .map(huiVien -> toHuiKeuNgayResponse(huiVien, selectedDate))
            .collect(Collectors.toList());

        return ResponseUtil.wrapOrNotFound(Optional.of(response));
    }

    private boolean parseDate(String date) {
        try {
            if (date.length() == 7) {
                date = date.substring(0, 6) + "0" + date.substring(6);
            }
            selectedDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyyMMdd"));
            return true;
        } catch (DateTimeParseException e) {
            log.error("Invalid date format: {}", date, e);
            return false;
        }
    }

    private HuiKeuNgayResponse toHuiKeuNgayResponse(HuiVien huiVien, LocalDate selectedDate) {
        HuiKeuNgayResponse huiKeuNgayResponse = new HuiKeuNgayResponse();

        List<ChiTietHuiKeuResponse> chiTietResponses = huiVien.getChiTietHuis().stream()
            .filter( e -> getKyHienTai( Optional.ofNullable( e.getHui() ) ) != e.getHui().getSoPhan() )
            .map(chiTietHui -> toChiTietHuiKeu(chiTietHui, selectedDate))
            .collect(Collectors.toList());

        AtomicLong totalHuiSong = new AtomicLong();
        AtomicLong totalHuiChet = new AtomicLong();
        AtomicLong huiHot = new AtomicLong();

        chiTietResponses.forEach(response -> {
            if (response.getHuiHot() != 0L) {
                huiHot.addAndGet(response.getHuiHot());
            } else if (response.getHuiSong() != 0L) {
                totalHuiSong.addAndGet(response.getHuiSong());
            } else if (response.getHuiChet() != 0L) {
                totalHuiChet.addAndGet(response.getHuiChet());
            }
        });

        long remainingHuiHot = huiHot.get() - (totalHuiSong.get() + totalHuiChet.get());
        chiTietResponses.forEach(response -> response.setConLai(remainingHuiHot));

        huiKeuNgayResponse.setTenHuiVien(huiVien.getHoTen());
        huiKeuNgayResponse.setChiTiets(chiTietResponses);
        huiKeuNgayResponse.setConLai(remainingHuiHot);

        return huiKeuNgayResponse;
    }

    private ChiTietHuiKeuResponse toChiTietHuiKeu(ChiTietHui chiTietHui, LocalDate selectedDate) {
        ChiTietHuiKeuResponse response = new ChiTietHuiKeuResponse();

        Hui hui = chiTietHui.getHui();
        response.setHuiId(hui.getId());
        response.setTenHui(hui.getTenHui());

        if (chiTietHui.getNgayKhui() != null) {
            if (chiTietHui.getNgayKhui().equals(selectedDate) && chiTietHui.getTienHot() != null) {
                response.setHuiHot(chiTietHui.getTienHot());
            } else if (chiTietHui.getNgayKhui().isBefore(selectedDate)) {
                response.setHuiChet(hui.getDayHui());
            }
        } else {
            response.setHuiSong(hui.getDayHui());
        }

        return response;
    }
}
