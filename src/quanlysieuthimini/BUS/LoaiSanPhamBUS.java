/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package quanlysieuthimini.BUS;

import java.util.ArrayList;
import quanlysieuthimini.DTO.LoaiSanPhamDTO;
import quanlysieuthimini.DAO.LoaiSanPhamDAO;
    
public class LoaiSanPhamBUS {
    private final LoaiSanPhamDAO NccDAO = new LoaiSanPhamDAO();
    private ArrayList<LoaiSanPhamDTO> listNcc = new ArrayList<>();
    
    public LoaiSanPhamBUS() {
        this.listNcc = NccDAO.getAll();
    }

    public ArrayList<LoaiSanPhamDTO> getAll() {
        return this.listNcc;
    }

    public LoaiSanPhamDTO getByIndex(int index) {
        return this.listNcc.get(index);
    }

    public boolean add(LoaiSanPhamDTO ncc) {
        boolean check = NccDAO.insert(ncc);
        if (check) {
            this.listNcc.add(ncc);
        }
        return check;
    }
    
    public boolean delete(LoaiSanPhamDTO ncc, int index) {
        boolean check = NccDAO.delete(ncc.getMaLoai());
        if (check) {
            this.listNcc.remove(index);
        }
        return check;
    }

    public boolean update(LoaiSanPhamDTO ncc) {
        boolean check = NccDAO.update(ncc);
        if (check) {
            this.listNcc.set(getIndexByMaNCC(ncc.getMaLoai()), ncc);
        }
        return check;
    }
    
    public int getIndexByMaNCC(int maTL) {
        int i = 0;
        int vitri = -1;
        while (i < this.listNcc.size() && vitri == -1) {
            if (listNcc.get(i).getMaLoai() == maTL) {
                vitri = i;
            } else {
                i++;
            }
        }
        return vitri;
    }
    
    public String[] getArrTenLoai() {
        int size = listNcc.size();
        String[] result = new String[size];
        for (int i = 0; i < size; i++) {
            result[i] = listNcc.get(i).getTenLoai();
        }
        return result;
    }

    public String getTenLoai(int mancc) {
        return this.listNcc.get(getIndexByMaNCC(mancc)).getTenLoai();
    }
    
    public LoaiSanPhamDTO findCT(ArrayList<LoaiSanPhamDTO> ncc, String tenTL) {
        LoaiSanPhamDTO p = null;
        int i = 0;
        while (i < ncc.size() && p == null) {
            if (ncc.get(i).getTenLoai().equals(tenTL)) {
                p = ncc.get(i);
            } else {
                i++;
            }
        }
        return p;
    }
    
    public ArrayList<LoaiSanPhamDTO> search(String txt, String type) {
        ArrayList<LoaiSanPhamDTO> result = new ArrayList<>();
        txt = txt.toLowerCase();
        switch (type) {
            case "Tất cả" -> {
                for (LoaiSanPhamDTO i : listNcc) {
                    if (Integer.toString(i.getMaLoai()).contains(txt) || i.getTenLoai().contains(txt) || i.getCachBaoQuan().contains(txt)) {
                        result.add(i);
                    }
                }
            }
            case "Mã Loại" -> {
                for (LoaiSanPhamDTO i : listNcc) {
                    if (Integer.toString(i.getMaLoai()).contains(txt)) {
                        result.add(i);
                    }
                }
            }
            case "Tên Loại" -> {
                for (LoaiSanPhamDTO i : listNcc) {
                    if (i.getTenLoai().toLowerCase().contains(txt)) {
                        result.add(i);
                    }
                }
            }
            case "Cách Bảo Quản" -> {
                for (LoaiSanPhamDTO i : listNcc) {
                    if (i.getCachBaoQuan().toLowerCase().contains(txt)) {
                        result.add(i);
                    }
                }
            }
        }
        return result;
    }
}
