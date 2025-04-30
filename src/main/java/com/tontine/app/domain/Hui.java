package com.tontine.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tontine.app.domain.enumeration.LoaiHui;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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

    @Column(name = "ngay_tao")
    private LocalDate ngayTao;

    @Enumerated(EnumType.STRING)
    @Column(name = "loai_hui")
    private LoaiHui loaiHui;

    @Column(name = "day_hui")
    private Long dayHui;

    @Column(name = "tham_keu")
    private Long thamKeu;

    @Column(name = "so_phan")
    private Integer soPhan;

    @OneToMany(cascade = { CascadeType.PERSIST, CascadeType.REMOVE }, orphanRemoval = true, mappedBy = "hui", fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "hui" }, allowSetters = true)
    private Set<ChiTietHui> chiTietHuis = new HashSet<>();

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

    public LocalDate getNgayTao() {
        return this.ngayTao;
    }

    public Hui ngayTao(LocalDate ngayTao) {
        this.setNgayTao(ngayTao);
        return this;
    }

    public void setNgayTao(LocalDate ngayTao) {
        this.ngayTao = ngayTao;
    }

    public LoaiHui getLoaiHui() {
        return this.loaiHui;
    }

    public Hui loaiHui(LoaiHui loaiHui) {
        this.setLoaiHui(loaiHui);
        return this;
    }

    public void setLoaiHui(LoaiHui loaiHui) {
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

    public Long getThamKeu() {
        return this.thamKeu;
    }

    public Hui thamKeu(Long thamKeu) {
        this.setThamKeu(thamKeu);
        return this;
    }

    public void setThamKeu(Long thamKeu) {
        this.thamKeu = thamKeu;
    }

    public Integer getSoPhan() {
        return this.soPhan;
    }

    public Hui soPhan(Integer soPhan) {
        this.setSoPhan(soPhan);
        return this;
    }

    public void setSoPhan(Integer soPhan) {
        this.soPhan = soPhan;
    }

    public Set<ChiTietHui> getChiTietHuis() {
        return this.chiTietHuis;
    }

    public void setChiTietHuis(Set<ChiTietHui> chiTietHuis) {
        if (this.chiTietHuis != null) {
            this.chiTietHuis.forEach(i -> i.setHui(null));
        }
        if (chiTietHuis != null) {
            chiTietHuis.forEach(i -> i.setHui(this));
        }
        this.chiTietHuis = chiTietHuis;
    }

    public Hui chiTietHuis(Set<ChiTietHui> chiTietHuis) {
        this.setChiTietHuis(chiTietHuis);
        return this;
    }

    public Hui addChiTietHui(ChiTietHui chiTietHui) {
        this.chiTietHuis.add(chiTietHui);
        chiTietHui.setHui(this);
        return this;
    }

    public Hui removeChiTietHui(ChiTietHui chiTietHui) {
        this.chiTietHuis.remove(chiTietHui);
        chiTietHui.setHui(null);
        return this;
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
            ", ngayTao='" + getNgayTao() + "'" +
            ", loaiHui='" + getLoaiHui() + "'" +
            ", dayHui=" + getDayHui() +
            ", thamKeu=" + getThamKeu() +
            ", soPhan=" + getSoPhan() +
            "}";
    }
}
