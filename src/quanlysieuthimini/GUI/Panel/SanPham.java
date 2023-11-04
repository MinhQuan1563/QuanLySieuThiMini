package quanlysieuthimini.GUI.Panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.logging.Level;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import quanlysieuthimini.GUI.Dialog.SanPhamDialog;
import java.io.IOException;
import javax.swing.JOptionPane;
import quanlysieuthimini.BUS.HangSanXuatBUS;
import quanlysieuthimini.BUS.SanPhamBUS;
import quanlysieuthimini.DTO.SanPhamDTO;
import quanlysieuthimini.GUI.Main;
import quanlysieuthimini.GUI.Component.IntegratedSearch;
import quanlysieuthimini.GUI.Component.MainFunction;
import quanlysieuthimini.GUI.Component.PanelBorderRadius;
import quanlysieuthimini.GUI.Component.TableSorter;
import quanlysieuthimini.helper.JTableExporter;

public final class SanPham extends JPanel implements ActionListener{
    JFrame owner = (JFrame) SwingUtilities.getWindowAncestor(this);
    public SanPhamBUS spBUS = new SanPhamBUS(this);
    PanelBorderRadius main, functionBar;
    JPanel pnlBorder1, pnlBorder2, pnlBorder3, pnlBorder4, contentCenter;
    JTable tableSanPham;
    JScrollPane scrollTableSanPham;
    MainFunction mainFunction;
    public IntegratedSearch search;
    Main m;
    public ArrayList<SanPhamDTO> listsp = spBUS.getAll();

    Color BackgroundColor = new Color(240,247,250);
    private DefaultTableModel tblModel;

