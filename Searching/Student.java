
import java.util.Arrays;
import java.util.Comparator;

public class Student implements Comparable<Student> {
    private final String msv;
    private final String hoDem;
    private final String ten;
    private final VietDate ngaySinh;     
    private final String que; 
    private double diemtb;

    public Student(String msv, String hoDem, String ten, VietDate ngaySinh, String que) {
        this.msv = msv;
        this.hoDem = hoDem;
        this.ten = ten;
        this.ngaySinh = ngaySinh;
        this.que = que;
    }

    public Student(String student) {
        String[] a = student.split(";");
        if (a.length < 5) {
            throw new IllegalArgumentException("Dữ liệu không hợp lệ: " + student);
        }
        this.msv = a[0];
        this.hoDem = a[1];
        this.ten = a[2];
        this.ngaySinh = new VietDate(a[3]);
        this.que = a[4]; 
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

    @Override
    public String toString() {
        return String.format("MSV: %s, Họ đệm: %s, Tên: %s, Ngày sinh: %s, Quê quán: %s", 
                             msv, hoDem, ten, ngaySinh, que);
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
        a[0] = new Student("SV002,Nguyen Van,C,01/01/2000,Hanoi");
        a[1] = new Student("SV001,Tran Thi,B,20/05/1999,Saigon");
        a[2] = new Student("SV003,Le Van,A,15/07/2005,Danang");
        a[3] = new Student("SV008,Vu Minh,C,01/01/2000,Hanoi");
        a[4] = new Student("SV006,Nguyen Thi,B,20/05/1999,Saigon");
        a[5] = new Student("SV004,Nguyen Tran,B,15/07/2005,Danang");
        a[6] = new Student("SV007,Nguyen Van,D,01/01/2000,Hanoi");
        a[7] = new Student("SV005,Do Trung,B,20/05/1999,Saigon");
        a[8] = new Student("SV009,Vo Thi,D,15/07/2005,Danang");
    

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
