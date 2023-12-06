/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package quanlysieuthimini.BUS;

import quanlysieuthimini.GUI.Panel.NhanVien;
import quanlysieuthimini.GUI.Dialog.NhanVienDialog;
import quanlysieuthimini.helper.Validation;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
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
import quanlysieuthimini.DAO.NhanVienDAO;
import quanlysieuthimini.DAO.TaiKhoanDAO;
import quanlysieuthimini.DTO.NhanVienDTO;
import quanlysieuthimini.helper.Formater;

public class NhanVienBUS implements ActionListener, DocumentListener {

    public NhanVien nv;
    private JTextField textField;
    public ArrayList<NhanVienDTO> listNv = NhanVienDAO.getInstance().selectAll();
    public NhanVienDAO nhanVienDAO = NhanVienDAO.getInstance();

    public NhanVienBUS() {
    }

    public NhanVienBUS(NhanVien nv) {
        this.nv = nv;
    }

    public NhanVienBUS(JTextField textField, NhanVien nv) {
        this.textField = textField;
        this.nv = nv;
    }

    public ArrayList<NhanVienDTO> getAll() {
        return this.listNv;
    }

    public NhanVienDTO getByIndex(int index) {
        return this.listNv.get(index);
    }

    public int getIndexById(int manv) {
        int i = 0;
        int vitri = -1;
        int size = this.listNv.size();
        while (i < size && vitri == -1) {
            if (this.listNv.get(i).getMaNV() == manv) {
                vitri = i;
            } else {
                i++;
            }
        }
        return vitri;
    }
    
    public NhanVienDTO getById(int manv) {
        return NhanVienDAO.getInstance().getById(manv);
    }
    
    public String getNameById(int manv) {
        return nhanVienDAO.getById(manv).getTenNV();
    }

    public String[] getArrTenNhanVien() {
        int size = listNv.size();
        String[] result = new String[size];
        for (int i = 0; i < size; i++) {
            result[i] = listNv.get(i).getTenNV();
        }
        return result;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String btn = e.getActionCommand();
        switch (btn) {
            case "THÊM" -> {
                NhanVienDialog nvthem = new NhanVienDialog(this, nv.owner, true, "Thêm nhân viên", "create");
            }
            case "SỬA" -> {
                int index = nv.getRow();
                if (index < 0) {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn nhân viên cần sửa");
                } else {
                    NhanVienDialog nvsua = new NhanVienDialog(this, nv.owner, true, "Sửa nhân viên", "update", nv.getNhanVien());
                }
            }
            case "XÓA" -> {
                if (nv.getRow() < 0) {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn nhân viên cần xóa");
                } else {
                    int input = JOptionPane.showConfirmDialog(null,
                        "Bạn có chắc chắn muốn xóa nhân viên này!", "Xóa nhân viên",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
                    if (input == 0) {
                        deleteNv(nv.getNhanVien());

                    }
                }
            }
            case "CHI TIẾT" -> {
                if (nv.getRow() < 0) {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn nhân viên cần xóa");
                } else {
                    NhanVienDialog nvsua = new NhanVienDialog(this, nv.owner, true, "Xem nhân viên", "detail", nv.getNhanVien());
                }
            }
            case "NHẬP EXCEL" -> {
                importExcel();
            }
            case "XUẤT EXCEL" -> {
                String[] header = new String[]{"MãNV", "Tên nhân viên", "Email nhân viên", "Số điên thoại", "Giới tính", "Ngày sinh"};
                exportExcel(listNv, header);
            }
        }
        nv.loadDataTalbe(listNv);
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
                    int id = NhanVienDAO.getInstance().getAutoIncrement();
                    String tennv = excelRow.getCell(1).getStringCellValue();
                    String email = excelRow.getCell(2).getStringCellValue();
                    String sdt = excelRow.getCell(3).getStringCellValue();
                    String gioitinh = excelRow.getCell(4).getStringCellValue();
                    if (gioitinh.equals("Nam") || gioitinh.equals("nam")) {
                        gt = 1;
                    } else {
                        gt = 0;
                    }
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ROOT);
                    String strNgaySinh = excelRow.getCell(5).getStringCellValue();
                    Date ngaysinh = format.parse(strNgaySinh);
                    java.sql.Date birth = new java.sql.Date(ngaysinh.getTime());
                    System.err.println("Ngay sinh: " + birth);
                    
                    if (Validation.isEmpty(tennv) || Validation.isEmpty(email)
                            || !Validation.isEmail(email) || Validation.isEmpty(sdt)
                            || Validation.isEmpty(sdt)
                            || sdt.length() < 10 || Validation.isEmpty(gioitinh)) {
                        check = 0;
                    }
                    else if(checkDup(tennv, email, sdt, gt)) {
                        check = 0;
                    }
                    if (check == 0) {
                        k += 1;
                    } else {
                        NhanVienDTO nvDTO = new NhanVienDTO(id, tennv, "", sdt, email, birth, gt, 0, "" , 1);
                        NhanVienDAO.getInstance().insert(nvDTO);
                        insertNv(nvDTO);
                    }
                }

