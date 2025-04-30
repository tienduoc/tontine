package com.tontine.app.web.rest;

import com.tontine.app.domain.HuiVien;
import com.tontine.app.response.HuiKhuiResponse;
import com.tontine.app.response.PhieuDongHuiResponse;
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
import java.util.Collections;
import java.util.List;

/**
 * REST controller for managing Hui-Khui related operations.
 */
@RestController
@RequestMapping("/api/ds-hui-khui")
public class HuiKhuiResource {

    private final HuiService huiService;
    private final HuiVienService huiVienService;
    private final HuiKhuiMapper huiKhuiMapper;

    public HuiKhuiResource(HuiService huiService, HuiVienService huiVienService, HuiKhuiMapper huiKhuiMapper) {
        this.huiService = huiService;
        this.huiVienService = huiVienService;
        this.huiKhuiMapper = huiKhuiMapper;
    }

    /**
     * GET /api/ds-hui-khui : Get all huis for a specific date.
     *
     * @param date the date in format yyyyMMdd
     * @return the ResponseEntity with status 200 (OK) and the list of huis in body,
     *         or with status 400 (Bad Request) if the date format is invalid
     */
    @GetMapping
    public ResponseEntity<List<HuiVien>> getAllHuis(@RequestParam String date) {
        return DateUtils.parseDate(date)
                .map(huiService::getHuisByNgayKhui)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().body(Collections.emptyList()));
    }

    /**
     * GET /api/ds-hui-khui/{userId} : Get all huis for a specific user.
     *
     * @param userId the ID of the user
     * @return the ResponseEntity with status 200 (OK) and the list of hui responses in body
     */
    @GetMapping("/{userId}")
    public ResponseEntity<List<HuiKhuiResponse>> getHui(@PathVariable Long userId) {
        List<HuiKhuiResponse> huiResponses = huiVienService.findOne(userId)
                .map(huiKhuiMapper::mapToHuiKhuiResponses)
                .orElse(Collections.emptyList());

        return ResponseEntity.ok(huiResponses);
    }

    /**
     * GET /api/ds-hui-khui/user/{userId} : Get phieu dong hui for a specific user and date.
     *
     * @param userId the ID of the user
     * @param date the date in format yyyyMMdd
     * @return the ResponseEntity with status 200 (OK) and the phieu dong hui response in body
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<PhieuDongHuiResponse> getPhieuDongHui(
            @PathVariable long userId,
            @RequestParam String date
    ) {
        return huiVienService.findOne(userId)
                .map(huiVien -> {
                    try {
                        LocalDate parsedDate = DateUtils.parseDateOrThrow(date);
                        return ResponseEntity.ok(huiKhuiMapper.buildPhieuDongHuiResponse(huiVien, parsedDate));
                    } catch (IllegalArgumentException e) {
                        return ResponseEntity.badRequest().body(new PhieuDongHuiResponse());
                    }
                })
                .orElse(ResponseEntity.ok(new PhieuDongHuiResponse()));
    }
}
