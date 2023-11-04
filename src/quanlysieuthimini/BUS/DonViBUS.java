/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package quanlysieuthimini.BUS;

import quanlysieuthimini.DAO.DonViDAO;
import quanlysieuthimini.DTO.DonViDTO;
import java.util.ArrayList;

public class DonViBUS {
    private final DonViDAO NccDAO = new DonViDAO();
    private ArrayList<DonViDTO> listNcc = new ArrayList<>();
    
    public DonViBUS() {
        this.listNcc = NccDAO.getAll();
    }

    public ArrayList<DonViDTO> getAll() {
        return this.listNcc;
    }

    public DonViDTO getByIndex(int index) {
        return this.listNcc.get(index);
    }

    public boolean add(DonViDTO ncc) {
        boolean check = NccDAO.insert(ncc);
        if (check) {
            this.listNcc.add(ncc);
        }
        return check;
    }
    
    public boolean delete(DonViDTO ncc, int index) {
        boolean check = NccDAO.delete(ncc.getMaDV());
        if (check) {
            this.listNcc.remove(index);
        }
        return check;
    }

    public boolean update(DonViDTO ncc) {
        boolean check = NccDAO.update(ncc);
        if (check) {
            this.listNcc.set(getIndexByMaNCC(ncc.getMaDV()), ncc);
        }
        return check;
    }
    
    public int getIndexByMaNCC(int maTL) {
        int i = 0;
        int vitri = -1;
        while (i < this.listNcc.size() && vitri == -1) {
            if (listNcc.get(i).getMaDV() == maTL) {
                vitri = i;
            } else {
                i++;
            }
        }
        return vitri;
    }
    
    public String[] getArrTenDonVi() {
        int size = listNcc.size();
        String[] result = new String[size];
        for (int i = 0; i < size; i++) {
            result[i] = listNcc.get(i).getTenDV();
        }
        return result;
    }

    public String getTenDonVi(int mancc) {
        return this.listNcc.get(getIndexByMaNCC(mancc)).getTenDV();
    }
    
    public DonViDTO findCT(ArrayList<DonViDTO> ncc, String tenTL) {
        DonViDTO p = null;
        int i = 0;
        while (i < ncc.size() && p == null) {
            if (ncc.get(i).getTenDV().equals(tenTL)) {
                p = ncc.get(i);
            } else {
                i++;
            }
        }
        return p;
    }
    
    public ArrayList<DonViDTO> search(String txt, String type) {
        ArrayList<DonViDTO> result = new ArrayList<>();
        txt = txt.toLowerCase();
        switch (type) {
            case "Tất cả" -> {
                for (DonViDTO i : listNcc) {
                    if (Integer.toString(i.getMaDV()).contains(txt) || i.getTenDV().contains(txt)) {
                        result.add(i);
                    }
                }
            }
            case "Mã Đơn Vị" -> {
                for (DonViDTO i : listNcc) {
                    if (Integer.toString(i.getMaDV()).contains(txt)) {
                        result.add(i);
                    }
                }
            }
            case "Tên Đơn Vị" -> {
                for (DonViDTO i : listNcc) {
                    if (i.getTenDV().toLowerCase().contains(txt)) {
                        result.add(i);
                    }
                }
            }
        }
        return result;
    }
}
