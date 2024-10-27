package com.tontine.app.service;

import com.tontine.app.domain.ChiTietHui;
import com.tontine.app.domain.Hui;
import com.tontine.app.repository.ChiTietHuiRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ChiTietHuiService {

    private final Logger log = LoggerFactory.getLogger(ChiTietHuiService.class);

    private final ChiTietHuiRepository chiTietHuiRepository;

    private final HuiService huiService;


    public ChiTietHuiService(ChiTietHuiRepository chiTietHuiRepository, HuiService huiService, CacheManager cacheManager) {
        this.chiTietHuiRepository = chiTietHuiRepository;
        this.huiService = huiService;
    }

    public ChiTietHui save(ChiTietHui chiTietHui) {
        log.debug("Request to save ChiTietHui : {}", chiTietHui);
        chiTietHui.setTienHot(HuiHelper.calculateTienHotHui(chiTietHui));
        return chiTietHuiRepository.save(chiTietHui);
    }

    public synchronized ChiTietHui update(final ChiTietHui chiTietHui) {
        log.debug("Request to update ChiTietHui : {}", chiTietHui);
        Optional<ChiTietHui> chiTietHuiDb = chiTietHuiRepository.findById(chiTietHui.getId());
        Optional<Hui> hui = huiService.findOne(chiTietHui.getHui().getId());
        if (chiTietHuiDb.isPresent() && hui.isPresent() && chiTietHui.getThamKeu() != null) {
            long tongSoKiHienTai = hui.get().getChiTietHuis().stream().filter(e -> e.getKy() != null).count();
            // Tham keu = 0 => clear thong tin hot hui
            List<ChiTietHui> listKiGreater;
            if (chiTietHui.getThamKeu() < 0) {
                // Re-calculate ki number
                listKiGreater = chiTietHuiRepository.findChiTietHuisByKyGreaterThanAndHuiId(chiTietHui.getKy(), chiTietHui.getHui().getId());

                listKiGreater.forEach(cth -> cth.setKy(cth.getKy() - 1));
                chiTietHuiRepository.saveAll(listKiGreater);

                ChiTietHui chiTietHuiDbUpdated = chiTietHuiDb.get();
                chiTietHuiDbUpdated.setKy(null);
                chiTietHuiDbUpdated.ngayKhui(null);
                chiTietHuiDbUpdated.setThamKeu(null);
                chiTietHuiDbUpdated.setTienHot(null);
                return chiTietHuiRepository.save(chiTietHuiDbUpdated);
            }

            if (chiTietHuiDb.get().getKy() == null) {
                chiTietHui.setKy((int) tongSoKiHienTai + 1);
            }
            chiTietHui.setTienHot(HuiHelper.calculateTienHotHui(chiTietHui));
        }
        return chiTietHuiRepository.save(chiTietHui);
    }

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
                if (chiTietHui.getKy() != null) {
                    existingChiTietHui.setKy(chiTietHui.getKy());
                }
                if (chiTietHui.getTienHot() != null) {
                    existingChiTietHui.setTienHot(chiTietHui.getTienHot());
                }
                if (chiTietHui.getNickNameHuiVien() != null) {
                    existingChiTietHui.setNickNameHuiVien(chiTietHui.getNickNameHuiVien());
                }

                return existingChiTietHui;
            })
            .map(chiTietHuiRepository::save);
    }

    @Transactional(readOnly = true)
    public Page<ChiTietHui> findAll(Pageable pageable) {
        return chiTietHuiRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Optional<ChiTietHui> findOne(Long id) {
        return chiTietHuiRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<ChiTietHui> findByNgayKhui( LocalDate date) {
        return chiTietHuiRepository.findAllByNgayKhui( date );
    }

    public void delete(Long id) {
        log.debug("Request to delete ChiTietHui : {}", id);
        chiTietHuiRepository.deleteById(id);
    }
}
