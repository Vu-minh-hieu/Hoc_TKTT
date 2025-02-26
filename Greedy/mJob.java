import java.util.*;

    class mJob implements Comparable<mJob>{
        int id, processingTime, deadline;
    
        mJob(int id, int processingTime, int deadline) {
            this.id = id;
            this.processingTime = processingTime;
            this.deadline = deadline;
        }
        @Override
        public int compareTo(mJob other) {
            return Double.compare(this.deadline, other.deadline);
        }
        @Override
        public String toString() {
            return "Job " + id + " (p=" + processingTime + ", d=" + deadline + ")";
        }
    }

