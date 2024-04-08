package com.tontine.app.web.rest;

import com.tontine.app.domain.ChiTietHui;
import com.tontine.app.domain.Hui;
import com.tontine.app.domain.ThongKe;
import com.tontine.app.repository.HuiRepository;
import com.tontine.app.service.HuiService;
import com.tontine.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

/**
 * REST controller for managing {@link com.tontine.app.domain.Hui}.
 */
@RestController
@RequestMapping("/api")
public class HuiResource {

    private final Logger log = LoggerFactory.getLogger(HuiResource.class);

    private static final String ENTITY_NAME = "hui";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HuiService huiService;

    private final HuiRepository huiRepository;

    public HuiResource(HuiService huiService, HuiRepository huiRepository) {
        this.huiService = huiService;
        this.huiRepository = huiRepository;
    }

    /**
     * {@code POST  /huis} : Create a new hui.
     *
     * @param hui the hui to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new hui, or with status {@code 400 (Bad Request)} if
     * the hui has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/huis")
    public ResponseEntity<Hui> createHui(@RequestBody Hui hui) throws URISyntaxException {
        log.debug("REST request to save Hui : {}", hui);
        if (hui.getId() != null) {
            throw new BadRequestAlertException("A new hui cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Hui result = huiService.save(hui);
        return ResponseEntity
            .created(new URI("/api/huis/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /huis/:id} : Updates an existing hui.
     *
     * @param id  the id of the hui to save.
     * @param hui the hui to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hui,
     * or with status {@code 400 (Bad Request)} if the hui is not valid,
     * or with status {@code 500 (Internal Server Error)} if the hui couldn't be updated.
     */
    @PutMapping("/huis/{id}")
    public ResponseEntity<Hui> updateHui(@PathVariable(value = "id", required = false) final Long id, @RequestBody Hui hui) {
        log.debug("REST request to update Hui : {}, {}", id, hui);
        validateHuiId(id, hui);

        if (hui.getChiTietHuis().stream().anyMatch(e -> e.getKy() != null)) {
            throw new BadRequestAlertException("Khong the thay doi so Day sau khi khui", ENTITY_NAME, "dayinvalid");
        }

        Hui result = huiService.update(hui);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, hui.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /huis/:id} : Partial updates given fields of an existing hui, field will ignore if it is null
     *
     * @param id  the id of the hui to save.
     * @param hui the hui to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hui,
     * or with status {@code 400 (Bad Request)} if the hui is not valid,
     * or with status {@code 404 (Not Found)} if the hui is not found,
     * or with status {@code 500 (Internal Server Error)} if the hui couldn't be updated.
     */
    @PatchMapping(value = "/huis/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Hui> partialUpdateHui(@PathVariable(value = "id", required = false) final Long id, @RequestBody Hui hui) {
        log.debug("REST request to partial update Hui partially : {}, {}", id, hui);
        validateHuiId(id, hui);

        Optional<Hui> result = huiService.partialUpdate(hui);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, hui.getId().toString())
        );
    }

    private void validateHuiId(Long id, Hui hui) {
        if (hui.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hui.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!huiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
    }

    /**
     * {@code GET  /huis} : get all the huis.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of huis in body.
     */
    @GetMapping("/huis")
    public ResponseEntity<List<Hui>> getAllHuis(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Huis");
        Page<Hui> page = huiService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/huis/thongke")
    public ResponseEntity<ThongKe> getHuiStats(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        Page<Hui> page = huiService.findAll(pageable);
        AtomicInteger soHuiSong = new AtomicInteger();
        AtomicInteger soHuiChet = new AtomicInteger();
        for (Hui hui : page.getContent()) {
            Optional<ChiTietHui> ky = Optional.empty();
            if (!hui.getChiTietHuis().isEmpty()) {
                ky = hui.getChiTietHuis().stream().filter(e -> e.getKy() != null).max(Comparator.comparingInt(ChiTietHui::getKy));
            }
            if (ky.isPresent() && Objects.equals(hui.getSoPhan(), ky.get().getKy())) {
                soHuiChet.getAndIncrement();
            } else {
                soHuiSong.getAndIncrement();
            }
        }
        return ResponseUtil.wrapOrNotFound(Optional.of(new ThongKe(soHuiSong.get(), soHuiChet.get())));
    }

    /**
     * {@code GET  /huis/:id} : get the "id" hui.
     *
     * @param id the id of the hui to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the hui, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/huis/{id}")
    public ResponseEntity<Hui> getHui(@PathVariable Long id) {
        log.debug("REST request to get Hui : {}", id);
        Optional<Hui> hui = huiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(hui);
    }

    /**
     * {@code DELETE  /huis/:id} : delete the "id" hui.
     *
     * @param id the id of the hui to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/huis/{id}")
    public ResponseEntity<Void> deleteHui(@PathVariable Long id) {
        log.debug("REST request to delete Hui : {}", id);
        huiService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
