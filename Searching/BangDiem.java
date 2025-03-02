import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BangDiem
{
    private ST<String, Double> diem;
    private Mon mon;

    public BangDiem(Mon mon)
    {
        this.mon = mon;
        this.diem = new ST<>();
    }
    
    public BangDiem(String file)
    {
        this.diem = new ST<>();
        
        In in = new In(file);
        while (in.hasNextLine()) {
            String line = in.readLine();
            String[] a = line.split(";");
            if (a.length < 2) {
                throw new IllegalArgumentException("Dữ liệu không hợp lệ: " + line);
            }
            String ma = a[0];
            double d = Double.parseDouble(a[1]);
            diem.put(ma, d);
        }
        
        String mon = file.replace(".csv", "");
        Pattern pattern = Pattern.compile("([a-zA-Z]+)(\\d{1})(\\d{1})"); // Tạo pattern
        Matcher matcher = pattern.matcher(mon); // Tạo matcher để kiểm tra
        
        if (matcher.matches()) {
            int soTC = Integer.parseInt(matcher.group(2));
            int ky = Integer.parseInt(matcher.group(3));
            this.mon = new Mon(matcher.group(1), soTC, ky);
        } else {
            System.out.println("Không khớp với định dạng: " + mon);
        }
    }
    
    public void addDiem(DSSV dssv) {
        ST<String, Student> ds = dssv.getDSSV();
        for (String msv : ds.keys()) {
            Student sv = ds.get(msv);

            if (diem.contains(msv)) {
                sv.addDiem(mon, diem.get(msv));
            }
        }
    }
    
}
