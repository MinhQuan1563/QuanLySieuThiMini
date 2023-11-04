/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package quanlysieuthimini.BUS;

import java.util.ArrayList;
import quanlysieuthimini.DAO.NhaCungCapDAO;
import quanlysieuthimini.DTO.NhaCungCapDTO;

public class NhaCungCapBUS {

    private final NhaCungCapDAO NccDAO = new NhaCungCapDAO();
    private ArrayList<NhaCungCapDTO> listNcc = new ArrayList<>();

    public NhaCungCapBUS() {
        this.listNcc = NccDAO.getAll();
    }

    public static NhaCungCapBUS getInstance(){
        return new NhaCungCapBUS();
    }

    public ArrayList<NhaCungCapDTO> getAll() {
        return this.listNcc;
    }

    public NhaCungCapDTO getByIndex(int index) {
        return this.listNcc.get(index);
    }

    public boolean add(NhaCungCapDTO ncc) {
        boolean check = NccDAO.insert(ncc);
        if (check) {
            this.listNcc.add(ncc);
        }
        return check;
    }

    public boolean delete(NhaCungCapDTO ncc, int index) {
        boolean check = NccDAO.delete(ncc.getMaNCC());
        if (check) {
            this.listNcc.remove(index);
        }
        return check;
    }

    public boolean update(NhaCungCapDTO ncc) {
        boolean check = NccDAO.update(ncc);
        if (check) {
            this.listNcc.set(getIndexByMaNCC(ncc.getMaNCC()), ncc);
        }
        return check;
    }

    public int getIndexByMaNCC(int mancc) {
        int i = 0;
        int vitri = -1;
        while (i < this.listNcc.size() && vitri == -1) {
            if (listNcc.get(i).getMaNCC() == mancc) {
                vitri = i;
            } else {
                i++;
            }
        }
        return vitri;
    }

    public ArrayList<NhaCungCapDTO> search(String txt, String type) {
        ArrayList<NhaCungCapDTO> result = new ArrayList<>();
        txt = txt.toLowerCase();
        switch (type) {
            case "Tất cả" -> {
                for (NhaCungCapDTO i : listNcc) {
                    if (Integer.toString(i.getMaNCC()).contains(txt) || i.getTenNCC().contains(txt) || i.getDiaChi().contains(txt) || i.getEmail().contains(txt) || i.getSDT().contains(txt)) {
                        result.add(i);
                    }
                }
            }
            case "Mã nhà cung cấp" -> {
                for (NhaCungCapDTO i : listNcc) {
                    if (Integer.toString(i.getMaNCC()).contains(txt)) {
                        result.add(i);
                    }
                }
            }
            case "Tên nhà cung cấp" -> {
                for (NhaCungCapDTO i : listNcc) {
                    if (i.getTenNCC().toLowerCase().contains(txt)) {
                        result.add(i);
                    }
                }
            }
            case "Địa chỉ" -> {
                for (NhaCungCapDTO i : listNcc) {
                    if (i.getDiaChi().toLowerCase().contains(txt)) {
                        result.add(i);
                    }
                }
            }
            case "Số điện thoại" -> {
                for (NhaCungCapDTO i : listNcc) {
                    if (i.getSDT().toLowerCase().contains(txt)) {
                        result.add(i);
                    }
                }
            }
            case "Email" -> {
                for (NhaCungCapDTO i : listNcc) {
                    if (i.getEmail().toLowerCase().contains(txt)) {
                        result.add(i);
                    }
                }
            }
        }
        return result;
    }

    public String[] getArrTenNhaCungCap() {
        int size = listNcc.size();
        String[] result = new String[size];
        for (int i = 0; i < size; i++) {
            result[i] = listNcc.get(i).getTenNCC();
        }
        return result;
    }

    public String getTenNhaCungCap(int mancc) {
        return this.listNcc.get(getIndexByMaNCC(mancc)).getTenNCC();
    }

    public NhaCungCapDTO findCT(ArrayList<NhaCungCapDTO> ncc, String tenncc) {
        NhaCungCapDTO p = null;
        int i = 0;
        while (i < ncc.size() && p == null) {
            if (ncc.get(i).getTenNCC().equals(tenncc)) {
                p = ncc.get(i);
            } else {
                i++;
            }
        }
        return p;
    }
}
