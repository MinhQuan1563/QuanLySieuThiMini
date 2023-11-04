package quanlysieuthimini.GUI.Component;

//import quanlysieuthimini.GUI.Panel.ChuyenKho;
//import quanlysieuthimini.GUI.Panel.KhachHang;
//import quanlysieuthimini.GUI.Panel.KhuVucKho;
//import quanlysieuthimini.GUI.Panel.NhaCungCap;
import quanlysieuthimini.GUI.Panel.NhanVien;
import quanlysieuthimini.GUI.Panel.PhanQuyen;
//import quanlysieuthimini.GUI.Panel.PhieuNhap;
//import quanlysieuthimini.GUI.Panel.PhieuXuat;
//import quanlysieuthimini.GUI.Panel.QuanLyThuocTinhSP;
import quanlysieuthimini.GUI.Panel.SanPham;
import quanlysieuthimini.GUI.Panel.TaiKhoan;
import quanlysieuthimini.GUI.Panel.BeginForm;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import quanlysieuthimini.DAO.ChiTietQuyenDAO;
import quanlysieuthimini.DAO.NhanVienDAO;
import quanlysieuthimini.DAO.QuyenDAO;
import quanlysieuthimini.GUI.Dialog.MyAccount;
import quanlysieuthimini.DTO.ChiTietQuyenDTO;
import quanlysieuthimini.DTO.NhanVienDTO;
import quanlysieuthimini.DTO.QuyenDTO;
import quanlysieuthimini.DTO.TaiKhoanDTO;
import quanlysieuthimini.GUI.Log_In;
import quanlysieuthimini.GUI.Main;
import quanlysieuthimini.GUI.Panel.NhaCungCap;

public class MenuTaskbar extends JPanel {

    BeginForm trangChu;
//    SanPham sanPham;
//    QuanLyThuocTinhSP quanLyThuocTinhSP;
//    KhuVucKho quanLyKho;
//    PhieuNhap phieuNhap;
//    PhieuXuat phieuXuat;
//    KhachHang khachHang;
    NhaCungCap nhacungcap;
    NhanVien nhanVien;
    SanPham sanPham;
    TaiKhoan taiKhoan;
    PhanQuyen phanQuyen;
    //ThongKe thongKe;
    //{"Trang chủ", "home.svg", "trangchu"},
    String[][] getSt = {
        {"Bán hàng", "home.svg", "banhang"},
        {"Nhập hàng", "home.svg", "nhaphang"},
        {"Sản phẩm", "product.svg", "sanpham"},
        {"Thành phần", "brand.svg", "thanhphan"},
        {"Nhân viên", "area.svg", "nhanvien"},
        {"Khách hàng", "import.svg", "khachhang"},
        {"Nhà cung cấp", "export.svg", "nhacungcap"},
        {"Khuyến mãi", "customer.svg", "khuyenmai"},
        {"Phân công ca", "supplier.svg", "phancongca"},
        {"Tài khoản", "account.svg", "taikhoan"},
        {"Phân quyền", "permission.svg", "phanquyen"},
        {"Thống kê", "statistical.svg", "thongke"},
        {"Đăng xuất", "log_out.svg", "dangxuat"}, 
    };

    Main main;
    TaiKhoanDTO user;
    public itemTaskbar[] listitem;

    JLabel lblTenQuyen, lblUsername;
    JScrollPane scrollPane;

    //tasbarMenu chia thành 3 phần chính là pnlCenter, pnlTop, pnlBottom
    JPanel pnlCenter, pnlTop, pnlBottom, bar1, bar2, bar3, bar4;

    Color FontColor = new Color(96, 125, 139);
    Color DefaultColor = new Color(255, 255, 255);
    Color HowerFontColor = new Color(1, 87, 155);
    Color HowerBackgroundColor = new Color(187, 222, 251);
    private ArrayList<ChiTietQuyenDTO> listQuyen;
    QuyenDTO nhomQuyenDTO;
    public NhanVienDTO nhanVienDTO;
    JFrame owner = (JFrame) SwingUtilities.getWindowAncestor(this);

    public MenuTaskbar(Main main) {
        this.main = main;
        initComponent();
    }

