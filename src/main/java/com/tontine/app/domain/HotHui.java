package com.tontine.app.domain;

import com.tontine.app.domain.enumeration.LoaiHui;
import java.io.Serializable;
import java.time.LocalDate;

public class HotHui implements Serializable {

    private String tenHui;
    private LocalDate ngayMo;
    private LocalDate ngayHot;
    private Long dayHui;
    private Integer soPhan;
    private LoaiHui khui;
    private String huiVien;
    private Integer soKy;
    private Long thamKeu;
    private Long daHot;
    private Long chuaHot;
    private Long tienHui;
    private Long truThao;
    private Long conLai;

    public String getTenHui() {
        return tenHui;
    }

    public void setTenHui(String tenHui) {
        this.tenHui = tenHui;
    }

    public LocalDate getNgayMo() {
        return ngayMo;
    }

    public void setNgayMo(LocalDate ngayMo) {
        this.ngayMo = ngayMo;
    }

    public LocalDate getNgayHot() {
        return ngayHot;
    }

    public void setNgayHot(LocalDate ngayHot) {
        this.ngayHot = ngayHot;
    }

    public Long getDayHui() {
        return dayHui;
    }

    public void setDayHui(Long dayHui) {
        this.dayHui = dayHui;
    }

    public Integer getSoPhan() {
        return soPhan;
    }

    public void setSoPhan(Integer soPhan) {
        this.soPhan = soPhan;
    }

    public LoaiHui getKhui() {
        return khui;
    }

    public void setKhui(LoaiHui khui) {
        this.khui = khui;
    }

    public String getHuiVien() {
        return huiVien;
    }

    public void setHuiVien(String huiVien) {
        this.huiVien = huiVien;
    }

    public Integer getSoKy() {
        return soKy;
    }

    public void setSoKy(Integer soKy) {
        this.soKy = soKy;
    }

    public Long getThamKeu() {
        return thamKeu;
    }

    public void setThamKeu(Long thamKeu) {
        this.thamKeu = thamKeu;
    }

    public Long getDaHot() {
        return daHot;
    }

    public void setDaHot(Long daHot) {
        this.daHot = daHot;
    }

    public Long getChuaHot() {
        return chuaHot;
    }

    public void setChuaHot(Long chuaHot) {
        this.chuaHot = chuaHot;
    }

    public Long getTienHui() {
        return tienHui;
    }

    public void setTienHui(Long tienHui) {
        this.tienHui = tienHui;
    }

    public Long getTruThao() {
        return truThao;
    }

    public void setTruThao(Long truThao) {
        this.truThao = truThao;
    }

    public Long getConLai() {
        return conLai;
    }

    public void setConLai(Long conLai) {
        this.conLai = conLai;
    }
}
