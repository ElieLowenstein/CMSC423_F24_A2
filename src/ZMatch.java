import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.StringBuilder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.List;
import java.lang.Math;

public class ZMatch {
        public static void main(String[] args) throws IOException {
          String genome = args[0];
          String reads = args[1];
          String genome_string = genome_constuction(genome);
          try(BufferedReader br = new BufferedReader(new FileReader(reads))) {
            String read = "";
            String line = br.readLine();
            String title = "";
            int count = 0; 
            List<Integer> start_pos = new ArrayList<Integer>();
            List<Integer> longest_sub = new ArrayList<Integer>();
      
            while (line != null) {
              if (line.charAt(0) == '>' && count != 0) {
                read = read.replaceAll("\n", "");
                String regex = ":(\\d+):";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(title);
                if (matcher.find()) {
                  String middleNumber = matcher.group(1);       
                  start_pos.add(Integer.parseInt(middleNumber));
                }
                Integer longest_sub_length = z_algorithm(read, genome_string);
                longest_sub.add(longest_sub_length);
                read = "";
                title = line;
                line = br.readLine();
                continue;
              }

              if (count != 0) {
                read = read + line;
              } else {
                title = line;
              }
              line = br.readLine();
              count++;
            }
            read = read.replaceAll("\n", "");
            String regex = ":(\\d+):";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(title);
            if (matcher.find()) {
              String middleNumber = matcher.group(1);       
              start_pos.add(Integer.parseInt(middleNumber));
            }
            Integer longest_sub_length = z_algorithm(read, genome_string);
            longest_sub.add(longest_sub_length);
            br.close();
            String result = "{\"occs\": " + start_pos + ", \"longest_prefs\": " + longest_sub + "}";
            System.out.println(result);

          } 

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

        public static Integer z_algorithm(String read, String genome) {
          Integer result = 0;
          String concat = read + "$" + genome;
          Integer len = concat.length();
          int[] z = new int[len];
          int L = 0, R = 0, k;
          for (int i = 1; i < len; i++) {
            if (i > R) {
              L = R = i;
              while (R < len && concat.charAt(R - L) == concat.charAt(R)) {
                R++;
              }
              z[i] = R - L;
              R--;
            } else {
              k = i - L;
              if (z[k] < R - i - 1) {
                z[i] = z[k];
              } else {
                L = i;
                while (R < len && concat.charAt(R - L) == concat.charAt(R)) {
                  R++;
                }
                z[i] = R - L;
                R--;
              }
            }
          }
          int read_length = read.length();
          for (int i = read_length + 1; i < len; i++) {
            if (z[i] < read_length) {
                result = Math.max(result, z[i]);
            }
          }
          return result;
        } 
}
