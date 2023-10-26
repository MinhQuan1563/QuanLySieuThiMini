/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package quanlysieuthimini.DTO;

import java.util.Date;


public class SanPhamDTO {
    private int MaSP, MaLoai, MaHang, MaDV, TrangThai;
    private String TenSP, HinhAnh;
    private int SoLuong, DungTich;
    private double DonGia;
    private Date NgaySanXuat, HanSuDung;

    public SanPhamDTO() {
    }

    public SanPhamDTO(int MaSP, int MaLoai, int MaHang, int MaDV, String TenSP, int SoLuong, int DungTich, double DonGia, Date NgaySanXuat, Date HanSuDung, String HinhAnh, int TrangThai) {
        this.MaSP = MaSP;
        this.MaLoai = MaLoai;
        this.MaHang = MaHang;
        this.MaDV = MaDV;
        this.TenSP = TenSP;
        this.SoLuong = SoLuong;
        this.DungTich = DungTich;
        this.DonGia = DonGia;
        this.NgaySanXuat = NgaySanXuat;
        this.HanSuDung = HanSuDung;
        this.HinhAnh = HinhAnh;
        this.TrangThai = TrangThai;
    }

    public int getMaSP() {
        return MaSP;
    }

    public void setMaSP(int MaSP) {
        this.MaSP = MaSP;
    }

    public int getMaLoai() {
        return MaLoai;
    }

    public void setMaLoai(int MaLoai) {
        this.MaLoai = MaLoai;
    }

    public int getMaHang() {
        return MaHang;
    }

    public void setMaHang(int MaHang) {
        this.MaHang = MaHang;
    }

    public int getMaDV() {
        return MaDV;
    }

    public void setMaDV(int MaDV) {
        this.MaDV = MaDV;
    }

    public String getTenSP() {
        return TenSP;
    }

    public void setTenSP(String TenSP) {
        this.TenSP = TenSP;
    }

    public int getSoLuong() {
        return SoLuong;
    }

    public void setSoLuong(int SoLuong) {
        this.SoLuong = SoLuong;
    }

    public int getDungTich() {
        return DungTich;
    }

    public void setDungTich(int DungTich) {
        this.DungTich = DungTich;
    }

    public double getDonGia() {
        return DonGia;
    }

    public void setDonGia(double DonGia) {
        this.DonGia = DonGia;
    }

    public Date getNgaySanXuat() {
        return NgaySanXuat;
    }

    public void setNgaySanXuat(Date NgaySanXuat) {
        this.NgaySanXuat = NgaySanXuat;
    }

    public Date getHanSuDung() {
        return HanSuDung;
    }

    public void setHanSuDung(Date HanSuDung) {
        this.HanSuDung = HanSuDung;
    }

    public String getHinhAnh() {
        return HinhAnh;
    }

    public void setHinhAnh(String HinhAnh) {
        this.HinhAnh = HinhAnh;
    }

    public int getTrangThai() {
        return TrangThai;
    }

    public void setTrangThai(int TrangThai) {
        this.TrangThai = TrangThai;
    }
}
