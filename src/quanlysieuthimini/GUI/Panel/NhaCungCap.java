package quanlysieuthimini.GUI.Panel;

import quanlysieuthimini.GUI.Dialog.NhaCungCapDialog;
import quanlysieuthimini.BUS.NhaCungCapBUS;
import quanlysieuthimini.DAO.NhaCungCapDAO;
import quanlysieuthimini.DTO.NhaCungCapDTO;
import quanlysieuthimini.GUI.Component.IntegratedSearch;
import quanlysieuthimini.GUI.Component.MainFunction;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import quanlysieuthimini.GUI.Component.PanelBorderRadius;
import quanlysieuthimini.GUI.Main;
import quanlysieuthimini.helper.JTableExporter;
import quanlysieuthimini.helper.Validation;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import quanlysieuthimini.DTO.NhanVienDTO;

public final class NhaCungCap extends JPanel implements ActionListener, ItemListener {

    PanelBorderRadius main, functionBar;
    JPanel pnlBorder1, pnlBorder2, pnlBorder3, pnlBorder4, contentCenter;
    JTable tableNhaCungCap;
    JScrollPane scrollTableSanPham;
    MainFunction mainFunction;
    IntegratedSearch search;
    JFrame owner = (JFrame) SwingUtilities.getWindowAncestor(this);
    Color BackgroundColor = new Color(240, 247, 250);
    DefaultTableModel tblModel;
    Main m;
    public NhaCungCapBUS nccBUS = new NhaCungCapBUS();
    public ArrayList<NhaCungCapDTO> listncc = nccBUS.getAll();

    private void initComponent() {
        //Set model table
        tableNhaCungCap = new JTable();
        scrollTableSanPham = new JScrollPane();
        tblModel = new DefaultTableModel();
        String[] header = new String[]{"Mã NCC", "Tên nhà cung cấp", "Địa chỉ", "Email", "Số điện thoại"};
        tblModel.setColumnIdentifiers(header);
        tableNhaCungCap.setModel(tblModel);
        tableNhaCungCap.setFocusable(false);
        scrollTableSanPham.setViewportView(tableNhaCungCap);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        TableColumnModel columnModel = tableNhaCungCap.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(2);
        columnModel.getColumn(2).setPreferredWidth(300);
        
        columnModel.getColumn(0).setCellRenderer(centerRenderer);
        columnModel.getColumn(1).setCellRenderer(centerRenderer);
        columnModel.getColumn(2).setCellRenderer(centerRenderer);
        columnModel.getColumn(3).setCellRenderer(centerRenderer);
        columnModel.getColumn(4).setCellRenderer(centerRenderer);

        this.setBackground(BackgroundColor);
        this.setLayout(new BorderLayout(0, 0));
        this.setOpaque(true);

        // pnlBorder1, pnlBorder2, pnlBorder3, pnlBorder4 chỉ để thêm contentCenter ở giữa mà contentCenter không bị dính sát vào các thành phần khác
        pnlBorder1 = new JPanel();
        pnlBorder1.setPreferredSize(new Dimension(0, 10));
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

        // functionBar là thanh bên trên chứa các nút chức năng như thêm xóa sửa, và tìm kiếm
        functionBar = new PanelBorderRadius();
        functionBar.setPreferredSize(new Dimension(0, 100));
        functionBar.setLayout(new GridLayout(1, 2, 50, 0));
        functionBar.setBorder(new EmptyBorder(10, 10, 10, 10));

        String[] action = {"create", "update", "delete", "detail", "import", "export"};
        mainFunction = new MainFunction(m.user.getMaQuyen(), "nhacungcap", action);
        for (String ac : action) {
            mainFunction.btn.get(ac).addActionListener(this);
        }
        functionBar.add(mainFunction);

        search = new IntegratedSearch(new String[]{"Tất cả", "Mã nhà cung cấp", "Tên nhà cung cấp", "Địa chỉ", "Email", "Số điện thoại"});
        search.cbxChoose.addItemListener(this);
        search.txtSearchForm.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String type = (String) search.cbxChoose.getSelectedItem();
                String txt = search.txtSearchForm.getText();
                listncc = nccBUS.search(txt, type);
                loadDataTable(listncc);
            }
        });

        search.btnReset.addActionListener(this);
        functionBar.add(search);
        contentCenter.add(functionBar, BorderLayout.NORTH);
        // main là phần ở dưới để thống kê bảng biểu
        main = new PanelBorderRadius();
        BoxLayout boxly = new BoxLayout(main, BoxLayout.Y_AXIS);
        main.setLayout(boxly);
