import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class menuSV
{
    private DSSV dssv;
    private List<BangDiem> dsMon;
    
    public menuSV() {
        this.dssv = new DSSV();
        dsMon = new ArrayList<>();
        
        In in = new In("sv.csv");
        while (in.hasNextLine()) {
            String line = in.readLine();
            Student sv = new Student(line);
            dssv.addSV(sv);
        }
        
        dsMon.add(new BangDiem("toan31.csv"));
        dsMon.add(new BangDiem("ly22.csv"));
        dsMon.add(new BangDiem("hoa21.csv"));
        
        for (BangDiem bd : dsMon) {
            bd.addDiem(dssv);
        }
        
    }

    public Student getsv(String masv){
        return dssv.getSV(masv);
    }
    
    public DSSV getdssv(){
        return dssv;
    }
    
    public double TKHKLop(int ky){
        double s = 0;
        int n = 0;
        for (var x : dssv.getDSSV().keys()) {
            Student sv = dssv.getDSSV().get(x);
            s += sv.TBHK(ky);
            n ++;
        }
        
        if (n == 0){
            return 0;
        }
        
        return s / n;
    }
    
    public static void main(String[] args) {
        menuSV menu = new menuSV();
        
        StdOut.println("SV can tim la: ");
        StdOut.println(menu.getsv("1006"));
 
        List<Student> students = new ArrayList<>(menu.getdssv().getAllSV());
        
        students.sort(new Student.msvOrder());
        StdOut.println("Sort by MSV");
        for (Student s : students) {
            StdOut.println(s);
        }
    
        students.sort(new Student.hoTenOrder());
        System.out.println("Sort by name");
        for (Student s : students) {
            System.out.println(s);
        }
        
        System.out.println("Tong ket HK 1 lop: " + menu.TKHKLop(1));
        System.out.println("Tong ket HK 2 lop: " + menu.TKHKLop(2));
    }
}
