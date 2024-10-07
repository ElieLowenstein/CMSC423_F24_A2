import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.StringBuilder;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.lang.Math;
public class RandSim {
      public static void main(String[] args) throws IOException {
        int rlen = 0;
        double tdepth = 0;
        double theta = 0;
        try {
          rlen = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
          System.err.println("Invalid integer read length.");
        }

        try {
          tdepth = Double.parseDouble(args[1]);
        } catch (NumberFormatException e) {
          System.err.println("Invalid double target depth.");
        }

        try {
          theta = Double.parseDouble(args[2]);
        } catch (NumberFormatException e) {
          System.err.println("Invalid double theta.");
        }

        String genome = genome_constuction(args[3]);
        String out_stem = args[4];
        int num_reads = (int) Math.ceil((tdepth * genome.length()) / rlen);
        List<List> lists = generate_reads(genome, rlen, theta, num_reads, out_stem);
        List<Integer> start_pos = lists.get(0);
        List<Integer> depth = lists.get(1);
        int num_islands = countIslands(start_pos, rlen, theta);
        double avg_depth = (double) num_reads*rlen / genome.length();
        double var_depth = find_variance(depth, avg_depth, genome.length());
        long bases_covered = depth.stream().filter(d -> d > 0).count();
        writeStats(out_stem, num_reads, num_islands, bases_covered, avg_depth, var_depth);
      }
      public static double find_variance(List<Integer> depth, double avg_depth, int genome_length) {
        double sum = 0;
        for (int i = 0; i < genome_length; i++) {
          sum += Math.pow(depth.get(i) - avg_depth, 2);
        }
        return sum / genome_length;
      }
      public static void writeStats(String outputStem, int numReads, int numIslands, long bases_covered, double avg_depth, double var_depth) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputStem + ".stats"))) {
          writer.write("{");
          writer.newLine();
          writer.write("\t\"num_reads\": " + numReads + ",");
          writer.newLine();
          writer.write("\t\"bases_covered\": " + bases_covered + ",");
          writer.newLine();
          writer.write("\t\"avg_depth\": " + avg_depth + ",");
          writer.newLine();
          writer.write("\t\"var_depth\": " + var_depth + ",");
          writer.newLine();
          writer.write("\t\"num_islands\": " + numIslands);
          writer.newLine();
          writer.write("}");
        }
      }
      public static int countIslands(List<Integer> startPositions, int rlen, double theta) {
        int numIslands = 1;  
        int requiredOverlap = (int) (theta * rlen);  
        
        for (int i = 1; i < startPositions.size(); i++) {
            int prevReadEnd = startPositions.get(i - 1) + rlen; 
            int currReadStart = startPositions.get(i);  
            
            if (prevReadEnd - currReadStart <= requiredOverlap) {
                numIslands++;
            }
        }
        
        return numIslands;
    }
      public static List<List> generate_reads(String genome, int rlen, double theta, int num_reads, String outputStem) throws IOException {
        int length = genome.length();
        List<Integer> start_pos = new ArrayList<>();
        List<List> response = new ArrayList<>();
        ArrayList<Integer> depth = new ArrayList<>(Collections.nCopies(length, 0));
        Random rand = new Random();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputStem + ".fa"))) {

          for (int i = 0; i < num_reads; i++) {
            int start = rand.nextInt(length - rlen + 1);
            String read = genome.substring(start, start + rlen);
            String header = ">read_" + i + ":" + start + ":" + rlen;
            writer.write(header);
            writer.newLine();
            writer.write(read);
            writer.newLine();
            start_pos.add(start);
            for (int j = start; j < start + rlen; j++) {
              depth.set(j, depth.get(j) + 1);
            }
          }
        } 
        Collections.sort(start_pos);
        response.add(start_pos);
        response.add(depth);
        return response;
      }
      public static String genome_constuction(String filename) throws IOException {
        try(BufferedReader br = new BufferedReader(new FileReader(filename))) {
          StringBuilder genome = new StringBuilder();
          String line = br.readLine();
    
          while (line != null) {
            if (!line.startsWith(">")) {
              genome.append(line.trim());
            }              
            line = br.readLine(); 
          }
          return genome.toString();
        }
      }
}
