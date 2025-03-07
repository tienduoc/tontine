package com.tontine.app.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PhieuDongHuiResponse {

    private String huiVien;

    private int soDay;

    private int soChan;

    private int chet;

    private int song;

    private long huiSong;

    private long huiChet;

    private long canBangAmDuong;

    private long loiNhuan;

    private List<DayHui> dayHuis;

    private long tongSoTienBoTham;

    private long tongSoTienDong;

    public String getHuiVien() {
        return huiVien;
    }

    public void setHuiVien(String huiVien) {
        this.huiVien = huiVien;
    }

    public int getSoDay() {
        return soDay;
    }

    public void setSoDay(int soDay) {
        this.soDay = soDay;
    }

    public int getSoChan() {
        return soChan;
    }

    public void setSoChan(int soChan) {
        this.soChan = soChan;
    }

    public int getChet() {
        return chet;
    }

    public void setChet(int chet) {
        this.chet = chet;
    }

    public int getSong() {
        return song;
    }

    public void setSong(int song) {
        this.song = song;
    }

    public long getHuiSong() {
        return huiSong;
    }

    public void setHuiSong(long huiSong) {
        this.huiSong = huiSong;
    }

    public long getHuiChet() {
        return huiChet;
    }

    public void setHuiChet(long huiChet) {
        this.huiChet = huiChet;
    }

    public long getCanBangAmDuong() {
        return canBangAmDuong;
    }

    public void setCanBangAmDuong(long canBangAmDuong) {
        this.canBangAmDuong = canBangAmDuong;
    }

    public long getLoiNhuan() {
        return loiNhuan;
    }

    public void setLoiNhuan(long loiNhuan) {
        this.loiNhuan = loiNhuan;
    }

    public List<DayHui> getDayHuis() {
        return dayHuis;
    }

    public void setDayHuis(List<DayHui> dayHuis) {
        this.dayHuis = dayHuis;
    }

    public long getTongSoTienBoTham() {
        return tongSoTienBoTham;
    }

    public void setTongSoTienBoTham(long tongSoTienBoTham) {
        this.tongSoTienBoTham = tongSoTienBoTham;
    }

    public long getTongSoTienDong() {
        return tongSoTienDong;
    }

    public void setTongSoTienDong(long tongSoTienDong) {
        this.tongSoTienDong = tongSoTienDong;
    }
}
