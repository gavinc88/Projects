#include <assert.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "memory.h"

/* Pointer to simulator memory */
uint8_t *mem;

/* Called by program loader to initialize memory. */
uint8_t *init_mem() {
  assert (mem == NULL);
  mem = calloc(MEM_SIZE, sizeof(uint8_t)); // allocate zeroed memory
  return mem;
}

/* Returns 1 if memory access is ok, otherwise 0 */
int access_ok(uint32_t mipsaddr, mem_unit_t size) {

  /* TODO YOUR CODE HERE */
	if(mipsaddr == 0){
		printf("Accessing null. Exiting program.");
		return 0;
	}  
	if(mipsaddr >= MEM_SIZE){
		printf("Address outside of range. Exiting program.");
		return 0;
	}
	if(mipsaddr%size != 0){
		printf("Misaligned. Exiting program.");
		return 0;
	}

  return 1;
}

/* Writes size bytes of value into mips memory at mipsaddr */
void store_mem(uint32_t mipsaddr, mem_unit_t size, uint32_t value) {
  if (!access_ok(mipsaddr, size)) {
    fprintf(stderr, "%s: bad write=%08x\n", __FUNCTION__, mipsaddr);
    exit(-1);
  }
	int i;
  /* TODO YOUR CODE HERE */
	switch(size){
	case SIZE_BYTE:
		*(mem + mipsaddr) = (uint8_t)value;
		break;
	case SIZE_HALF_WORD:
		for(i = 0; i < 2; i++){
			*(mem + mipsaddr + i) = (uint8_t)(value >> (i*8));
		}
		break;
	case SIZE_WORD:
		for(i = 0; i < 4; i++){
			*(mem + mipsaddr + i) = (uint8_t)(value >> (i*8));
		}
		break;
	default:
		printf("invalid store");
	}
}

/* Returns zero-extended value from mips memory */
uint32_t load_mem(uint32_t mipsaddr, mem_unit_t size) {
  if (!access_ok(mipsaddr, size)) {
    fprintf(stderr, "%s: bad read=%08x\n", __FUNCTION__, mipsaddr);
    exit(-1);
  }

  /* TODO YOUR CODE HERE */
  switch(size){
	case SIZE_BYTE:
		return (*(uint32_t*)(mem + mipsaddr)) & 0x000000ff;
	case SIZE_HALF_WORD:
		return (*(uint32_t*)(mem + mipsaddr)) & 0x0000ffff;
	case SIZE_WORD:
		return *(uint32_t*)(mem + mipsaddr);
	default:
		printf("invalid load");
	}  

  // incomplete stub to let mipscode/simple execute
  // (only handles size == SIZE_WORD correctly)
  // feel free to delete and implement your own way
  return *(uint32_t*)(mem + mipsaddr);
}
