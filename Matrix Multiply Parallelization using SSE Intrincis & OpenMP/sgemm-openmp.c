#include <emmintrin.h>
#include <omp.h>

void transpose(int m, int n, float *dst, float *src){
	int a,b;
	for(a = 0; a < m; a++){
		for( b = 0; b < n; b++){
			dst[b+a*n] = src[a+b*m];
		}
	}
}

void sgemm(int m, int n, float *A, float *C){

	float *B = (float*) malloc(m * n * sizeof(float));
	transpose(m, n, A, B);
	
	for(int i = 0; i < m; i+=4){
		for(int k = 0; k < n; k++){
			__m128 scalar = _mm_loadu_ps(A + j + k * m);
			for(int j = 0; j < m; j+=4){
				__m128 column1 = _mm_loadu_ps(A + i + k * m);			
				__m128 part1 = _mm_mul_ps(scalar, column1);
				__m128 total1 = _mm_add_ps(_mm_loadu_ps(C + i + j * m), part1);
				_mm_storeu_ps(C + i + j * m, total1);
			}
		}
	}
				//C[i+j*m] += A[i+k*m] * A[j+k*m];

}