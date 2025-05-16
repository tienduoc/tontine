package com.tontine.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tontine.app.domain.enumeration.LoaiHui;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A Hui entity representing a Tontine group.
 */
@Entity
@Table(name = "hui")
public class Hui implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "ten_hui")
    private String tenHui;

    @Column(name = "ngay_tao")
    private LocalDate ngayTao;

    @Enumerated(EnumType.STRING)
    @Column(name = "loai_hui")
    private LoaiHui loaiHui;

    @Column(name = "day_hui")
    private Long dayHui;

    @Column(name = "tham_keu")
    private Long thamKeu;

    @Column(name = "so_phan")
    private Integer soPhan;

    @OneToMany(
        cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
        orphanRemoval = true,
        mappedBy = "hui",
        fetch = FetchType.EAGER
    )
    @JsonIgnoreProperties(value = {"hui"}, allowSetters = true)
    private Set<ChiTietHui> chiTietHuis = new HashSet<>();

    /**
     * Default constructor
     */
    public Hui() {
        // Default constructor required by JPA
    }

    /**
     * Constructor with required fields
     */
    public Hui(String tenHui, LocalDate ngayTao, LoaiHui loaiHui, Long dayHui, Integer soPhan) {
        this.tenHui = tenHui;
        this.ngayTao = ngayTao;
        this.loaiHui = loaiHui;
        this.dayHui = dayHui;
        this.soPhan = soPhan;
    }

    // Getters and setters with fluent API pattern

    public Long getId() {
        return id;
    }

    public Hui id(Long id) {
        this.id = id;
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenHui() {
        return tenHui;
    }

    public Hui tenHui(String tenHui) {
        this.tenHui = tenHui;
        return this;
    }

    public void setTenHui(String tenHui) {
        this.tenHui = tenHui;
    }

    public LocalDate getNgayTao() {
        return ngayTao;
    }

    public Hui ngayTao(LocalDate ngayTao) {
        this.ngayTao = ngayTao;
        return this;
    }

    public void setNgayTao(LocalDate ngayTao) {
        this.ngayTao = ngayTao;
    }

    public LoaiHui getLoaiHui() {
        return loaiHui;
    }

    public Hui loaiHui(LoaiHui loaiHui) {
        this.loaiHui = loaiHui;
        return this;
    }

    public void setLoaiHui(LoaiHui loaiHui) {
        this.loaiHui = loaiHui;
    }

    public Long getDayHui() {
        return dayHui;
    }

    public Hui dayHui(Long dayHui) {
        this.dayHui = dayHui;
        return this;
    }

    public void setDayHui(Long dayHui) {
        this.dayHui = dayHui;
    }

    public Long getThamKeu() {
        return thamKeu;
    }

    public Hui thamKeu(Long thamKeu) {
        this.thamKeu = thamKeu;
        return this;
    }

    public void setThamKeu(Long thamKeu) {
        this.thamKeu = thamKeu;
    }

    public Integer getSoPhan() {
        return soPhan;
    }

    public Hui soPhan(Integer soPhan) {
        this.soPhan = soPhan;
        return this;
    }

    public void setSoPhan(Integer soPhan) {
        this.soPhan = soPhan;
    }

    public Set<ChiTietHui> getChiTietHuis() {
        return chiTietHuis;
    }

    public void setChiTietHuis(Set<ChiTietHui> chiTietHuis) {
        Optional.ofNullable(this.chiTietHuis)
            .ifPresent(currentSet -> currentSet.forEach(item -> item.setHui(null)));

        Optional.ofNullable(chiTietHuis)
            .ifPresent(newSet -> newSet.forEach(item -> item.setHui(this)));

        this.chiTietHuis = chiTietHuis;
    }

    public Hui chiTietHuis(Set<ChiTietHui> chiTietHuis) {
        this.setChiTietHuis(chiTietHuis);
        return this;
    }

    public Hui addChiTietHui(ChiTietHui chiTietHui) {
        this.chiTietHuis.add(chiTietHui);
        chiTietHui.setHui(this);
        return this;
    }

    public Hui removeChiTietHui(ChiTietHui chiTietHui) {
        this.chiTietHuis.remove(chiTietHui);
        chiTietHui.setHui(null);
        return this;
    }

    /**
     * Gets ChiTietHui entries for a specific cycle number
     *
     * @param ky the cycle number
     * @return set of ChiTietHui objects for the cycle
     */
    public Set<ChiTietHui> getChiTietHuisByKy(Integer ky) {
        return this.chiTietHuis.stream()
            .filter(chiTietHui -> Objects.equals(chiTietHui.getKy(), ky))
            .collect(Collectors.toSet());
    }

    /**
     * Find the maximum cycle number in this Hui
     *
     * @return the maximum cycle number or null if no cycles exist
     */
    public Integer getMaxKy() {
        return this.chiTietHuis.stream()
            .map(ChiTietHui::getKy)
            .filter(Objects::nonNull)
            .max(Integer::compareTo)
            .orElse(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Hui)) return false;
        Hui hui = (Hui) o;
        return id != null && id.equals(hui.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass().hashCode());
    }

    @Override
    public String toString() {
        return "Hui{" +
            "id=" + id +
            ", tenHui='" + tenHui + '\'' +
            ", ngayTao=" + ngayTao +
            ", loaiHui=" + loaiHui +
            ", dayHui=" + dayHui +
            ", thamKeu=" + thamKeu +
            ", soPhan=" + soPhan +
            "}";
    }
}