    public MenuTaskbar(Main main, TaiKhoanDTO tk) {
        this.main = main;
        this.user = tk;
        this.nhomQuyenDTO = QuyenDAO.getInstance().getById(tk.getMaQuyen());
        this.nhanVienDTO = NhanVienDAO.getInstance().getById(tk.getMaNV());
        listQuyen = ChiTietQuyenDAO.getInstance().getAll(tk.getMaQuyen());
        initComponent();
    }

    private void initComponent() {
        listitem = new itemTaskbar[getSt.length];
        this.setOpaque(true);
        this.setBackground(DefaultColor);
        this.setLayout(new BorderLayout(0, 0));

        // bar1, bar là các đường kẻ mỏng giữa taskbarMenu và MainContent
        pnlTop = new JPanel();
        pnlTop.setPreferredSize(new Dimension(250, 80));
        pnlTop.setBackground(DefaultColor);
        pnlTop.setLayout(new BorderLayout(0, 0));
        this.add(pnlTop, BorderLayout.NORTH);

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BorderLayout(0, 0));
        pnlTop.add(info, BorderLayout.CENTER);

        // Cái info này bỏ vô cho đẹp tí, mai mốt có gì xóa đi đê hiển thị thông tin tài khoản và quyền
        in4(info);

        bar1 = new JPanel();
        bar1.setBackground(new Color(204, 214, 219));
        bar1.setPreferredSize(new Dimension(1, 0));
        pnlTop.add(bar1, BorderLayout.EAST);

        bar2 = new JPanel();
        bar2.setBackground(new Color(204, 214, 219));
        bar2.setPreferredSize(new Dimension(0, 1));
        pnlTop.add(bar2, BorderLayout.SOUTH);

        pnlCenter = new JPanel();
        pnlCenter.setPreferredSize(new Dimension(230, 600));
        pnlCenter.setBackground(DefaultColor);
        pnlCenter.setLayout(new FlowLayout(0, 0, 5));

        bar3 = new JPanel();
        bar3.setBackground(new Color(204, 214, 219));
        bar3.setPreferredSize(new Dimension(1, 1));
        this.add(bar3, BorderLayout.EAST);

