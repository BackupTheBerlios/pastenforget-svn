/*
 * testDnx.c
 *
 * Created on: 29.09.2010
 * Author: christof
 */

#include "include/xmega.h"
#include "include/utils.h"
#include "include/dynamixel.h"

#define TEST_ON
#ifdef TEST_ON

int main() {
	XM_init_cpu();
	XM_init_dnx();

	DNX_setSpeed(0x01, 0x00);
	/*
	UTL_wait(20);
	DNX_setAngle(0x01, 0x00);
	UTL_wait(20);
	DNX_setAngle(0x01, 0xFF);
	UTL_wait(20);

	DNX_setSpeed(0x01, 0xFF);
	UTL_wait(20);
	DNX_setAngle(0x01, 0x00);
	UTL_wait(20);
	DNX_setAngle(0x01, 0xFF);
	*/

	while(XM_RX_buffer_L.putIndex >= XM_RX_buffer_L.getIndex)
		;

	DEBUG_BYTE((XM_RX_buffer_L.buffer, XM_RX_buffer_L.putIndex))

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
