package com.tontine.app.web.rest;

import com.tontine.app.repository.HuiVienRepository;
import com.tontine.app.service.HuiVienService;
import com.tontine.app.service.dto.HuiVienDTO;
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
 * REST controller for managing {@link com.tontine.app.domain.HuiVien}.
 */
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

    /**
     * {@code POST  /hui-viens} : Create a new huiVien.
     *
     * @param huiVienDTO the huiVienDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new huiVienDTO, or with status {@code 400 (Bad Request)} if the huiVien has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/hui-viens")
    public ResponseEntity<HuiVienDTO> createHuiVien(@RequestBody HuiVienDTO huiVienDTO) throws URISyntaxException {
        log.debug("REST request to save HuiVien : {}", huiVienDTO);
        if (huiVienDTO.getId() != null) {
            throw new BadRequestAlertException("A new huiVien cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HuiVienDTO result = huiVienService.save(huiVienDTO);
        return ResponseEntity
            .created(new URI("/api/hui-viens/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /hui-viens/:id} : Updates an existing huiVien.
     *
     * @param id the id of the huiVienDTO to save.
     * @param huiVienDTO the huiVienDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated huiVienDTO,
     * or with status {@code 400 (Bad Request)} if the huiVienDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the huiVienDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/hui-viens/{id}")
    public ResponseEntity<HuiVienDTO> updateHuiVien(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody HuiVienDTO huiVienDTO
    ) throws URISyntaxException {
        log.debug("REST request to update HuiVien : {}, {}", id, huiVienDTO);
        if (huiVienDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, huiVienDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!huiVienRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        HuiVienDTO result = huiVienService.update(huiVienDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, huiVienDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /hui-viens/:id} : Partial updates given fields of an existing huiVien, field will ignore if it is null
     *
     * @param id the id of the huiVienDTO to save.
     * @param huiVienDTO the huiVienDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated huiVienDTO,
     * or with status {@code 400 (Bad Request)} if the huiVienDTO is not valid,
     * or with status {@code 404 (Not Found)} if the huiVienDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the huiVienDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/hui-viens/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HuiVienDTO> partialUpdateHuiVien(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody HuiVienDTO huiVienDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update HuiVien partially : {}, {}", id, huiVienDTO);
        if (huiVienDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, huiVienDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!huiVienRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HuiVienDTO> result = huiVienService.partialUpdate(huiVienDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, huiVienDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /hui-viens} : get all the huiViens.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of huiViens in body.
     */
    @GetMapping("/hui-viens")
    public ResponseEntity<List<HuiVienDTO>> getAllHuiViens(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of HuiViens");
        Page<HuiVienDTO> page = huiVienService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /hui-viens/:id} : get the "id" huiVien.
     *
     * @param id the id of the huiVienDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the huiVienDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/hui-viens/{id}")
    public ResponseEntity<HuiVienDTO> getHuiVien(@PathVariable Long id) {
        log.debug("REST request to get HuiVien : {}", id);
        Optional<HuiVienDTO> huiVienDTO = huiVienService.findOne(id);
        return ResponseUtil.wrapOrNotFound(huiVienDTO);
    }

    /**
     * {@code DELETE  /hui-viens/:id} : delete the "id" huiVien.
     *
     * @param id the id of the huiVienDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
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
