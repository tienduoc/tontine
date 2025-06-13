package com.tontine.app.service.mapper;

import com.tontine.app.domain.ChiTietHui;
import com.tontine.app.domain.Hui;
import com.tontine.app.domain.HuiVien;
import com.tontine.app.response.HuiKhuiResponse;
import com.tontine.app.service.ChiTietHuiService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class HuiKhuiMapper {
    private final ChiTietHuiService chiTietHuiService;

    public HuiKhuiMapper(ChiTietHuiService chiTietHuiService) {
        this.chiTietHuiService = chiTietHuiService;
    }

    public List<HuiKhuiResponse> mapToHuiKhuiResponses(HuiVien huiVien, String date) {
        if (huiVien == null) return Collections.emptyList();
        LocalDate parsedDate = LocalDate.parse(date);

        Set<Long> huiThamGiaIds = huiVien.getChiTietHuis().stream()
            .map(cth -> cth.getHui().getId())
            .collect(Collectors.toSet());

        Set<Long> huiKhuiIds = chiTietHuiService.findByNgayKhui(parsedDate).stream()
            .map(cth -> cth.getHui().getId())
            .collect(Collectors.toSet());

        // Only keep IDs present in both sets
        huiThamGiaIds.retainAll(huiKhuiIds);

        return huiVien.getChiTietHuis().stream()
            .filter(cth -> huiThamGiaIds.contains(cth.getHui().getId()))
            .sorted(Comparator.comparing(cth -> cth.getHui().getDayHui()))
            .map(cth -> toResponse(cth, parsedDate))
            .sorted(Comparator.comparing(HuiKhuiResponse::getDayHui))
            .collect(Collectors.toList());
    }

    private HuiKhuiResponse toResponse(ChiTietHui chiTietHui, LocalDate selectedDate) {
        Hui hui = chiTietHui.getHui();
        HuiKhuiResponse response = new HuiKhuiResponse();

        response.setChiTietId(chiTietHui.getId());
        response.setHuiVienId(chiTietHui.getHuiVien().getId());
        response.setHuiId(hui.getId());
        response.setDayHui(hui.getTenHui());
        response.setSoTien(hui.getDayHui());
        response.setSoChan(hui.getSoPhan());

        ChiTietHui latestHotHui = getLatestHotHui(hui);
        if (latestHotHui != null) {
            response.setSoKyDong(latestHotHui.getKy());
            response.setSoTienBoTham(latestHotHui.getThamKeu());
        }

        if (latestHotHui != null) {
            boolean isBefore = chiTietHui.getNgayKhui() != null && chiTietHui.getNgayKhui().isBefore(selectedDate);
            boolean isAfter = chiTietHui.getNgayKhui() != null && chiTietHui.getNgayKhui().isAfter(selectedDate);
            if (isBefore) {
                response.setChet(true);
                response.setSoTienDong(hui.getDayHui());
            } else if (chiTietHui.getTienHot() == null || isAfter) {
                response.setSong(true);
                response.setSoTienDong(hui.getDayHui() - latestHotHui.getThamKeu());
            }
        }
        if (chiTietHui.getTienHot() != null && chiTietHui.getNgayKhui() != null && chiTietHui.getNgayKhui().isEqual(selectedDate)) {
            response.setSoTienHot(chiTietHui.getTienHot());
        }

        return response;
    }

    private ChiTietHui getLatestHotHui(Hui hui) {
        return hui.getChiTietHuis().stream()
            .filter(cth -> cth.getKy() != null)
            .max(Comparator.comparing(ChiTietHui::getKy))
            .orElse(null);
    }
}
