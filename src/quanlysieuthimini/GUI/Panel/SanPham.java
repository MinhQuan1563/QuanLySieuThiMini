package quanlysieuthimini.GUI.Panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import quanlysieuthimini.BUS.HangSanXuatBUS;
import quanlysieuthimini.BUS.LoaiSanPhamBUS;
import static quanlysieuthimini.BUS.NhanVienBUS.isPhoneNumber;
import quanlysieuthimini.BUS.SanPhamBUS;
import quanlysieuthimini.DAO.NhanVienDAO;
import quanlysieuthimini.DAO.SanPhamDAO;
import quanlysieuthimini.DTO.NhanVienDTO;
import quanlysieuthimini.DTO.SanPhamDTO;
import quanlysieuthimini.GUI.Main;
import quanlysieuthimini.GUI.Component.IntegratedSearch;
import quanlysieuthimini.GUI.Component.MainFunction;
import quanlysieuthimini.GUI.Component.PanelBorderRadius;
import quanlysieuthimini.GUI.Component.TableSorter;
import quanlysieuthimini.helper.Formater;
import quanlysieuthimini.helper.JTableExporter;
import quanlysieuthimini.helper.Validation;

public final class SanPham extends JPanel implements ActionListener, ItemListener{
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
    HangSanXuatBUS hangsxBUS = new HangSanXuatBUS();
    LoaiSanPhamBUS loaispBUS = new LoaiSanPhamBUS();

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