    private void initComponent(){
        this.setBackground(BackgroundColor);
        this.setLayout(new BorderLayout(0,0));
        this.setOpaque(true);

        // pnlBorder1, pnlBorder2, pnlBorder3, pnlBorder4 chỉ để thêm contentCenter ở giữa mà contentCenter không bị dính sát vào các thành phần khác
        pnlBorder1 = new JPanel();
        pnlBorder1.setPreferredSize(new Dimension(0,10));
        pnlBorder1.setBackground(BackgroundColor);
        this.add(pnlBorder1, BorderLayout.NORTH);
        
        pnlBorder2 = new JPanel();
        pnlBorder2.setPreferredSize(new Dimension(0, 10));
        pnlBorder2.setBackground(BackgroundColor);
        this.add(pnlBorder2, BorderLayout.SOUTH);

        pnlBorder3 = new JPanel();
        pnlBorder3.setPreferredSize(new Dimension(10, 0));
        pnlBorder3.setBackground(BackgroundColor);
        this.add(pnlBorder3, BorderLayout.EAST);

        pnlBorder4 = new JPanel();
        pnlBorder4.setPreferredSize(new Dimension(10, 0));
        pnlBorder4.setBackground(BackgroundColor);
        this.add(pnlBorder4, BorderLayout.WEST);

        contentCenter = new JPanel();
        contentCenter.setPreferredSize(new Dimension(1100, 600));
        contentCenter.setBackground(BackgroundColor);
        contentCenter.setLayout(new BorderLayout(10, 10));
        this.add(contentCenter, BorderLayout.CENTER);

        functionBar = new PanelBorderRadius();
        functionBar.setPreferredSize(new Dimension(0,100));
        functionBar.setLayout(new GridLayout(1,2,50,0));
        functionBar.setBorder(new EmptyBorder(10,10,10,10));
        contentCenter.add(functionBar,BorderLayout.NORTH);

        String[] action = {"create","update", "delete", "detail", "import","export"};
        mainFunction = new MainFunction(m.user.getMaQuyen(), "sanpham", action);
        for (String ac : action){
            mainFunction.btn.get(ac).addActionListener(this);
        }
        functionBar.add(mainFunction);
        // search = new IntegratedSearch(new String[] {"Tất cả"});
        // functionBar.add(search);
        // search.btnReset.addActionListener(spBUS);
        // search.cbxChoose.addActionListener(spBUS);
        // search.txtSearchForm.getDocument().addDocumentListener(new SanPhamBUS(search.txtSearchForm,this));

        search = new IntegratedSearch(new String[] {"Tất cả"});
        search.txtSearchForm.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String txt = search.txtSearchForm.getText();
                listsp = spBUS.search(txt);
                loadDataTalbe(listsp);
            }

        });

        search.btnReset.addActionListener((ActionEvent e) -> {
            search.txtSearchForm.setText("");
            listsp = spBUS.getAll();
            loadDataTalbe(listsp);
        });
        functionBar.add(search);

        // main là phần ở dưới để thống kê bảng biểu
        main = new PanelBorderRadius();
        BoxLayout boxly = new BoxLayout(main,BoxLayout.Y_AXIS);
        main.setLayout(boxly);
        //        main.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentCenter.add(main, BorderLayout.CENTER);

        tableSanPham = new JTable();
        scrollTableSanPham = new JScrollPane();
        tblModel = new DefaultTableModel();
        String[] header = new String[] {"Mã SP", "Tên SP", "Đơn giá", "Số lượng", "Hãng"};
        tblModel.setColumnIdentifiers(header);
        tableSanPham.setModel(tblModel);
        tableSanPham.setFocusable(false);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        tableSanPham.setDefaultRenderer(Object.class, centerRenderer);
        centerRenderer.setHorizontalAlignment((JLabel.CENTER));
        tableSanPham.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tableSanPham.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        tableSanPham.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        tableSanPham.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        tableSanPham.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        scrollTableSanPham.setViewportView(tableSanPham);
        tableSanPham.getColumnModel().getColumn(1).setPreferredWidth(180);
        tableSanPham.setFocusable(false);
        tableSanPham.setAutoCreateRowSorter(true);
        TableSorter.configureTableColumnSorter(tableSanPham, 2, TableSorter.INTEGER_COMPARATOR);
        tableSanPham.setDefaultEditor(Object.class, null);
        main.add(scrollTableSanPham);
        
    }



    public SanPham(Main m){
        this.m = m;
        initComponent();
        tableSanPham.setDefaultEditor(Object.class, null);
        loadDataTalbe(listsp);
    }

    public int getRow(){
        return tableSanPham.getSelectedRow();
    }

    public SanPhamDTO getSanPham(){
        return listsp.get(tableSanPham.getSelectedRow());
    }

    public void loadDataTalbe(ArrayList<SanPhamDTO> list){
        listsp = list;
        tblModel.setRowCount(0);
        for(SanPhamDTO sanpham : listsp){
            tblModel.addRow(new Object[]{
                sanpham.getMaSP(), sanpham.getTenSP(), sanpham.getDonGia(), sanpham.getSoLuong(), HangSanXuatBUS.getInstance().getTenNhaHang(sanpham.getMaHang())
            });
        }
    }
