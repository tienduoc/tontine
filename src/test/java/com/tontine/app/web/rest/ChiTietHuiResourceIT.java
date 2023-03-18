package com.tontine.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tontine.app.IntegrationTest;
import com.tontine.app.domain.ChiTietHui;
import com.tontine.app.repository.ChiTietHuiRepository;
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
        ChiTietHui chiTietHui = new ChiTietHui();
        return chiTietHui;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChiTietHui createUpdatedEntity(EntityManager em) {
        ChiTietHui chiTietHui = new ChiTietHui();
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
            .andExpect(jsonPath("$.[*].id").value(hasItem(chiTietHui.getId().intValue())));
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
            .andExpect(jsonPath("$.id").value(chiTietHui.getId().intValue()));
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
