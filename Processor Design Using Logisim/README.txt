Project Title: Processor Design Using Logisim

Author: Gavin Chu

Description:
	This project implements a 2-stage pipelined 16-bit processor with Logisim
that contains a separate data and instruction memory. 
	The main framework is inside "cpu.circ". The main circuit contains:
- "Regfile.circ," which manages four 16-bit registers, 
- "alu.circ," which performs different operations such as add and multiply,
- an ALU control that calls specific operations from "alu.circ"
	I learned how a basic computer processor works.

Instruction:
- Download Logisim to view the processor design
- open "cpu-harness.circ" and load assembly code ("strLen.hex" or "octal.hex") into the instruction memory and run the processor to run through the instructions in the assembly code files
- results of assembly code output in the displays