#include <stdio.h>
#include <stdlib.h>
#include <arpa/inet.h>

int main(){
    int client_socket;
    struct sockaddr_in clientAddr;
    int clientAddr_len, readLen, recvByte, maxBuff;
    char message[BUFSIZ];
    char rBuff[BUFSIZ];

    client_socket = socket(AF_INET, SOCK_STREAM, 0);

    printf("===client===\n");

    clientAddr.sin_family = AF_INET;         // IPv4 주소 체계
    clientAddr.sin_addr.s_addr = INADDR_ANY; // 모든 가능한 IP 주소
    clientAddr.sin_port = htons(9002); 

    if(connect(client_socket, (struct sockaddr *)&clientAddr, sizeof(clientAddr)) == -1){
        perror("연결 오류\n");
        return 1;
    }

    while(1){
        printf("메시지 입력: ");
		fgets(message,BUFSIZ-1,stdin);

        if(strncmp(message, "END", 3) == 0) break;

		send(client_socket, message, BUFSIZ-1, 0);
    }
    close(client_socket);

    return 0;
}