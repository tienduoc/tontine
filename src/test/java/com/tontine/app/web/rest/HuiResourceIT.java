package com.tontine.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tontine.app.IntegrationTest;
import com.tontine.app.domain.Hui;
import com.tontine.app.repository.HuiRepository;
import com.tontine.app.service.dto.HuiDTO;
import com.tontine.app.service.mapper.HuiMapper;
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
 * Integration tests for the {@link HuiResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HuiResourceIT {

    private static final String DEFAULT_TEN_HUI = "AAAAAAAAAA";
    private static final String UPDATED_TEN_HUI = "BBBBBBBBBB";

    private static final String DEFAULT_LOAI_HUI = "AAAAAAAAAA";
    private static final String UPDATED_LOAI_HUI = "BBBBBBBBBB";

    private static final Long DEFAULT_DAY_HUI = 1L;
    private static final Long UPDATED_DAY_HUI = 2L;

    private static final Integer DEFAULT_KI_HIEN_TAI = 1;
    private static final Integer UPDATED_KI_HIEN_TAI = 2;

    private static final Integer DEFAULT_PHAN_CHOI = 1;
    private static final Integer UPDATED_PHAN_CHOI = 2;

    private static final String ENTITY_API_URL = "/api/huis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HuiRepository huiRepository;

    @Autowired
    private HuiMapper huiMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHuiMockMvc;

    private Hui hui;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Hui createEntity(EntityManager em) {
        Hui hui = new Hui()
            .tenHui(DEFAULT_TEN_HUI)
            .loaiHui(DEFAULT_LOAI_HUI)
            .dayHui(DEFAULT_DAY_HUI)
            .kiHienTai(DEFAULT_KI_HIEN_TAI)
            .phanChoi(DEFAULT_PHAN_CHOI);
        return hui;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Hui createUpdatedEntity(EntityManager em) {
        Hui hui = new Hui()
            .tenHui(UPDATED_TEN_HUI)
            .loaiHui(UPDATED_LOAI_HUI)
            .dayHui(UPDATED_DAY_HUI)
            .kiHienTai(UPDATED_KI_HIEN_TAI)
            .phanChoi(UPDATED_PHAN_CHOI);
        return hui;
    }

    @BeforeEach
    public void initTest() {
        hui = createEntity(em);
    }

    @Test
    @Transactional
    void createHui() throws Exception {
        int databaseSizeBeforeCreate = huiRepository.findAll().size();
        // Create the Hui
        HuiDTO huiDTO = huiMapper.toDto(hui);
        restHuiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(huiDTO)))
            .andExpect(status().isCreated());

        // Validate the Hui in the database
        List<Hui> huiList = huiRepository.findAll();
        assertThat(huiList).hasSize(databaseSizeBeforeCreate + 1);
        Hui testHui = huiList.get(huiList.size() - 1);
        assertThat(testHui.getTenHui()).isEqualTo(DEFAULT_TEN_HUI);
        assertThat(testHui.getLoaiHui()).isEqualTo(DEFAULT_LOAI_HUI);
        assertThat(testHui.getDayHui()).isEqualTo(DEFAULT_DAY_HUI);
        assertThat(testHui.getKiHienTai()).isEqualTo(DEFAULT_KI_HIEN_TAI);
        assertThat(testHui.getPhanChoi()).isEqualTo(DEFAULT_PHAN_CHOI);
    }

    @Test
    @Transactional
    void createHuiWithExistingId() throws Exception {
        // Create the Hui with an existing ID
        hui.setId(1L);
        HuiDTO huiDTO = huiMapper.toDto(hui);

        int databaseSizeBeforeCreate = huiRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHuiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(huiDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Hui in the database
        List<Hui> huiList = huiRepository.findAll();
        assertThat(huiList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllHuis() throws Exception {
        // Initialize the database
        huiRepository.saveAndFlush(hui);

        // Get all the huiList
        restHuiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hui.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenHui").value(hasItem(DEFAULT_TEN_HUI)))
            .andExpect(jsonPath("$.[*].loaiHui").value(hasItem(DEFAULT_LOAI_HUI)))
            .andExpect(jsonPath("$.[*].dayHui").value(hasItem(DEFAULT_DAY_HUI.intValue())))
            .andExpect(jsonPath("$.[*].kiHienTai").value(hasItem(DEFAULT_KI_HIEN_TAI)))
            .andExpect(jsonPath("$.[*].phanChoi").value(hasItem(DEFAULT_PHAN_CHOI)));
    }

    @Test
    @Transactional
    void getHui() throws Exception {
        // Initialize the database
        huiRepository.saveAndFlush(hui);

        // Get the hui
        restHuiMockMvc
            .perform(get(ENTITY_API_URL_ID, hui.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(hui.getId().intValue()))
            .andExpect(jsonPath("$.tenHui").value(DEFAULT_TEN_HUI))
            .andExpect(jsonPath("$.loaiHui").value(DEFAULT_LOAI_HUI))
            .andExpect(jsonPath("$.dayHui").value(DEFAULT_DAY_HUI.intValue()))
            .andExpect(jsonPath("$.kiHienTai").value(DEFAULT_KI_HIEN_TAI))
            .andExpect(jsonPath("$.phanChoi").value(DEFAULT_PHAN_CHOI));
    }

    @Test
    @Transactional
    void getNonExistingHui() throws Exception {
        // Get the hui
        restHuiMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHui() throws Exception {
        // Initialize the database
        huiRepository.saveAndFlush(hui);

        int databaseSizeBeforeUpdate = huiRepository.findAll().size();

        // Update the hui
        Hui updatedHui = huiRepository.findById(hui.getId()).get();
        // Disconnect from session so that the updates on updatedHui are not directly saved in db
        em.detach(updatedHui);
        updatedHui
            .tenHui(UPDATED_TEN_HUI)
            .loaiHui(UPDATED_LOAI_HUI)
            .dayHui(UPDATED_DAY_HUI)
            .kiHienTai(UPDATED_KI_HIEN_TAI)
            .phanChoi(UPDATED_PHAN_CHOI);
        HuiDTO huiDTO = huiMapper.toDto(updatedHui);

        restHuiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, huiDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(huiDTO))
            )
            .andExpect(status().isOk());

        // Validate the Hui in the database
        List<Hui> huiList = huiRepository.findAll();
        assertThat(huiList).hasSize(databaseSizeBeforeUpdate);
        Hui testHui = huiList.get(huiList.size() - 1);
        assertThat(testHui.getTenHui()).isEqualTo(UPDATED_TEN_HUI);
        assertThat(testHui.getLoaiHui()).isEqualTo(UPDATED_LOAI_HUI);
        assertThat(testHui.getDayHui()).isEqualTo(UPDATED_DAY_HUI);
        assertThat(testHui.getKiHienTai()).isEqualTo(UPDATED_KI_HIEN_TAI);
        assertThat(testHui.getPhanChoi()).isEqualTo(UPDATED_PHAN_CHOI);
    }

    @Test
    @Transactional
    void putNonExistingHui() throws Exception {
        int databaseSizeBeforeUpdate = huiRepository.findAll().size();
        hui.setId(count.incrementAndGet());

        // Create the Hui
        HuiDTO huiDTO = huiMapper.toDto(hui);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHuiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, huiDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(huiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Hui in the database
        List<Hui> huiList = huiRepository.findAll();
        assertThat(huiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHui() throws Exception {
        int databaseSizeBeforeUpdate = huiRepository.findAll().size();
        hui.setId(count.incrementAndGet());

        // Create the Hui
        HuiDTO huiDTO = huiMapper.toDto(hui);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHuiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(huiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Hui in the database
        List<Hui> huiList = huiRepository.findAll();
        assertThat(huiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHui() throws Exception {
        int databaseSizeBeforeUpdate = huiRepository.findAll().size();
        hui.setId(count.incrementAndGet());

        // Create the Hui
        HuiDTO huiDTO = huiMapper.toDto(hui);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHuiMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(huiDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Hui in the database
        List<Hui> huiList = huiRepository.findAll();
        assertThat(huiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHuiWithPatch() throws Exception {
        // Initialize the database
        huiRepository.saveAndFlush(hui);

        int databaseSizeBeforeUpdate = huiRepository.findAll().size();

        // Update the hui using partial update
        Hui partialUpdatedHui = new Hui();
        partialUpdatedHui.setId(hui.getId());

        partialUpdatedHui.loaiHui(UPDATED_LOAI_HUI).kiHienTai(UPDATED_KI_HIEN_TAI).phanChoi(UPDATED_PHAN_CHOI);

        restHuiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHui.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHui))
            )
            .andExpect(status().isOk());

        // Validate the Hui in the database
        List<Hui> huiList = huiRepository.findAll();
        assertThat(huiList).hasSize(databaseSizeBeforeUpdate);
        Hui testHui = huiList.get(huiList.size() - 1);
        assertThat(testHui.getTenHui()).isEqualTo(DEFAULT_TEN_HUI);
        assertThat(testHui.getLoaiHui()).isEqualTo(UPDATED_LOAI_HUI);
        assertThat(testHui.getDayHui()).isEqualTo(DEFAULT_DAY_HUI);
        assertThat(testHui.getKiHienTai()).isEqualTo(UPDATED_KI_HIEN_TAI);
        assertThat(testHui.getPhanChoi()).isEqualTo(UPDATED_PHAN_CHOI);
    }

    @Test
    @Transactional
    void fullUpdateHuiWithPatch() throws Exception {
        // Initialize the database
        huiRepository.saveAndFlush(hui);

        int databaseSizeBeforeUpdate = huiRepository.findAll().size();

        // Update the hui using partial update
        Hui partialUpdatedHui = new Hui();
        partialUpdatedHui.setId(hui.getId());

        partialUpdatedHui
            .tenHui(UPDATED_TEN_HUI)
            .loaiHui(UPDATED_LOAI_HUI)
            .dayHui(UPDATED_DAY_HUI)
            .kiHienTai(UPDATED_KI_HIEN_TAI)
            .phanChoi(UPDATED_PHAN_CHOI);

        restHuiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHui.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHui))
            )
            .andExpect(status().isOk());

        // Validate the Hui in the database
        List<Hui> huiList = huiRepository.findAll();
        assertThat(huiList).hasSize(databaseSizeBeforeUpdate);
        Hui testHui = huiList.get(huiList.size() - 1);
        assertThat(testHui.getTenHui()).isEqualTo(UPDATED_TEN_HUI);
        assertThat(testHui.getLoaiHui()).isEqualTo(UPDATED_LOAI_HUI);
        assertThat(testHui.getDayHui()).isEqualTo(UPDATED_DAY_HUI);
        assertThat(testHui.getKiHienTai()).isEqualTo(UPDATED_KI_HIEN_TAI);
        assertThat(testHui.getPhanChoi()).isEqualTo(UPDATED_PHAN_CHOI);
    }

    @Test
    @Transactional
    void patchNonExistingHui() throws Exception {
        int databaseSizeBeforeUpdate = huiRepository.findAll().size();
        hui.setId(count.incrementAndGet());

        // Create the Hui
        HuiDTO huiDTO = huiMapper.toDto(hui);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHuiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, huiDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(huiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Hui in the database
        List<Hui> huiList = huiRepository.findAll();
        assertThat(huiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHui() throws Exception {
        int databaseSizeBeforeUpdate = huiRepository.findAll().size();
        hui.setId(count.incrementAndGet());

        // Create the Hui
        HuiDTO huiDTO = huiMapper.toDto(hui);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHuiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(huiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Hui in the database
        List<Hui> huiList = huiRepository.findAll();
        assertThat(huiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHui() throws Exception {
        int databaseSizeBeforeUpdate = huiRepository.findAll().size();
        hui.setId(count.incrementAndGet());

        // Create the Hui
        HuiDTO huiDTO = huiMapper.toDto(hui);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHuiMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(huiDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Hui in the database
        List<Hui> huiList = huiRepository.findAll();
        assertThat(huiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHui() throws Exception {
        // Initialize the database
        huiRepository.saveAndFlush(hui);

        int databaseSizeBeforeDelete = huiRepository.findAll().size();

        // Delete the hui
        restHuiMockMvc.perform(delete(ENTITY_API_URL_ID, hui.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Hui> huiList = huiRepository.findAll();
        assertThat(huiList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
