package com.tontine.app.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PhieuDongHuiResponse {
    private String huiVien;

    private LocalDate ngay;

    private List<HuiKhuiResponse> huiKhuiResponseList;

    public String getHuiVien() {
        return huiVien;
    }

    public void setHuiVien(String huiVien) {
        this.huiVien = huiVien;
    }

    public LocalDate getNgay() {
        return ngay;
    }

    public void setNgay(LocalDate ngay) {
        this.ngay = ngay;
    }

    public List<HuiKhuiResponse> getHuiKhuiResponseList() {
        return huiKhuiResponseList;
    }

    public void setHuiKhuiResponseList(List<HuiKhuiResponse> huiKhuiResponseList) {
        this.huiKhuiResponseList = huiKhuiResponseList;
    }
}
