import java.util.*;

    class MinLatenessScheduler {
        private List<mJob> jobs;
    
        MinLatenessScheduler(List<mJob> jobs) {
            this.jobs = jobs;
        }
    
        public int minLatenessSchedule() {
            // 1️⃣ Sắp xếp công việc theo deadline tăng dần
            jobs.sort(Comparator.comparingInt(j -> j.deadline));
    
            int time = 0;           // Thời gian bắt đầu
            int maxLateness = 0;    // Độ trễ lớn nhất
    
            System.out.println("Scheduled Jobs Order:");
            for (mJob job : jobs) {
                System.out.println(job);
                
                time += job.processingTime;  // Cập nhật thời gian hoàn thành công việc
                int lateness = Math.max(0, time - job.deadline);  // Tính độ trễ
                maxLateness = Math.max(maxLateness, lateness);    // Cập nhật max lateness
            }
    
            return maxLateness;  // Trả về độ trễ tối đa
        }
    
        public static void main(String[] args) {
            List<mJob> jobs = Arrays.asList(
                new mJob(1, 3, 9),  // ID=1, p=3, d=9
                new mJob(2, 2, 5),
                new mJob(3, 1, 8),
                new mJob(4, 5, 12)
            );
    
            MinLatenessScheduler scheduler = new MinLatenessScheduler(jobs);
            int maxLateness = scheduler.minLatenessSchedule();
            System.out.println("Minimum Maximum Lateness: " + maxLateness);
        }
    }