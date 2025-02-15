package com.tontine.app.response;

public class ChiTietHuiKeuResponse {
    private String tenHui;

    private Long huiId;

    private long huiHot;

    private long huiSong;

    private long huiChet;

    private long conLai;

    public String getTenHui() {
        return tenHui;
    }
 
    public void setTenHui( String tenHui ) {
        this.tenHui = tenHui;
    }

    public Long getHuiId() {
        return huiId;
    }

    public void setHuiId( Long huiId ) {
        this.huiId = huiId;
    }

    public long getHuiHot() {
        return huiHot;
    }

    public void setHuiHot( long huiHot ) {
        this.huiHot = huiHot;
    }

    public long getHuiSong() {
        return huiSong;
    }

    public void setHuiSong( long huiSong ) {
        this.huiSong = huiSong;
    }

    public long getHuiChet() {
        return huiChet;
    }

    public void setHuiChet( long huiChet ) {
        this.huiChet = huiChet;
    }

    public long getConLai() {
        return conLai;
    }

    public void setConLai( long conLai ) {
        this.conLai = conLai;
    }
}
