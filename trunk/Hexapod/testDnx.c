/*
 * testDnx.c
 *
 * Created on: 29.09.2010
 * Author: christof
 */

#include "include/xmega.h"
#include "include/dynamixel.h"

#define TEST_ON
#ifdef TEST_ON

int main() {
	XM_init_cpu();
	XM_init_dnx();

	int i;
	for (i = 0; i < 10000; i++)
		; // delay

	for(i = 0; i < 256; i++)
		XM_RX_buffer_L[i] = 0xAA;

	DNX_sendTest();
	// DNX_setLed(0x01, 0x01);
	// DNX_getAngle(0x01);


	bool error = false;
	while (XM_sendCount > XM_receiveCount)
		;

	XM_LED_OFF

	// int i;
	/*
	 for(i = 0; i < XM_receiveCount; i++) {
	 if(XM_sendBuffer[i] != XM_RX_buffer_L[i] ) {
	 error = true;
	 break;
	 }
	 }
	 */
	int h = 1;
	if (XM_RX_buffer_L[h] != XM_sendBuffer[h]) {
		error = true;
	}

	if (error == false)
		XM_LED_ON

	while (1)
		;
	return 0;
}

/*
 ISR(USARTC0_RXC_vect) {
 USART_RXComplete(&XM_servo_data_L);
 if (USART_RXBufferData_Available(&XM_servo_data_L)) {
 // copy buffer to IRmsgRx
 XM_RX_buffer_L[0] = USART_RXBuffer_GetByte(&XM_servo_data_L);
 }
 }
 */

#endif /* TEST_ON */
