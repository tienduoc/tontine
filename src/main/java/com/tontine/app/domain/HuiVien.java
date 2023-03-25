package com.tontine.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * A HuiVien.
 */
@Entity
@Table(name = "hui_vien")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HuiVien implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "ho_ten")
    private String hoTen;

    @Column(name = "sdt")
    private String sdt;

    @OneToMany(mappedBy = "huiVien", fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = { "huiVien" }, allowSetters = true)
    private Set<ChiTietHui> chiTietHuis = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public HuiVien id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHoTen() {
        return this.hoTen;
    }

    public HuiVien hoTen(String hoTen) {
        this.setHoTen(hoTen);
        return this;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getSdt() {
        return this.sdt;
    }

    public HuiVien sdt(String sdt) {
        this.setSdt(sdt);
        return this;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public Set<ChiTietHui> getChiTietHuis() {
        return this.chiTietHuis;
    }

    public void setChiTietHuis(Set<ChiTietHui> chiTietHuis) {
        if (this.chiTietHuis != null) {
            this.chiTietHuis.forEach(i -> i.setHuiVien(null));
        }
        if (chiTietHuis != null) {
            chiTietHuis.forEach(i -> i.setHuiVien(this));
        }
        this.chiTietHuis = chiTietHuis;
    }

    public HuiVien chiTietHuis(Set<ChiTietHui> chiTietHuis) {
        this.setChiTietHuis(chiTietHuis);
        return this;
    }

    public HuiVien addChiTietHui(ChiTietHui chiTietHui) {
        this.chiTietHuis.add(chiTietHui);
        chiTietHui.setHuiVien(this);
        return this;
    }

    public HuiVien removeChiTietHui(ChiTietHui chiTietHui) {
        this.chiTietHuis.remove(chiTietHui);
        chiTietHui.setHuiVien(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HuiVien)) {
            return false;
        }
        return id != null && id.equals(((HuiVien) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HuiVien{" +
            "id=" + getId() +
            ", hoTen='" + getHoTen() + "'" +
            ", sdt='" + getSdt() + "'" +
            "}";
    }
}
