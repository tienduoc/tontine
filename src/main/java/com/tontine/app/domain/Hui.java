package com.tontine.app.domain;

import java.io.Serializable;
import javax.persistence.*;

/**
 * A Hui.
 */
@Entity
@Table(name = "hui")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Hui implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "ten_hui")
    private String tenHui;

    @Column(name = "loai_hui")
    private String loaiHui;

    @Column(name = "day_hui")
    private Long dayHui;

    @Column(name = "ki_hien_tai")
    private Integer kiHienTai;

    @Column(name = "phan_choi")
    private Integer phanChoi;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Hui id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenHui() {
        return this.tenHui;
    }

    public Hui tenHui(String tenHui) {
        this.setTenHui(tenHui);
        return this;
    }

    public void setTenHui(String tenHui) {
        this.tenHui = tenHui;
    }

    public String getLoaiHui() {
        return this.loaiHui;
    }

    public Hui loaiHui(String loaiHui) {
        this.setLoaiHui(loaiHui);
        return this;
    }

    public void setLoaiHui(String loaiHui) {
        this.loaiHui = loaiHui;
    }

    public Long getDayHui() {
        return this.dayHui;
    }

    public Hui dayHui(Long dayHui) {
        this.setDayHui(dayHui);
        return this;
    }

    public void setDayHui(Long dayHui) {
        this.dayHui = dayHui;
    }

    public Integer getKiHienTai() {
        return this.kiHienTai;
    }

    public Hui kiHienTai(Integer kiHienTai) {
        this.setKiHienTai(kiHienTai);
        return this;
    }

    public void setKiHienTai(Integer kiHienTai) {
        this.kiHienTai = kiHienTai;
    }

    public Integer getPhanChoi() {
        return this.phanChoi;
    }

    public Hui phanChoi(Integer phanChoi) {
        this.setPhanChoi(phanChoi);
        return this;
    }

    public void setPhanChoi(Integer phanChoi) {
        this.phanChoi = phanChoi;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Hui)) {
            return false;
        }
        return id != null && id.equals(((Hui) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Hui{" +
            "id=" + getId() +
            ", tenHui='" + getTenHui() + "'" +
            ", loaiHui='" + getLoaiHui() + "'" +
            ", dayHui=" + getDayHui() +
            ", kiHienTai=" + getKiHienTai() +
            ", phanChoi=" + getPhanChoi() +
            "}";
    }
}
