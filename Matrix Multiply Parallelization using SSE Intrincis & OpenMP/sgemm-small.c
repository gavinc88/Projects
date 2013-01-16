#include <emmintrin.h>
/*
  void transpose( int m, int n, float *dst, float *src ) {
  int a,b;
  for( a = 0; a < m; a++){
  for( b = 0; b < n; b++){
  dst[b+a*n] = src[a+b*m];
  }
  }
  }
*/
void sgemm( int m, int n, float *A, float *C )
{
    //. UNROLLING BY 20. Upper limit 15 registers used
    if(m % 20 == 0 && n % 5 == 0){
		for(int j = 0; j < m/2*2; j+=2){
			for(int k = 0; k < n; k+= 5) {
				
				for(int i = 0; i < m/20*20; i+=20){
					//loading 20 parts of a C column to get sum.
					__m128 c_part1 = _mm_loadu_ps(C + i + j * m + 0);
					__m128 c_part2 = _mm_loadu_ps(C + i + j * m + 4);
					__m128 c_part3 = _mm_loadu_ps(C + i + j * m + 8);
					__m128 c_part4 = _mm_loadu_ps(C + i + j * m + 12);
					__m128 c_part5 = _mm_loadu_ps(C + i + j * m + 16);
					
					__m128 scalar;
					//z increments across columns.
					for(int z = 0; z < 5; z++){
						//loads 20 parts of the A matrix.
						__m128 a_part1 = _mm_loadu_ps(A + i + (z + k) * m + 0);
						__m128 a_part2 = _mm_loadu_ps(A + i + (z + k) * m + 4);
						__m128 a_part3 = _mm_loadu_ps(A + i + (z + k) * m + 8);
						__m128 a_part4 = _mm_loadu_ps(A + i + (z + k) * m + 12);
						__m128 a_part5 = _mm_loadu_ps(A + i + (z + k) * m + 16);

						//gets the scalar to multiply the columns with
						scalar = _mm_load1_ps(A + j + (z + k) * m);	

						//multiplies scalar with part into c_part for a partial sum
						__m128 temp_sum1 = _mm_mul_ps(scalar, a_part1);
						__m128 temp_sum2 = _mm_mul_ps(scalar, a_part2);
						__m128 temp_sum3 = _mm_mul_ps(scalar, a_part3);
						__m128 temp_sum4 = _mm_mul_ps(scalar, a_part4);
						__m128 temp_sum5 = _mm_mul_ps(scalar, a_part5);

						//accumulate partial sum across columns for each cell
						c_part1 = _mm_add_ps(c_part1, temp_sum1);
						c_part2 = _mm_add_ps(c_part2, temp_sum2);
						c_part3 = _mm_add_ps(c_part3, temp_sum3);
						c_part4 = _mm_add_ps(c_part4, temp_sum4);
						c_part5 = _mm_add_ps(c_part5, temp_sum5);
					}

					//store back to C
					_mm_storeu_ps((C + i + j * m + 0), c_part1);
					_mm_storeu_ps((C + i + j * m + 4), c_part2);
					_mm_storeu_ps((C + i + j * m + 8), c_part3);
					_mm_storeu_ps((C + i + j * m + 12), c_part4);
					_mm_storeu_ps((C + i + j * m + 16), c_part5);
				}

				//UNROLLED 2
				for(int i = 0; i < m/20*20; i+=20){
					//loading 20 parts of a C column to get sum.
					__m128 c_part1 = _mm_loadu_ps(C + i + (j+1) * m + 0);
					__m128 c_part2 = _mm_loadu_ps(C + i + (j+1) * m + 4);
					__m128 c_part3 = _mm_loadu_ps(C + i + (j+1) * m + 8);
					__m128 c_part4 = _mm_loadu_ps(C + i + (j+1) * m + 12);
					__m128 c_part5 = _mm_loadu_ps(C + i + (j+1) * m + 16);
					
					__m128 scalar;
					//z increments across columns.
					for(int z = 0; z < 5; z++){
						//loads 20 parts of the A matrix.
						__m128 a_part1 = _mm_loadu_ps(A + i + (z + k) * m + 0);
						__m128 a_part2 = _mm_loadu_ps(A + i + (z + k) * m + 4);
						__m128 a_part3 = _mm_loadu_ps(A + i + (z + k) * m + 8);
						__m128 a_part4 = _mm_loadu_ps(A + i + (z + k) * m + 12);
						__m128 a_part5 = _mm_loadu_ps(A + i + (z + k) * m + 16);

						//gets the scalar to multiply the columns with
						scalar = _mm_load1_ps(A + (j+1) + (z + k) * m);	

						//multiplies scalar with part into c_part for a partial sum
						__m128 temp_sum1 = _mm_mul_ps(scalar, a_part1);
						__m128 temp_sum2 = _mm_mul_ps(scalar, a_part2);
						__m128 temp_sum3 = _mm_mul_ps(scalar, a_part3);
						__m128 temp_sum4 = _mm_mul_ps(scalar, a_part4);
						__m128 temp_sum5 = _mm_mul_ps(scalar, a_part5);

						//accumulate partial sum across columns for each cell
						c_part1 = _mm_add_ps(c_part1, temp_sum1);
						c_part2 = _mm_add_ps(c_part2, temp_sum2);
						c_part3 = _mm_add_ps(c_part3, temp_sum3);
						c_part4 = _mm_add_ps(c_part4, temp_sum4);
						c_part5 = _mm_add_ps(c_part5, temp_sum5);
					}

					//store back to C
					_mm_storeu_ps((C + i + (j+1) * m + 0), c_part1);
					_mm_storeu_ps((C + i + (j+1) * m + 4), c_part2);
					_mm_storeu_ps((C + i + (j+1) * m + 8), c_part3);
					_mm_storeu_ps((C + i + (j+1) * m + 12), c_part4);
					_mm_storeu_ps((C + i + (j+1) * m + 16), c_part5);
				}	
			}
		}
    }

    // UNROLLING BY 16. 
    else if(m % 16 < 4){
	for(int k = 0; k < n; k++){
	    for(int j = 0; j < m; j++) {
		__m128 scalar = _mm_load1_ps(A + j + k * m);
		for( int i = 0; i < m/16*16; i += 16 ) {
		    __m128 column1 = _mm_loadu_ps(A + i + k * m);			
		    __m128 column2 = _mm_loadu_ps(A + i + k * m + 4);
		    __m128 column3 = _mm_loadu_ps(A + i + k * m + 8);
		    __m128 column4 = _mm_loadu_ps(A + i + k * m + 12);
		    __m128 part1 = _mm_mul_ps(scalar, column1);
		    __m128 part2 = _mm_mul_ps(scalar, column2);
		    __m128 part3 = _mm_mul_ps(scalar, column3);
		    __m128 part4 = _mm_mul_ps(scalar, column4);
		    __m128 total1 = _mm_add_ps(_mm_loadu_ps(C + i + j * m + 0), part1);
		    __m128 total2 = _mm_add_ps(_mm_loadu_ps(C + i + j * m + 4), part2);
		    __m128 total3 = _mm_add_ps(_mm_loadu_ps(C + i + j * m + 8), part3);
		    __m128 total4 = _mm_add_ps(_mm_loadu_ps(C + i + j * m + 12), part4);
		    _mm_storeu_ps(C+i+j*m, total1);
		    _mm_storeu_ps(C+i+j*m+4, total2);
		    _mm_storeu_ps(C+i+j*m+8, total3);
		    _mm_storeu_ps(C+i+j*m+12, total4);
		}
		for( int i = m/16*16; i < m; i++ ) {
		    C[i+j*m] += A[i+k*m] * A[j+k*m];
		}
	    }	
	}	
    }

    // UNROLLING BY 12. 
    else if(m % 12 < 3){
	for(int k = 0; k < n; k++){
	    for(int j = 0; j < m; j++) {
		__m128 scalar = _mm_load1_ps(A + j + k * m);
		for( int i = 0; i < m/12*12; i += 12 ) {
		    __m128 column1 = _mm_loadu_ps(A + i + k * m);			
		    __m128 column2 = _mm_loadu_ps(A + i + k * m + 4);
		    __m128 column3 = _mm_loadu_ps(A + i + k * m + 8);
		    __m128 part1 = _mm_mul_ps(scalar, column1);
		    __m128 part2 = _mm_mul_ps(scalar, column2);
		    __m128 part3 = _mm_mul_ps(scalar, column3);
		    __m128 total1 = _mm_add_ps(_mm_loadu_ps(C + i + j * m + 0), part1);
		    __m128 total2 = _mm_add_ps(_mm_loadu_ps(C + i + j * m + 4), part2);
		    __m128 total3 = _mm_add_ps(_mm_loadu_ps(C + i + j * m + 8), part3);
		    _mm_storeu_ps(C+i+j*m, total1);
		    _mm_storeu_ps(C+i+j*m+4, total2);
		    _mm_storeu_ps(C+i+j*m+8, total3);
		}
		for( int i = m/12*12; i < m; i++ ) {
		    C[i+j*m] += A[i+k*m] * A[j+k*m];
		}
	    }	
	}
    }

    // UNROLLING BY 8. 
    else{
	for(int k = 0; k < n; k++){
	    for(int j = 0; j < m; j++) {
		__m128 scalar = _mm_load1_ps(A + j + k * m);
		for( int i = 0; i < m/8*8; i += 8 ) {
		    __m128 column1 = _mm_loadu_ps(A + i + k * m);			
		    __m128 column2 = _mm_loadu_ps(A + i + k * m + 4);
		    __m128 part1 = _mm_mul_ps(scalar, column1);
		    __m128 part2 = _mm_mul_ps(scalar, column2);
		    __m128 total1 = _mm_add_ps(_mm_loadu_ps(C + i + j * m + 0), part1);
		    __m128 total2 = _mm_add_ps(_mm_loadu_ps(C + i + j * m + 4), part2);
		    _mm_storeu_ps(C+i+j*m, total1);
		    _mm_storeu_ps(C+i+j*m+4, total2);
		}
		for( int i = m/8*8; i < m; i++ ) {
		    C[i+j*m] += A[i+k*m] * A[j+k*m];
		}	
	    }	
	}		
    }
}

