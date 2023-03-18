package com.tontine.app.web.rest;

import com.tontine.app.domain.ChiTietHui;
import com.tontine.app.repository.ChiTietHuiRepository;
import com.tontine.app.service.ChiTietHuiService;
import com.tontine.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.tontine.app.domain.ChiTietHui}.
 */
@RestController
@RequestMapping("/api")
public class ChiTietHuiResource {

    private final Logger log = LoggerFactory.getLogger(ChiTietHuiResource.class);

    private static final String ENTITY_NAME = "chiTietHui";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ChiTietHuiService chiTietHuiService;

    private final ChiTietHuiRepository chiTietHuiRepository;

    public ChiTietHuiResource(ChiTietHuiService chiTietHuiService, ChiTietHuiRepository chiTietHuiRepository) {
        this.chiTietHuiService = chiTietHuiService;
        this.chiTietHuiRepository = chiTietHuiRepository;
    }

    /**
     * {@code POST  /chi-tiet-huis} : Create a new chiTietHui.
     *
     * @param chiTietHui the chiTietHui to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new chiTietHui, or with status {@code 400 (Bad Request)} if the chiTietHui has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/chi-tiet-huis")
    public ResponseEntity<ChiTietHui> createChiTietHui(@RequestBody ChiTietHui chiTietHui) throws URISyntaxException {
        log.debug("REST request to save ChiTietHui : {}", chiTietHui);
        if (chiTietHui.getId() != null) {
            throw new BadRequestAlertException("A new chiTietHui cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ChiTietHui result = chiTietHuiService.save(chiTietHui);
        return ResponseEntity
            .created(new URI("/api/chi-tiet-huis/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /chi-tiet-huis/:id} : Updates an existing chiTietHui.
     *
     * @param id the id of the chiTietHui to save.
     * @param chiTietHui the chiTietHui to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated chiTietHui,
     * or with status {@code 400 (Bad Request)} if the chiTietHui is not valid,
     * or with status {@code 500 (Internal Server Error)} if the chiTietHui couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/chi-tiet-huis/{id}")
    public ResponseEntity<ChiTietHui> updateChiTietHui(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ChiTietHui chiTietHui
    ) throws URISyntaxException {
        log.debug("REST request to update ChiTietHui : {}, {}", id, chiTietHui);
        if (chiTietHui.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, chiTietHui.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!chiTietHuiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ChiTietHui result = chiTietHuiService.update(chiTietHui);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, chiTietHui.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /chi-tiet-huis/:id} : Partial updates given fields of an existing chiTietHui, field will ignore if it is null
     *
     * @param id the id of the chiTietHui to save.
     * @param chiTietHui the chiTietHui to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated chiTietHui,
     * or with status {@code 400 (Bad Request)} if the chiTietHui is not valid,
     * or with status {@code 404 (Not Found)} if the chiTietHui is not found,
     * or with status {@code 500 (Internal Server Error)} if the chiTietHui couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/chi-tiet-huis/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ChiTietHui> partialUpdateChiTietHui(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ChiTietHui chiTietHui
    ) throws URISyntaxException {
        log.debug("REST request to partial update ChiTietHui partially : {}, {}", id, chiTietHui);
        if (chiTietHui.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, chiTietHui.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!chiTietHuiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ChiTietHui> result = chiTietHuiService.partialUpdate(chiTietHui);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, chiTietHui.getId().toString())
        );
    }

    /**
     * {@code GET  /chi-tiet-huis} : get all the chiTietHuis.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of chiTietHuis in body.
     */
    @GetMapping("/chi-tiet-huis")
    public ResponseEntity<List<ChiTietHui>> getAllChiTietHuis(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of ChiTietHuis");
        Page<ChiTietHui> page = chiTietHuiService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /chi-tiet-huis/:id} : get the "id" chiTietHui.
     *
     * @param id the id of the chiTietHui to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the chiTietHui, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/chi-tiet-huis/{id}")
    public ResponseEntity<ChiTietHui> getChiTietHui(@PathVariable Long id) {
        log.debug("REST request to get ChiTietHui : {}", id);
        Optional<ChiTietHui> chiTietHui = chiTietHuiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(chiTietHui);
    }

    /**
     * {@code DELETE  /chi-tiet-huis/:id} : delete the "id" chiTietHui.
     *
     * @param id the id of the chiTietHui to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/chi-tiet-huis/{id}")
    public ResponseEntity<Void> deleteChiTietHui(@PathVariable Long id) {
        log.debug("REST request to delete ChiTietHui : {}", id);
        chiTietHuiService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