//public void actionPerformed(ActionEvent e) {
//        if (e.getSource() == mainFunction.btn.get("create")) {
//            SanPhamDialog spDialog = new SanPhamDialog(this, owner, "Thêm sản phẩm mới", true, "create");
//        } else if (e.getSource() == mainFunction.btn.get("update")) {
//            int index = getRowSelected();
//            if (index != -1) {
//                SanPhamDialog spDialog = new SanPhamDialog(this, owner, "Chỉnh sửa sản phẩm", true, "update", listSP.get(index));
//            }
//        } else if (e.getSource() == mainFunction.btn.get("delete")) {
//            int index = getRowSelected();
//            if (index != -1) {
//                int input = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn xóa Sản phẩm :)!", "Xóa sản phẩm", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
//                if (input == 0) {
//                    spBUS.delete(listSP.get(index));
//                    loadDataTalbe(listSP);
//                }
//            }
//        } else if (e.getSource() == mainFunction.btn.get("detail")) {
//            int index = getRowSelected();
//            if (index != -1) {
//                SanPhamDialog spDialog = new SanPhamDialog(this, owner, "Xem chi tiết sản phẩm", true, "view", listSP.get(index));
//            }
//        } else if (e.getSource() == mainFunction.btn.get("phone")) {
//            int index = getRowSelected();
//            if (index != -1) {
//                ChiTietSanPhamDialog ct = new ChiTietSanPhamDialog(owner, "Tất cả sản phẩm", true, listSP.get(index));
//            }
//        } else if (e.g
//    }etSource() == mainFunction.btn.get("export")) {
//            try {
//                JTableExporter.exportJTableToExcel(tableSanPham);
//            } catch (IOException ex) {
//                Logger.getLogger(SanPham.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        } else if(e.getSource() == mainFunction.btn.get("import")) {
//            JOptionPane.showMessageDialog(null, "Chức năng không khả dụng");
//        }
//    }
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == mainFunction.btn.get("create")){
            SanPhamDialog spDialog = new SanPhamDialog(this, owner,"Thêm sản phẩm mới",true, "create");
        }else if (e.getSource() == mainFunction.btn.get("update")){
            int index = getRowSelected();
            if(index != -1){
                SanPhamDialog spDialog = new SanPhamDialog(this, owner, "Chỉnh sửa sản phẩm", true, "update", listsp.get(index));
            }
        } else if (e.getSource() == mainFunction.btn.get("delete")) {
            int index = getRowSelected();
            if (index != -1) {
                int input = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn xóa Sản phẩm :)!", "Xóa sản phẩm", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
                if (input == 0) {
                    spBUS.delete(listsp.get(index));
                    loadDataTalbe(listsp);
                }
            }
        } else if (e.getSource() == mainFunction.btn.get("detail")) {
            int index = getRowSelected();
            if (index != -1) {
                SanPhamDialog spDialog = new SanPhamDialog(this, owner, "Xem chi tiết sản phẩm", true, "view", listsp.get(index));
            }
        } else if (e.getSource() == mainFunction.btn.get("phone")) {
            int index = getRowSelected();
            if (index != -1) {
//                ChiTietSanPhamDialog ct = new ChiTietSanPhamDialog(owner, "Tất cả sản phẩm", true, listSP.get(index));
                  System.out.println("phone");
            }
        } else if (e.getSource() == mainFunction.btn.get("export")) {
            try {
                JTableExporter.exportJTableToExcel(tableSanPham);
            } catch (IOException ex) {
                Logger.getLogger(SanPham.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if(e.getSource() == mainFunction.btn.get("import")) {
            JOptionPane.showMessageDialog(null, "Chức năng không khả dụng");
        }
    }

    public int getRowSelected(){
        int index = tableSanPham.getSelectedRow();
        if(index == -1){
                JOptionPane.showMessageDialog(this,"Vui lòng chọn sản phẩm");
        }
        return index;
    }
}

//package GUI.Panel;
//
//import BUS.SanPhamBUS;
//import DAO.HeDieuHanhDAO;
//import DAO.KhuVucKhoDAO;
//import DAO.ThuongHieuDAO;
//import DAO.XuatXuDAO;
//import GUI.Component.IntegratedSearch;
//import GUI.Component.MainFunction;
//import GUI.Main;
//import java.awt.*;
//import javax.swing.*;
//import javax.swing.border.EmptyBorder;
//import GUI.Component.PanelBorderRadius;
//import GUI.Component.TableSorter;
//import GUI.Dialog.ChiTietSanPhamDialog;
//import GUI.Dialog.SanPhamDialog;
//import helper.JTableExporter;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.KeyAdapter;
//import java.awt.event.KeyEvent;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import javax.swing.table.DefaultTableCellRenderer;
//import javax.swing.table.DefaultTableModel;
//import javax.swing.table.TableColumnModel;
//
//public final class SanPham extends JPanel implements ActionListener {
//    PanelBorderRadius main, functionBar;
//    JPanel pnlBorder1, pnlBorder2, pnlBorder3, pnlBorder4, contentCenter;
//    JFrame owner = (JFrame) SwingUtilities.getWindowAncestor(this);
//    JTable tableSanPham;
//    JScrollPane scrollTableSanPham;
//    MainFunction mainFunction;
//    IntegratedSearch search;
//    DefaultTableModel tblModel;
//    Main m;
//    public SanPhamBUS spBUS = new SanPhamBUS();
//    
//    public ArrayList<DTO.SanPhamDTO> listSP = spBUS.getAll();
//
//    Color BackgroundColor = new Color(240, 247, 250);
//
//    private void initComponent() {
//        this.setBackground(BackgroundColor);
//        this.setLayout(new BorderLayout(0, 0));
//        this.setOpaque(true);
//        tableSanPham = new JTable();
//        scrollTableSanPham = new JScrollPane();
//        tblModel = new DefaultTableModel();
//        String[] header = new String[]{"Mã SP", "Tên sản phẩm", "Số lượng tồn","Thương hiệu", "Hệ điều hành", "Kích thước màn","Chip xử lý","Dung lượng pin","Xuất xứ", "Khu vực kho"};
//        tblModel.setColumnIdentifiers(header);
//        tableSanPham.setModel(tblModel);
//        scrollTableSanPham.setViewportView(tableSanPham);
//        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
//        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
//        TableColumnModel columnModel = tableSanPham.getColumnModel();
//        for (int i = 0; i < 10; i++) {
//            if (i != 1) {
//                columnModel.getColumn(i).setCellRenderer(centerRenderer);
//            }
//        }
//        tableSanPham.getColumnModel().getColumn(1).setPreferredWidth(180);
//        tableSanPham.setFocusable(false);
//        tableSanPham.setAutoCreateRowSorter(true);
//        TableSorter.configureTableColumnSorter(tableSanPham, 2, TableSorter.INTEGER_COMPARATOR);
//        tableSanPham.setDefaultEditor(Object.class, null);
//        initPadding();
//
//        contentCenter = new JPanel();
//        contentCenter.setBackground(BackgroundColor);
//        contentCenter.setLayout(new BorderLayout(10, 10));
//        this.add(contentCenter, BorderLayout.CENTER);
//
//        // functionBar là thanh bên trên chứa các nút chức năng như thêm xóa sửa, và tìm kiếm
//        functionBar = new PanelBorderRadius();
//        functionBar.setPreferredSize(new Dimension(0, 100));
//        functionBar.setLayout(new GridLayout(1, 2, 50, 0));
//        functionBar.setBorder(new EmptyBorder(10, 10, 10, 10));
//
//        String[] action = {"create", "update", "delete", "detail", "phone", "export"};
//        mainFunction = new MainFunction(m.user.getManhomquyen(), "sanpham", action);
//        for (String ac : action) {
//            mainFunction.btn.get(ac).addActionListener(this);
//        }
//        functionBar.add(mainFunction);
//
//        search = new IntegratedSearch(new String[]{"Tất cả"});
//        search.txtSearchForm.addKeyListener(new KeyAdapter() {
//            @Override
//            public void keyReleased(KeyEvent e) {
//                String txt = search.txtSearchForm.getText();
//                listSP = spBUS.search(txt);
//                loadDataTalbe(listSP);
//            }
//
//        });
//
//        search.btnReset.addActionListener((ActionEvent e) -> {
//            search.txtSearchForm.setText("");
//            listSP = spBUS.getAll();
//            loadDataTalbe(listSP);
//        });
//        functionBar.add(search);
//
//        contentCenter.add(functionBar, BorderLayout.NORTH);
//
//        // main là phần ở dưới để thống kê bảng biểu
//        main = new PanelBorderRadius();
//        BoxLayout boxly = new BoxLayout(main, BoxLayout.Y_AXIS);
//        main.setLayout(boxly);
//        main.setBorder(new EmptyBorder(0, 0, 0, 0));
//        contentCenter.add(main, BorderLayout.CENTER);
//        main.add(scrollTableSanPham);
//    }
//
//    public SanPham(Main m) {
//        this.m = m;
//        initComponent();
//        loadDataTalbe(listSP);
//    }
//
//    public void loadDataTalbe(ArrayList<DTO.SanPhamDTO> result) {
//        tblModel.setRowCount(0);
//        for (DTO.SanPhamDTO sp : result) {
//            tblModel.addRow(new Object[]{sp.getMasp(), sp.getTensp(), 
//                sp.getSoluongton(), ThuongHieuDAO.getInstance().selectById(sp.getThuonghieu()+"").getTenthuonghieu(), 
//                HeDieuHanhDAO.getInstance().selectById(sp.getHedieuhanh()+"").getTenhdh(),
//                sp.getKichthuocman() + " inch", 
//                sp.getChipxuly(),sp.getDungluongpin() +"mAh",
//                XuatXuDAO.getInstance().selectById(sp.getXuatxu()+"").getTenxuatxu(), 
//                KhuVucKhoDAO.getInstance().selectById(sp.getKhuvuckho()+"").getTenkhuvuc()
//            });
//        }
//    }
//
//    @Override
//    public void actionPerformed(ActionEvent e) {
//        if (e.getSource() == mainFunction.btn.get("create")) {
//            SanPhamDialog spDialog = new SanPhamDialog(this, owner, "Thêm sản phẩm mới", true, "create");
//        } else if (e.getSource() == mainFunction.btn.get("update")) {
//            int index = getRowSelected();
//            if (index != -1) {
//                SanPhamDialog spDialog = new SanPhamDialog(this, owner, "Chỉnh sửa sản phẩm", true, "update", listSP.get(index));
//            }
//        } else if (e.getSource() == mainFunction.btn.get("delete")) {
//            int index = getRowSelected();
//            if (index != -1) {
//                int input = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn xóa Sản phẩm :)!", "Xóa sản phẩm", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
//                if (input == 0) {
//                    spBUS.delete(listSP.get(index));
//                    loadDataTalbe(listSP);
//                }
//            }
//        } else if (e.getSource() == mainFunction.btn.get("detail")) {
//            int index = getRowSelected();
//            if (index != -1) {
//                SanPhamDialog spDialog = new SanPhamDialog(this, owner, "Xem chi tiết sản phẩm", true, "view", listSP.get(index));
//            }
//        } else if (e.getSource() == mainFunction.btn.get("phone")) {
//            int index = getRowSelected();
//            if (index != -1) {
//                ChiTietSanPhamDialog ct = new ChiTietSanPhamDialog(owner, "Tất cả sản phẩm", true, listSP.get(index));
//            }
//        } else if (e.g
//    }etSource() == mainFunction.btn.get("export")) {
//            try {
//                JTableExporter.exportJTableToExcel(tableSanPham);
//            } catch (IOException ex) {
//                Logger.getLogger(SanPham.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        } else if(e.getSource() == mainFunction.btn.get("import")) {
//            JOptionPane.showMessageDialog(null, "Chức năng không khả dụng");
//        }
//    }
//
//    public int getRowSelected() {
//        int index = tableSanPham.getSelectedRow();
//        if (index == -1) {
//            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm");
//        }
//        return index;
//
//    private void initPadding() {
//        pnlBorder1 = new JPanel();
//        pnlBorder1.setPreferredSize(new Dimension(0, 10));
//        pnlBorder1.setBackground(BackgroundColor);
//        this.add(pnlBorder1, BorderLayout.NORTH);
//
//        pnlBorder2 = new JPanel();
//        pnlBorder2.setPreferredSize(new Dimension(0, 10));
//        pnlBorder2.setBackground(BackgroundColor);
//        this.add(pnlBorder2, BorderLayout.SOUTH);
//
//        pnlBorder3 = new JPanel();
//        pnlBorder3.setPreferredSize(new Dimension(10, 0));
//        pnlBorder3.setBackground(BackgroundColor);
//        this.add(pnlBorder3, BorderLayout.EAST);
//
//        pnlBorder4 = new JPanel();
//        pnlBorder4.setPreferredSize(new Dimension(10, 0));
//        pnlBorder4.setBackground(BackgroundColor);
//        this.add(pnlBorder4, BorderLayout.WEST);
//    }
//
//}
