/*
 * dynamixel.c
 *
 * Created on: 27.09.2010
 * Author: christof
 */

#include "include/dynamixel.h"
#include "include/xmega.h"

#define START_BYTE 0xFF

// Instruction Set (Manual page 19)
#define PING 0x01
#define RD_DATA 0x02
#define WR_DATA 0x03
#define REG_WR 0x04
#define ACT 0x05
#define RESET 0x06
#define SYC_WR 0x83

// Control Table (Manuel page 12) L/H?
#define ID 0x03
#define BD 0x04
#define MAX_TMP 0x0B
#define STS_RT_LVL 0x10
#define ALR_SHUTDWN 0x12
#define GL_POS 0x1E
#define LED 0x19
#define MV_SPEED 0x20
#define PRT_POS 0x24
#define PRT_SPEED 0x26
#define PRT_TMP 0x2B

byte DNX_getChecksum(byte* packet, byte l) {
	byte i, chksm = 0;
	for (i = 2; i < l - 1; i++)
		chksm += packet[i];
	return ~chksm;
}

void DNX_send(byte* packet, byte l) {
	packet[l - 1] = DNX_getChecksum(packet, l);

	// FIXME
	//DEBUG(("packet send;", sizeof("packet send;")))
	XM_USART_Send(&XM_servo_data_L, packet, l);
	/*
	 if (packet[2] < 4) { // use right servo
	 XM_USART_Send(XM_USART_SERVO_L, packet, l);
	 } else { // use left servo
	 XM_USART_Send(XM_USART_SERVO_R, packet, l);
	 }
	 */
}

void DNX_receive(byte* packet) {
	// TODO
	;
}


void DNX_sendTest() {
	byte packet[8];
	packet[0] = START_BYTE;
	packet[1] = START_BYTE;
	packet[2] = 0x00;
	packet[3] = 0xAA; // length
	packet[4] = 0x00;
	packet[5] = START_BYTE;
	packet[6] = START_BYTE;
	// packet[7] = checksum will set in send
	DNX_send(packet, 8);
}

void DNX_setAngle(double value, byte id) {
	byte packet[8];
	byte angle; // = calculate value
	packet[0] = START_BYTE;
	packet[1] = START_BYTE;
	packet[2] = id;
	packet[3] = 0x04; // length
	packet[4] = WR_DATA;
	packet[5] = GL_POS;
	packet[6] = angle;
	// packet[7] = checksum will set in send
	DNX_send(packet, 8);
}

void DNX_setId(byte idOld, byte idNew) {
	byte packet[8];
	packet[0] = START_BYTE;
	packet[1] = START_BYTE;
	packet[2] = idOld;
	packet[3] = 0x04; // length
	packet[4] = WR_DATA;
	packet[5] = ID;
	packet[6] = idNew;
	// packet[7] = checksum will set in send
	DNX_send(packet, 8);

}

void DNX_setSpeed(byte speed, byte id) {
	byte packet[8];
	packet[0] = START_BYTE;
	packet[1] = START_BYTE;
	packet[2] = id;
	packet[3] = 0x04; // length
	packet[4] = WR_DATA;
	packet[5] = MV_SPEED;
	packet[6] = speed;
	// packet[7] = checksum will set in send
	DNX_send(packet, 8);
}

void DNX_setLed(byte value, byte id) {
	byte packet[8];
	packet[0] = START_BYTE;
	packet[1] = START_BYTE;
	packet[2] = id;
	packet[3] = 0x04; // length
	packet[4] = WR_DATA;
	packet[5] = LED;
	packet[6] = value;
	// packet[7] = checksum will set in send
	DNX_send(packet, 8);
}

double DNX_getAngle(byte id) {
	// TODO
	byte packet[7];
	packet[0] = START_BYTE;
	packet[1] = START_BYTE;
	packet[2] = id;
	packet[3] = 0x03; // length
	packet[4] = RD_DATA;
	packet[5] = PRT_POS;
	// packet[6] = checksum will set in send
	DNX_send(packet, 7);
	// TODO DNX_receive();
	return -1;
}

byte DNX_getSpeed(byte id) {
	byte packet[7];
	packet[0] = START_BYTE;
	packet[1] = START_BYTE;
	packet[2] = id;
	packet[3] = 0x03; // length
	packet[4] = RD_DATA;
	packet[5] = PRT_SPEED;
	// packet[6] = checksum will set in send
	DNX_send(packet, 7);
	// TODO DNX_receive();
	return 0x00;
}

byte DNX_getLed(byte id) {
	byte packet[7];
	packet[0] = START_BYTE;
	packet[1] = START_BYTE;
	packet[2] = id;
	packet[3] = 0x03; // length
	packet[4] = RD_DATA;
	packet[5] = LED;
	// packet[6] = checksum will set in send
	DNX_send(packet, 7);
	// TODO DNX_receive();
	return 0x00;
}
