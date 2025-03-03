
import java.util.Arrays;
import java.util.Comparator;

public class Student implements Comparable<Student> {
    private final String msv;
    private final String hoDem;
    private final String ten;
    private final VietDate ngaySinh;     
    private final String que; 
    private ST<Mon, Double> bangDiem;

    public Student(String msv, String hoDem, String ten, VietDate ngaySinh, String que) {
        this.msv = msv;
        this.hoDem = hoDem;
        this.ten = ten;
        this.ngaySinh = ngaySinh;
        this.que = que;
        bangDiem = new ST<>();
    }

    public Student(String student) {
        String[] a = student.split(";");
        if (a.length < 5) {
            throw new IllegalArgumentException("Dữ liệu không hợp lệ: " + student);
        }
        this.msv = a[0].trim();
        this.hoDem = a[1].trim();
        this.ten = a[2].trim();
        this.ngaySinh = new VietDate(a[3].trim());
        this.que = a[4].trim();
        bangDiem = new ST<>();
    }

    public String msv() {
        return msv;
    }
 
    public String hoDem() {
        return hoDem;
    }
    
    public String ten() {
        return ten;
    }
    
    public void addDiem(Mon tenMon, double diem){
        bangDiem.put(tenMon, diem);
    }
    
    public double diemTBC(){
        double sum = 0;
        int sumTC = 0;
        for (var m : bangDiem.keys()) {
            sum += bangDiem.get(m) * m.soTC();
            sumTC += m.soTC();
        }
        
        if (sumTC == 0){
            return 0;
        }
            
        return sum / sumTC;
    }
    
    public ST<Mon, Double> getBDiem() {
        return bangDiem;
    }
    
    public double TBHK(int ky){
        double sum = 0;
        int sumTC = 0;
        for (var m : bangDiem.keys()) {
            if (m.kyThu() == ky){
                sum += bangDiem.get(m) * m.soTC();
                sumTC += m.soTC();
            }
        }
        
        if (sumTC == 0){
            return 0;
        }
        
        return sum / sumTC;
    }
    
    @Override
    public String toString() {
        return String.format("MSV: %s, Họ đệm: %s, Tên: %s, Ngày sinh: %s, Quê quán: %s, Điểm TB: %.2f, TBHK 1: %.2f, TBHK 2: %.2f", 
                             msv, hoDem, ten, ngaySinh, que, diemTBC(), TBHK(1), TBHK(2));
    }

    @Override
    public int compareTo(Student other) {
        return this.msv.compareTo(other.msv);
    }

    public static class msvOrder implements Comparator<Student> {
        @Override
        public int compare(Student v, Student w) {
            return v.msv.compareTo(w.msv);
        }
    }
    
    public static class hoTenOrder implements Comparator<Student> {
        @Override
        public int compare(Student v, Student w) {
        int cmp = v.ten.compareToIgnoreCase(w.ten);
        if (cmp == 0) {
            return v.hoDem.compareToIgnoreCase(w.hoDem);
        }
        return cmp;
        }
    }
    
    public static void main(String[] args) {
        Student[] a = new Student[9];
        a[0] = new Student("SV002;Nguyen Van;C;01/01/2000;Hanoi");
        a[1] = new Student("SV001;Tran Thi;B;20/05/1999;Saigon");
        a[2] = new Student("SV003;Le Van;A;15/07/2005;Danang");
        a[3] = new Student("SV008;Vu Minh;C;01/01/2000;Hanoi");
        a[4] = new Student("SV006;Nguyen Thi;B;20/05/1999;Saigon");
        a[5] = new Student("SV004;Nguyen Tran;B;15/07/2005;Danang");
        a[6] = new Student("SV007;Nguyen Van;D;01/01/2000;Hanoi");
        a[7] = new Student("SV005;Do Trung;B;20/05/1999;Saigon");
        a[8] = new Student("SV009;Vo Thi;D;15/07/2005;Danang");
        Mon toan = new Mon("toan", 3, 1);
        Mon ly = new Mon("ly", 2, 2);
        Mon hoa = new Mon("hoa", 2, 1);
        a[1].addDiem(toan, 9);
        a[1].addDiem(ly, 8);
        a[1].addDiem(hoa, 4);
        a[2].addDiem(toan, 8);
        a[2].addDiem(ly, 7);
        a[2].addDiem(hoa, 6);
        
        StdOut.println("Unsorted");
        for (int i = 0; i < a.length; i++)
            StdOut.println(a[i]);
        StdOut.println();
        
        StdOut.println("Sort by CompareTo");
        Arrays.sort(a);
        for (int i = 0; i < a.length; i++)
            StdOut.println(a[i]);
        StdOut.println();
        
        StdOut.println("Sort by msv");
        Arrays.sort(a, new Student.msvOrder());
        for (int i = 0; i < a.length; i++)
            StdOut.println(a[i]);
        StdOut.println();
        
        StdOut.println("Sort by customer");
        Arrays.sort(a, new Student.hoTenOrder());
        for (int i = 0; i < a.length; i++)
            StdOut.println(a[i]);
        StdOut.println();

    }
}
