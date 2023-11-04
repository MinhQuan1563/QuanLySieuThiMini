/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package quanlysieuthimini.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import quanlysieuthimini.DAO.DAOInterface.DAOInterface;
import quanlysieuthimini.DTO.HangSanXuatDTO;

public class HangSanXuatDAO implements DAOInterface<HangSanXuatDTO>{
    public static HangSanXuatDAO getInstance() {
        return new HangSanXuatDAO();
    }

    @Override
    public boolean insert(HangSanXuatDTO t) {
        boolean result = false;
        try {
            Connection con = ConnectionDB.openConnection();
            String sql = "INSERT INTO `hangsanxuat`(`MaHang`, `TenHang`, `TruSo`, `TrangThai`) VALUES (?,?,?,1)";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            
            pst.setInt(1, t.getMaHang());
            pst.setString(2, t.getTenHang());
            pst.setString(3, t.getTruSo());
            
            result = pst.executeUpdate()>=1;
            ConnectionDB.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(LoaiSanPhamDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public boolean update(HangSanXuatDTO t) {
        boolean result = false;
        try {
            Connection con = ConnectionDB.openConnection();
            String sql = "UPDATE `hangsanxuat` SET `TenHang`=?, `TruSo`=? WHERE `MaHang`= ?";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setString(1, t.getTenHang());
            pst.setInt(5, t.getMaHang());
            result = pst.executeUpdate()>=1;
            ConnectionDB.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(NhaCungCapDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public boolean delete(int id) {
        boolean result = false;
        try {
            Connection con = ConnectionDB.openConnection();
            String sql = "UPDATE hangsanxuat SET TrangThai = 0 WHERE MaHang = ?";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setInt(1, id);
            result = pst.executeUpdate()>=1;
            ConnectionDB.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(NhaCungCapDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public ArrayList<HangSanXuatDTO> getAll() {
        ArrayList<HangSanXuatDTO> result = new ArrayList<>();
        try {
            Connection con = ConnectionDB.openConnection();
            String sql = "SELECT * FROM hangsanxuat WHERE TrangThai = 1";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs = (ResultSet) pst.executeQuery();
            while(rs.next()){
                int mancc = rs.getInt("MaHang");
                String tenncc = rs.getString("TenHang");
                String truso = rs.getString("TruSo");
                HangSanXuatDTO ncc = new HangSanXuatDTO(mancc, tenncc, truso);
                result.add(ncc);
            }
            ConnectionDB.closeConnection(con);
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public HangSanXuatDTO getById(int id) {
        HangSanXuatDTO result = null;
        try {
            Connection con = ConnectionDB.openConnection();
            String sql = "SELECT * FROM hangsanxuat WHERE MaHang=?";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setInt(1, id);
            ResultSet rs = (ResultSet) pst.executeQuery();
            while(rs.next()){
                int mancc = rs.getInt("MaHang");
                String tenncc = rs.getString("TenHang");
                String truso = rs.getString("TruSo");
                
                result = new HangSanXuatDTO(mancc,tenncc,truso);
            }
            ConnectionDB.closeConnection(con);
        } catch (Exception e) {
        }
        return result;
    }

//    @Override
//    public ArrayList<HangSanXuatDTO> getByCondition(String condition) {
//        ArrayList<HangSanXuatDTO> result = new ArrayList<>();
//        
//        Connection connect = ConnectionDB.openConnection();
//        if (connect != null) {
//            
//            try {
//                String sql = "SELECT * FROM hangsanxuat WHERE TrangThai=1 AND " + condition;
//
//                //Bước 2: tạo đối tượng preparedStatement
//                PreparedStatement stmt = connect.prepareStatement(sql);  
//
//                ResultSet rs = stmt.executeQuery();
//                
//                //Bước 3: lấy dữ liệu
//                while(rs.next()) {
//                    int MaHang = rs.getInt("MaHang");
//                    String TenHang = rs.getString("TenHang");
//                    String TruSo = rs.getString("TruSo");
//                    
//                    HangSanXuatDTO s = new HangSanXuatDTO(MaHang, TenHang, TruSo);
//                 
//                    result.add(s);
//                }
//            } catch (SQLException ex) {
//                Logger.getLogger(LoaiSanPhamDAO.class.getName()).log(Level.SEVERE, null, ex);
//            } finally {
//                ConnectionDB.closeConnection(connect);
//            }
//        }
//        
//        return result;
//    }
    
    @Override
    public int getAutoIncrement() {
        int result = -1;
        try {
            Connection con = ConnectionDB.openConnection();
            String sql = "SELECT `AUTO_INCREMENT` FROM  INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'quanlisieuthimini' AND   TABLE_NAME   = 'hangsanxuat'";
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
            Logger.getLogger(HangSanXuatDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}
