import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class menuSV
{
    private menuSV() {}

    public static void main(String[] args) {
        QlySV qlSV = new QlySV();
        String filename = "sv.csv";
       
        /*try (Scanner sc = new Scanner(new File(filename))) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                qlSV.addSV(new Student(line));
            }
        } catch (FileNotFoundException e) {
            System.err.println("Lỗi: Không tìm thấy file " + filename);
            e.printStackTrace();
            return;
        }*/
        In in = new In(filename);
        while (in.hasNextLine()) {
            String line = in.readLine();
            Student sv = new Student(line);
            qlSV.addSV(sv);
        }
        
        StdOut.println("SV can tim la: ");
        qlSV.getSV("1006");
            
        List<Student> students = new ArrayList<>(qlSV.getAllSV());
        
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
    
    }
}
