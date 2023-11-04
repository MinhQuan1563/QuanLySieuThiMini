package quanlysieuthimini.DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.time.LocalDate;
import quanlysieuthimini.DAO.DAOInterface.DAOInterface;
import quanlysieuthimini.DTO.KhachHangDTO;
import quanlysieuthimini.DTO.SanPhamDTO;


public class SanPhamDAO implements DAOInterface<SanPhamDTO> {

    public static SanPhamDAO getInstance() {
        return new SanPhamDAO();
    }
    
    @Override
    public boolean insert(SanPhamDTO t) {
        boolean result = false;
        try {
            Connection con = (Connection) ConnectionDB.openConnection();
            String sql = "INSERT into sanpham "
                        + "(MaLoai,MaHang,MaDV,TenSP,DonGia,SoLuong,DungTich,NgaySanXuat,HanSuDung,HinhAnh) "
                        + "VALUES (?,?,?,?,?,?,?,?,?,?)";
            
            PreparedStatement stmt = (PreparedStatement) con.prepareStatement(sql);
            
                stmt.setInt(1,t.getMaLoai());
                stmt.setInt(2,t.getMaHang());
                stmt.setInt(3,t.getMaDV());
                stmt.setString(4,t.getTenSP());
                stmt.setDouble(5,t.getDonGia());
                stmt.setInt(6,t.getSoLuong());
                stmt.setInt(7,t.getDungTich());
                stmt.setObject(8,  t.getNgaySanXuat());
                stmt.setObject(9,  t.getHanSuDung());
                stmt.setString(10,t.getHinhAnh());
            
            result = stmt.executeUpdate()>=1;
            ConnectionDB.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(LoaiSanPhamDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public boolean update(SanPhamDTO t) {
        boolean result = false;
        //Bước 1: tạo kết nối với sql
        Connection connect = ConnectionDB.openConnection();

        if (connect != null) {
            try {
                String sql = "UPDATE sanpham SET "
                        + "MaLoai= ?,MaHang= ?,MaDV= ?,TenSP= ?,DonGia= ?,SoLuong= ?,DungTich= ?"
                        +",NgaySanXuat= ?,HanSuDung= ?,HinhAnh= ?,TrangThai= ?"
                        + " WHERE MaSP=?";

                //Bước 2: tạo đối tượng preparedStatement
                PreparedStatement stmt = connect.prepareStatement(sql);

                stmt.setInt(1,t.getMaLoai());
                stmt.setInt(2,t.getMaHang());
                stmt.setInt(3,t.getMaDV());
                stmt.setString(4,t.getTenSP());
                stmt.setDouble(5,t.getDonGia());
                stmt.setInt(6,t.getSoLuong());
                stmt.setInt(7,t.getDungTich());
                stmt.setObject(8,  t.getNgaySanXuat());
                stmt.setObject(9, t.getHanSuDung());
                stmt.setString(10,t.getHinhAnh());
                stmt.setInt(11,t.getTrangThai());
                stmt.setInt(12,t.getMaSP());
                
                result = stmt.executeUpdate()>=1;
                System.out.println("Success DAO");
            } catch (SQLException ex) {
                Logger.getLogger(SanPhamDAO.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                ConnectionDB.closeConnection(connect);
            }
        }
        return result;
    }

    @Override
    public boolean delete(int id) {
        boolean result = false;
        //Bước 1: tạo kết nối với sql
        Connection connect = ConnectionDB.openConnection();
        if (connect != null) {
            try {
                String sql = "UPDATE sanpham SET TrangThai = 0 "
                        + "WHERE MaSP=?";

                //Bước 2: tạo đối tượng preparedStatement
                PreparedStatement stmt = connect.prepareStatement(sql);
                
                stmt.setInt(1, id);

                result = stmt.executeUpdate()>=1;
            } catch (SQLException ex) {
                Logger.getLogger(SanPhamDAO.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                ConnectionDB.closeConnection(connect);
            }
        }

        return result;
    }

    @Override
    public ArrayList<SanPhamDTO> getAll() {
        ArrayList<SanPhamDTO> result = new ArrayList<>();

        Connection connect = ConnectionDB.openConnection();
        if (connect != null) {

            try {
                String sql = "SELECT * FROM sanpham WHERE TrangThai = 1";

                //Bước 2: tạo đối tượng preparedStatement
                PreparedStatement stmt = connect.prepareStatement(sql);

                ResultSet rs = stmt.executeQuery();

                //Bước 3: lấy dữ liệu
                while(rs.next()) {
                    SanPhamDTO s = new SanPhamDTO();
                    s.setMaSP(rs.getInt("MaSP"));
                    s.setMaLoai(rs.getInt("MaLoai"));
                    s.setMaHang(rs.getInt("MaHang"));
                    s.setMaDV(rs.getInt("MaDV"));
                    s.setTenSP(rs.getString("TenSP"));
                    s.setDonGia(rs.getDouble("DonGia"));
                    s.setSoLuong(rs.getInt("SoLuong"));
                    s.setDungTich(rs.getInt("DungTich"));
                    s.setNgaySanXuat(rs.getDate("NgaySanXuat").toLocalDate());
                    s.setHanSuDung(rs.getDate("HanSuDung").toLocalDate());
                    s.setHinhAnh(rs.getString("HinhAnh"));
                    s.setTrangThai(rs.getInt("TrangThai"));

                    result.add(s);
                }
            } catch (SQLException ex) {
                Logger.getLogger(SanPhamDAO.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                ConnectionDB.closeConnection(connect);
            }
        }

        return result;
    }

    public ArrayList<SanPhamDTO> selectAll() {
        ArrayList<SanPhamDTO> result = new ArrayList<>();

        Connection connect = ConnectionDB.openConnection();
        if (connect != null) {

            try {
                String sql = "SELECT * FROM sanpham";

                //Bước 2: tạo đối tượng preparedStatement
                PreparedStatement stmt = connect.prepareStatement(sql);

                ResultSet rs = stmt.executeQuery();

                //Bước 3: lấy dữ liệu
                while(rs.next()) {
                    SanPhamDTO s = new SanPhamDTO();
                    s.setMaSP(rs.getInt("MaSP"));
                    s.setMaLoai(rs.getInt("MaLoai"));
                    s.setMaHang(rs.getInt("MaHang"));
                    s.setMaDV(rs.getInt("MaDV"));
                    s.setTenSP(rs.getString("TenSP"));
                    s.setDonGia(rs.getDouble("DonGia"));
                    s.setSoLuong(rs.getInt("SoLuong"));
                    s.setDungTich(rs.getInt("DungTich"));
                    s.setNgaySanXuat(rs.getDate("NgaySanXuat").toLocalDate());
                    s.setHanSuDung(rs.getDate("HanSuDung").toLocalDate());
                    s.setHinhAnh(rs.getString("HinhAnh"));
                    s.setTrangThai(rs.getInt("TrangThai"));

                    result.add(s);
                }
            } catch (SQLException ex) {
                Logger.getLogger(SanPhamDAO.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                ConnectionDB.closeConnection(connect);
            }
        }

        return result;
    }

    @Override
    public SanPhamDTO getById(int id) {
        SanPhamDTO s = new SanPhamDTO();

        Connection connect = ConnectionDB.openConnection();
        if (connect != null) {
            try {
                String sql = "SELECT * FROM sanpham WHERE TrangThai = 1 AND MaSP= " + id  ;

                //Bước 2: tạo đối tượng preparedStatement
                PreparedStatement stmt = connect.prepareStatement(sql);

                ResultSet rs = stmt.executeQuery();

                //Bước 3: lấy dữ liệu
                while(rs.next()) {
                    s.setMaSP(rs.getInt("MaSP"));
                    s.setMaLoai(rs.getInt("MaLoai"));
                    s.setMaHang(rs.getInt("MaHang"));
                    s.setMaDV(rs.getInt("MaDV"));
                    s.setTenSP(rs.getString("TenSP"));
                    s.setDonGia(rs.getDouble("DonGia"));
                    s.setSoLuong(rs.getInt("SoLuong"));
                    s.setDungTich(rs.getInt("DungTich"));
                    s.setNgaySanXuat(rs.getDate("NgaySanXuat").toLocalDate());
                    s.setHanSuDung(rs.getDate("HanSuDung").toLocalDate());
                    s.setHinhAnh(rs.getString("HinhAnh"));
                    s.setTrangThai(rs.getInt("TrangThai"));
                }
            } catch (SQLException ex) {
                Logger.getLogger(SanPhamDAO.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                ConnectionDB.closeConnection(connect);
            }
        }

        return s;
    }
    
    @Override
    public int getAutoIncrement() {
        int result = -1;
        try {
            Connection con = (Connection) ConnectionDB.openConnection();
            String sql = "SELECT `AUTO_INCREMENT` FROM  INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'quanlysieuthimini' AND   TABLE_NAME   = 'sanpham'";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs2 = pst.executeQuery(sql);
            if (!rs2.isBeforeFirst() ) {
                System.out.println("No data");
            } else {
                while ( rs2.next() ) {
                    result = rs2.getInt("AUTO_INCREMENT");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoaiSanPhamDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

//    @Override
//    public ArrayList<SanPhamDTO> getByCondition(String condition) {
//        ArrayList<SanPhamDTO> result = new ArrayList<>();
//
//        Connection connect = ConnectionDB.openConnection();
//        if (connect != null) {
//
//            try {
//                String sql = "SELECT * FROM sanpham WHERE TrangThai=1 AND " + condition;
//
//                //Bước 2: tạo đối tượng preparedStatement
//                PreparedStatement stmt = connect.prepareStatement(sql);
//
//                ResultSet rs = stmt.executeQuery();
//
//                //Bước 3: lấy dữ liệu
//                while(rs.next()) {
//                    SanPhamDTO s = new SanPhamDTO();
//                    s.setMaSP(rs.getInt(1));
//                    s.setMaLoai(rs.getInt(2));
//                    s.setMaHang(rs.getInt(3));
//                    s.setMaDV(rs.getInt(4));
//                    s.setMaNCC(rs.getInt(5));
//                    s.setTenSP(rs.getString(6));
//                    s.setSoLuong(rs.getInt(7));
//                    s.setDungTich(rs.getInt(8));
//                    s.setDonGia(rs.getDouble(9));
//                    s.setNgaySanXuat(rs.getDate(10).toLocalDate());
//                    s.setHanSuDung(rs.getDate(11).toLocalDate());
//                    s.setHinhAnh(rs.getString(12));
//                    s.setTrangThai(rs.getBoolean(13));
//
//                    result.add(s);
//                }
//            } catch (SQLException ex) {
//                Logger.getLogger(SanPhamDAO.class.getName()).log(Level.SEVERE, null, ex);
//            } finally {
//                ConnectionDB.closeConnection(connect);
//            }
//        }
//
//        return result;
//    }
    
}
