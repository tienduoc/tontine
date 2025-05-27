package com.tontine.app.web.rest;

import com.tontine.app.domain.ChiTietHui;
import com.tontine.app.repository.ChiTietHuiRepository;
import com.tontine.app.service.ChiTietHuiService;
import com.tontine.app.web.rest.errors.BadRequestAlertException;
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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/chi-tiet-huis")
public class ChiTietHuiResource {

    private static final String ENTITY_NAME = "chiTietHui";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ChiTietHuiService chiTietHuiService;
    private final ChiTietHuiRepository chiTietHuiRepository;

    public ChiTietHuiResource(ChiTietHuiService chiTietHuiService, ChiTietHuiRepository chiTietHuiRepository) {
        this.chiTietHuiService = chiTietHuiService;
        this.chiTietHuiRepository = chiTietHuiRepository;
    }

    @PostMapping
    public ResponseEntity<ChiTietHui> createChiTietHui(@Valid @RequestBody ChiTietHui chiTietHui) {
        if (chiTietHui.getId() != null) {
            throw new BadRequestAlertException("A new chiTietHui cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ChiTietHui result = chiTietHuiService.save(chiTietHui);
        URI location = buildLocationUri(result.getId());
        return ResponseEntity
            .created(location)
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChiTietHui> updateChiTietHui(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ChiTietHui chiTietHui
    ) {
        validateChiTietHuiForUpdate(id, chiTietHui);
        ChiTietHui result = chiTietHuiService.update(chiTietHui);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, chiTietHui.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public ResponseEntity<ChiTietHui> partialUpdateChiTietHui(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ChiTietHui chiTietHui
    ) {
        validateChiTietHuiForUpdate(id, chiTietHui);
        Optional<ChiTietHui> result = chiTietHuiService.partialUpdate(chiTietHui);
        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, chiTietHui.getId().toString())
        );
    }

    @GetMapping
    public ResponseEntity<List<ChiTietHui>> getAllChiTietHuis(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        Page<ChiTietHui> page = chiTietHuiService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(
            ServletUriComponentsBuilder.fromCurrentRequest(),
            page
        );

        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChiTietHui> getChiTietHui(@PathVariable Long id) {
        Optional<ChiTietHui> chiTietHui = chiTietHuiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(chiTietHui);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChiTietHui(@PathVariable Long id) {
        chiTietHuiService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    private void validateChiTietHuiForUpdate(Long id, ChiTietHui chiTietHui) {
        if (chiTietHui.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, chiTietHui.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!chiTietHuiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
    }

    private URI buildLocationUri(Long id) {
        return ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(id)
            .toUri();
    }
}
