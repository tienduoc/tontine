package com.tontine.app.service;

import com.tontine.app.domain.ChiTietHui;
import com.tontine.app.domain.Hui;
import com.tontine.app.repository.ChiTietHuiRepository;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ChiTietHui}.
 */
@Service
@Transactional
public class ChiTietHuiService {

    private final Logger log = LoggerFactory.getLogger(ChiTietHuiService.class);

    private final ChiTietHuiRepository chiTietHuiRepository;

    private final HuiService huiService;

    private final CacheManager cacheManager;

    public ChiTietHuiService(ChiTietHuiRepository chiTietHuiRepository, HuiService huiService, CacheManager cacheManager) {
        this.chiTietHuiRepository = chiTietHuiRepository;
        this.huiService = huiService;
        this.cacheManager = cacheManager;
    }

    /**
     * Save a chiTietHui.
     *
     * @param chiTietHui the entity to save.
     * @return the persisted entity.
     */
    public ChiTietHui save(ChiTietHui chiTietHui) {
        log.debug("Request to save ChiTietHui : {}", chiTietHui);
        chiTietHui.setTienHot(HuiHelper.calculateTienHotHui(chiTietHui));
        return chiTietHuiRepository.save(chiTietHui);
    }

    /**
     * Update a chiTietHui.
     *
     * @param chiTietHui the entity to save.
     * @return the persisted entity.
     */
    public ChiTietHui update(ChiTietHui chiTietHui) {
        log.debug("Request to update ChiTietHui : {}", chiTietHui);
        // Calculate ky number
        Optional<Hui> hui = huiService.findOne(chiTietHui.getHui().getId());
        hui
            .flatMap(value -> value.getChiTietHuis().stream().filter(e -> e.getKy() != null).max(Comparator.comparingInt(ChiTietHui::getKy))
            )
            .ifPresent(existedMember -> chiTietHui.setKy(existedMember.getKy() + 1));
        chiTietHui.setTienHot(HuiHelper.calculateTienHotHui(chiTietHui));
        return chiTietHuiRepository.save(chiTietHui);
    }

    /**
     * Partially update a chiTietHui.
     *
     * @param chiTietHui the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ChiTietHui> partialUpdate(ChiTietHui chiTietHui) {
        log.debug("Request to partially update ChiTietHui : {}", chiTietHui);

        return chiTietHuiRepository
            .findById(chiTietHui.getId())
            .map(existingChiTietHui -> {
                if (chiTietHui.getThamKeu() != null) {
                    existingChiTietHui.setThamKeu(chiTietHui.getThamKeu());
                }
                if (chiTietHui.getNgayKhui() != null) {
                    existingChiTietHui.setNgayKhui(chiTietHui.getNgayKhui());
                }

                return existingChiTietHui;
            })
            .map(chiTietHuiRepository::save);
    }

    /**
     * Get all the chiTietHuis.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ChiTietHui> findAll(Pageable pageable) {
        log.debug("Request to get all ChiTietHuis");
        return chiTietHuiRepository.findAll(pageable);
    }

    /**
     * Get one chiTietHui by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ChiTietHui> findOne(Long id) {
        log.debug("Request to get ChiTietHui : {}", id);
        ChiTietHui cacheChiTietHui = Objects
            .requireNonNull(cacheManager.getCache(chiTietHuiRepository.CHI_TIET_HUI_BY_ID))
            .get(id, ChiTietHui.class);
        if (cacheChiTietHui != null) {
            return Optional.of(cacheChiTietHui);
        }
        return chiTietHuiRepository.findById(id);
    }

    /**
     * Delete the chiTietHui by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ChiTietHui : {}", id);
        chiTietHuiRepository.deleteById(id);
    }
}
