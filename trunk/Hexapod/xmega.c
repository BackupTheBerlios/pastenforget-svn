/*
 * xmega.c
 *
 *  Created on: 30.09.2010
 *      Author: christof
 */

#include "include/xmega.h"

#define XM_PORT_LED PORTQ
#define XM_LED_MASK (1<<PIN3)
#define XM_OE_MASK (1<<PIN0)

void XM_init_cpu() {
	// TODO
	/******************************************************************
	 * System Clock 32MHz (XOSC Quarz 16MHz, PLL Faktor 2)
	 ******************************************************************/

	/* Nach dem Reset ist die Quelle des Systemtaktes der interne
	 2MHz RC-Oszillator (System Clock Selection: RC2MHz)
	 */

	// Oszillator XOSC konfigurieren (12..16MHz, 256 clocks startup time)
	CLKSYS_XOSC_Config(OSC_FRQRANGE_12TO16_gc, false,
			OSC_XOSCSEL_XTAL_256CLK_gc);

	// Oszillator XOSC enable
	CLKSYS_Enable(OSC_XOSCEN_bm);

	// Warten bis der Oszillator bereit ist
	do {
	} while (CLKSYS_IsReady(OSC_XOSCRDY_bm) == 0);

	// PLL source ist XOSC, Multiplikator x2
	CLKSYS_PLL_Config(OSC_PLLSRC_XOSC_gc, 2);

	// Enable PLL
	CLKSYS_Enable(OSC_PLLEN_bm);

	// Prescalers konfigurieren
	CLKSYS_Prescalers_Config(CLK_PSADIV_1_gc, CLK_PSBCDIV_1_1_gc);

	// Warten bis PLL locked
	do {
	} while (CLKSYS_IsReady(OSC_PLLRDY_bm) == 0);

	// Main Clock Source ist Ausgang von PLL
	CLKSYS_Main_ClockSource_Select(CLK_SCLKSEL_PLL_gc);

	// Nun ist der System Clock 32MHz !

	/* Hinweis:
	 32kHz TOSC kann nicht in Verbindung mit PLL genutzt werden, da
	 die minimale Eingangsfrequenz des PLLs 400kHz betraegt.
	 */

	/******************************************************************
	 * Debug-Usart initialisieren, 8N1 250kBit
	 ******************************************************************/

	// TODO
	XM_debug_data.usart = &XM_USART_DEBUG;
	// XM_debug_data.buffer = ...?
	// XM_debug_data.dreIntLevel = ...?
	XM_PORT_DEBUG.DIRSET = PIN3_bm; // Pin3 von PortF (TXD0) ist Ausgang
	XM_PORT_DEBUG.DIRCLR = PIN2_bm; // Pin2 von PortF (RXD0) ist Eingang

	// USARTF0, 8 Data bits, No Parity, 1 Stop bit.
	USART_Format_Set(XM_debug_data.usart, USART_CHSIZE_8BIT_gc,
			USART_PMODE_DISABLED_gc, false);

	/* Bitrate einstellen

	 Beispiele BSEL in Abhaengigkeit von der Bitrate, fck = 32MHz, Error < 0,8%

	 7 = 250.000bps
	 30 = 128.000bps
	 34 =  57.600bps
	 51 =  38.400bps
	 68 =  28.800bps
	 103 =  19.200bps
	 138 =  14.400bps
	 207 =   9.600bps
	 416 =   4.800bps
	 832 =   2.400bps
	 1666 =   1.200bps

	 Bemerkung: Geprueft wurde mit 250.000bps im USBxpress Modus
	 */

	USART_Baudrate_Set(XM_debug_data.usart, 7, 0); // 250.000bps (BSEL = 7)

	/* Enable RX and TX. */
	USART_Rx_Enable(XM_debug_data.usart);
	USART_Tx_Enable(XM_debug_data.usart);

	USART_GetChar(XM_debug_data.usart); // Flush Receive Buffer
	DEBUG(("init_cpu;", sizeof("init_cpu;")))
	DEBUG(("DEBUG-USART ... ON;", sizeof("DEBUG-USART ... ON;")));

	// Set LED
	XM_PORT_LED.DIRSET = XM_LED_MASK;
	XM_PORT_LED.OUTSET = XM_LED_MASK;
}

