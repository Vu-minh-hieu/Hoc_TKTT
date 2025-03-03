
import java.util.Arrays;
import java.util.Comparator;

public class ThiSinh implements Comparable<ThiSinh> {
    private final String hoDem;
    private final String ten;
    private final VietDate ngaySinh;     
    private final float dtbcpt;

    public ThiSinh(String hoDem, String ten, VietDate ngaySinh, float dtbcpt) {
        this.hoDem = hoDem;
        this.ten = ten;
        this.ngaySinh = ngaySinh;
        this.dtbcpt = dtbcpt;
    }

    public ThiSinh(String ts) {
        String[] a = ts.split(";");
        if (a.length < 4) {
            throw new IllegalArgumentException("Dữ liệu không hợp lệ: " + ts);
        }
        this.hoDem = a[0].trim();
        this.ten = a[1].trim();
        this.ngaySinh = new VietDate(a[2].trim());
        this.dtbcpt = Float.parseFloat(a[3].trim());
    }
 
    public String hoDem() {
        return hoDem;
    }
    
    public String ten() {
        return ten;
    }
    
    public VietDate ngaySinh() {
        return ngaySinh;
    }

    public float dtbcpt() {
        return dtbcpt;
    }
    
    @Override
    public int hashCode() {
        int hash = 1;
        hash = 31 * hash + hoDem.hashCode();
        hash = 31 * hash + ten.hashCode();
        hash = 31 * hash + ngaySinh.hashCode();
        hash = 31 * hash + ((Float) dtbcpt).hashCode();
        return hash;
    }
    
    @Override
    public String toString() {
        return String.format("Họ đệm: %s, Tên: %s, Ngày sinh: %s, Điểm TBCPT: %.2f", 
                             hoDem, ten, ngaySinh, dtbcpt);
    }

    @Override
    public int compareTo(ThiSinh other) {
        int cmp = this.ten.compareToIgnoreCase(other.ten);
        if (cmp == 0) {
            return this.hoDem.compareToIgnoreCase(other.hoDem);
        }
        return cmp;
    }
    
    public static void main(String[] args) {
        ThiSinh[] a = new ThiSinh[5];
        a[0] = new ThiSinh("Nguyễn Văn;Hùng;15/03/2005;8.5");
        a[1] = new ThiSinh("Trần Thị;Lan;22/07/2005;7.8");
        a[2] = new ThiSinh("Lê Văn;Phong;10/11/2005;9.0");
        a[3] = new ThiSinh("Phạm Thị;Mai;05/09/2005;8.2");
        a[4] = new ThiSinh("Hoàng Văn;Dũng;30/12/2005;7.5");
        
        StdOut.println("Unsorted");
        for (int i = 0; i < a.length; i++)
            StdOut.println(a[i]);
        StdOut.println();
        
        StdOut.println("Sort by CompareTo");
        Arrays.sort(a);
        for (int i = 0; i < a.length; i++)
            StdOut.println(a[i]);
        StdOut.println();

    }
}
