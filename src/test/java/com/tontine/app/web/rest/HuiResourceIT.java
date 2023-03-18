package com.tontine.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tontine.app.IntegrationTest;
import com.tontine.app.domain.Hui;
import com.tontine.app.domain.enumeration.LoaiHui;
import com.tontine.app.repository.HuiRepository;
import java.time.LocalDate;
import java.time.ZoneId;
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

    private static final LocalDate DEFAULT_NGAY_TAO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_NGAY_TAO = LocalDate.now(ZoneId.systemDefault());

    private static final LoaiHui DEFAULT_LOAI_HUI = LoaiHui.NGAY;
    private static final LoaiHui UPDATED_LOAI_HUI = LoaiHui.TUAN;

    private static final Long DEFAULT_DAY_HUI = 1L;
    private static final Long UPDATED_DAY_HUI = 2L;

    private static final Long DEFAULT_THAM_KEU = 1L;
    private static final Long UPDATED_THAM_KEU = 2L;

    private static final Integer DEFAULT_SO_PHAN = 1;
    private static final Integer UPDATED_SO_PHAN = 2;

    private static final String ENTITY_API_URL = "/api/huis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HuiRepository huiRepository;

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
            .ngayTao(DEFAULT_NGAY_TAO)
            .loaiHui(DEFAULT_LOAI_HUI)
            .dayHui(DEFAULT_DAY_HUI)
            .thamKeu(DEFAULT_THAM_KEU)
            .soPhan(DEFAULT_SO_PHAN);
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
            .ngayTao(UPDATED_NGAY_TAO)
            .loaiHui(UPDATED_LOAI_HUI)
            .dayHui(UPDATED_DAY_HUI)
            .thamKeu(UPDATED_THAM_KEU)
            .soPhan(UPDATED_SO_PHAN);
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
        restHuiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hui)))
            .andExpect(status().isCreated());

        // Validate the Hui in the database
        List<Hui> huiList = huiRepository.findAll();
        assertThat(huiList).hasSize(databaseSizeBeforeCreate + 1);
        Hui testHui = huiList.get(huiList.size() - 1);
        assertThat(testHui.getTenHui()).isEqualTo(DEFAULT_TEN_HUI);
        assertThat(testHui.getNgayTao()).isEqualTo(DEFAULT_NGAY_TAO);
        assertThat(testHui.getLoaiHui()).isEqualTo(DEFAULT_LOAI_HUI);
        assertThat(testHui.getDayHui()).isEqualTo(DEFAULT_DAY_HUI);
        assertThat(testHui.getThamKeu()).isEqualTo(DEFAULT_THAM_KEU);
        assertThat(testHui.getSoPhan()).isEqualTo(DEFAULT_SO_PHAN);
    }

    @Test
    @Transactional
    void createHuiWithExistingId() throws Exception {
        // Create the Hui with an existing ID
        hui.setId(1L);

        int databaseSizeBeforeCreate = huiRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHuiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hui)))
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
            .andExpect(jsonPath("$.[*].ngayTao").value(hasItem(DEFAULT_NGAY_TAO.toString())))
            .andExpect(jsonPath("$.[*].loaiHui").value(hasItem(DEFAULT_LOAI_HUI.toString())))
            .andExpect(jsonPath("$.[*].dayHui").value(hasItem(DEFAULT_DAY_HUI.intValue())))
            .andExpect(jsonPath("$.[*].thamKeu").value(hasItem(DEFAULT_THAM_KEU.intValue())))
            .andExpect(jsonPath("$.[*].soPhan").value(hasItem(DEFAULT_SO_PHAN)));
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
            .andExpect(jsonPath("$.ngayTao").value(DEFAULT_NGAY_TAO.toString()))
            .andExpect(jsonPath("$.loaiHui").value(DEFAULT_LOAI_HUI.toString()))
            .andExpect(jsonPath("$.dayHui").value(DEFAULT_DAY_HUI.intValue()))
            .andExpect(jsonPath("$.thamKeu").value(DEFAULT_THAM_KEU.intValue()))
            .andExpect(jsonPath("$.soPhan").value(DEFAULT_SO_PHAN));
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
            .ngayTao(UPDATED_NGAY_TAO)
            .loaiHui(UPDATED_LOAI_HUI)
            .dayHui(UPDATED_DAY_HUI)
            .thamKeu(UPDATED_THAM_KEU)
            .soPhan(UPDATED_SO_PHAN);

        restHuiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedHui.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedHui))
            )
            .andExpect(status().isOk());

        // Validate the Hui in the database
        List<Hui> huiList = huiRepository.findAll();
        assertThat(huiList).hasSize(databaseSizeBeforeUpdate);
        Hui testHui = huiList.get(huiList.size() - 1);
        assertThat(testHui.getTenHui()).isEqualTo(UPDATED_TEN_HUI);
        assertThat(testHui.getNgayTao()).isEqualTo(UPDATED_NGAY_TAO);
        assertThat(testHui.getLoaiHui()).isEqualTo(UPDATED_LOAI_HUI);
        assertThat(testHui.getDayHui()).isEqualTo(UPDATED_DAY_HUI);
        assertThat(testHui.getThamKeu()).isEqualTo(UPDATED_THAM_KEU);
        assertThat(testHui.getSoPhan()).isEqualTo(UPDATED_SO_PHAN);
    }

    @Test
    @Transactional
    void putNonExistingHui() throws Exception {
        int databaseSizeBeforeUpdate = huiRepository.findAll().size();
        hui.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHuiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hui.getId()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hui))
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

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHuiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(hui))
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

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHuiMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hui)))
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

        partialUpdatedHui.ngayTao(UPDATED_NGAY_TAO).dayHui(UPDATED_DAY_HUI).thamKeu(UPDATED_THAM_KEU);

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
        assertThat(testHui.getNgayTao()).isEqualTo(UPDATED_NGAY_TAO);
        assertThat(testHui.getLoaiHui()).isEqualTo(DEFAULT_LOAI_HUI);
        assertThat(testHui.getDayHui()).isEqualTo(UPDATED_DAY_HUI);
        assertThat(testHui.getThamKeu()).isEqualTo(UPDATED_THAM_KEU);
        assertThat(testHui.getSoPhan()).isEqualTo(DEFAULT_SO_PHAN);
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
            .ngayTao(UPDATED_NGAY_TAO)
            .loaiHui(UPDATED_LOAI_HUI)
            .dayHui(UPDATED_DAY_HUI)
            .thamKeu(UPDATED_THAM_KEU)
            .soPhan(UPDATED_SO_PHAN);

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
        assertThat(testHui.getNgayTao()).isEqualTo(UPDATED_NGAY_TAO);
        assertThat(testHui.getLoaiHui()).isEqualTo(UPDATED_LOAI_HUI);
        assertThat(testHui.getDayHui()).isEqualTo(UPDATED_DAY_HUI);
        assertThat(testHui.getThamKeu()).isEqualTo(UPDATED_THAM_KEU);
        assertThat(testHui.getSoPhan()).isEqualTo(UPDATED_SO_PHAN);
    }

    @Test
    @Transactional
    void patchNonExistingHui() throws Exception {
        int databaseSizeBeforeUpdate = huiRepository.findAll().size();
        hui.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHuiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, hui.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(hui))
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

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHuiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(hui))
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

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHuiMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(hui)))
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
