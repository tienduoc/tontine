package com.tontine.app.service;

import com.tontine.app.domain.ChiTietHui;
import com.tontine.app.domain.Hui;
import com.tontine.app.repository.ChiTietHuiRepository;
import com.tontine.app.repository.HuiRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
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
        CompletableFuture.runAsync(() ->
            hui
                .getChiTietHuis()
                .forEach(cth -> {
                    if (cth.getThamKeu() != null) {
                        cth.setTienHot(HuiHelper.calculateTienHotHui(cth));
                    }
                })
        );
        return huiRepository.save(hui);
    }

    public Hui update(Hui hui) {
        log.debug("Request to update Hui : {}", hui);
        return huiRepository.save(hui);
    }

    public Optional<Hui> partialUpdate(Hui hui) {
        log.debug("Request to partially update Hui : {}", hui);

        return huiRepository
            .findById(hui.getId())
            .map(existingHui -> {
                if (hui.getTenHui() != null) {
                    existingHui.setTenHui(hui.getTenHui());
                }
                if (hui.getNgayTao() != null) {
                    existingHui.setNgayTao(hui.getNgayTao());
                }
                if (hui.getLoaiHui() != null) {
                    existingHui.setLoaiHui(hui.getLoaiHui());
                }
                if (hui.getDayHui() != null) {
                    existingHui.setDayHui(hui.getDayHui());
                }
                if (hui.getThamKeu() != null) {
                    existingHui.setThamKeu(hui.getThamKeu());
                }
                if (hui.getSoPhan() != null) {
                    existingHui.setSoPhan(hui.getSoPhan());
                }

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
        return huiRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Hui> findOne(Long id) {
        log.debug("Request to get Hui : {}", id);
        return huiRepository.findById(id);
    }

    public void delete(Long id) {
        log.debug("Request to delete Hui : {}", id);
        huiRepository.deleteById(id);
    }

    public List<Hui> getHuisByNgayKhui(LocalDate date) {
        return chiTietHuiRepository.findAllByNgayKhui(date)
            .stream()
            .map(ChiTietHui::getHui)
            .distinct()
            .sorted(Comparator.comparing(Hui::getNgayTao).reversed())
            .collect(Collectors.toList());
    }
}
