import java.util.*;

class IntervalScheduling {
    private List<Job> manyJobs;
    private PriorityQueue<Job> minPQ;

    IntervalScheduling(List<Job> jobs) {
        this.manyJobs = jobs;
        this.minPQ = new PriorityQueue<>(jobs);
    }

    public List<Job> Result() {
        List<Job> result = new ArrayList<>();
        int lastFinishTime = 0;
        
        while (!minPQ.isEmpty()) {
            Job job = minPQ.poll();
            if (job.start >= lastFinishTime) {
                result.add(job);
                lastFinishTime = job.finish;
            }
        }
        return result;
    }

    public static void main(String[] args) {
        List<Job> jobs = Arrays.asList(new Job( 0, 6, 60 ),
                                            new Job( 1, 4, 30 ),
                                            new Job( 3, 5, 10 ),
                                            new Job( 5, 7, 30 ),
                                            new Job( 5, 9, 50 ),
                                            new Job( 7, 8, 10 ));
        
        IntervalScheduling job = new IntervalScheduling(jobs);
        
        System.out.println("Scheduled Jobs: " + job.Result());
    }
}
