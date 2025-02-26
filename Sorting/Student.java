/******************************************************************************
 *  Compilation:  javac Transaction.java
 *  Execution:    java Transaction
 *  Dependencies: StdOut.java
 *  
 *  Data type for commercial transactions.
 *
 ******************************************************************************/

 

import java.util.Arrays;
import java.util.Comparator;

/**
 *  The {@code Transaction} class is an immutable data type to encapsulate a
 *  commercial transaction with a customer name, date, and amount.
 *  <p>
 *  For additional documentation, 
 *  see <a href="https://algs4.cs.princeton.edu/12oop">Section 1.2</a> of 
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne. 
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class Student implements Comparable<Student> {
    private final String  who;      // customer
    private final VietDate    date;     // date
    private final double  point;   // amount


    /**
     * Initializes a new transaction from the given arguments.
     *
     * @param  who the person involved in this transaction
     * @param  when the date of this transaction
     * @param  amount the amount of this transaction
     * @throws IllegalArgumentException if {@code amount} 
     *         is {@code Double.NaN}, {@code Double.POSITIVE_INFINITY},
     *         or {@code Double.NEGATIVE_INFINITY}
     */
    public Student(String who, VietDate date, double point) {
        if (Double.isNaN(point) || Double.isInfinite(point))
            throw new IllegalArgumentException("point cannot be NaN or infinite");
        this.who    = who;
        this.date   = date;
        this.point = point;
    }

    /**
     * Initializes a new transaction by parsing a string of the form NAME DATE AMOUNT.
     *
     * @param  transaction the string to parse
     * @throws IllegalArgumentException if {@code amount} 
     *         is {@code Double.NaN}, {@code Double.POSITIVE_INFINITY},
     *         or {@code Double.NEGATIVE_INFINITY}
     */
    public Student(String student) {
        String[] a = student.split("\\s+");
        who    = a[0];
        date   = new VietDate(a[1]);
        point = Double.parseDouble(a[2]);
        if (Double.isNaN(point) || Double.isInfinite(point))
            throw new IllegalArgumentException("point cannot be NaN or infinite");
    }

    /**
     * Returns the name of the customer involved in this transaction.
     *
     * @return the name of the customer involved in this transaction
     */
    public String who() {
        return who;
    }
 
    /**
     * Returns the date of this transaction.
     *
     * @return the date of this transaction
     */
    public VietDate date() {
        return date;
    }
 
    /**
     * Returns the amount of this transaction.
     *
     * @return the amount of this transaction
     */
    public double point() {
        return point;
    }

    /**
     * Returns a string representation of this transaction.
     *
     * @return a string representation of this transaction
     */
    @Override
    public String toString() {
        return String.format("%-10s %10s %8.2f", who, date, point);
    }

    /**
     * Compares two transactions by amount.
     *
     * @param  that the other transaction
     * @return { a negative integer, zero, a positive integer}, depending
     *         on whether the amount of this transaction is { less than,
     *         equal to, or greater than } the amount of that transaction
     */
    public int compareTo(Student that) {
        return Double.compare(this.point, that.point);
    }    

    /**
     * Compares this transaction to the specified object.
     *
     * @param  other the other transaction
     * @return true if this transaction is equal to {@code other}; false otherwise
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null) return false;
        if (other.getClass() != this.getClass()) return false;
        Student that = (Student) other;
        return (this.point == that.point) && (this.who.equals(that.who))
                                            && (this.date.equals(that.date));
    }


    /**
     * Returns a hash code for this transaction.
     *
     * @return a hash code for this transaction
     */
    public int hashCode() {
        int hash = 1;
        hash = 31*hash + who.hashCode();
        hash = 31*hash + date.hashCode();
        hash = 31*hash + ((Double) point).hashCode();
        return hash;
        // return Objects.hash(who, date, point);
    }

    /**
     * Compares two transactions by customer name.
     */
    public static class WhoOrder implements Comparator<Student> {

        @Override
        public int compare(Student v, Student w) {
            return v.who.compareTo(w.who);
        }
    }

    /**
     * Compares two transactions by date.
     */
    public static class DateOrder implements Comparator<Student> {

        @Override
        public int compare(Student v, Student w) {
            return v.date.compareTo(w.date);
        }
    }

    /**
     * Compares two transactions by amount.
     */
    public static class HowMuchOrder implements Comparator<Student> {

        @Override
        public int compare(Student v, Student w) {
            return Double.compare(v.point, w.point);
        }
    }


    /**
     * Unit tests the {@code Transaction} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        Student[] a = new Student[4];
        a[0] = new Student("Turing   17/6/1990  21");
        a[1] = new Student("Tarjan   26/3/2002 25");
        a[2] = new Student("Knuth    14/6/1999  28.5");
        a[3] = new Student("Dijkstra 22/8/2007 24.25");

        StdOut.println("Unsorted");
        for (int i = 0; i < a.length; i++)
            StdOut.println(a[i]);
        StdOut.println();
        
        StdOut.println("Sort by CompareTo");
        Arrays.sort(a);
        for (int i = 0; i < a.length; i++)
            StdOut.println(a[i]);
        StdOut.println();
        
                StdOut.println("Sort by date");
        Arrays.sort(a, new Student.DateOrder());
        for (int i = 0; i < a.length; i++)
            StdOut.println(a[i]);
        StdOut.println();

        StdOut.println("Sort by customer");
        Arrays.sort(a, new Student.WhoOrder());
        for (int i = 0; i < a.length; i++)
            StdOut.println(a[i]);
        StdOut.println();

        StdOut.println("Sort by point");
        Arrays.sort(a, new Student.HowMuchOrder());
        for (int i = 0; i < a.length; i++)
            StdOut.println(a[i]);
        StdOut.println();
    }

}




/******************************************************************************
 *  Copyright 2002-2016, Robert Sedgewick and Kevin Wayne.
 *
 *  This file is part of algs4.jar, which accompanies the textbook
 *
 *      Algorithms, 4th edition by Robert Sedgewick and Kevin Wayne,
 *      Addison-Wesley Professional, 2011, ISBN 0-321-57351-X.
 *      http://algs4.cs.princeton.edu
 *
 *
 *  algs4.jar is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  algs4.jar is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with algs4.jar.  If not, see http://www.gnu.org/licenses.
 ******************************************************************************/