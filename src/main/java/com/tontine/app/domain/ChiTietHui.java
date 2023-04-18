package com.tontine.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;

/**
 * A ChiTietHui.
 */
@Entity
@Table(name = "chi_tiet_hui")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ChiTietHui implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "tham_keu")
    private Long thamKeu;

    @Column(name = "ngay_khui")
    private LocalDate ngayKhui;

    @Column(name = "ky")
    private Integer ky;

    @Column(name = "tien_hot")
    private Long tienHot;

    @Column(name = "nick_name_hui_vien")
    private String nickNameHuiVien;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JsonIgnoreProperties(value = { "chiTietHuis" }, allowSetters = true)
    private Hui hui;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JsonIgnoreProperties(value = { "chiTietHuis" }, allowSetters = true)
    private HuiVien huiVien;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ChiTietHui id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getThamKeu() {
        return this.thamKeu;
    }

    public ChiTietHui thamKeu(Long thamKeu) {
        this.setThamKeu(thamKeu);
        return this;
    }

    public void setThamKeu(Long thamKeu) {
        this.thamKeu = thamKeu;
    }

    public LocalDate getNgayKhui() {
        return this.ngayKhui;
    }

    public ChiTietHui ngayKhui(LocalDate ngayKhui) {
        this.setNgayKhui(ngayKhui);
        return this;
    }

    public void setNgayKhui(LocalDate ngayKhui) {
        this.ngayKhui = ngayKhui;
    }

    public Integer getKy() {
        return this.ky;
    }

    public ChiTietHui ky(Integer ky) {
        this.setKy(ky);
        return this;
    }

    public void setKy(Integer ky) {
        this.ky = ky;
    }

    public Long getTienHot() {
        return this.tienHot;
    }

    public ChiTietHui tienHot(Long tienHot) {
        this.setTienHot(tienHot);
        return this;
    }

    public void setTienHot(Long tienHot) {
        this.tienHot = tienHot;
    }

    public String getNickNameHuiVien() {
        return this.nickNameHuiVien;
    }

    public ChiTietHui nickNameHuiVien(String nickNameHuiVien) {
        this.setNickNameHuiVien(nickNameHuiVien);
        return this;
    }

    public void setNickNameHuiVien(String nickNameHuiVien) {
        this.nickNameHuiVien = nickNameHuiVien;
    }

    public Hui getHui() {
        return this.hui;
    }

    public void setHui(Hui hui) {
        this.hui = hui;
    }

    public ChiTietHui hui(Hui hui) {
        this.setHui(hui);
        return this;
    }

    public HuiVien getHuiVien() {
        return this.huiVien;
    }

    public void setHuiVien(HuiVien huiVien) {
        this.huiVien = huiVien;
    }

    public ChiTietHui huiVien(HuiVien huiVien) {
        this.setHuiVien(huiVien);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChiTietHui)) {
            return false;
        }
        return id != null && id.equals(((ChiTietHui) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ChiTietHui{" +
            "id=" + getId() +
            ", thamKeu=" + getThamKeu() +
            ", ngayKhui='" + getNgayKhui() + "'" +
            ", ky=" + getKy() +
            ", tienHot=" + getTienHot() +
            ", nickNameHuiVien='" + getNickNameHuiVien() + "'" +
            "}";
    }
}
