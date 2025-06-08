package com.tontine.app.service.dto;

import java.io.Serializable;
import java.util.Objects;

public class HuiVienDTO implements Serializable {
    private Long id;
    private String hoTen;
    private String sdt;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getHoTen() {
        return hoTen;
    }
    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }
    public String getSdt() {
        return sdt;
    }
    public void setSdt(String sdt) {
        this.sdt = sdt;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HuiVienDTO)) {
            return false;
        }
        HuiVienDTO huiVienDTO = (HuiVienDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, huiVienDTO.id);
    }
    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
    @Override
    public String toString() {
        return "HuiVienDTO{" + "id=" + getId() + ", hoTen='" + getHoTen() + "'" + ", sdt='" + getSdt() + "'" + "}";
    }
}
