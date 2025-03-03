
public class FileFrequencyIndex
{
    private ST<String, ST<String, Integer>> st;
    
    public FileFrequencyIndex( ){
        st = new ST<>();
    }
    
    public void addFile(String file){
        In in = new In(file);
        
        while(in.hasNextLine()){
            String line = in.readLine();
            String[] s = line.split(" ");
            
            for (String x : s) {
                put(x, file);
            }
        }
    }
    
    private void put(String word, String file) {
        var st_trg = st.get(word);
        
        if (st_trg == null) {
            ST<String, Integer> st_trgNew = new ST<>();
            st_trgNew.put(file, 1);
            st.put(word, st_trgNew);
        } 
        else {
            boolean contains = st_trg.contains(file);
            
            if (contains) {
                st_trg.put(file, st_trg.get(file) + 1);
            } else {
                st_trg.put(file, 1);
            }
        }
    }
    
    private void getIndex(String word) {
        var st_trg = st.get(word);
        StdOut.println(word);
        
        for (Object o : st.keys()) {
            String file = (String) o;
            StdOut.println(file + " " + st.get(file));
        }
    }
    
    public static void main(String[] args) {
        FileFrequencyIndex f = new FileFrequencyIndex();
        
        for (int i = 1; i <= 4; i++) {
            f.addFile("ex" + i + ".txt");
        }
        
        f.getIndex("was");
    }
}
