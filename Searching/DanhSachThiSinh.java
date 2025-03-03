import java.util.List;
import java.util.ArrayList;


public class DanhSachThiSinh {
    private final SeparateChainingHashST<ThiSinh, ST<String, Float>> bangDiem;

    public DanhSachThiSinh() {
        bangDiem = new SeparateChainingHashST<>();
    }

    public void addThiSinh(ThiSinh thiSinh) {
        bangDiem.put(thiSinh, null);
    }

    public void add(ThiSinh thiSinh, String mon, float diem) {
        ST st = bangDiem.get(thiSinh);
        if (st == null) {
            st = new ST<>();
            st.put(mon, diem);
            bangDiem.put(thiSinh, st);
        } else {
            st.put(mon, diem);
        }
    }

    public void getDiemThiSinh(ThiSinh thiSinh) {
        var st = bangDiem.get(thiSinh);
        if (!bangDiem.contains(thiSinh)) {
            StdOut.println("Không tìm thấy thí sinh");
            return;
        }
        StdOut.print(thiSinh);
        for (var x : st.keys()){
            StdOut.print(" " + x + " " + st.get(x) + " ");
        }
        StdOut.println();
    }

    public List<ThiSinh> getAllSV() {
        List<ThiSinh> list = new ArrayList<>();
        for (ThiSinh key : bangDiem.keys()) {
            list.add(key);
        }
        return list;
    }
    
    public static void main(String[] args) {
        DanhSachThiSinh ds = new DanhSachThiSinh();
        In in = new In("thisinh.csv");
        while (in.hasNextLine()) {
            String line = in.readLine();
            String[] s = line.split(";");
            String ho = s[0];
            String ten = s[1];
            VietDate ns = new VietDate(s[2]);
            float diem = Float.parseFloat(s[3]);
            ThiSinh thiSinh = new ThiSinh(ho, ten, ns, diem);
            ds.add(thiSinh, "Toán", Float.parseFloat(s[4]));
            ds.add(thiSinh, "Lý", Float.parseFloat(s[5]));
            ds.add(thiSinh, "Hoá", Float.parseFloat(s[6]));
        }
        
        for (ThiSinh s : ds.getAllSV()) {
            ds.getDiemThiSinh(s);
        }
    }
}