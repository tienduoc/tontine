package com.tontine.app.web.rest;

import com.tontine.app.domain.HuiVien;
import com.tontine.app.repository.HuiVienRepository;
import com.tontine.app.service.HuiService;
import com.tontine.app.service.HuiVienService;
import com.tontine.app.util.HuiUtils;
import com.tontine.app.web.rest.errors.BadRequestAlertException;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class HuiVienResource {

    private static final String ENTITY_NAME = "huiVien";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HuiVienService huiVienService;
    private final HuiVienRepository huiVienRepository;
    private final HuiService huiService;

    public HuiVienResource(HuiVienService huiVienService, HuiVienRepository huiVienRepository, HuiService huiService) {
        this.huiVienService = huiVienService;
        this.huiVienRepository = huiVienRepository;
        this.huiService = huiService;
    }

    @PostMapping("/hui-viens")
    public ResponseEntity<HuiVien> createHuiVien(@RequestBody HuiVien huiVien) {
        if (huiVien.getId() != null) {
            throw new BadRequestAlertException("A new huiVien cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HuiVien result = huiVienService.save(huiVien);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(result.getId())
            .toUri();
        return ResponseEntity.created(location)
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/hui-viens/{id}")
    public ResponseEntity<HuiVien> updateHuiVien(@PathVariable Long id, @RequestBody HuiVien huiVien) {
        validateHuiVienId(id, huiVien);
        HuiVien result = huiVienService.update(huiVien);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, huiVien.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/hui-viens/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public ResponseEntity<HuiVien> partialUpdateHuiVien(@PathVariable Long id, @RequestBody HuiVien huiVien) {
        validateHuiVienId(id, huiVien);
        Optional<HuiVien> result = huiVienService.partialUpdate(huiVien);
        return ResponseUtil.wrapOrNotFound(result,
                                           HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, huiVien.getId().toString()));
    }

    @GetMapping("/hui-viens")
    public ResponseEntity<List<HuiVien>> getAllHuiVien(@ParameterObject Pageable pageable) {
        Page<HuiVien> page = huiVienService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/hui-viens/{id}")
    public ResponseEntity<HuiVien> getHuiVienById(@PathVariable Long id) {
        Optional<HuiVien> huiVienOpt = huiVienService.findOne(id);
        huiVienOpt.ifPresent( huiVien -> HuiUtils.calculateTongHui( huiVien, huiService ));
        return ResponseUtil.wrapOrNotFound(huiVienOpt);
    }

    @DeleteMapping("/hui-viens/{id}")
    public ResponseEntity<Void> deleteHuiVien(@PathVariable Long id) {
        huiVienService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    private void validateHuiVienId(Long id, HuiVien huiVien) {
        if (huiVien.getId() == null || !Objects.equals(id, huiVien.getId()) || !huiVienRepository.existsById(id)) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
    }
}
