package com.android.starter.programs;

/*
   The Computer Language Benchmarks Game
   https://salsa.debian.org/benchmarksgame-team/benchmarksgame/

   contributed by Francois Green
*/

// 28/05/2022
// https://benchmarksgame-team.pages.debian.net/benchmarksgame/program/regexredux-java-1.html

import java.io.*;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.*;

import java9.util.function.BiFunction;
import java9.util.stream.Collectors;
import java9.util.stream.Stream;
import java9.util.stream.StreamSupport;

public class RegexRedux {
    public void runCode(InputStream in, FileOutputStream writer) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        {
            byte[] buf = new byte[65536];
            int count;
            while ((count = in.read(buf)) > 0) {
                baos.write(buf, 0, count);
            }
        }
        final String input = baos.toString("US-ASCII");

        final int initialLength = input.length();

        final String sequence = input.replaceAll(">.*\n|\n", "");

        final int codeLength = sequence.length();

        final List<String> variants = Arrays.asList("agggtaaa|tttaccct",
                "[cgt]gggtaaa|tttaccc[acg]",
                "a[act]ggtaaa|tttacc[agt]t",
                "ag[act]gtaaa|tttac[agt]ct",
                "agg[act]taaa|ttta[agt]cct",
                "aggg[acg]aaa|ttt[cgt]ccct",
                "agggt[cgt]aa|tt[acg]accct",
                "agggta[cgt]a|t[acg]taccct",
                "agggtaa[cgt]|[acg]ttaccct");

        BiFunction<String, String, Entry<String, Long>> counts = (v, s) -> {
            //Java 9 Matcher.results isn't off by one
            Long count = Stream.of(s.split(v)).count() - 1;
            return new AbstractMap.SimpleEntry<>(v, count);
        };

        final Map<String, Long> results = StreamSupport.parallelStream(variants)
                .map(variant -> counts.apply(variant, sequence))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        for (String variant : variants) {
            writer.write((variant + " " + results.get(variant) + "\n").getBytes());
        }

        final Map<String, String> replacements = new LinkedHashMap<>();//Only works with LinkedHashMap
        {
            replacements.put("tHa[Nt]", "<4>");
            replacements.put("aND|caN|Ha[DS]|WaS", "<3>");
            replacements.put("a[NSt]|BY", "<2>");
            replacements.put("<[^>]*>", "|");
            replacements.put("\\|[^|][^|]*\\|", "-");
        }

        String buf = sequence;
        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            buf = Pattern.compile(entry.getKey()).matcher(buf).replaceAll(entry.getValue());
        }

        writer.write("\n".getBytes());
        writer.write((""+initialLength+"\n").getBytes());
        writer.write((""+codeLength+"\n").getBytes());
        writer.write((""+buf.length()+"\n").getBytes());
    }
}
