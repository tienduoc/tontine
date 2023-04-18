package com.tontine.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tontine.app.IntegrationTest;
import com.tontine.app.domain.ChiTietHui;
import com.tontine.app.repository.ChiTietHuiRepository;
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
 * Integration tests for the {@link ChiTietHuiResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ChiTietHuiResourceIT {

    private static final Long DEFAULT_THAM_KEU = 1L;
    private static final Long UPDATED_THAM_KEU = 2L;

    private static final LocalDate DEFAULT_NGAY_KHUI = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_NGAY_KHUI = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_KY = 1;
    private static final Integer UPDATED_KY = 2;

    private static final Long DEFAULT_TIEN_HOT = 1L;
    private static final Long UPDATED_TIEN_HOT = 2L;

    private static final String DEFAULT_NICK_NAME_HUI_VIEN = "AAAAAAAAAA";
    private static final String UPDATED_NICK_NAME_HUI_VIEN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/chi-tiet-huis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ChiTietHuiRepository chiTietHuiRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restChiTietHuiMockMvc;

    private ChiTietHui chiTietHui;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChiTietHui createEntity(EntityManager em) {
        ChiTietHui chiTietHui = new ChiTietHui()
            .thamKeu(DEFAULT_THAM_KEU)
            .ngayKhui(DEFAULT_NGAY_KHUI)
            .ky(DEFAULT_KY)
            .tienHot(DEFAULT_TIEN_HOT)
            .nickNameHuiVien(DEFAULT_NICK_NAME_HUI_VIEN);
        return chiTietHui;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChiTietHui createUpdatedEntity(EntityManager em) {
        ChiTietHui chiTietHui = new ChiTietHui()
            .thamKeu(UPDATED_THAM_KEU)
            .ngayKhui(UPDATED_NGAY_KHUI)
            .ky(UPDATED_KY)
            .tienHot(UPDATED_TIEN_HOT)
            .nickNameHuiVien(UPDATED_NICK_NAME_HUI_VIEN);
        return chiTietHui;
    }

    @BeforeEach
    public void initTest() {
        chiTietHui = createEntity(em);
    }

    @Test
    @Transactional
    void createChiTietHui() throws Exception {
        int databaseSizeBeforeCreate = chiTietHuiRepository.findAll().size();
        // Create the ChiTietHui
        restChiTietHuiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(chiTietHui)))
            .andExpect(status().isCreated());

        // Validate the ChiTietHui in the database
        List<ChiTietHui> chiTietHuiList = chiTietHuiRepository.findAll();
        assertThat(chiTietHuiList).hasSize(databaseSizeBeforeCreate + 1);
        ChiTietHui testChiTietHui = chiTietHuiList.get(chiTietHuiList.size() - 1);
        assertThat(testChiTietHui.getThamKeu()).isEqualTo(DEFAULT_THAM_KEU);
        assertThat(testChiTietHui.getNgayKhui()).isEqualTo(DEFAULT_NGAY_KHUI);
        assertThat(testChiTietHui.getKy()).isEqualTo(DEFAULT_KY);
        assertThat(testChiTietHui.getTienHot()).isEqualTo(DEFAULT_TIEN_HOT);
        assertThat(testChiTietHui.getNickNameHuiVien()).isEqualTo(DEFAULT_NICK_NAME_HUI_VIEN);
    }

    @Test
    @Transactional
    void createChiTietHuiWithExistingId() throws Exception {
        // Create the ChiTietHui with an existing ID
        chiTietHui.setId(1L);

        int databaseSizeBeforeCreate = chiTietHuiRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restChiTietHuiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(chiTietHui)))
            .andExpect(status().isBadRequest());

        // Validate the ChiTietHui in the database
        List<ChiTietHui> chiTietHuiList = chiTietHuiRepository.findAll();
        assertThat(chiTietHuiList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllChiTietHuis() throws Exception {
        // Initialize the database
        chiTietHuiRepository.saveAndFlush(chiTietHui);

        // Get all the chiTietHuiList
        restChiTietHuiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chiTietHui.getId().intValue())))
            .andExpect(jsonPath("$.[*].thamKeu").value(hasItem(DEFAULT_THAM_KEU.intValue())))
            .andExpect(jsonPath("$.[*].ngayKhui").value(hasItem(DEFAULT_NGAY_KHUI.toString())))
            .andExpect(jsonPath("$.[*].ky").value(hasItem(DEFAULT_KY)))
            .andExpect(jsonPath("$.[*].tienHot").value(hasItem(DEFAULT_TIEN_HOT.intValue())))
            .andExpect(jsonPath("$.[*].nickNameHuiVien").value(hasItem(DEFAULT_NICK_NAME_HUI_VIEN)));
    }

    @Test
    @Transactional
    void getChiTietHui() throws Exception {
        // Initialize the database
        chiTietHuiRepository.saveAndFlush(chiTietHui);

        // Get the chiTietHui
        restChiTietHuiMockMvc
            .perform(get(ENTITY_API_URL_ID, chiTietHui.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(chiTietHui.getId().intValue()))
            .andExpect(jsonPath("$.thamKeu").value(DEFAULT_THAM_KEU.intValue()))
            .andExpect(jsonPath("$.ngayKhui").value(DEFAULT_NGAY_KHUI.toString()))
            .andExpect(jsonPath("$.ky").value(DEFAULT_KY))
            .andExpect(jsonPath("$.tienHot").value(DEFAULT_TIEN_HOT.intValue()))
            .andExpect(jsonPath("$.nickNameHuiVien").value(DEFAULT_NICK_NAME_HUI_VIEN));
    }

    @Test
    @Transactional
    void getNonExistingChiTietHui() throws Exception {
        // Get the chiTietHui
        restChiTietHuiMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingChiTietHui() throws Exception {
        // Initialize the database
        chiTietHuiRepository.saveAndFlush(chiTietHui);

        int databaseSizeBeforeUpdate = chiTietHuiRepository.findAll().size();

        // Update the chiTietHui
        ChiTietHui updatedChiTietHui = chiTietHuiRepository.findById(chiTietHui.getId()).get();
        // Disconnect from session so that the updates on updatedChiTietHui are not directly saved in db
        em.detach(updatedChiTietHui);
        updatedChiTietHui
            .thamKeu(UPDATED_THAM_KEU)
            .ngayKhui(UPDATED_NGAY_KHUI)
            .ky(UPDATED_KY)
            .tienHot(UPDATED_TIEN_HOT)
            .nickNameHuiVien(UPDATED_NICK_NAME_HUI_VIEN);

        restChiTietHuiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedChiTietHui.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedChiTietHui))
            )
            .andExpect(status().isOk());

        // Validate the ChiTietHui in the database
        List<ChiTietHui> chiTietHuiList = chiTietHuiRepository.findAll();
        assertThat(chiTietHuiList).hasSize(databaseSizeBeforeUpdate);
        ChiTietHui testChiTietHui = chiTietHuiList.get(chiTietHuiList.size() - 1);
        assertThat(testChiTietHui.getThamKeu()).isEqualTo(UPDATED_THAM_KEU);
        assertThat(testChiTietHui.getNgayKhui()).isEqualTo(UPDATED_NGAY_KHUI);
        assertThat(testChiTietHui.getKy()).isEqualTo(UPDATED_KY);
        assertThat(testChiTietHui.getTienHot()).isEqualTo(UPDATED_TIEN_HOT);
        assertThat(testChiTietHui.getNickNameHuiVien()).isEqualTo(UPDATED_NICK_NAME_HUI_VIEN);
    }

    @Test
    @Transactional
    void putNonExistingChiTietHui() throws Exception {
        int databaseSizeBeforeUpdate = chiTietHuiRepository.findAll().size();
        chiTietHui.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChiTietHuiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, chiTietHui.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chiTietHui))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChiTietHui in the database
        List<ChiTietHui> chiTietHuiList = chiTietHuiRepository.findAll();
        assertThat(chiTietHuiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchChiTietHui() throws Exception {
        int databaseSizeBeforeUpdate = chiTietHuiRepository.findAll().size();
        chiTietHui.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChiTietHuiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chiTietHui))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChiTietHui in the database
        List<ChiTietHui> chiTietHuiList = chiTietHuiRepository.findAll();
        assertThat(chiTietHuiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamChiTietHui() throws Exception {
        int databaseSizeBeforeUpdate = chiTietHuiRepository.findAll().size();
        chiTietHui.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChiTietHuiMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(chiTietHui)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ChiTietHui in the database
        List<ChiTietHui> chiTietHuiList = chiTietHuiRepository.findAll();
        assertThat(chiTietHuiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateChiTietHuiWithPatch() throws Exception {
        // Initialize the database
        chiTietHuiRepository.saveAndFlush(chiTietHui);

        int databaseSizeBeforeUpdate = chiTietHuiRepository.findAll().size();

        // Update the chiTietHui using partial update
        ChiTietHui partialUpdatedChiTietHui = new ChiTietHui();
        partialUpdatedChiTietHui.setId(chiTietHui.getId());

        partialUpdatedChiTietHui.ngayKhui(UPDATED_NGAY_KHUI).ky(UPDATED_KY);

        restChiTietHuiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChiTietHui.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedChiTietHui))
            )
            .andExpect(status().isOk());

        // Validate the ChiTietHui in the database
        List<ChiTietHui> chiTietHuiList = chiTietHuiRepository.findAll();
        assertThat(chiTietHuiList).hasSize(databaseSizeBeforeUpdate);
        ChiTietHui testChiTietHui = chiTietHuiList.get(chiTietHuiList.size() - 1);
        assertThat(testChiTietHui.getThamKeu()).isEqualTo(DEFAULT_THAM_KEU);
        assertThat(testChiTietHui.getNgayKhui()).isEqualTo(UPDATED_NGAY_KHUI);
        assertThat(testChiTietHui.getKy()).isEqualTo(UPDATED_KY);
        assertThat(testChiTietHui.getTienHot()).isEqualTo(DEFAULT_TIEN_HOT);
        assertThat(testChiTietHui.getNickNameHuiVien()).isEqualTo(DEFAULT_NICK_NAME_HUI_VIEN);
    }

    @Test
    @Transactional
    void fullUpdateChiTietHuiWithPatch() throws Exception {
        // Initialize the database
        chiTietHuiRepository.saveAndFlush(chiTietHui);

        int databaseSizeBeforeUpdate = chiTietHuiRepository.findAll().size();

        // Update the chiTietHui using partial update
        ChiTietHui partialUpdatedChiTietHui = new ChiTietHui();
        partialUpdatedChiTietHui.setId(chiTietHui.getId());

        partialUpdatedChiTietHui
            .thamKeu(UPDATED_THAM_KEU)
            .ngayKhui(UPDATED_NGAY_KHUI)
            .ky(UPDATED_KY)
            .tienHot(UPDATED_TIEN_HOT)
            .nickNameHuiVien(UPDATED_NICK_NAME_HUI_VIEN);

        restChiTietHuiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChiTietHui.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedChiTietHui))
            )
            .andExpect(status().isOk());

        // Validate the ChiTietHui in the database
        List<ChiTietHui> chiTietHuiList = chiTietHuiRepository.findAll();
        assertThat(chiTietHuiList).hasSize(databaseSizeBeforeUpdate);
        ChiTietHui testChiTietHui = chiTietHuiList.get(chiTietHuiList.size() - 1);
        assertThat(testChiTietHui.getThamKeu()).isEqualTo(UPDATED_THAM_KEU);
        assertThat(testChiTietHui.getNgayKhui()).isEqualTo(UPDATED_NGAY_KHUI);
        assertThat(testChiTietHui.getKy()).isEqualTo(UPDATED_KY);
        assertThat(testChiTietHui.getTienHot()).isEqualTo(UPDATED_TIEN_HOT);
        assertThat(testChiTietHui.getNickNameHuiVien()).isEqualTo(UPDATED_NICK_NAME_HUI_VIEN);
    }

    @Test
    @Transactional
    void patchNonExistingChiTietHui() throws Exception {
        int databaseSizeBeforeUpdate = chiTietHuiRepository.findAll().size();
        chiTietHui.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChiTietHuiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, chiTietHui.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(chiTietHui))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChiTietHui in the database
        List<ChiTietHui> chiTietHuiList = chiTietHuiRepository.findAll();
        assertThat(chiTietHuiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchChiTietHui() throws Exception {
        int databaseSizeBeforeUpdate = chiTietHuiRepository.findAll().size();
        chiTietHui.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChiTietHuiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(chiTietHui))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChiTietHui in the database
        List<ChiTietHui> chiTietHuiList = chiTietHuiRepository.findAll();
        assertThat(chiTietHuiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamChiTietHui() throws Exception {
        int databaseSizeBeforeUpdate = chiTietHuiRepository.findAll().size();
        chiTietHui.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChiTietHuiMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(chiTietHui))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ChiTietHui in the database
        List<ChiTietHui> chiTietHuiList = chiTietHuiRepository.findAll();
        assertThat(chiTietHuiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteChiTietHui() throws Exception {
        // Initialize the database
        chiTietHuiRepository.saveAndFlush(chiTietHui);

        int databaseSizeBeforeDelete = chiTietHuiRepository.findAll().size();

        // Delete the chiTietHui
        restChiTietHuiMockMvc
            .perform(delete(ENTITY_API_URL_ID, chiTietHui.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ChiTietHui> chiTietHuiList = chiTietHuiRepository.findAll();
        assertThat(chiTietHuiList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
