package com.tontine.app.web.rest;

import com.tontine.app.domain.HuiVien;
import com.tontine.app.repository.HuiVienRepository;
import com.tontine.app.service.HuiVienService;
import com.tontine.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

@RestController
@RequestMapping("/api")
public class HuiVienResource {

    private final Logger log = LoggerFactory.getLogger(HuiVienResource.class);
    private static final String ENTITY_NAME = "huiVien";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HuiVienService huiVienService;
    private final HuiVienRepository huiVienRepository;

    public HuiVienResource(HuiVienService huiVienService, HuiVienRepository huiVienRepository) {
        this.huiVienService = huiVienService;
        this.huiVienRepository = huiVienRepository;
    }

    @PostMapping("/hui-viens")
    public ResponseEntity<HuiVien> createHuiVien(@RequestBody HuiVien huiVien) throws URISyntaxException {
        log.debug("REST request to save HuiVien : {}", huiVien);
        if (huiVien.getId() != null) {
            throw new BadRequestAlertException("A new huiVien cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HuiVien result = huiVienService.save(huiVien);
        return ResponseEntity
            .created(new URI("/api/hui-viens/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/hui-viens/{id}")
    public ResponseEntity<HuiVien> updateHuiVien(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody HuiVien huiVien
    ) {
        log.debug("REST request to update HuiVien : {}, {}", id, huiVien);
        validateHuiVienId(id, huiVien);

        HuiVien result = huiVienService.update(huiVien);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, huiVien.getId().toString()))
            .body(result);
    }

    private void validateHuiVienId(Long id, HuiVien huiVien) {
        if (huiVien.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, huiVien.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!huiVienRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
    }

    @PatchMapping(value = "/hui-viens/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HuiVien> partialUpdateHuiVien(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody HuiVien huiVien
    ) {
        log.debug("REST request to partial update HuiVien partially : {}, {}", id, huiVien);
        validateHuiVienId(id, huiVien);

        Optional<HuiVien> result = huiVienService.partialUpdate(huiVien);
        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, huiVien.getId().toString())
        );
    }

    @GetMapping("/hui-viens")
    public ResponseEntity<List<HuiVien>> getAllHuiViens(@ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of HuiViens");
        Page<HuiVien> page = huiVienService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/hui-viens/{id}")
    public ResponseEntity<HuiVien> getHuiVien(@PathVariable Long id) {
        log.debug("REST request to get HuiVien : {}", id);
        Optional<HuiVien> huiVienOpt = huiVienService.findOne(id);

        if (huiVienOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        HuiVien huiVien = huiVienOpt.get();
        AtomicLong tongHuiSong = new AtomicLong(0L);
        AtomicLong tongHuiChet = new AtomicLong(0L);

        huiVien
            .getChiTietHuis()
            .forEach(chiTietHui -> {
                long calculatedValue;
                if (chiTietHui.getThamKeu() == null) {
                    calculatedValue = chiTietHui.getHui().getDayHui() * chiTietHui.getKy().longValue();
                    chiTietHui.setHuiSong(calculatedValue);
                    tongHuiSong.addAndGet(calculatedValue);
                } else {
                    calculatedValue = chiTietHui.getHui().getDayHui() * (chiTietHui.getHui().getSoPhan() - chiTietHui.getKy());
                    chiTietHui.setHuiChet(calculatedValue);
                    tongHuiChet.addAndGet(calculatedValue);
                }
            });
        huiVien.setTongHuiSong(tongHuiSong.get());
        huiVien.setTongHuiChet(tongHuiChet.get());

        return ResponseUtil.wrapOrNotFound(Optional.of(huiVien));
    }

    @DeleteMapping("/hui-viens/{id}")
    public ResponseEntity<Void> deleteHuiVien(@PathVariable Long id) {
        log.debug("REST request to delete HuiVien : {}", id);
        huiVienService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
