package com.tontine.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tontine.app.IntegrationTest;
import com.tontine.app.domain.HuiVien;
import com.tontine.app.repository.HuiVienRepository;
import com.tontine.app.service.dto.HuiVienDTO;
import com.tontine.app.service.mapper.HuiVienMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link HuiVienResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HuiVienResourceIT {

    private static final String DEFAULT_HO_TEN = "AAAAAAAAAA";
    private static final String UPDATED_HO_TEN = "BBBBBBBBBB";

    private static final String DEFAULT_SDT = "AAAAAAAAAA";
    private static final String UPDATED_SDT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/hui-viens";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HuiVienRepository huiVienRepository;

    @Autowired
    private HuiVienMapper huiVienMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHuiVienMockMvc;

    private HuiVien huiVien;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HuiVien createEntity(EntityManager em) {
        HuiVien huiVien = new HuiVien().hoTen(DEFAULT_HO_TEN).sdt(DEFAULT_SDT);
        return huiVien;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HuiVien createUpdatedEntity(EntityManager em) {
        HuiVien huiVien = new HuiVien().hoTen(UPDATED_HO_TEN).sdt(UPDATED_SDT);
        return huiVien;
    }

    @BeforeEach
    public void initTest() {
        huiVien = createEntity(em);
    }

    @Test
    @Transactional
    void createHuiVien() throws Exception {
        int databaseSizeBeforeCreate = huiVienRepository.findAll().size();
        // Create the HuiVien
        HuiVienDTO huiVienDTO = huiVienMapper.toDto(huiVien);
        restHuiVienMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(huiVienDTO)))
            .andExpect(status().isCreated());

        // Validate the HuiVien in the database
        List<HuiVien> huiVienList = huiVienRepository.findAll();
        assertThat(huiVienList).hasSize(databaseSizeBeforeCreate + 1);
        HuiVien testHuiVien = huiVienList.get(huiVienList.size() - 1);
        assertThat(testHuiVien.getHoTen()).isEqualTo(DEFAULT_HO_TEN);
        assertThat(testHuiVien.getSdt()).isEqualTo(DEFAULT_SDT);
    }

    @Test
    @Transactional
    void createHuiVienWithExistingId() throws Exception {
        // Create the HuiVien with an existing ID
        huiVien.setId(1L);
        HuiVienDTO huiVienDTO = huiVienMapper.toDto(huiVien);

        int databaseSizeBeforeCreate = huiVienRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHuiVienMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(huiVienDTO)))
            .andExpect(status().isBadRequest());

        // Validate the HuiVien in the database
        List<HuiVien> huiVienList = huiVienRepository.findAll();
        assertThat(huiVienList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllHuiViens() throws Exception {
        // Initialize the database
        huiVienRepository.saveAndFlush(huiVien);

        // Get all the huiVienList
        restHuiVienMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(huiVien.getId().intValue())))
            .andExpect(jsonPath("$.[*].hoTen").value(hasItem(DEFAULT_HO_TEN)))
            .andExpect(jsonPath("$.[*].sdt").value(hasItem(DEFAULT_SDT)));
    }

    @Test
    @Transactional
    void getHuiVien() throws Exception {
        // Initialize the database
        huiVienRepository.saveAndFlush(huiVien);

        // Get the huiVien
        restHuiVienMockMvc
            .perform(get(ENTITY_API_URL_ID, huiVien.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(huiVien.getId().intValue()))
            .andExpect(jsonPath("$.hoTen").value(DEFAULT_HO_TEN))
            .andExpect(jsonPath("$.sdt").value(DEFAULT_SDT));
    }

    @Test
    @Transactional
    void getNonExistingHuiVien() throws Exception {
        // Get the huiVien
        restHuiVienMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHuiVien() throws Exception {
        // Initialize the database
        huiVienRepository.saveAndFlush(huiVien);

        int databaseSizeBeforeUpdate = huiVienRepository.findAll().size();

        // Update the huiVien
        HuiVien updatedHuiVien = huiVienRepository.findById(huiVien.getId()).get();
        // Disconnect from session so that the updates on updatedHuiVien are not directly saved in db
        em.detach(updatedHuiVien);
        updatedHuiVien.hoTen(UPDATED_HO_TEN).sdt(UPDATED_SDT);
        HuiVienDTO huiVienDTO = huiVienMapper.toDto(updatedHuiVien);

        restHuiVienMockMvc
            .perform(
                put(ENTITY_API_URL_ID, huiVienDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(huiVienDTO))
            )
            .andExpect(status().isOk());

        // Validate the HuiVien in the database
        List<HuiVien> huiVienList = huiVienRepository.findAll();
        assertThat(huiVienList).hasSize(databaseSizeBeforeUpdate);
        HuiVien testHuiVien = huiVienList.get(huiVienList.size() - 1);
        assertThat(testHuiVien.getHoTen()).isEqualTo(UPDATED_HO_TEN);
        assertThat(testHuiVien.getSdt()).isEqualTo(UPDATED_SDT);
    }

    @Test
    @Transactional
    void putNonExistingHuiVien() throws Exception {
        int databaseSizeBeforeUpdate = huiVienRepository.findAll().size();
        huiVien.setId(count.incrementAndGet());

        // Create the HuiVien
        HuiVienDTO huiVienDTO = huiVienMapper.toDto(huiVien);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHuiVienMockMvc
            .perform(
                put(ENTITY_API_URL_ID, huiVienDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(huiVienDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HuiVien in the database
        List<HuiVien> huiVienList = huiVienRepository.findAll();
        assertThat(huiVienList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHuiVien() throws Exception {
        int databaseSizeBeforeUpdate = huiVienRepository.findAll().size();
        huiVien.setId(count.incrementAndGet());

        // Create the HuiVien
        HuiVienDTO huiVienDTO = huiVienMapper.toDto(huiVien);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHuiVienMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(huiVienDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HuiVien in the database
        List<HuiVien> huiVienList = huiVienRepository.findAll();
        assertThat(huiVienList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHuiVien() throws Exception {
        int databaseSizeBeforeUpdate = huiVienRepository.findAll().size();
        huiVien.setId(count.incrementAndGet());

        // Create the HuiVien
        HuiVienDTO huiVienDTO = huiVienMapper.toDto(huiVien);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHuiVienMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(huiVienDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HuiVien in the database
        List<HuiVien> huiVienList = huiVienRepository.findAll();
        assertThat(huiVienList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHuiVienWithPatch() throws Exception {
        // Initialize the database
        huiVienRepository.saveAndFlush(huiVien);

        int databaseSizeBeforeUpdate = huiVienRepository.findAll().size();

        // Update the huiVien using partial update
        HuiVien partialUpdatedHuiVien = new HuiVien();
        partialUpdatedHuiVien.setId(huiVien.getId());

        partialUpdatedHuiVien.sdt(UPDATED_SDT);

        restHuiVienMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHuiVien.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHuiVien))
            )
            .andExpect(status().isOk());

        // Validate the HuiVien in the database
        List<HuiVien> huiVienList = huiVienRepository.findAll();
        assertThat(huiVienList).hasSize(databaseSizeBeforeUpdate);
        HuiVien testHuiVien = huiVienList.get(huiVienList.size() - 1);
        assertThat(testHuiVien.getHoTen()).isEqualTo(DEFAULT_HO_TEN);
        assertThat(testHuiVien.getSdt()).isEqualTo(UPDATED_SDT);
    }

    @Test
    @Transactional
    void fullUpdateHuiVienWithPatch() throws Exception {
        // Initialize the database
        huiVienRepository.saveAndFlush(huiVien);

        int databaseSizeBeforeUpdate = huiVienRepository.findAll().size();

        // Update the huiVien using partial update
        HuiVien partialUpdatedHuiVien = new HuiVien();
        partialUpdatedHuiVien.setId(huiVien.getId());

        partialUpdatedHuiVien.hoTen(UPDATED_HO_TEN).sdt(UPDATED_SDT);

        restHuiVienMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHuiVien.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHuiVien))
            )
            .andExpect(status().isOk());

        // Validate the HuiVien in the database
        List<HuiVien> huiVienList = huiVienRepository.findAll();
        assertThat(huiVienList).hasSize(databaseSizeBeforeUpdate);
        HuiVien testHuiVien = huiVienList.get(huiVienList.size() - 1);
        assertThat(testHuiVien.getHoTen()).isEqualTo(UPDATED_HO_TEN);
        assertThat(testHuiVien.getSdt()).isEqualTo(UPDATED_SDT);
    }

    @Test
    @Transactional
    void patchNonExistingHuiVien() throws Exception {
        int databaseSizeBeforeUpdate = huiVienRepository.findAll().size();
        huiVien.setId(count.incrementAndGet());

        // Create the HuiVien
        HuiVienDTO huiVienDTO = huiVienMapper.toDto(huiVien);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHuiVienMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, huiVienDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(huiVienDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HuiVien in the database
        List<HuiVien> huiVienList = huiVienRepository.findAll();
        assertThat(huiVienList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHuiVien() throws Exception {
        int databaseSizeBeforeUpdate = huiVienRepository.findAll().size();
        huiVien.setId(count.incrementAndGet());

        // Create the HuiVien
        HuiVienDTO huiVienDTO = huiVienMapper.toDto(huiVien);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHuiVienMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(huiVienDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HuiVien in the database
        List<HuiVien> huiVienList = huiVienRepository.findAll();
        assertThat(huiVienList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHuiVien() throws Exception {
        int databaseSizeBeforeUpdate = huiVienRepository.findAll().size();
        huiVien.setId(count.incrementAndGet());

        // Create the HuiVien
        HuiVienDTO huiVienDTO = huiVienMapper.toDto(huiVien);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHuiVienMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(huiVienDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HuiVien in the database
        List<HuiVien> huiVienList = huiVienRepository.findAll();
        assertThat(huiVienList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHuiVien() throws Exception {
        // Initialize the database
        huiVienRepository.saveAndFlush(huiVien);

        int databaseSizeBeforeDelete = huiVienRepository.findAll().size();

        // Delete the huiVien
        restHuiVienMockMvc
            .perform(delete(ENTITY_API_URL_ID, huiVien.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<HuiVien> huiVienList = huiVienRepository.findAll();
        assertThat(huiVienList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
