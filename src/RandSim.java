public class RandSim {
        public static void main(String[] args) {
          int rlen;
          double tdepth;
          double theta;
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

          String genome = args[3];
          String out_stem = args[4];
        }
}
