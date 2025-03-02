import java.util.Arrays;
import java.util.Comparator;

public class Mon implements Comparable<Mon> {
    private final String tenMon;
    private final int soTC;
    private final int ky;

    public Mon(String tenMon, int soTC, int ky) {
        this.tenMon = tenMon;
        this.soTC = soTC;
        this.ky = ky;
    }
    
    public String tenMon() {
        return tenMon;
    }
    
    public int soTC(){
        return soTC;
    }
    
    public int kyThu(){
        return ky;
    }
    
    @Override
    public int compareTo(Mon other) {
        return this.tenMon.compareTo(other.tenMon);
    }
}
