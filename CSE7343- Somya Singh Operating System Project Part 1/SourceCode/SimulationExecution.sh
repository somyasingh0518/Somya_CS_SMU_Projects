#!/bin/bash

echo "Simiulation is Running";

echo "Running SJF";
java CPU "SJF" 0 > SJF.txt
echo "Output file is SJF.txt"
sed -n '/FINAL_REPORT_START/,/FINAL_REPORT_END/p' SJF.txt >> ProjectOutput.txt
sleep 1;

echo "Running Round Robin with Time Quantum 2";
java CPU "ROUND ROBIN" 2 > RR.txt
echo "Output file is RR.txt"
sed -n '/FINAL_REPORT_START/,/FINAL_REPORT_END/p' RR.txt >> ProjectOutput.txt
sleep 1;

echo "Running FCFS";
java CPU "FCFS" 0 > FCFS.txt
echo "Output file is FCFS.txt"
sed -n '/FINAL_REPORT_START/,/FINAL_REPORT_END/p' FCFS.txt >> ProjectOutput.txt
sleep 1;

echo "Running Priority";
java CPU "PRIORITY" 0 > Priority.txt
echo "Output file is Priority.txt"
sed -n '/FINAL_REPORT_START/,/FINAL_REPORT_END/p' Priority.txt >> ProjectOutput.txt
sleep 1;

echo "Simiulation is done";
