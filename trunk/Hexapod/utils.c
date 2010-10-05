/*
 * utils.c
 *
 * Created on: 27.09.2010
 * Author: christof
 */

#include <stdio.h>
#include <math.h>

#include "include/utils.h"

#define USART_ON
#ifdef USART_ON
#include "include/xmega.h"
#endif

void UTL_printMatrix(double** mat, int rows, int columns) {
	int r, c;
	char sep;
	printf("> DH04:\n");
	for (r = 0; r < rows; r++) {
		sep = ' ';
		for (c = 0; c < columns; c++) {
			printf("%c%10.5lf", sep, mat[r][c]);
			sep = '\t';

		}
		printf("\n");
	}
}

void UTL_printServos(servos s, char type) {
	printf("> Servos: ");
	if (type == UTL_RAD)
		printf("v1 = %10.5lf; v2 = %10.5lf; v2 = %10.5lf; (Bogenmaß)\n", s.v1,
				s.v2, s.v3);
	else if (type == UTL_DEG)
		printf("v1 = %10.5lf; v2 = %10.5lf; v3 = %10.5lf; (Winkel in Grad)\n",
				UTL_getDegree(s.v1), UTL_getDegree(s.v2), UTL_getDegree(s.v3));
	else
		printf("type not supported!\n");
}

void UTL_printPoint(point p) {
	printf("> Punkt: x = %10.5lf; y = %10.5lf; z = %10.5lf;\n", p.x, p.y, p.z);
}

double UTL_getRadiant(double angle) {
	return angle * (M_PI / 180);
}

double UTL_getDegree(double radiant) {
	return (radiant * 180) / M_PI;
}

point UTL_getPointOfDH(double** dh03) {
	point p;
	p.x = dh03[0][3];
	p.y = dh03[1][3];
	p.z = dh03[2][3];
	return p;
}

void UTL_printDebug(char* msg, byte l) {
#ifdef USART_ON
	byte i;
	for (i = 0; i < l; i++) {
		while (!USART_IsTXDataRegisterEmpty(XM_debug_data.usart))
			;
		USART_PutChar(XM_debug_data.usart, msg[i]);
	}
	while(!(XM_debug_data.usart->STATUS & 0x40))
					;
#else
	int i;
	for (i = 0; i < l; i++)
		printf("%c", msg[i]);
#endif
}

char *UTL_strToHex(const char *str) {
   static const char *hexchars = "0123456789ABCDEF";
   char hexstr[10];
   char* hexpoint = hexstr;
   const char *ptr;
   size_t i;
   if(hexpoint) {
      for(ptr = str, i = 0; *ptr != '\0'; ++ ptr) {
         hexpoint[i ++] = hexchars[(*ptr / 16) % 16];
         hexpoint[i ++] = hexchars[ *ptr       % 16];
      }
   }
   return hexpoint;
}
