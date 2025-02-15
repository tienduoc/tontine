package com.tontine.app.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HuiKeuNgayResponse {
    private String tenHuiVien;
    
    private long conLai;
    
    private long tongHuiHot;
    
    private long tongHuiSong;
    
    private long tongHuiChet;
    
    @JsonProperty( "chiTiet" )
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
    
    public long getConLai() {
        return conLai;
    }
    
    public void setConLai( long conLai ) {
        this.conLai = conLai;
    }
    
    public long getTongHuiHot() {
        return tongHuiHot;
    }
    
    public void setTongHuiHot( long tongHuiHot ) {
        this.tongHuiHot = tongHuiHot;
    }
    
    public long getTongHuiSong() {
        return tongHuiSong;
    }
    
    public void setTongHuiSong( long tongHuiSong ) {
        this.tongHuiSong = tongHuiSong;
    }
    
    public long getTongHuiChet() {
        return tongHuiChet;
    }
    
    public void setTongHuiChet( long tongHuiChet ) {
        this.tongHuiChet = tongHuiChet;
    }
}
