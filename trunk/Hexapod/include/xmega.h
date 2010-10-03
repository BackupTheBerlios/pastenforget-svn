/*
 * xmega.h
 *
 *  Created on: 30.09.2010
 *      Author: christof
 */

#ifndef XMEGA_H_
#define XMEGA_H_

#include "avr/io.h"
#include "avr_compiler.h"
#include "usart_driver.h"
#include "clksys_driver.h"
#include "utils.h"

#define XM_PORT_SERVO_L PORTC
#define XM_PORT_SERVO_R PORTD
#define XM_PORT_DEBUG PORTF

#define XM_USART_SERVO_L USARTC0
#define XM_USART_SERVO_R USARTD0
#define XM_USART_DEBUG USARTF0

USART_data_t XM_servo_data_L;
USART_data_t XM_servo_data_R;
USART_data_t XM_debug_data;

byte XM_RX_buffer_L[256];
byte XM_RX_buffer_R[256];

void XM_init_cpu();
void XM_init_dnx();
void XM_init_com();
void XM_USART_Send(USART_data_t *usart_data, uint8_t* txdata, uint8_t bytes);
#endif /* XMEGA_H_ */