        scrollPane = new JScrollPane(pnlCenter, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(new EmptyBorder(5, 10, 0, 10));
        this.add(scrollPane, BorderLayout.CENTER);

        pnlBottom = new JPanel();
        pnlBottom.setPreferredSize(new Dimension(250, 50));
        pnlBottom.setBackground(DefaultColor);
        pnlBottom.setLayout(new BorderLayout(0, 0));

        bar4 = new JPanel();
        bar4.setBackground(new Color(204, 214, 219));
        bar4.setPreferredSize(new Dimension(1, 1));
        pnlBottom.add(bar4, BorderLayout.EAST);

        this.add(pnlBottom, BorderLayout.SOUTH);

        for (int i = 0; i < getSt.length; i++) {
            if (i + 1 == getSt.length) {
                listitem[i] = new itemTaskbar(getSt[i][1], getSt[i][0]);
                pnlBottom.add(listitem[i]);
            } else {
                listitem[i] = new itemTaskbar(getSt[i][1], getSt[i][0]);
                pnlCenter.add(listitem[i]);
                if (i != 0) {
                    if (!checkRole(getSt[i][2])) {
                        listitem[i].setVisible(false);
                    }
                }
            }
        }

        listitem[0].setBackground(HowerBackgroundColor);
        listitem[0].setForeground(HowerFontColor);
        listitem[0].isSelected = true;

        for (int i = 0; i < getSt.length; i++) {
            listitem[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent evt) {
                    pnlMenuTaskbarMousePress(evt);
                }
            });
        }

        // Bán hàng
        listitem[0].addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt) {
                System.out.println("BanHang");
            }
        });
        
        // Nhập hàng
        listitem[1].addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt) {
                System.out.println("NhapHang");
            }
        });
        
        // Sản phẩm
        listitem[2].addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                sanPham = new SanPham(main);
                main.setPanel(sanPham);
            }
        });
        
        // Thành phần
        listitem[3].addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt) {
                System.out.println("ThanhPhan");
            }
        });

        // Nhân viên
        listitem[4].addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt) {
                nhanVien = new NhanVien(main);
                main.setPanel(nhanVien);
            }
        });
        
        // Khách hàng
        listitem[5].addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt) {
                System.out.println("KhachHang");
            }
        });
        
        // Nhà cung cấp
        listitem[6].addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt) {
                nhacungcap = new NhaCungCap(main);
                main.setPanel(nhacungcap);
            }
        });
        
        // Khuyến mãi
        listitem[7].addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt) {
                System.out.println("KhuyenMai");
            }
        });

        // Phân công ca
        listitem[8].addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt) {
                System.out.println("PhanCongCa");
            }
        });
        
        // Tài khoản
        listitem[9].addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt) {
                taiKhoan = new TaiKhoan(main);
                main.setPanel(taiKhoan);
            }
        });
        
        // Phân quyền
        listitem[10].addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt) {
                phanQuyen = new PhanQuyen(main);
                main.setPanel(phanQuyen);
            }
        });

        // Thống kê
        listitem[11].addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt) {
                System.out.println("ThongKe");
            }
        });

        listitem[12].addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt) {

                int input = JOptionPane.showConfirmDialog(null,
                        "Bạn có chắc chắn muốn đăng xuất?", "Đăng xuất",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
                if (input == 0) {
                    Log_In login = new Log_In();
                    main.dispose();
                    login.setVisible(true);
                }
            }
        });
    }

    public boolean checkRole(String s) {
        boolean check = false;
        for (int i = 0; i < listQuyen.size(); i++) {
            if (listQuyen.get(i).getHanhDong().equals("view")) {
                if (s.equals(listQuyen.get(i).getMaCN())) {
                    check = true;
                    return check;
                }
            }
        }
        return check;
    }

    public void pnlMenuTaskbarMousePress(MouseEvent evt) {
        for (int i = 0; i < getSt.length; i++) {
            if (evt.getSource() == listitem[i]) {
                listitem[i].isSelected = true;
                listitem[i].setBackground(HowerBackgroundColor);
                listitem[i].setForeground(HowerFontColor);
            } else {
                listitem[i].isSelected = false;
                listitem[i].setBackground(DefaultColor);
                listitem[i].setForeground(FontColor);
            }
        }
    }
    
    public void resetChange(){
        this.nhanVienDTO = new NhanVienDAO().getById(nhanVienDTO.getMaNV());
    }
    
    public void in4(JPanel info) {
        JPanel pnlIcon = new JPanel(new FlowLayout());
        pnlIcon.setPreferredSize(new Dimension(60, 0));
        pnlIcon.setOpaque(false);
        info.add(pnlIcon, BorderLayout.WEST);
        JLabel lblIcon = new JLabel();
        lblIcon.setPreferredSize(new Dimension(50, 70));
        if (nhanVienDTO.getGioiTinh() == 1) {
            lblIcon.setIcon(new FlatSVGIcon("./images/icon/man_50px.svg"));
        } else {
            lblIcon.setIcon(new FlatSVGIcon("./images/icon/women_50px.svg"));
        }
        pnlIcon.add(lblIcon);

        JPanel pnlInfo = new JPanel();
        pnlInfo.setOpaque(false);
        pnlInfo.setLayout(new FlowLayout(0, 10, 5));
        pnlInfo.setBorder(new EmptyBorder(15, 0, 0, 0));
        info.add(pnlInfo, BorderLayout.CENTER);

        lblUsername = new JLabel(nhanVienDTO.getTenNV());
        lblUsername.putClientProperty("FlatLaf.style", "font: 150% $semibold.font");
        pnlInfo.add(lblUsername);

        lblTenQuyen = new JLabel(nhomQuyenDTO.getTenQuyen());
        lblTenQuyen.putClientProperty("FlatLaf.style", "font: 120% $light.font");
        lblTenQuyen.setForeground(Color.GRAY);
        pnlInfo.add(lblTenQuyen);

        lblIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt) {
                MyAccount ma = new MyAccount(owner, MenuTaskbar.this, "Chỉnh sửa thông tin tài khoản", true);
            }
        });
    }
}
