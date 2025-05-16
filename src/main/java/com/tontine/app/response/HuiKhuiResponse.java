package com.tontine.app.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class HuiKhuiResponse {
    private Long chiTietId;
    private Long huiId;
    private Long huiVienId;
    private String dayHui;
    private long soTien;
    private int soChan;
    private int soKyDong;
    private int soDay;
    private long soTienBoTham;
    private Boolean song;
    private Boolean chet;
    private long soTienDong;
    private long soTienHot;
    private long tongBill;

    public Long getHuiVienId() {
        return huiVienId;
    }

    public void setHuiVienId(Long huiVienId) {
        this.huiVienId = huiVienId;
    }

    public Long getChiTietId() {
        return chiTietId;
    }

    public void setChiTietId(Long chiTietId) {
        this.chiTietId = chiTietId;
    }

    public Long getHuiId() {
        return huiId;
    }

    public void setHuiId(Long huiId) {
        this.huiId = huiId;
    }

    public String getDayHui() {
        return dayHui;
    }

    public void setDayHui(String dayHui) {
        this.dayHui = dayHui;
    }

    public long getSoTien() {
        return soTien;
    }

    public void setSoTien(long soTien) {
        this.soTien = soTien;
    }

    public int getSoChan() {
        return soChan;
    }

    public void setSoChan(int soChan) {
        this.soChan = soChan;
    }

    public int getSoKyDong() {
        return soKyDong;
    }

    public void setSoKyDong(int soKyDong) {
        this.soKyDong = soKyDong;
    }

    public int getSoDay() {
        return soDay;
    }

    public void setSoDay(int soDay) {
        this.soDay = soDay;
    }

    public long getSoTienBoTham() {
        return soTienBoTham;
    }

    public void setSoTienBoTham(long soTienBoTham) {
        this.soTienBoTham = soTienBoTham;
    }

    public Boolean getSong() {
        return song;
    }

    public void setSong(Boolean song) {
        this.song = song;
    }

    public Boolean getChet() {
        return chet;
    }

    public void setChet(Boolean chet) {
        this.chet = chet;
    }

    public long getSoTienDong() {
        return soTienDong;
    }

    public void setSoTienDong(long soTienDong) {
        this.soTienDong = soTienDong;
    }

    public long getSoTienHot() {
        return soTienHot;
    }

    public void setSoTienHot(long soTienHot) {
        this.soTienHot = soTienHot;
    }

    public long getTongBill() {
        return tongBill;
    }

    public void setTongBill(long tongBill) {
        this.tongBill = tongBill;
    }
}
