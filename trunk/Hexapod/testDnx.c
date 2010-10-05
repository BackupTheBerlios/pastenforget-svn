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

	for (i = 0; i < 1000; i++)
		;

	for (i = 0; i < 5; i++)
		;
	DNX_setLed(0x01, 0x01);

	while (1)
		;
	return 0;
}

ISR (USARTC0_DRE_vect)
// Send IRQ
{
	if (XM_sendCurrentCount < XM_sendCount) // Alles gesendet?
		USART_PutChar(&USARTC0, XM_sendBuffer[XM_sendCurrentCount++]); // TX Buffer fuellen

	else {
		USART_DreInterruptLevel_Set(&USARTC0, USART_DREINTLVL_OFF_gc); // DRE-IRQ sperren
		USART_TxdInterruptLevel_Set(&USARTC0, USART_TXCINTLVL_LO_gc); // Trans-IRO scharf machen
	}
}

ISR (USARTC0_TXC_vect)
// Ende des senden
{
	DEBUG(("ISR_TXC\n",sizeof("ISR_TXC\n")))
	XM_PORT_SERVO_L.OUTSET = XM_OE_MASK;
	USART_TxdInterruptLevel_Set(&USARTC0, USART_TXCINTLVL_OFF_gc);
}


ISR(USARTC0_RXC_vect)
{
	//DEBUG(("ISR_RXC",sizeof("ISR_RXC")))
	char* rx_buffer = {0x00};
	USART_RXComplete(&XM_servo_data_L);
			if (USART_RXBufferData_Available(&XM_servo_data_L)) {
				/* copy buffer to IRmsgRx */
				rx_buffer[0] = USART_RXBuffer_GetByte(&XM_servo_data_L);
			}
    DEBUG((UTL_strToHex(rx_buffer),sizeof("XX")))

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
