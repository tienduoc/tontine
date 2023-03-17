package com.tontine.app.web.rest;

import com.tontine.app.repository.HuiRepository;
import com.tontine.app.service.HuiService;
import com.tontine.app.service.dto.HuiDTO;
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
     * @param huiDTO the huiDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new huiDTO, or with status {@code 400 (Bad Request)} if the hui has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/huis")
    public ResponseEntity<HuiDTO> createHui(@RequestBody HuiDTO huiDTO) throws URISyntaxException {
        log.debug("REST request to save Hui : {}", huiDTO);
        if (huiDTO.getId() != null) {
            throw new BadRequestAlertException("A new hui cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HuiDTO result = huiService.save(huiDTO);
        return ResponseEntity
            .created(new URI("/api/huis/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /huis/:id} : Updates an existing hui.
     *
     * @param id the id of the huiDTO to save.
     * @param huiDTO the huiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated huiDTO,
     * or with status {@code 400 (Bad Request)} if the huiDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the huiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/huis/{id}")
    public ResponseEntity<HuiDTO> updateHui(@PathVariable(value = "id", required = false) final Long id, @RequestBody HuiDTO huiDTO)
        throws URISyntaxException {
        log.debug("REST request to update Hui : {}, {}", id, huiDTO);
        if (huiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, huiDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!huiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        HuiDTO result = huiService.update(huiDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, huiDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /huis/:id} : Partial updates given fields of an existing hui, field will ignore if it is null
     *
     * @param id the id of the huiDTO to save.
     * @param huiDTO the huiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated huiDTO,
     * or with status {@code 400 (Bad Request)} if the huiDTO is not valid,
     * or with status {@code 404 (Not Found)} if the huiDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the huiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/huis/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HuiDTO> partialUpdateHui(@PathVariable(value = "id", required = false) final Long id, @RequestBody HuiDTO huiDTO)
        throws URISyntaxException {
        log.debug("REST request to partial update Hui partially : {}, {}", id, huiDTO);
        if (huiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, huiDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!huiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HuiDTO> result = huiService.partialUpdate(huiDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, huiDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /huis} : get all the huis.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of huis in body.
     */
    @GetMapping("/huis")
    public ResponseEntity<List<HuiDTO>> getAllHuis(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Huis");
        Page<HuiDTO> page = huiService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /huis/:id} : get the "id" hui.
     *
     * @param id the id of the huiDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the huiDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/huis/{id}")
    public ResponseEntity<HuiDTO> getHui(@PathVariable Long id) {
        log.debug("REST request to get Hui : {}", id);
        Optional<HuiDTO> huiDTO = huiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(huiDTO);
    }

    /**
     * {@code DELETE  /huis/:id} : delete the "id" hui.
     *
     * @param id the id of the huiDTO to delete.
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
