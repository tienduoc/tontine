package com.tontine.app.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DayHuiResponse {
    private Long huiId;

    private String tenDayHui;

    private long soTien;

    private LocalDate ngayMo;

    private int kyHui;

    private int soKyDong;

    private LocalDate ngayHot;

    private long soTienBoTham;

    private Integer chet;

    private Integer song;

    private long soTienDong;

    private Long soTienHot;

    private Long tongSoTienPhaiDong;

    public Long getHuiId() {
        return huiId;
    }

    public void setHuiId(Long huiId) {
        this.huiId = huiId;
    }

    public String getTenDayHui() {
        return tenDayHui;
    }

    public void setTenDayHui(String tenDayHui) {
        this.tenDayHui = tenDayHui;
    }

    public long getSoTien() {
        return soTien;
    }

    public void setSoTien(long soTien) {
        this.soTien = soTien;
    }

    public LocalDate getNgayMo() {
        return ngayMo;
    }

    public void setNgayMo(LocalDate ngayMo) {
        this.ngayMo = ngayMo;
    }

    public int getKyHui() {
        return kyHui;
    }

    public void setKyHui(int kyHui) {
        this.kyHui = kyHui;
    }

    public int getSoKyDong() {
        return soKyDong;
    }

    public void setSoKyDong(int soKyDong) {
        this.soKyDong = soKyDong;
    }

    public LocalDate getNgayHot() {
        return ngayHot;
    }

    public void setNgayHot(LocalDate ngayHot) {
        this.ngayHot = ngayHot;
    }

    public long getSoTienBoTham() {
        return soTienBoTham;
    }

    public void setSoTienBoTham(long soTienBoTham) {
        this.soTienBoTham = soTienBoTham;
    }

    public Integer getChet() {
        return chet;
    }

    public void setChet(Integer chet) {
        this.chet = chet;
    }

    public Integer getSong() {
        return song;
    }

    public void setSong(Integer song) {
        this.song = song;
    }

    public long getSoTienDong() {
        return soTienDong;
    }

    public void setSoTienDong(long soTienDong) {
        this.soTienDong = soTienDong;
    }

    public Long getSoTienHot() {
        return soTienHot;
    }

    public void setSoTienHot(Long soTienHot) {
        this.soTienHot = soTienHot;
    }

    public Long getTongSoTienPhaiDong() {
        return tongSoTienPhaiDong;
    }

    public void setTongSoTienPhaiDong(Long tongSoTienPhaiDong) {
        this.tongSoTienPhaiDong = tongSoTienPhaiDong;
    }
}
