package com.tontine.app.service.mapper;

import com.tontine.app.domain.ChiTietHui;
import com.tontine.app.domain.Hui;
import com.tontine.app.domain.HuiVien;
import com.tontine.app.response.DayHuiResponse;
import com.tontine.app.response.HuiKhuiResponse;
import com.tontine.app.response.PhieuDongHuiResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class HuiKhuiMapper {

    public List<HuiKhuiResponse> mapToHuiKhuiResponses(HuiVien huiVien, String date) {
        if (huiVien == null) {
            return Collections.emptyList();
        }

        return huiVien.getChiTietHuis()
                      .stream()
                      .map(ChiTietHui::getHui)
                      .filter(Objects::nonNull)
                      .distinct()
                      .map(hui -> mapToHuiKhuiResponse(hui, huiVien.getId(), date))
                      .filter(Objects::nonNull)
                      .collect(Collectors.toList());
    }

    public HuiKhuiResponse mapToHuiKhuiResponse(Hui hui, Long huiVienId, String date) {
        if (hui == null || huiVienId == null) {
            return null;
        }

        HuiKhuiResponse response = new HuiKhuiResponse();

        response.setHuiId(hui.getId());
        response.setDayHui(hui.getTenHui());
        response.setSoTien(hui.getDayHui());
        response.setSoChan(hui.getSoPhan());

        setHuiStatus(hui, huiVienId, response, date);
        setLatestKyInfo(hui, response);
        calculateAndSetSoTienDong(hui, response);

        return response;
    }

    private void setHuiStatus(Hui hui, Long huiVienId, HuiKhuiResponse response, String date) {
        Boolean isHuiChet = hui.getChiTietHuis()
                               .stream()
                               .filter(chiTiet -> chiTiet.getHuiVien() != null)
                               .filter(chiTiet -> Objects.equals(chiTiet.getHuiVien().getId(), huiVienId))
                               .filter(chiTiet -> Objects.equals(chiTiet.getHui().getId(), hui.getId()))
                               .anyMatch(chiTiet -> chiTiet.getThamKeu() != null);

        response.setChet(isHuiChet);
        response.setSong(!isHuiChet);
    }

    private void setLatestKyInfo(Hui hui, HuiKhuiResponse response) {
        hui.getChiTietHuis()
           .stream()
           .filter(chiTiet -> chiTiet.getKy() != null)
           .max(Comparator.comparing(ChiTietHui::getKy))
           .ifPresent(chiTietHui -> {
               response.setSoKyDong(chiTietHui.getKy());
               response.setSoTienBoTham(chiTietHui.getThamKeu());

               if (chiTietHui.getTienHot() != null) {
                   response.setSoTienHot(chiTietHui.getTienHot());
               }
           });
    }

    private void calculateAndSetSoTienDong(Hui hui, HuiKhuiResponse response) {
        if (Boolean.TRUE.equals(response.getChet())) {
            response.setSoTienDong(hui.getDayHui());
        } else if (Boolean.TRUE.equals(response.getSong())) {
            Long boTham = Optional.of(response.getSoTienBoTham()).orElse(0L);
            response.setSoTienDong(hui.getDayHui() - boTham);
        }
    }

    public PhieuDongHuiResponse buildPhieuDongHuiResponse(HuiVien huiVien, LocalDate selectedDate) {
        if (huiVien == null) {
            return new PhieuDongHuiResponse();
        }

        PhieuDongHuiResponse response = new PhieuDongHuiResponse();
        response.setHuiVien(huiVien.getHoTen());
        response.setDayHuis(mapToDayHuiResponses(huiVien, selectedDate));
        return response;
    }

    public List<DayHuiResponse> mapToDayHuiResponses(HuiVien huiVien, LocalDate selectedDate) {
        if (huiVien == null || selectedDate == null) {
            return Collections.emptyList();
        }

        Set<Long> seenHuiIds = new HashSet<>();
        return huiVien.getChiTietHuis()
                      .stream()
                      .filter(chiTietHui -> selectedDate.equals(chiTietHui.getNgayKhui()))
                      .map(ChiTietHui::getHui)
                      .filter(Objects::nonNull)
                      .filter(hui -> seenHuiIds.add(hui.getId()))
                      .map(hui -> mapToDayHuiResponse(hui, huiVien.getId()))
                      .filter(Objects::nonNull)
                      .sorted(Comparator.comparing(DayHuiResponse::getNgayHot, Comparator.nullsLast(Comparator.reverseOrder()))
                                        .thenComparing(DayHuiResponse::getHuiId, Comparator.nullsLast(Comparator.naturalOrder())))
                      .collect(Collectors.toList());
    }

    public DayHuiResponse mapToDayHuiResponse(Hui hui, long userId) {
        if (hui == null) {
            return null;
        }

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
           .filter(chiTietHui -> chiTietHui.getHuiVien() != null && chiTietHui.getHuiVien().getId() == userId)
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

    public int countHuiInstances(Hui hui, long userId, boolean isSong) {
        if (hui == null) {
            return 0;
        }

        return (int) hui.getChiTietHuis()
                        .stream()
                        .filter(e -> e.getHuiVien() != null && e.getHuiVien().getId() == userId)
                        .filter(e -> isSong == (e.getThamKeu() == null))
                        .count();
    }

    public long calculateSoTienDong(Hui hui, Long thamKeu, int song, int chet) {
        if (hui == null) {
            return 0;
        }

        long soTienDong = hui.getDayHui() - Optional.ofNullable(thamKeu).orElse(0L);
        return soTienDong * (song + chet);
    }
}
