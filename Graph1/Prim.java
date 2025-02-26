/******************************************************************************
 * Compute a minimum spanning forest using Prim's algorithm.
 *
 *  %  java Prim "tinyEWG.txt", "0"
 *  0-7 0.16000
 *  1-7 0.19000
 *  0-2 0.26000
 *  2-3 0.17000
 *  5-7 0.28000
 *  4-5 0.35000
 *  6-2 0.40000
 *  1.81000
 *
 ******************************************************************************/

public class Prim {
    private double[] distTo;          // distTo[v] = trong so nho nhat trong cac duong noi tu v den dinh co trong cay
    private Edge[] edgeTo;    // edgeTo[v] = canh nho nhat trong cac duong noi tu v den dinh co trong cay
    private IndexMinPQ<Double> pq;    // priority queue of vertices
    private boolean[] inMST;    //kiem tra xem co o trong cay chua
    private Queue<Edge> path = new Queue<Edge>();
    public Prim(EdgeWeightedGraph G, int s) {
        for (Edge e : G.edges()) {
            if (e.weight() < 0)
                throw new IllegalArgumentException("edge " + e + " has negative weight");
        }

        distTo = new double[G.V()];
        edgeTo = new Edge[G.V()];
        inMST = new boolean[G.V()];
        
        validateVertex(s);
        
        for (int v = 0; v < G.V(); v++){
            distTo[v] = Double.POSITIVE_INFINITY;
        }
        distTo[s] = 0.0;
        
        pq = new IndexMinPQ<Double>(G.V());
        pq.insert(s, distTo[s]);
        
        // Bo sung vong while chon phan tu min trong PQ .......
        while(!pq.isEmpty()){
            int u = pq.delMin();
            inMST[u] = true;
            path.enqueue(edgeTo[u]);
            for (Edge e : G.adj(u)){
                relax(e, u);
            }
        }
    }

    // relax edge e and update pq if changed
    private void relax(Edge e, int u) {
        int w = e.other(u);
        //kiem tra w co trong cay chua
        if (inMST[w]) {
            return;
        }

        if (distTo[w] > e.weight()) {
            distTo[w] = e.weight();
            edgeTo[w] = e;
            if (pq.contains(w)) pq.decreaseKey(w, distTo[w]);
            else                pq.insert(w, distTo[w]);
        }
    }
    
    //tra ve trong luong nho nhat
    public double dist() {
        double minDist = 0.0;
        for ( Edge e : edgeTo ){
            if (e != null) {
                minDist += e.weight();
            }
        }
        return minDist;
    }

    //tra ve cay khung nho nhat
    public Iterable<Edge> path() {
        return path;
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int v) {
        int V = distTo.length;
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        EdgeWeightedGraph G = new EdgeWeightedGraph(in);
        int s = Integer.parseInt(args[1]);

        // compute shortest paths
        Prim sp = new Prim(G, s);

        // print min spanning tree
        for (Edge e : sp.path()) {
            StdOut.print(e);
            StdOut.println();
        }
        StdOut.printf("Total " + sp.dist());
        }
    }

