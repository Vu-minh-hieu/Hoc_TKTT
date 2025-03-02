import java.util.ArrayList;
import java.util.List;

public class DSSV
{
    private ST<String, Student> dssv;

    public DSSV()
    {
        this.dssv = new ST<>();
    }

    public void addSV(Student student) {
        dssv.put(student.msv(), student);
    }

    public void delSV(String msv) {
        dssv.delete(msv);
    }

    public Student getSV(String msv) {
        return dssv.get(msv);
    }
    
    public ST<String, Student> getDSSV() {
        return dssv;
    }
    
    public List<Student> getAllSV() {
        List<Student> list = new ArrayList<>();
        for (String key : dssv.keys()) {
            list.add(dssv.get(key));
        }
        return list;
    }
}