                if (k != 0) {
                    JOptionPane.showMessageDialog(null, "Những dữ liệu không chuẩn không được thêm vào");
                } else {
                    JOptionPane.showMessageDialog(null, "Nhập dữ liệu thành công");            
                }
            } catch (FileNotFoundException ex) {
                System.out.println("Lỗi đọc file");
                err =1;
            } catch (IOException ex) {
                System.out.println("Lỗi IO");
                err=1;
            } catch (ParseException ex){
                System.out.println(ex);
                err=1;
            } catch (Exception ex){
                System.out.println("Lỗi read null");
                System.out.println(ex);
                err=1;
            }
            if(err != 0)
                JOptionPane.showMessageDialog(null, "Dữ liệu không hoàn thiện!\nHủy quá trình nhập, vui lòng kiểm tra lại dữ liệu");
            
        }
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        String text = textField.getText();
        if (text.length() == 0) {
            nv.loadDataTalbe(listNv);
        } else {
            ArrayList<NhanVienDTO> listSearch = search(text);
            searchLoadTable(listSearch);
        }
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        String text = textField.getText();
        if (text.length() == 0) {
            nv.loadDataTalbe(listNv);
        } else {
            ArrayList<NhanVienDTO> listSearch = search(text);
            searchLoadTable(listSearch);
        }
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
//        System.out.println("Text field changed: " + textField.getText());
    }

    public void insertNv(NhanVienDTO nv) {
        listNv.add(nv);
    }

    public void updateNv(int index, NhanVienDTO nv) {
        listNv.set(index, nv);
    }

    public int getIndex() {
        return nv.getRow();
    }

    public void deleteNv(NhanVienDTO nv) {
        NhanVienDAO.getInstance().delete(nv.getMaNV());
        TaiKhoanDAO.getInstance().delete(nv.getMaNV());
        listNv.removeIf(n -> (n.getMaNV() == nv.getMaNV()));
        loadTable();
    }

    public void loadTable() {
        nv.loadDataTalbe(listNv);
    }

    public void searchLoadTable(ArrayList<NhanVienDTO> list) {
        nv.loadDataTalbe(list);
    }

    public void openFile(String file) {
        try {
            File path = new File(file);
            Desktop.getDesktop().open(path);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void exportExcel(ArrayList<NhanVienDTO> list, String[] header) {
        try {
            if (!list.isEmpty()) {
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.setDialogTitle("Chọn đường dẫn lưu file Excel");
                FileNameExtensionFilter filter = new FileNameExtensionFilter("XLSX files", "xlsx");
                jFileChooser.setFileFilter(filter);
                jFileChooser.setAcceptAllFileFilterUsed(false);
                jFileChooser.showSaveDialog(nv.owner);
                File saveFile = jFileChooser.getSelectedFile();
                if (saveFile != null) {
                    if (!saveFile.toString().toLowerCase().endsWith(".xlsx")) {
                        saveFile = new File(saveFile.toString() + ".xlsx");
                    }
                    Workbook wb = new XSSFWorkbook();
                    Sheet sheet = wb.createSheet("Nhân viên");

                    writeHeader(header, sheet, 0);
                    int rowIndex = 1;
                    for (NhanVienDTO nv : list) {
                        Row row = sheet.createRow(rowIndex++);
                        writeNhanVien(nv, row);
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

    public ArrayList<NhanVienDTO> search(String text) {
        String luachon = (String) nv.search.cbxChoose.getSelectedItem();
        text = text.toLowerCase();
        ArrayList<NhanVienDTO> result = new ArrayList<>();
        switch (luachon) {
            case "Tất cả" -> {
                for (NhanVienDTO i : this.listNv) {
                    if (i.getTenNV().toLowerCase().contains(text) || i.getEmail().toLowerCase().contains(text)
                            || i.getSDT().toLowerCase().contains(text)) {
                        result.add(i);
                    }
                }
            }
            case "Họ tên" -> {
                for (NhanVienDTO i : this.listNv) {
                    if (i.getTenNV().toLowerCase().contains(text)) {
                        result.add(i);
                    }
                }
            }
            case "Email" -> {
                for (NhanVienDTO i : this.listNv) {
                    if (i.getEmail().toLowerCase().contains(text)
                           ) {
                        result.add(i);
                    }
                }
            }
            default ->
                throw new AssertionError();
        }

        return result;
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

    private static void writeNhanVien(NhanVienDTO nv, Row row) {
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
        cell.setCellValue(nv.getMaNV());

        cell = row.createCell(1);
        cell.setCellValue(nv.getTenNV());

        cell = row.createCell(2);
        cell.setCellValue(nv.getEmail());

        cell = row.createCell(3);
        cell.setCellValue(nv.getSDT());

        cell = row.createCell(4);
        cell.setCellValue(nv.getGioiTinh() == 1 ? "Nam" : "Nữ");

        cell = row.createCell(5);
        cell.setCellValue("" + nv.getNgaySinh());
    }

//    public void importExcel() {
//        File excelFile;
//        FileInputStream excelFIS = null;
//        BufferedInputStream excelBIS = null;
//        XSSFWorkbook excelJTableImport = null;
//        JFileChooser jf = new JFileChooser();
//        int result = jf.showOpenDialog(null);
//        jf.setDialogTitle("Open file");
//        Workbook workbook = null;
//        int k = 0;
//        if (result == JFileChooser.APPROVE_OPTION) {
//            try {
//                excelFile = jf.getSelectedFile();
//                excelFIS = new FileInputStream(excelFile);
//                excelBIS = new BufferedInputStream(excelFIS);
//                excelJTableImport = new XSSFWorkbook(excelBIS);
//                XSSFSheet excelSheet = excelJTableImport.getSheetAt(0);
//
//                for (int row = 1; row <= excelSheet.getLastRowNum(); row++) {
//                    int check = 1;
//                    int gt;
//                    XSSFRow excelRow = excelSheet.getRow(row);
//                    int id = NhanVienDAO.getInstance().getAutoIncrement();
//                    String tennv = excelRow.getCell(0).getStringCellValue();
//                    String gioitinh = excelRow.getCell(1).getStringCellValue();
//                    if (gioitinh.equals("Nam") || gioitinh.equals("nam")) {
//                        gt = 1;
//                    } else {
//                        gt = 0;
//                    }
//                    String sdt = excelRow.getCell(3).getStringCellValue();
//                    Date ngaysinh = (Date) excelRow.getCell(2).getDateCellValue();
//                    java.sql.Date birth = new java.sql.Date(ngaysinh.getTime());
//                    String email = excelRow.getCell(4).getStringCellValue();
//                    if (Validation.isEmpty(tennv) || Validation.isEmpty(email)
//                            || !Validation.isEmail(email) || Validation.isEmpty(sdt)
//                            || Validation.isEmpty(sdt) || !isPhoneNumber(sdt)
//                            || sdt.length() != 10 || Validation.isEmpty(gioitinh)) {
//                        check = 0;
//                    }
//                    if (check == 0) {
//                        k += 1;
//                    } else {
//                        NhanVienDTO nvdto = new NhanVienDTO(id, tennv, gt, birth, sdt, 1, email);
//                        NhanVienDAO.getInstance().insert(nvdto);
//                    }
//                    JOptionPane.showMessageDialog(null, "Nhập thành công");
//                }
//
//            } catch (FileNotFoundException ex) {
//                System.out.println("Lỗi đọc file");
//            } catch (IOException ex) {
//                System.out.println("Lỗi đọc file");
//            }
//        }
//        if (k != 0) {
//            JOptionPane.showMessageDialog(null, "Những dữ liệu không chuẩn không được thêm vào");
//        }
//    }
    
    public boolean checkDup(String tennv, String email, String sdt, int gt) {
        for (NhanVienDTO nvDTO : listNv) {
            if (nvDTO.getTenNV().equalsIgnoreCase(tennv)
                && nvDTO.getEmail().equalsIgnoreCase(email)
                && nvDTO.getSDT().equalsIgnoreCase(sdt)
                && nvDTO.getGioiTinh() == gt) {
                return true;
            }
        }
        return false;
    }

    public static boolean isPhoneNumber(String str) {
        // Loại bỏ khoảng trắng và dấu ngoặc đơn nếu có
        str = str.replaceAll("\\s+", "").replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("\\-", "");

        // Kiểm tra xem chuỗi có phải là một số điện thoại hợp lệ hay không
        if (str.matches("\\d{10}")) { // Kiểm tra số điện thoại 10 chữ số
            return true;
        } else if (str.matches("\\d{3}-\\d{3}-\\d{4}")) { // Kiểm tra số điện thoại có dấu gạch ngang
            return true;
        } else {
            return str.matches("\\(\\d{3}\\)\\d{3}-\\d{4}"); // Kiểm tra số điện thoại có dấu ngoặc đơn
        }        // Trả về false nếu chuỗi không phải là số điện thoại hợp lệ

    }
    
//    public static boolean isPhoneNumber(String str) {
//        String regExp = "/((09|03|07|08|05)+([0-9]{8})\b)/g";
//        
//    }
}
