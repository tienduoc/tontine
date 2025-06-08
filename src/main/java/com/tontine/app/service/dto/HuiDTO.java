package com.tontine.app.service.dto;

import java.io.Serializable;
import java.util.Objects;

public class HuiDTO implements Serializable {
    private Long id;
    private String tenHui;
    private String loaiHui;
    private Long dayHui;
    private Integer kiHienTai;
    private Integer phanChoi;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTenHui() {
        return tenHui;
    }
    public void setTenHui(String tenHui) {
        this.tenHui = tenHui;
    }
    public String getLoaiHui() {
        return loaiHui;
    }
    public void setLoaiHui(String loaiHui) {
        this.loaiHui = loaiHui;
    }
    public Long getDayHui() {
        return dayHui;
    }
    public void setDayHui(Long dayHui) {
        this.dayHui = dayHui;
    }
    public Integer getKiHienTai() {
        return kiHienTai;
    }
    public void setKiHienTai(Integer kiHienTai) {
        this.kiHienTai = kiHienTai;
    }
    public Integer getPhanChoi() {
        return phanChoi;
    }
    public void setPhanChoi(Integer phanChoi) {
        this.phanChoi = phanChoi;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HuiDTO)) {
            return false;
        }
        HuiDTO huiDTO = (HuiDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, huiDTO.id);
    }
    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
    @Override
    public String toString() {
        return "HuiDTO{" + "id=" + getId() + ", tenHui='" + getTenHui() + "'" + ", loaiHui='" + getLoaiHui() + "'" + ", dayHui=" + getDayHui() + ", kiHienTai=" + getKiHienTai() + "}";
    }
}
