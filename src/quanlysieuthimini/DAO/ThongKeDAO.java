/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package quanlysieuthimini.DAO;

import quanlysieuthimini.DTO.ThongKe.ThongKeDoanhThuDTO;
import quanlysieuthimini.DTO.ThongKe.ThongKeKhachHangDTO;
import quanlysieuthimini.DTO.ThongKe.ThongKeNhaCungCapDTO;
import quanlysieuthimini.DTO.ThongKe.ThongKeTheoThangDTO;
import quanlysieuthimini.DTO.ThongKe.ThongKeTungNgayTrongThangDTO;
import java.beans.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThongKeDAO {

    public static ThongKeDAO getInstance() {
        return new ThongKeDAO();
    }

    public ArrayList<ThongKeDoanhThuDTO> getDoanhThuTheoTungNam(int year_start, int year_end) {
        ArrayList<ThongKeDoanhThuDTO> result = new ArrayList<>();
        try {
            Connection con = ConnectionDB.openConnection();
            String sqlSetStartYear = "SET @start_year = ?;";
            String sqlSetEndYear = "SET @end_year = ?;";
            String sqlSelect = """
                     WITH RECURSIVE years(year) AS ( 
                                                    SELECT @start_year 
                                                    UNION ALL 
                                                    SELECT year + 1 
                                                    FROM years 
                                                    WHERE year < @end_year 
                     				) 
                                                 SELECT years.year AS nam, 
                                                 COALESCE(SUM(chitietphieunhap.SoLuong * chitietphieunhap.DonGia), 0) AS chiphi, 
                                                 COALESCE(SUM(chitiethoadon.SoLuong * chitiethoadon.DonGia), 0) AS doanhthu 
                                                 FROM years 
                                                 LEFT JOIN phieunhap ON YEAR(phieunhap.NgayNhap) = years.year AND phieunhap.TrangThai = 2
                                                 LEFT JOIN chitietphieunhap ON phieunhap.MaPN = chitietphieunhap.MaPN 
                                                 LEFT JOIN hoadon ON YEAR(hoadon.NgayLap) = years.year 
                                                 LEFT JOIN chitiethoadon ON hoadon.MaHD = chitiethoadon.MaHD 
                                                 GROUP BY years.year 
                                                 ORDER BY years.year;""";
            PreparedStatement pstStartYear = con.prepareStatement(sqlSetStartYear);
            PreparedStatement pstEndYear = con.prepareStatement(sqlSetEndYear);
            PreparedStatement pstSelect = con.prepareStatement(sqlSelect);

            pstStartYear.setInt(1, year_start);
            pstEndYear.setInt(1, year_end);

            pstStartYear.execute();
            pstEndYear.execute();

            ResultSet rs = pstSelect.executeQuery();
            while (rs.next()) {
                int thoigian = rs.getInt("nam");
                Long chiphi = rs.getLong("chiphi");
                Long doanhthu = rs.getLong("doanhthu");
                ThongKeDoanhThuDTO x = new ThongKeDoanhThuDTO(thoigian, chiphi, doanhthu, doanhthu - chiphi);
                result.add(x);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static ArrayList<ThongKeKhachHangDTO> getThongKeKhachHang(String text, Date timeStart, Date timeEnd) {
        ArrayList<ThongKeKhachHangDTO> result = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeEnd.getTime());
        // Đặt giá trị cho giờ, phút, giây và mili giây của Calendar
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        try {
            Connection con = ConnectionDB.openConnection();
            String sql = """
                          WITH kh AS (
                         SELECT khachhang.MaKH, khachhang.TenKH , COUNT(hoadon.MaHD ) AS tongsophieu, SUM(hoadon.TongTien) AS tongsotien
                         FROM khachhang
                         JOIN hoadon ON khachhang.MaKH = hoadon.MaKH
                         WHERE hoadon.NgayLap BETWEEN ? AND ? 
                         GROUP BY khachhang.MaKH, khachhang.TenKH)
                         SELECT MaKH,TenKH,COALESCE(kh.tongsophieu, 0) AS soluong ,COALESCE(kh.tongsotien, 0) AS total 
                         FROM kh WHERE TenKH LIKE ? OR MaKH LIKE ?""";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setTimestamp(1, new Timestamp(timeStart.getTime()));
            pst.setTimestamp(2, new Timestamp(calendar.getTimeInMillis()));
            pst.setString(3, "%" + text + "%");
            pst.setString(4, "%" + text + "%");

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int MaKH = rs.getInt("MaKH");
                String tenkh = rs.getString("TenKH");
                int soluong = rs.getInt("soluong");
                long TongTien = rs.getInt("total");
                ThongKeKhachHangDTO x = new ThongKeKhachHangDTO(MaKH, tenkh, soluong, TongTien);
                result.add(x);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static ArrayList<ThongKeNhaCungCapDTO> getThongKeNCC(String text, Date timeStart, Date timeEnd) {
        ArrayList<ThongKeNhaCungCapDTO> result = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeEnd.getTime());
        // Đặt giá trị cho giờ, phút, giây và mili giây của Calendar
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        try {
            Connection con = ConnectionDB.openConnection();
            String sql = """
                          WITH ncc AS (
                         SELECT nhacungcap.MaNCC, nhacungcap.TenNCC , COUNT(phieunhap.MaPN ) AS tongsophieu, SUM(phieunhap.TongTien) AS tongsotien
                         FROM nhacungcap
                         JOIN phieunhap ON nhacungcap.MaNCC = phieunhap.MaNCC
                         WHERE phieunhap.NgayNhap BETWEEN ? AND ? 
                         GROUP BY nhacungcap.MaNCC, nhacungcap.TenNCC)
                         SELECT MaNCC,TenNCC,COALESCE(ncc.tongsophieu, 0) AS soluong ,COALESCE(ncc.tongsotien, 0) AS total 
                         FROM ncc WHERE TenNCC LIKE ? OR MaNCC LIKE ?""";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setTimestamp(1, new Timestamp(timeStart.getTime()));
            pst.setTimestamp(2, new Timestamp(calendar.getTimeInMillis()));
            pst.setString(3, "%" + text + "%");
            pst.setString(4, "%" + text + "%");

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int mancc = rs.getInt("MaNCC");
                String tenncc = rs.getString("TenNCC");
                int soluong = rs.getInt("soluong");
                long TongTien = rs.getInt("total");
                ThongKeNhaCungCapDTO x = new ThongKeNhaCungCapDTO(mancc, tenncc, soluong, TongTien);
                result.add(x);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<ThongKeTheoThangDTO> getThongKeTheoThang(int nam) {
        ArrayList<ThongKeTheoThangDTO> result = new ArrayList<>();
        try {
            Connection con = ConnectionDB.openConnection();
            
            String sql = "SELECT months.month AS thang, \n"
                    + " COALESCE(SUM(chitietphieunhap.SoLuong * chitietphieunhap.DonGia), 0) AS chiphi,\n" 
                    + " COALESCE(SUM(chitiethoadon.SoLuong * chitiethoadon.DonGia), 0) AS doanhthu\n"
                    + "FROM (\n"
                    + "       SELECT 1 AS month\n"
                    + "       UNION ALL SELECT 2\n"
                    + "       UNION ALL SELECT 3\n"
                    + "       UNION ALL SELECT 4\n"
                    + "       UNION ALL SELECT 5\n"
                    + "       UNION ALL SELECT 6\n"
                    + "       UNION ALL SELECT 7\n"
                    + "       UNION ALL SELECT 8\n"
                    + "       UNION ALL SELECT 9\n"
                    + "       UNION ALL SELECT 10\n"
                    + "       UNION ALL SELECT 11\n"
                    + "       UNION ALL SELECT 12\n"
                    + "     ) AS months\n"
                    + "LEFT JOIN phieunhap ON MONTH(phieunhap.NgayNhap) = months.month AND YEAR(phieunhap.NgayNhap) = ? AND phieunhap.TrangThai = 2\n" 
                    + "LEFT JOIN chitietphieunhap ON phieunhap.MaPN = chitietphieunhap.MaPN\n" 
                    + "LEFT JOIN hoadon ON MONTH(hoadon.NgayLap) = months.month AND YEAR(hoadon.NgayLap) = ? \n" 
                    + "LEFT JOIN chitiethoadon ON hoadon.MaHD = chitiethoadon.MaHD\n"
                    + "GROUP BY months.month\n"
                    + "ORDER BY months.month;";
            
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, nam);
            pst.setInt(2, nam);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int thang = rs.getInt("thang");
                int chiphi = rs.getInt("chiphi");
                int doanhthu = rs.getInt("doanhthu");
                int loinhuan = doanhthu - chiphi;
                ThongKeTheoThangDTO thongke = new ThongKeTheoThangDTO(thang, chiphi, doanhthu, loinhuan);
                result.add(thongke);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<ThongKeTungNgayTrongThangDTO> getThongKeTungNgayTrongThang(int thang, int nam) {
        ArrayList<ThongKeTungNgayTrongThangDTO> result = new ArrayList<>();
        try {
            String ngayString = nam + "-" + thang + "-" + "01";
            Connection con = ConnectionDB.openConnection();
            
             String sql = "SELECT \n"
                    + "  dates.date AS ngay, \n"
                    + " COALESCE(SUM(chitietphieunhap.SoLuong * chitietphieunhap.DonGia), 0) AS chiphi, \n" 
                    + " COALESCE(SUM(chitiethoadon.SoLuong * chitiethoadon.DonGia), 0) AS doanhthu\n" 
                    + "FROM (\n"
                    + "  SELECT DATE('" + ngayString + "') + INTERVAL c.number DAY AS date\n"
                    + "  FROM (\n"
                    + "    SELECT 0 AS number\n"
                    + "    UNION ALL SELECT 1\n"
                    + "    UNION ALL SELECT 2\n"
                    + "    UNION ALL SELECT 3\n"
                    + "    UNION ALL SELECT 4\n"
                    + "    UNION ALL SELECT 5\n"
                    + "    UNION ALL SELECT 6\n"
                    + "    UNION ALL SELECT 7\n"
                    + "    UNION ALL SELECT 8\n"
                    + "    UNION ALL SELECT 9\n"
                    + "    UNION ALL SELECT 10\n"
                    + "    UNION ALL SELECT 11\n"
                    + "    UNION ALL SELECT 12\n"
                    + "    UNION ALL SELECT 13\n"
                    + "    UNION ALL SELECT 14\n"
                    + "    UNION ALL SELECT 15\n"
                    + "    UNION ALL SELECT 16\n"
                    + "    UNION ALL SELECT 17\n"
                    + "    UNION ALL SELECT 18\n"
                    + "    UNION ALL SELECT 19\n"
                    + "    UNION ALL SELECT 20\n"
                    + "    UNION ALL SELECT 21\n"
                    + "    UNION ALL SELECT 22\n"
                    + "    UNION ALL SELECT 23\n"
                    + "    UNION ALL SELECT 24\n"
                    + "    UNION ALL SELECT 25\n"
                    + "    UNION ALL SELECT 26\n"
                    + "    UNION ALL SELECT 27\n"
                    + "    UNION ALL SELECT 28\n"
                    + "    UNION ALL SELECT 29\n"
                    + "    UNION ALL SELECT 30\n"
                    + "  ) AS c\n"
                    + "  WHERE DATE('" + ngayString + "') + INTERVAL c.number DAY <= LAST_DAY('" + ngayString + "')\n"
                    + ") AS dates\n"
                    + "LEFT JOIN phieunhap ON DATE(phieunhap.NgayNhap) = dates.date AND phieunhap.TrangThai = 2\n" 
                    + "LEFT JOIN chitietphieunhap ON phieunhap.MaPN = chitietphieunhap.MaPN\n" 
                    + "LEFT JOIN hoadon ON DATE(hoadon.NgayLap) = dates.date\n" 
                    + "LEFT JOIN chitiethoadon ON hoadon.MaHD = chitiethoadon.MaHD\n" 
                    + "GROUP BY dates.date\n"
                    + "ORDER BY dates.date;";
            
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Date ngay = rs.getDate("ngay");
                int chiphi = rs.getInt("chiphi");
                int doanhthu = rs.getInt("doanhthu");
                int loinhuan = doanhthu - chiphi;
                ThongKeTungNgayTrongThangDTO tn = new ThongKeTungNgayTrongThangDTO(ngay, chiphi, doanhthu, loinhuan);
                result.add(tn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<ThongKeTungNgayTrongThangDTO> getThongKe7NgayGanNhat() {
        ArrayList<ThongKeTungNgayTrongThangDTO> result = new ArrayList<>();
        try {
            Connection con = ConnectionDB.openConnection();
            //LEFT JOIN ctsanpham ON ctsanpham.MaHD = chitiethoadon.MaHD AND ctsanpham.maphienbansp = chitiethoadon.maphienbansp
            //LEFT JOIN chitietphieunhap ON ctsanpham.MaPN = chitietphieunhap.MaPN AND ctsanpham.maphienbansp = chitietphieunhap.maphienbansp
            String sql = """
                         WITH RECURSIVE dates(date) AS ( 
                                                     SELECT DATE_SUB(CURDATE(), INTERVAL 6 DAY) 
                                                     UNION ALL 
                                                     SELECT DATE_ADD(date, INTERVAL 1 DAY) 
                                                     FROM dates WHERE date < CURDATE() 
                         						) 
                                                 SELECT dates.date AS ngay, 
                                                 COALESCE(SUM(chitiethoadon.SoLuong * chitiethoadon.DonGia), 0) AS doanhthu, 
                                                 COALESCE(SUM(chitietphieunhap.SoLuong * chitietphieunhap.DonGia), 0) AS chiphi 
                                                 FROM dates 
                                                 LEFT JOIN hoadon ON DATE(hoadon.NgayLap) = dates.date 
                                                 LEFT JOIN chitiethoadon ON hoadon.MaHD = chitiethoadon.MaHD 
                                                 LEFT JOIN phieunhap ON DATE(phieunhap.NgayNhap) = dates.date AND phieunhap.TrangThai = 2
                                                 LEFT JOIN chitietphieunhap ON phieunhap.MaPN = chitietphieunhap.MaPN 
                                                 GROUP BY dates.date 
                                                 ORDER BY dates.date;""";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Date ngay = rs.getDate("ngay");
                int chiphi = rs.getInt("chiphi");
                int doanhthu = rs.getInt("doanhthu");
                int loinhuan = doanhthu - chiphi;
                ThongKeTungNgayTrongThangDTO tn = new ThongKeTungNgayTrongThangDTO(ngay, chiphi, doanhthu, loinhuan);
                result.add(tn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<ThongKeTungNgayTrongThangDTO> getThongKeTuNgayDenNgay(String star, String end) {
        ArrayList<ThongKeTungNgayTrongThangDTO> result = new ArrayList<>();
        try {
            Connection con = ConnectionDB.openConnection();
            String setStar = "SET @start_date = '" + star + "'";
            String setEnd = "SET @end_date = '" + end + "'  ;";
            String sqlSelect = "SELECT \n"
                    + "  dates.date AS ngay, \n"
                    + "  COALESCE(SUM(chitietphieunhap.DonGia), 0) AS chiphi, \n"
                    + "  COALESCE(SUM(chitiethoadon.DonGia), 0) AS doanhthu\n"
                    + "FROM (\n"
                    + "  SELECT DATE_ADD(@start_date, INTERVAL c.number DAY) AS date\n"
                    + "  FROM (\n"
                    + "    SELECT a.number + b.number * 31 AS number\n"
                    + "    FROM (\n"
                    + "      SELECT 0 AS number\n"
                    + "      UNION ALL SELECT 1\n"
                    + "      UNION ALL SELECT 2\n"
                    + "      UNION ALL SELECT 3\n"
                    + "      UNION ALL SELECT 4\n"
                    + "      UNION ALL SELECT 5\n"
                    + "      UNION ALL SELECT 6\n"
                    + "      UNION ALL SELECT 7\n"
                    + "      UNION ALL SELECT 8\n"
                    + "      UNION ALL SELECT 9\n"
                    + "      UNION ALL SELECT 10\n"
                    + "      UNION ALL SELECT 11\n"
                    + "      UNION ALL SELECT 12\n"
                    + "      UNION ALL SELECT 13\n"
                    + "      UNION ALL SELECT 14\n"
                    + "      UNION ALL SELECT 15\n"
                    + "      UNION ALL SELECT 16\n"
                    + "      UNION ALL SELECT 17\n"
                    + "      UNION ALL SELECT 18\n"
                    + "      UNION ALL SELECT 19\n"
                    + "      UNION ALL SELECT 20\n"
                    + "      UNION ALL SELECT 21\n"
                    + "      UNION ALL SELECT 22\n"
                    + "      UNION ALL SELECT 23\n"
                    + "      UNION ALL SELECT 24\n"
                    + "      UNION ALL SELECT 25\n"
                    + "      UNION ALL SELECT 26\n"
                    + "      UNION ALL SELECT 27\n"
                    + "      UNION ALL SELECT 28\n"
                    + "      UNION ALL SELECT 29\n"
                    + "      UNION ALL SELECT 30\n"
                    + "    ) AS a\n"
                    + "    CROSS JOIN (\n"
                    + "      SELECT 0 AS number\n"
                    + "      UNION ALL SELECT 1\n"
                    + "      UNION ALL SELECT 2\n"
                    + "      UNION ALL SELECT 3\n"
                    + "      UNION ALL SELECT 4\n"
                    + "      UNION ALL SELECT 5\n"
                    + "      UNION ALL SELECT 6\n"
                    + "      UNION ALL SELECT 7\n"
                    + "      UNION ALL SELECT 8\n"
                    + "      UNION ALL SELECT 9\n"
                    + "      UNION ALL SELECT 10\n"
                    + "    ) AS b\n"
                    + "  ) AS c\n"
                    + " WHERE DATE_ADD(@start_date, INTERVAL c.number DAY) <= @end_date\n" 
                    + ") AS dates\n" 
                    + "LEFT JOIN phieunhap ON DATE(phieunhap.NgayNhap) = dates.date AND phieunhap.TrangThai = 2\n" 
                    + "LEFT JOIN chitietphieunhap ON phieunhap.MaPN = chitietphieunhap.MaPN\n" 
                    + "LEFT JOIN hoadon ON DATE(hoadon.NgayLap) = dates.date\n" 
                    + "LEFT JOIN chitiethoadon ON hoadon.MaHD = chitiethoadon.MaHD\n" 
                    + "GROUP BY dates.date\n" 
                    + "ORDER BY dates.date;";

            PreparedStatement pstStart = con.prepareStatement(setStar);
            PreparedStatement pstEnd = con.prepareStatement(setEnd);
            PreparedStatement pstSelect = con.prepareStatement(sqlSelect);

            pstStart.execute();
            pstEnd.execute();
            ResultSet rs = pstSelect.executeQuery();
            while (rs.next()) {
                Date ngay = rs.getDate("ngay");
                int chiphi = rs.getInt("chiphi");
                int doanhthu = rs.getInt("doanhthu");
                int loinhuan = doanhthu - chiphi;
                ThongKeTungNgayTrongThangDTO tn = new ThongKeTungNgayTrongThangDTO(ngay, chiphi, doanhthu, loinhuan);
                result.add(tn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

}
