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

import java.text.Collator;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
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

        // Refactored to use streams instead of forEach loop
        CompletableFuture
            .runAsync(() ->
                hui.getChiTietHuis().stream()
                    .filter(chiTiet -> chiTiet.getThamKeu() != null)
                    .forEach(chiTiet -> chiTiet.setTienHot(HuiUtils.calculateTienHotHui(chiTiet)))
            );

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
    public List<Hui> findAll() {
        return huiRepository.findAllWithChiTietHuis();
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
        Collator vietnameseCollator = Collator.getInstance(new Locale("vi", "VN"));

        List<ChiTietHui> chiTietHuiList = chiTietHuiRepository.findAllByNgayKhui(date);
        List<Hui> huiList = chiTietHuiList.stream().map(ChiTietHui::getHui).collect(Collectors.toList());

        return huiList.stream()
            .flatMap(hui -> hui.getChiTietHuis().stream())
            .map(ChiTietHui::getHuiVien)
            .distinct()
            .sorted((hv1, hv2) -> vietnameseCollator.compare(hv1.getHoTen(), hv2.getHoTen()))
            .collect(Collectors.toList());
    }
}
