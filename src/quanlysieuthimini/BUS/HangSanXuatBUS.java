/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package quanlysieuthimini.BUS;

import quanlysieuthimini.DAO.HangSanXuatDAO;
import quanlysieuthimini.DTO.HangSanXuatDTO;
import java.util.ArrayList;

public class HangSanXuatBUS {
    private final HangSanXuatDAO NccDAO = new HangSanXuatDAO();
    private ArrayList<HangSanXuatDTO> listNcc = new ArrayList<>();
    public static HangSanXuatBUS getInstance(){
        return new HangSanXuatBUS();
    }
    public HangSanXuatBUS() {
        this.listNcc = NccDAO.getAll();
    }

    public ArrayList<HangSanXuatDTO> getAll() {
        return this.listNcc;
    }

    public HangSanXuatDTO getByIndex(int index) {
        return this.listNcc.get(index);
    }

    public boolean add(HangSanXuatDTO ncc) {
        boolean check = NccDAO.insert(ncc);
        if (check) {
            this.listNcc.add(ncc);
        }
        return check;
    }
    
    public boolean delete(HangSanXuatDTO ncc, int index) {
        boolean check = NccDAO.delete(ncc.getMaHang());
        if (check) {
            this.listNcc.remove(index);
        }
        return check;
    }

    public boolean update(HangSanXuatDTO ncc) {
        boolean check = NccDAO.update(ncc);
        if (check) {
            this.listNcc.set(getIndexByMaNCC(ncc.getMaHang()), ncc);
        }
        return check;
    }
    
    public int getIndexByMaNCC(int maTL) {
        int i = 0;
        int vitri = -1;
        while (i < this.listNcc.size() && vitri == -1) {
            if (listNcc.get(i).getMaHang() == maTL) {
                vitri = i;
            } else {
                i++;
            }
        }
        return vitri;
    }
    
    public String[] getArrTenHang() {
        int size = listNcc.size();
        String[] result = new String[size];
        for (int i = 0; i < size; i++) {
            result[i] = listNcc.get(i).getTenHang();
        }
        return result;
    }

    public String getTenNhaHang(int mancc) {
        return this.listNcc.get(getIndexByMaNCC(mancc)).getTenHang();
    }
    
    public HangSanXuatDTO findCT(ArrayList<HangSanXuatDTO> ncc, String tenTL) {
        HangSanXuatDTO p = null;
        int i = 0;
        while (i < ncc.size() && p == null) {
            if (ncc.get(i).getTenHang().equals(tenTL)) {
                p = ncc.get(i);
            } else {
                i++;
            }
        }
        return p;
    }
    
    public ArrayList<HangSanXuatDTO> search(String txt, String type) {
        ArrayList<HangSanXuatDTO> result = new ArrayList<>();
        txt = txt.toLowerCase();
        switch (type) {
            case "Tất cả" -> {
                for (HangSanXuatDTO i : listNcc) {
                    if (Integer.toString(i.getMaHang()).contains(txt) || i.getTenHang().contains(txt)) {
                        result.add(i);
                    }
                }
            }
            case "Mã Hãng" -> {
                for (HangSanXuatDTO i : listNcc) {
                    if (Integer.toString(i.getMaHang()).contains(txt)) {
                        result.add(i);
                    }
                }
            }
            case "Tên Hãng" -> {
                for (HangSanXuatDTO i : listNcc) {
                    if (i.getTenHang().toLowerCase().contains(txt)) {
                        result.add(i);
                    }
                }
            }
        }
        return result;
    }
}