//        main.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentCenter.add(main, BorderLayout.CENTER);
        main.add(scrollTableSanPham);
    }

    public NhaCungCap(Main m) {
        this.m = m;
        initComponent();
        tableNhaCungCap.setDefaultEditor(Object.class, null);
        loadDataTable(listncc);
    }

    public void loadDataTable(ArrayList<NhaCungCapDTO> result) {
        tblModel.setRowCount(0);
        for (NhaCungCapDTO ncc : result) {
            tblModel.addRow(new Object[]{
                ncc.getMaNCC(), ncc.getTenNCC(), ncc.getDiaChi(), ncc.getEmail(), ncc.getSDT()
            });
        }
    }

    public void openFile(String file) {
        try {
            File path = new File(file);
            Desktop.getDesktop().open(path);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void importExcel() {
        File excelFile;
        FileInputStream excelFIS = null;
        BufferedInputStream excelBIS = null;
        XSSFWorkbook excelJTableImport = null;
        ArrayList<NhaCungCapDTO> listExcel = new ArrayList<NhaCungCapDTO>();
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
                    XSSFRow excelRow = excelSheet.getRow(row);
                    int id = NhaCungCapDAO.getInstance().getAutoIncrement();
                    String tenNCC = excelRow.getCell(1).getStringCellValue();
                    String diachi = excelRow.getCell(2).getStringCellValue();
                    String email = excelRow.getCell(3).getStringCellValue();
                    String sdt = excelRow.getCell(4).getStringCellValue();
                    
                    NhaCungCapDTO nccDTO = new NhaCungCapDTO(id, tenNCC, diachi, email, sdt);
                    
                    if (Validation.isEmpty(tenNCC) || Validation.isEmpty(email)
                            || !Validation.isEmail(email) || Validation.isEmpty(sdt) || !isPhoneNumber(sdt)
                            || sdt.length() != 10 || Validation.isEmpty(diachi)) {
                        check = 0;
                    }
                    else if(nccBUS.checkDup(nccDTO)) {
                        check = 0;
                    }
                    
                    if (check == 0) {
                        k += 1;
                    } else {
                        nccBUS.add(new NhaCungCapDTO(id, tenNCC, diachi, email, sdt));
                    }
                }
                if (k != 0) {
                    JOptionPane.showMessageDialog(this, "Những dữ liệu không chuẩn không được thêm vào");
                } else {
                    JOptionPane.showMessageDialog(this, "Nhập dữ liệu thành công");
                }
            } catch (FileNotFoundException ex) {
                System.out.println("Lỗi đọc file");
                err = 1;
            } catch (IOException ex) {
                System.out.println("lỗi IO");
                err = 1;
            } catch (Exception ex){
                System.out.println("Lỗi read null");
                System.out.println(ex);
                err = 1;
            }
            if(err != 0)
                JOptionPane.showMessageDialog(null, "Dữ liệu không hoàn thiện!\nHủy quá trình nhập, vui lòng kiểm tra lại dữ liệu");
        }

        loadDataTable(listncc);
    }
    
    public void exportExcel(ArrayList<NhaCungCapDTO> list, String[] header) {
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
                    Sheet sheet = wb.createSheet("Nhà Cung Cấp");

                    writeHeader(header, sheet, 0);
                    int rowIndex = 1;
                    for (NhaCungCapDTO ncc : list) {
                        Row row = sheet.createRow(rowIndex++);
                        writeNhaCungCap(ncc, row);
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
    public int getRowSelected() {
        int index = tableNhaCungCap.getSelectedRow();
        if (index == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhà cung cấp");
        }
        return index;
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
        org.apache.poi.ss.usermodel.Font font = sheet.getWorkbook().createFont();
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
    private static void writeNhaCungCap(NhaCungCapDTO ncc, Row row) {
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
        cell.setCellValue(ncc.getMaNCC());

        cell = row.createCell(1);
        cell.setCellValue(ncc.getTenNCC());

        cell = row.createCell(2);
        cell.setCellValue(ncc.getEmail());

        cell = row.createCell(3);
        cell.setCellValue(ncc.getDiaChi());

        cell = row.createCell(4);
        cell.setCellValue(ncc.getEmail());

        cell = row.createCell(5);
        cell.setCellValue("" + ncc.getSDT());
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == mainFunction.btn.get("create")) {
            NhaCungCapDialog dvtDialog = new NhaCungCapDialog(this, owner, "Thêm nhà cung cấp", true, "create");
        } else if (e.getSource() == mainFunction.btn.get("update")) {
            int index = getRowSelected();
            if (index != -1) {
                NhaCungCapDialog nccDialog = new NhaCungCapDialog(this, owner, "Chỉnh sửa nhà cung cấp", true, "update", listncc.get(index));
            }
        } else if (e.getSource() == mainFunction.btn.get("delete")) {
            int index = getRowSelected();
            if (index != -1) {
                int input = JOptionPane.showConfirmDialog(null,
                        "Bạn có chắc chắn muốn xóa nhà cung cấp!", "Xóa nhà cung cấp",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
                if (input == 0) {
                    nccBUS.delete(listncc.get(index), index);
                    loadDataTable(listncc);
                }
            }
        } else if (e.getSource() == mainFunction.btn.get("detail")) {
            int index = getRowSelected();
            if (index != -1) {
                NhaCungCapDialog nccDialog = new NhaCungCapDialog(this, owner, "Chi tiết nhà cung cấp", true, "view", listncc.get(index));
            }
        } else if (e.getSource() == search.btnReset) {
            search.txtSearchForm.setText("");
            listncc = nccBUS.getAll();
            loadDataTable(listncc);
        } else if (e.getSource() == mainFunction.btn.get("import")) {
            importExcel();
        } else if (e.getSource() == mainFunction.btn.get("export")) {
            try {
                JTableExporter.exportJTableToExcel(tableNhaCungCap);
            } catch (IOException ex) {
                Logger.getLogger(NhaCungCap.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public static boolean isPhoneNumber(String str) {
        // Loại bỏ khoảng trắng và dấu ngoặc đơn nếu có
        str = str.replaceAll("\\s+", "").replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("\\-", "");

        // Kiểm tra xem chuỗi có phải là một số điện thoại hợp lệ hay không
        if (str.matches("\\d{10}")) { // Kiểm tra số điện thoại 10 chữ số
            return true;
        } else if (str.matches("\\d{3}-\\d{3}-\\d{4}")) { // Kiểm tra số điện thoại có dấu gạch ngang
            return true;
        } else if (str.matches("\\(\\d{3}\\)\\d{3}-\\d{4}")) { // Kiểm tra số điện thoại có dấu ngoặc đơn
            return true;
        } else {
            return false; // Trả về false nếu chuỗi không phải là số điện thoại hợp lệ
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        String type = (String) search.cbxChoose.getSelectedItem();
        String txt = search.txtSearchForm.getText();
        listncc = nccBUS.search(txt, type);
        loadDataTable(listncc);
    }
}
