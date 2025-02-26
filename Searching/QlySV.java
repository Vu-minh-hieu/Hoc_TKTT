import java.util.ArrayList;
import java.util.List;

public class QlySV
{
    private ST<String, Student> QLSV;

    public QlySV()
    {
        this.QLSV = new ST<>();
    }

    public void addSV(Student student) {
        QLSV.put(student.msv(), student);
    }

    public void delSV(String msv) {
        QLSV.delete(msv);
    }

    public void getSV(String msv) {
        if (QLSV.contains(msv)) StdOut.println(QLSV.get(msv));
            else                StdOut.println("Not found");
    }
    
    public List<Student> getAllSV() {
        List<Student> list = new ArrayList<>();
        for (String key : QLSV.keys()) {
            list.add(QLSV.get(key));
        }
        return list;
    }
}
