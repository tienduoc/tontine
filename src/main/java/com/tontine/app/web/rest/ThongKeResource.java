package com.tontine.app.web.rest;

import static com.tontine.app.web.rest.HuiHelper.getKyHienTai;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import com.tontine.app.domain.ChiTietHui;
import com.tontine.app.domain.Hui;
import com.tontine.app.domain.HuiVien;
import com.tontine.app.repository.ChiTietHuiRepository;
import com.tontine.app.response.ChiTietHuiKeuResponse;
import com.tontine.app.response.HuiKeuNgayResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.ResponseUtil;

@RestController
@RequestMapping("/api")
public class ThongKeResource {
    
    private static final String DATE_FORMAT = "yyyyMMdd";
    private final ChiTietHuiRepository chiTietHuiRepository;
    
    public ThongKeResource(ChiTietHuiRepository chiTietHuiRepository) {
        this.chiTietHuiRepository = chiTietHuiRepository;
    }
    
    @GetMapping("/thong-ke")
    public ResponseEntity<List<HuiKeuNgayResponse>> getHuiKeu(@RequestParam(required = false) String date) {
        List<HuiVien> huiViens = getDistinctHuiViens();
        
        List<HuiKeuNgayResponse> response = huiViens.stream()
          .map(huiVien -> toHuiKeuNgayResponse(huiVien, parseDate(date)))
          .collect(Collectors.toList());
        
        return ResponseUtil.wrapOrNotFound(Optional.of(response));
    }
    
    private LocalDate parseDate(String date) {
        return date != null ? LocalDate.parse(date, DateTimeFormatter.ofPattern(DATE_FORMAT)) : null;
    }
    
    private List<HuiVien> getDistinctHuiViens() {
        return chiTietHuiRepository.findAll().stream()
          .map(ChiTietHui::getHuiVien)
          .filter(Objects::nonNull)
          .distinct()
          .collect(Collectors.toList());
    }
    
    private HuiKeuNgayResponse toHuiKeuNgayResponse(HuiVien huiVien, LocalDate selectedDate) {
        HuiKeuNgayResponse response = new HuiKeuNgayResponse();
        List<ChiTietHuiKeuResponse> chiTietResponses = getChiTietHuiKeuResponses(huiVien, selectedDate);
        
        AtomicLong totalHuiSong = new AtomicLong();
        AtomicLong totalHuiChet = new AtomicLong();
        AtomicLong huiHot = new AtomicLong();
        
        calculateHuiSongHuiCHet( chiTietResponses, totalHuiSong, totalHuiChet, huiHot);
        
        long conLai = huiHot.get() - (totalHuiSong.get() + totalHuiChet.get());
        chiTietResponses.forEach(responseItem -> responseItem.setConLai(conLai));
        
        setHuiKeuNgayResponseFields(response, huiVien, chiTietResponses, conLai, huiHot, totalHuiSong, totalHuiChet);
        
        return response;
    }
    
    private List<ChiTietHuiKeuResponse> getChiTietHuiKeuResponses(HuiVien huiVien, LocalDate selectedDate) {
        return huiVien.getChiTietHuis().stream()
          .filter(e -> getKyHienTai(Optional.ofNullable(e.getHui())) != e.getHui().getSoPhan())
          .map(chiTietHui -> toChiTietHuiKeuResponse(chiTietHui, selectedDate))
          .collect(Collectors.toList());
    }
    
    private void calculateHuiSongHuiCHet( List<ChiTietHuiKeuResponse> chiTietResponses, AtomicLong totalHuiSong,
                                          AtomicLong totalHuiChet, AtomicLong huiHot) {
        chiTietResponses.forEach(response -> {
            if (response.getHuiHot() != 0L) {
                huiHot.addAndGet(response.getHuiHot());
            } else if (response.getHuiSong() != 0L) {
                totalHuiSong.addAndGet(response.getHuiSong());
            } else if (response.getHuiChet() != 0L) {
                totalHuiChet.addAndGet(response.getHuiChet());
            }
        });
    }
    
    private void setHuiKeuNgayResponseFields(HuiKeuNgayResponse response, HuiVien huiVien,
                                             List<ChiTietHuiKeuResponse> chiTietResponses, long conLai,
                                             AtomicLong huiHot, AtomicLong totalHuiSong, AtomicLong totalHuiChet) {
        response.setTenHuiVien(huiVien.getHoTen());
        response.setChiTiets(chiTietResponses);
        response.setConLai(conLai);
        response.setTongHuiHot(huiHot.get());
        response.setTongHuiSong(totalHuiSong.get());
        response.setTongHuiChet(totalHuiChet.get());
    }
    
    private ChiTietHuiKeuResponse toChiTietHuiKeuResponse(ChiTietHui chiTiet, LocalDate selectedDate) {
        ChiTietHuiKeuResponse response = new ChiTietHuiKeuResponse();
        
        Hui hui = chiTiet.getHui();
        response.setHuiId(hui.getId());
        response.setTenHui(hui.getTenHui());
        
        long kyHienTai = getKyHienTai(Optional.of(hui));
        Long dayHui = hui.getDayHui();
        if (chiTiet.getNgayKhui() != null) {
            if (chiTiet.getNgayKhui().equals(selectedDate) && chiTiet.getTienHot() != null) {
                response.setHuiHot(chiTiet.getTienHot());
            } else if (chiTiet.getNgayKhui().isBefore(selectedDate)) {
                response.setHuiChet(dayHui * (hui.getSoPhan() - kyHienTai));
            }
        } else {
            response.setHuiSong(dayHui * kyHienTai);
        }
        
        return response;
    }
}