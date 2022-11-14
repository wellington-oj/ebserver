package com.android.starter.programs;

/* The Computer Language Benchmarks Game
   https://salsa.debian.org/benchmarksgame-team/benchmarksgame/

   contributed by Isaac Gouy
   converted to Java by Oleg Mazurov
*/

// 28/05/2022
// https://benchmarksgame-team.pages.debian.net/benchmarksgame/program/fannkuchredux-java-2.html

// problemas para rodar as versões mais rápidas

//2022-05-28 20:11:34.448 22533-22564/com.android.starter E/AndroidRuntime: FATAL EXCEPTION: Thread-2
//        Process: com.android.starter, PID: 22533
//        java.lang.ArrayIndexOutOfBoundsException: length=0; index=0
//        at com.android.starter.programs.Fannkuchredux.runTask(Fannkuchredux.java:106)
//        at com.android.starter.programs.Fannkuchredux.run(Fannkuchredux.java:130)
//        at java.lang.Thread.run(Thread.java:764)

import java.io.FileOutputStream;
import java.io.IOException;

public class Fannkuchredux
{
    public static int fannkuch(int n, FileOutputStream writer) throws IOException {
        int[] perm = new int[n];
        int[] perm1 = new int[n];
        int[] count = new int[n];
        int maxFlipsCount = 0;
        int permCount = 0;
        int checksum = 0;

        for(int i=0; i<n; i++) perm1[i] = i;
        int r = n;

        while (true) {

            while (r != 1){ count[r-1] = r; r--; }

            for(int i=0; i<n; i++) perm[i] = perm1[i];
            int flipsCount = 0;
            int k;

            while ( !((k=perm[0]) == 0) ) {
                int k2 = (k+1) >> 1;
                for(int i=0; i<k2; i++) {
                    int temp = perm[i]; perm[i] = perm[k-i]; perm[k-i] = temp;
                }
                flipsCount++;
            }

            maxFlipsCount = Math.max(maxFlipsCount, flipsCount);
            checksum += permCount%2 == 0 ? flipsCount : -flipsCount;

            // Use incremental change to generate another permutation
            while (true) {
                if (r == n) {
                    writer.write((""+checksum+"\n").getBytes());
                    return maxFlipsCount;
                }
                int perm0 = perm1[0];
                int i = 0;
                while (i < r) {
                    int j = i + 1;
                    perm1[i] = perm1[j];
                    i = j;
                }
                perm1[r] = perm0;

                count[r] = count[r] - 1;
                if (count[r] > 0) break;
                r++;
            }

            permCount++;
        }
    }

    public void runCode(int n, FileOutputStream writer) throws IOException {
        writer.write(("Pfannkuchen("+n+") = "+fannkuch(n, writer)).getBytes());
    }
}