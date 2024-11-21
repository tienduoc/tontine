package com.tontine.app.web.rest;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import com.tontine.app.domain.ChiTietHui;
import com.tontine.app.domain.Hui;
import com.tontine.app.domain.ThongKe;
import com.tontine.app.repository.HuiRepository;
import com.tontine.app.service.HuiService;
import com.tontine.app.web.rest.errors.BadRequestAlertException;
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
public class HuiResource {

    private static final String ENTITY_NAME = "hui";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HuiService huiService;
    private final HuiRepository huiRepository;

    public HuiResource(HuiService huiService, HuiRepository huiRepository) {
        this.huiService = huiService;
        this.huiRepository = huiRepository;
    }

    @PostMapping("/huis")
    public ResponseEntity<Hui> createHui(@RequestBody Hui hui) {
        if (hui.getId() != null) {
            throw new BadRequestAlertException("A new hui cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Hui result = huiService.save(hui);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(result.getId())
            .toUri();
        return ResponseEntity.created(location)
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/huis/{id}")
    public ResponseEntity<Hui> updateHui(@PathVariable Long id, @RequestBody Hui hui) {
        validateHuiId(id, hui);
        checkHuiChiTiet(hui);

        Hui result = huiService.update(hui);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, hui.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/huis/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public ResponseEntity<Hui> partialUpdateHui(@PathVariable Long id, @RequestBody Hui hui) {
        validateHuiId(id, hui);
        Optional<Hui> result = huiService.partialUpdate(hui);
        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, hui.getId().toString()));
    }

    private void validateHuiId(Long id, Hui hui) {
        if (hui.getId() == null || !Objects.equals( id, hui.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!huiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
    }

    private void checkHuiChiTiet(Hui hui) {
        if (hui.getChiTietHuis().stream().anyMatch(e -> e.getKy() != null)) {
            throw new BadRequestAlertException("Cannot change the number of Chi Tiet after opening", ENTITY_NAME, "dayinvalid");
        }
    }

    @GetMapping("/huis")
    public ResponseEntity<List<Hui>> getAllHuis(Pageable pageable) {
        Page<Hui> page = huiService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/huis/thongke")
    public ResponseEntity<ThongKe> getHuiStats(Pageable pageable) {
        Page<Hui> page = huiService.findAll(pageable);
        AtomicInteger soHuiSong = new AtomicInteger();
        AtomicInteger soHuiChet = new AtomicInteger();

        page.getContent().forEach(hui -> {
            boolean isHuiChet = hui.getChiTietHuis().stream()
                .map( ChiTietHui::getKy)
                .filter( Objects::nonNull )
                .max(Integer::compareTo)
                .filter(ky -> Objects.equals(hui.getSoPhan(), ky))
                .isPresent();
            if (isHuiChet) {
                soHuiChet.getAndIncrement();
            } else {
                soHuiSong.getAndIncrement();
            }
        });

        return ResponseUtil.wrapOrNotFound(Optional.of(new ThongKe(soHuiSong.get(), soHuiChet.get())));
    }

    @GetMapping("/huis/{id}")
    public ResponseEntity<Hui> getHui(@PathVariable Long id) {
        Optional<Hui> hui = huiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(hui);
    }

    @DeleteMapping("/huis/{id}")
    public ResponseEntity<Void> deleteHui(@PathVariable Long id) {
        huiService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
