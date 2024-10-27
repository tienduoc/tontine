package com.tontine.app.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HuiKeuNgayResponse {
    private String tenHuiVien;

    @JsonProperty("chiTiet")
    private List<ChiTietHuiKeuResponse> chiTiets;

    public String getTenHuiVien() {
        return tenHuiVien;
    }

    public void setTenHuiVien( String tenHuiVien ) {
        this.tenHuiVien = tenHuiVien;
    }

    public List<ChiTietHuiKeuResponse> getChiTiets() {
        return chiTiets;
    }

    public void setChiTiets( List<ChiTietHuiKeuResponse> chiTiets ) {
        this.chiTiets = chiTiets;
    }
}
