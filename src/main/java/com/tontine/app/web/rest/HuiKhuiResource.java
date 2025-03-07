package com.tontine.app.web.rest;

import com.tontine.app.domain.Hui;
import com.tontine.app.service.HuiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.ResponseUtil;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class HuiKhuiResource {

    private final HuiService huiService;

    public HuiKhuiResource(HuiService huiService) {
        this.huiService = huiService;
    }

    @GetMapping("/ds-hui-khui")
    public ResponseEntity<List<Hui>> getAllHuis() {
        List<Hui> huis = huiService.findAll();
        return ResponseEntity.ok().body(huis);
    }

    @GetMapping("/ds-hui-khui/{id}")
    public ResponseEntity<Hui> getHui(@PathVariable Long id) {
        Optional<Hui> hui = huiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(hui);
    }
}
