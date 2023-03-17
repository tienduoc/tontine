package com.tontine.app.domain;

import java.io.Serializable;
import javax.persistence.*;

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
