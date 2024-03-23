package com.tontine.app.domain;

import java.io.Serializable;

public class ThongKe implements Serializable {

    private static final long serialVersionUID = 1L;

    private int soHuiSong;

    private int soHuiChet;

    public int getSoHuiSong() {
        return soHuiSong;
    }

    public void setSoHuiSong(int soHuiSong) {
        this.soHuiSong = soHuiSong;
    }

    public int getSoHuiChet() {
        return soHuiChet;
    }

    public void setSoHuiChet(int soHuiChet) {
        this.soHuiChet = soHuiChet;
    }
}
