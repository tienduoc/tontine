package com.tontine.app.service;

import com.tontine.app.domain.ChiTietHui;
import com.tontine.app.domain.Hui;
import com.tontine.app.domain.HuiVien;
import com.tontine.app.repository.ChiTietHuiRepository;
import com.tontine.app.repository.HuiRepository;
import com.tontine.app.util.HuiUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class HuiService {

    private final Logger log = LoggerFactory.getLogger(HuiService.class);

    private final HuiRepository huiRepository;

    private final ChiTietHuiRepository chiTietHuiRepository;

    public HuiService(HuiRepository huiRepository, ChiTietHuiRepository chiTietHuiRepository) {
        this.huiRepository = huiRepository;
        this.chiTietHuiRepository = chiTietHuiRepository;
    }

    public synchronized Hui save(final Hui hui) {
        log.debug("Request to save Hui : {}", hui);

        hui.getChiTietHuis().forEach(cth -> {
            if (cth.getThamKeu() != null) {
                cth.setTienHot(HuiUtils.calculateTienHotHui(cth));
            }
        });

        return huiRepository.save(hui);
    }

    public Hui update(Hui hui) {
        log.debug("Request to update Hui : {}", hui);
        return huiRepository.save(hui);
    }

    public Optional<Hui> partialUpdate(Hui hui) {
        log.debug("Request to partially update Hui : {}", hui);

        return huiRepository.findByIdWithChiTietHuis(hui.getId())
            .map(existingHui -> {
                // Apply non-null updates
                Optional.ofNullable(hui.getTenHui()).ifPresent(existingHui::setTenHui);
                Optional.ofNullable(hui.getNgayTao()).ifPresent(existingHui::setNgayTao);
                Optional.ofNullable(hui.getLoaiHui()).ifPresent(existingHui::setLoaiHui);
                Optional.ofNullable(hui.getDayHui()).ifPresent(existingHui::setDayHui);
                Optional.ofNullable(hui.getThamKeu()).ifPresent(existingHui::setThamKeu);
                Optional.ofNullable(hui.getSoPhan()).ifPresent(existingHui::setSoPhan);

                return existingHui;
            })
            .map(huiRepository::save);
    }

    @Transactional(readOnly = true)
    public Page<Hui> findAll(Pageable pageable) {
        log.debug("Request to get all Huis");
        return huiRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Optional<Hui> findOne(Long id) {
        log.debug("Request to get Hui : {}", id);
        return huiRepository.findByIdWithChiTietHuis(id);
    }

    public void delete(Long id) {
        log.debug("Request to delete Hui : {}", id);
        huiRepository.deleteById(id);
    }

    public List<HuiVien> getHuisByNgayKhui(LocalDate date) {
        return new ArrayList<>(chiTietHuiRepository.findAllByNgayKhuiWithHuiAndHuiVien(date).stream()
            .map(ChiTietHui::getHuiVien)
            .filter(Objects::nonNull)
            .collect(Collectors.toMap(
                HuiVien::getId,
                Function.identity(),
                (existing, replacement) -> existing,
                LinkedHashMap::new
            ))
            .values());
    }
}
