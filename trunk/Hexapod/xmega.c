/*
 * xmega.c
 *
 *  Created on: 30.09.2010
 *      Author: christof
 */

#include "include/xmega.h"

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
	XM_PORT_DEBUG.DIRSET = PIN3_bm; // Pin3 von PortF (TXD0) ist Ausgang
	XM_PORT_DEBUG.DIRCLR = PIN2_bm; // Pin2 von PortF (RXD0) ist Eingang

	// Use USART and initialize buffers
	USART_InterruptDriver_Initialize(&XM_debug_data, &XM_USART_DEBUG,
			USART_DREINTLVL_OFF_gc);
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

	// Init LED
	XM_PORT_LED.DIRSET = XM_LED_MASK;
	//XM_LED_OFF
}

void XM_init_dnx() {
	//Disable Interrupts
	//cli();
	// Set pins for TX and RX
	XM_PORT_SERVO_R.DIRSET = PIN3_bm; // Pin3 of PortC (TXD0) is output
	XM_PORT_SERVO_R.DIRCLR = PIN2_bm; // Pin2 of PortC (RXD0) is input

	XM_PORT_SERVO_L.DIRSET = PIN3_bm;
	XM_PORT_SERVO_L.DIRCLR = PIN2_bm;

	// Set pin, dir and out for OE
	XM_PORT_SERVO_R.DIRSET = XM_OE_MASK;
	XM_PORT_SERVO_R.OUTSET = XM_OE_MASK;

	XM_PORT_SERVO_L.DIRSET = XM_OE_MASK;
	XM_PORT_SERVO_L.OUTSET = XM_OE_MASK;

	// Use USARTC0 / USARTD0 and initialize buffers
	USART_InterruptDriver_Initialize(&XM_servo_data_R, &XM_USART_SERVO_R,
			USART_DREINTLVL_LO_gc);
	USART_InterruptDriver_Initialize(&XM_servo_data_L, &XM_USART_SERVO_L,
			USART_DREINTLVL_LO_gc);

	// 8 Data bits, No Parity, 1 Stop bit
	USART_Format_Set(XM_servo_data_R.usart, USART_CHSIZE_8BIT_gc,
			USART_PMODE_DISABLED_gc, false);
	USART_Format_Set(XM_servo_data_L.usart, USART_CHSIZE_8BIT_gc,
			USART_PMODE_DISABLED_gc, false);

	//USART_DreInterruptLevel_Set(XM_servo_data_R.usart, USART_DREINTLVL_LO_gc);
	//USART_DreInterruptLevel_Set(XM_servo_data_L.usart, USART_DREINTLVL_LO_gc);


	// Enable TXC interrupt
	//USART_TxdInterruptLevel_Set(XM_servo_data_R.usart, USART_TXCINTLVL_LO_gc);
	//USART_TxdInterruptLevel_Set(XM_servo_data_L.usart, USART_TXCINTLVL_LO_gc);

	// Enable RXC interrupt
	//USART_RxdInterruptLevel_Set(XM_servo_data_R.usart, USART_RXCINTLVL_LO_gc);
	USART_RxdInterruptLevel_Set(XM_servo_data_L.usart, USART_RXCINTLVL_LO_gc);

	// Set Baudrate
	USART_Baudrate_Set(XM_servo_data_R.usart, 1, 0); // 57.600bps (BSEL = 34)
	USART_Baudrate_Set(XM_servo_data_L.usart, 1, 0); // 57.600bps (BSEL = 34)

	// Enable RX and TX
	USART_Rx_Enable(XM_servo_data_R.usart);
	USART_Rx_Enable(XM_servo_data_L.usart);

	USART_Tx_Enable(XM_servo_data_R.usart);
	USART_Tx_Enable(XM_servo_data_L.usart);

	// Flush Receive Buffer
	USART_GetChar(XM_servo_data_R.usart); // Flush Receive Buffer
	USART_GetChar(XM_servo_data_L.usart); // Flush Receive Buffer

	// Enable PMIC interrupt level low
	PMIC.CTRL |= PMIC_LOLVLEX_bm;

	// Enable global interrupts
	sei();

}

void XM_init_com() {
	// TODO
}

void XM_USART_Send(USART_data_t* usart_data, byte* txdata, byte bytes) {
	// ATTENTION! NO DEBUG OUT POSSIBLE -> INFINITE LOOP!
	if (usart_data->usart == &XM_USART_DEBUG)
		return;

	XM_sendCount = bytes;
	XM_sendCurrentCount = 0;
	XM_sendBuffer = txdata;

	XM_receiveCount = 0;

	// Set OE zo 0
	if (usart_data->usart == &XM_USART_SERVO_L) {
		XM_PORT_SERVO_L.OUTCLR = XM_OE_MASK;
	}
	if (usart_data->usart == &XM_USART_SERVO_R) {
		XM_PORT_SERVO_R.OUTCLR = XM_OE_MASK;
	}

	// Send data
	while (!USART_IsTXDataRegisterEmpty(usart_data->usart))
		;

	// Enable DRE-Interrupt with activation of TXD-Interrupt
	USART_DreInterruptLevel_Set(usart_data->usart, USART_DREINTLVL_LO_gc);
}

ISR(USARTC0_DRE_vect) {
	// Send IRQ
	if (XM_sendCurrentCount < XM_sendCount) // Alles gesendet?
		USART_PutChar(&USARTC0, XM_sendBuffer[XM_sendCurrentCount++]); // TX Buffer fuellen

	else {
		USART_DreInterruptLevel_Set(&USARTC0, USART_DREINTLVL_OFF_gc); // DRE-IRQ sperren
		USART_TxdInterruptLevel_Set(&USARTC0, USART_TXCINTLVL_LO_gc); // Trans-IRO scharf machen
	}
}

ISR(USARTC0_TXC_vect) {
	// Ende des senden
	DEBUG(("ISR_TXC;",sizeof("ISR_TXC;")))
	XM_PORT_SERVO_L.OUTSET = XM_OE_MASK;
	USART_TxdInterruptLevel_Set(&USARTC0, USART_TXCINTLVL_OFF_gc);
}

ISR(USARTC0_RXC_vect) {
	DEBUG(("ISR_RXC;",sizeof("ISR_RXC;")))
	 // char* rx_buffer = {0x00};
	 USART_RXComplete(&XM_servo_data_L);
	 if (USART_RXBufferData_Available(&XM_servo_data_L)) {
	 // copy buffer to IRmsgRx
		 XM_RX_buffer_L[XM_receiveCount++] = USART_RXBuffer_GetByte(&XM_servo_data_L);
		 DEBUG(("ISR_RXC2;",sizeof("ISR_RXC2;")))
	 }
}
