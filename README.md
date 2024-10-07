# CMSC423: Fall 2024 Project 2 sample data

The file names encode the parameters used.  Specifically, to generate `reads_100_5_0.30.fa` (and the corresponding `.stats`), the simulator was run on `genome.fa` with read length 100, expected coverage 5, 
and $\theta = 0.3$.  

To generate `reads_200_40_0.20.fa` (and the corresponding `.stats`), the simulator was run on `genome_small.fa` with read length 200, expected coverage 40, 
and $\theta = 0.2$.  

I wrote this project in Java. I found implementing the Z algorithm the hardest. I understand it well in theory but implementing it was harder then I thought it would be. I consulted https://www.w3schools.com/java/java_regex.asp to help me implement regex in java which I didn't know how to do but I knew I wanted to use it to parse out the starting pos in the reads for the z-alg part of the project. 