        search = new IntegratedSearch(new String[] {"Tất cả", "Mã sản phẩm", "Tên sản phẩm", "Loại sản phẩm", "Hãng sản xuất"});
        search.cbxChoose.addItemListener(this);
        search.txtSearchForm.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String type = (String) search.cbxChoose.getSelectedItem();
                String txt = search.txtSearchForm.getText();
                loadDataTable(search(txt,type));
                if(txt.equals("")){
                    loadDataTable(listsp);
                }
                    
            }

        });

        search.btnReset.addActionListener((ActionEvent e) -> {
            search.txtSearchForm.setText("");
            listsp = spBUS.getAll();
            loadDataTable(listsp);
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
        String[] header = new String[] {"Mã sản phẩm", "Tên sản phẩm", "Loại", "Đơn giá", "Số lượng", "Hãng sản xuất"};
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
        loadDataTable(listsp);
    }

    public int getRow(){
        return tableSanPham.getSelectedRow();
    }

    public SanPhamDTO getSanPham(){
        return listsp.get(tableSanPham.getSelectedRow());
    }
    public ArrayList<SanPhamDTO> search(String text, String type) {
        text = text.toLowerCase();
        ArrayList<SanPhamDTO> result = new ArrayList<>();
        switch (type) {
            case "Tất cả" -> {
                for (SanPhamDTO i : listsp) {
                    if (Integer.toString(i.getMaSP()).toLowerCase().contains(text) || i.getTenSP().toLowerCase().contains(text) 
                       || hangsxBUS.getTenHang(i.getMaHang()).toLowerCase().contains(text) 
                       || loaispBUS.getTenLoai(i.getMaLoai()).toLowerCase().contains(text))  {
                        result.add(i);
                    }
                }
            }
            case "Mã sản phẩm" -> {
                for (SanPhamDTO i : listsp) {
                    if (Integer.toString(i.getMaSP()).toLowerCase().contains(text) ) {
                        result.add(i);
                    }
                }
            }
            case "Tên sản phẩm" -> {
                for (SanPhamDTO i : listsp) {
                    if (i.getTenSP().toLowerCase().contains(text)) {
                        result.add(i);
                    }
                }
            }
            case "Loại sản phẩm" -> {
                for (SanPhamDTO i : listsp) {
                    if (loaispBUS.getTenLoai(i.getMaLoai()).toLowerCase().contains(text)) {
                        result.add(i);
                    }
                }
            }
            case "Hãng sản xuất" -> {
                for (SanPhamDTO i : listsp) {
                    if (hangsxBUS.getTenHang(i.getMaHang()).toLowerCase().contains(text)) {
                        result.add(i);
                    }
                }
            }
        }
        return result;
    }

    public void loadDataTable(ArrayList<SanPhamDTO> list){
        tblModel.setRowCount(0);
        for(SanPhamDTO sanpham : list){
            tblModel.addRow(new Object[]{
                sanpham.getMaSP(), 
                sanpham.getTenSP(),
                loaispBUS.getTenLoai(sanpham.getMaLoai()),
                Formater.FormatVND(sanpham.getDonGia()), 
                sanpham.getSoLuong(), 
                hangsxBUS.getTenHang(sanpham.getMaHang())
            });
        }
    }
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
                    loadDataTable(listsp);
                }
            }
        } else if (e.getSource() == mainFunction.btn.get("detail")) {
            int index = getRowSelected();
            if (index != -1) {
                SanPhamDialog spDialog = new SanPhamDialog(this, owner, "Xem chi tiết sản phẩm", true, "view", listsp.get(index));
            }
        }
        else if (e.getSource() == mainFunction.btn.get("export")) {
            try {
                JTableExporter.exportJTableToExcel(tableSanPham);
            } catch (IOException ex) {
                Logger.getLogger(SanPham.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if(e.getSource() == mainFunction.btn.get("import")) {
            importExcel();
//            JOptionPane.showMessageDialog(this, "Chức năng không khả dụng !", "Thông báo", JOptionPane.WARNING_MESSAGE);
        }
    }
    @Override
    public void itemStateChanged(ItemEvent e) {
        String type = (String) search.cbxChoose.getSelectedItem();
        String txt = search.txtSearchForm.getText();
        loadDataTable(search(txt, type));
                
    }
    public int getRowSelected(){
        int index = tableSanPham.getSelectedRow();
        if(index == -1){
            JOptionPane.showMessageDialog(this,"Vui lòng chọn sản phẩm");
        }
        return index;
    }
    public void importExcel(){
        DateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
        File excelFile;
        FileInputStream excelFIS = null;
        BufferedInputStream excelBIS = null;
        XSSFWorkbook excelJTableImport = null;
        JFileChooser jf = new JFileChooser();
        jf.setDialogTitle("Open file");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("XLSX files", "xlsx");
        jf.setFileFilter(filter);
        jf.setAcceptAllFileFilterUsed(false);
        int result = jf.showOpenDialog(null);
        Workbook workbook = null;
        int k = 0;
        int err = 0;
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                excelFile = jf.getSelectedFile();
                excelFIS = new FileInputStream(excelFile);
                excelBIS = new BufferedInputStream(excelFIS);
                excelJTableImport = new XSSFWorkbook(excelBIS);
                XSSFSheet excelSheet = excelJTableImport.getSheetAt(0);

                for (int row = 1; row <= excelSheet.getLastRowNum(); row++) {
                    int check = 1;
                    int gt;
                    XSSFRow excelRow = excelSheet.getRow(row);
                    int masp = SanPhamDAO.getInstance().getAutoIncrement();
                    String maloaisp = excelRow.getCell(1).getStringCellValue();
//                    ,mahangsx,madonvi,vtensp,vmavach,vsoluong,vdungtich,vdongia,vngaySX, vhanSD,vhinhanh
                    String mahangsx = excelRow.getCell(2).getStringCellValue();
                    String madonvi = excelRow.getCell(3).getStringCellValue();
                    String mavach = excelRow.getCell(4).getStringCellValue();
                    String vtensp = excelRow.getCell(5).getStringCellValue();
                    String dongia = excelRow.getCell(6).getStringCellValue();
                    String soluong = excelRow.getCell(7).getStringCellValue();
                    String dungtich = excelRow.getCell(8).getStringCellValue();
                    String strngaySX = excelRow.getCell(9).getStringCellValue();
                    String strhanSD = excelRow.getCell(10).getStringCellValue();
                    String vhinhanh = excelRow.getCell(11).getStringCellValue();
                    Date ngaySX = (Date) formatter.parse(strngaySX);
                    Date hanSD = (Date) formatter.parse(strhanSD);
                    java.sql.Date vngaySX = new java.sql.Date(ngaySX.getTime());
                    java.sql.Date vhanSD = new java.sql.Date(hanSD.getTime());
                  
                    int vmaloaisp = Integer.parseInt(maloaisp);
                    int vmahangsx = Integer.parseInt(mahangsx);
                    int vmadonvi = Integer.parseInt(madonvi);
                    int vsoluong = Integer.parseInt(soluong);
                    int vdungtich = Integer.parseInt(dungtich);
                    double vdongia = Double.parseDouble(dongia);
                    if (!Validation.isNumber(maloaisp) || !Validation.isNumber(mahangsx)
                        || !Validation.isNumber(madonvi)  || Validation.isEmpty(vtensp)
                        || !Validation.isNumber(soluong)
                        || !Validation.isNumber(dungtich)  || !Validation.isNumber(dongia)
                        || Validation.isEmpty(strngaySX)  || Validation.isEmpty(strhanSD)){
                        check = 0;
                    }else           //int MaLoai, int MaHang, int MaDV, String TenSP, String MaVach, int SoLuong, int DungTich, double DonGia, Date NgaySanXuat, Date HanSuDung
                        if(checkDup(vmaloaisp,vmahangsx,vmadonvi,vtensp,mavach,vsoluong,vdungtich,vdongia)){
                            check = 0;
                            System.out.println("duplicate " + check);
                        }
                    if (check == 0) {
                        k += 1;
                    } else {
                        SanPhamDTO spDTO = new SanPhamDTO(masp,vmaloaisp,vmahangsx,vmadonvi,vtensp,mavach,vsoluong,vdungtich,vdongia,vngaySX, vhanSD,vhinhanh,1);
                        spBUS.add(spDTO);
                    }
                }
                if (k != 0) {
                    JOptionPane.showMessageDialog(null, "Những dữ liệu không chuẩn không được thêm vào");
                } else {
                    JOptionPane.showMessageDialog(null, "Nhập dữ liệu thành công");
                }

            } catch (FileNotFoundException ex) {
                System.out.println("Lỗi đọc file");
                System.out.println(ex);
                err = 1;
            } catch (IOException ex) {
                System.out.println("Lỗi input output");
                System.out.println(ex);
                err = 1;
            } catch (ParseException ex){
                System.out.println("Lỗi parse date");
                System.out.println(ex);
                err = 1;
            } catch (Exception ex){
                System.out.println("Lỗi read null");
                System.out.println(ex);
                err = 1;
            }
            if(err != 0)
                JOptionPane.showMessageDialog(null, "Dữ liệu không hoàn thiện!\nHủy quá trình nhập, vui lòng kiểm tra lại dữ liệu");
        }
        loadDataTable(listsp);
    }
    public boolean checkDup(int MaLoai, int MaHang, int MaDV, String TenSP, String MaVach, int SoLuong, int DungTich, double DonGia) {
        for (SanPhamDTO sp : listsp) {
            if (sp.getMaLoai() == MaLoai
                && sp.getMaHang()== MaHang
                && sp.getMaDV()== MaDV
                && sp.getTenSP().equalsIgnoreCase(TenSP)
                && sp.getMaVach().equalsIgnoreCase(MaVach)
                && sp.getSoLuong()== SoLuong
                && sp.getDungTich()== DungTich
                && sp.getDonGia()== DonGia){              
                
                return true;
            }
                System.out.println(sp.toString());
        }
        return false;
    }
    public void exportExcel(ArrayList<SanPhamDTO> list, String[] header) {
        try {
            if (!list.isEmpty()) {
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.setDialogTitle("Chọn đường dẫn lưu file Excel");
                FileNameExtensionFilter filter = new FileNameExtensionFilter("XLSX files", "xlsx");
                jFileChooser.setFileFilter(filter);
                jFileChooser.setAcceptAllFileFilterUsed(false);
                jFileChooser.showSaveDialog(owner);
                File saveFile = jFileChooser.getSelectedFile();
                if (saveFile != null) {
                    if (!saveFile.toString().toLowerCase().endsWith(".xlsx")) {
                        saveFile = new File(saveFile.toString() + ".xlsx");
                    }
                    Workbook wb = new XSSFWorkbook();
                    Sheet sheet = wb.createSheet("Sản phẩm");

                    writeHeader(header, sheet, 0);
                    int rowIndex = 1;
                    for (SanPhamDTO sp : list) {
                        Row row = sheet.createRow(rowIndex++);
                        writeSanPham(sp, row);
                    }
                    FileOutputStream out = new FileOutputStream(new File(saveFile.toString()));
                    wb.write(out);
                    wb.close();
                    out.close();
                    openFile(saveFile.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void writeHeader(String[] list, Sheet sheet, int rowIndex) {
        CellStyle cellStyle = createStyleForHeader(sheet);
        Row row = sheet.createRow(rowIndex);
        Cell cell;
        for (int i = 0; i < list.length; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(list[i]);
            sheet.autoSizeColumn(i);
        }
    }
    private static CellStyle createStyleForHeader(Sheet sheet) {
        // Create font
        Font font = sheet.getWorkbook().createFont();
        font.setFontName("Times New Roman");
        font.setBold(true);
        font.setFontHeightInPoints((short) 14); // font size
        font.setColor(IndexedColors.WHITE.getIndex()); // text color

        // Create CellStyle
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        return cellStyle;
    }

    private static void writeSanPham(SanPhamDTO nv, Row row) {
        CellStyle cellStyleFormatNumber = null;
        if (cellStyleFormatNumber == null) {
            // Format number
            short format = (short) BuiltinFormats.getBuiltinFormat("#,##0");
            // DataFormat df = workbook.createDataFormat();
            // short format = df.getFormat("#,##0");

            //Create CellStyle
            Workbook workbook = row.getSheet().getWorkbook();
            cellStyleFormatNumber = workbook.createCellStyle();
            cellStyleFormatNumber.setDataFormat(format);
        }
        Cell cell = row.createCell(0);
        cell.setCellValue(nv.getMaSP());

        cell = row.createCell(1);
        cell.setCellValue(nv.getMaLoai());

        cell = row.createCell(2);
        cell.setCellValue(nv.getMaHang());

        cell = row.createCell(3);
        cell.setCellValue(nv.getMaDV());

        cell = row.createCell(4);
        cell.setCellValue(nv.getMaVach());

        cell = row.createCell(5);
        cell.setCellValue("" + nv.getTenSP());
        
        cell = row.createCell(6);
        cell.setCellValue("" + nv.getDonGia());
        
        cell = row.createCell(7);
        cell.setCellValue("" + nv.getSoLuong());
        
        cell = row.createCell(8);
        cell.setCellValue("" + nv.getDungTich());
        
        cell = row.createCell(9);
        cell.setCellValue("" + nv.getNgaySanXuat());
        
        cell = row.createCell(10);
        cell.setCellValue("" + nv.getHanSuDung());
    }
    public void openFile(String file) {
        try {
            File path = new File(file);
            Desktop.getDesktop().open(path);
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