void XM_init_dnx() {
	// TODO Init Right
	XM_servo_data_R.usart = &XM_USART_SERVO_R;
	// XM_servo_data_R.buffer = ...?
	// XM_servo_data_R.dreIntLevel = ...?

	XM_PORT_SERVO_R.DIRSET = PIN3_bm; // Pin3 von PortC (TXD0) ist Ausgang
	XM_PORT_SERVO_R.DIRCLR = PIN2_bm; // Pin2 von PortC (RXD0) ist Eingang
	XM_PORT_SERVO_R.DIRSET = XM_OE_MASK;
	XM_PORT_SERVO_R.OUTSET = XM_OE_MASK;

	/* Use USARTC0 and initialize buffers. */
	USART_InterruptDriver_Initialize(&XM_servo_data_R, &XM_USART_SERVO_R,
			USART_DREINTLVL_LO_gc);

	// USARTF0, 8 Data bits, No Parity, 1 Stop bit.
	USART_Format_Set(XM_servo_data_R.usart, USART_CHSIZE_8BIT_gc,
			USART_PMODE_DISABLED_gc, false);
	/* Enable RXC interrupt. */
	USART_RxdInterruptLevel_Set(XM_servo_data_R.usart, USART_RXCINTLVL_MED_gc);

	USART_Baudrate_Set(XM_servo_data_R.usart, 34, 0); // 57.600bps (BSEL = 34)

	/* Enable RX and TX. */
	USART_Rx_Enable(XM_servo_data_R.usart);
	USART_Tx_Enable(XM_servo_data_R.usart);

	USART_GetChar(XM_servo_data_R.usart); // Flush Receive Buffer

	// TODO Init Left
	XM_servo_data_L.usart = &XM_USART_SERVO_L;
	// XM_servo_data_L.buffer = ...?
	// XM_servo_data_L.dreIntLevel = ...?

	XM_PORT_SERVO_L.DIRSET = PIN3_bm; // Pin3 von PortC (TXD0) ist Ausgang
	XM_PORT_SERVO_L.DIRCLR = PIN2_bm; // Pin2 von PortC (RXD0) ist Eingang
	XM_PORT_SERVO_L.DIRSET = XM_OE_MASK;
	XM_PORT_SERVO_L.OUTSET = XM_OE_MASK;

	/* Use USARTC0 and initialize buffers. */
	USART_InterruptDriver_Initialize(&XM_servo_data_L, &XM_USART_SERVO_L,
			USART_DREINTLVL_LO_gc);

	// USARTF0, 8 Data bits, No Parity, 1 Stop bit.
	USART_Format_Set(XM_servo_data_L.usart, USART_CHSIZE_8BIT_gc,
			USART_PMODE_DISABLED_gc, false);
	/* Enable RXC interrupt. */
	USART_RxdInterruptLevel_Set(XM_servo_data_L.usart, USART_RXCINTLVL_MED_gc);

	USART_Baudrate_Set(XM_servo_data_L.usart, 34, 0); // 57.600bps (BSEL = 34)

	/* Enable RX and TX. */
	USART_Rx_Enable(XM_servo_data_L.usart);
	USART_Tx_Enable(XM_servo_data_L.usart);

	USART_GetChar(XM_servo_data_L.usart); // Flush Receive Buffer
}

void XM_init_com() {
	// TODO
}

void XM_USART_Send(USART_data_t* usart_data, byte* txdata, byte bytes) {
	byte i = 0;
	/* FIXME new way;
	 while (i < bytes) {
	 bool byteToBuffer;
	 byteToBuffer = USART_TXBuffer_PutByte(usart_data, *(txdata + i));
	 if (byteToBuffer) {
	 i++;
	 }
	 }
	 */
	// FIXME old way
	if (usart_data->usart == &XM_USART_SERVO_L) {
		XM_PORT_SERVO_L.OUTCLR = XM_OE_MASK;
		DEBUG(("OE_L=0;", sizeof("OE_L=0;")))
	}
	if (usart_data->usart == &XM_USART_SERVO_R) {
		XM_PORT_SERVO_R.OUTCLR = XM_OE_MASK;
		DEBUG(("OE_R=0;", sizeof("OE_R=0;")))
	}

	for (i = 0; i < bytes; i++) {
		while (!USART_IsTXDataRegisterEmpty(usart_data->usart))
			;
		USART_PutChar(usart_data->usart, txdata[i]);
	}

	if (usart_data->usart == &XM_USART_SERVO_L) {
		XM_PORT_SERVO_L.OUTSET = XM_OE_MASK;
		DEBUG(("OE_L=1;", sizeof("OE_L=1;")))
	}
	if (usart_data->usart == &XM_USART_SERVO_R) {
		XM_PORT_SERVO_R.OUTSET = XM_OE_MASK;
		DEBUG(("OE_R=1;", sizeof("OE_R=1;")))
	}
}
