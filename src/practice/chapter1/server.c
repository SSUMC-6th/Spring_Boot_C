#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <arpa/inet.h>

int main(){
    int server_socket, client_socket;
    int readLen;
    char rBuff[BUFSIZ];

    // 소켓 생성
    server_socket = socket(AF_INET, SOCK_STREAM, 0);
    if(server_socket == -1){
        perror("소켓 생성 실패\n");
        return 1;    
    }

    //
    struct sockaddr_in serverAddr, clientAddr;
    memset(&serverAddr, 0, sizeof(serverAddr));
    serverAddr.sin_family = AF_INET;         // IPv4 주소 체계
    serverAddr.sin_addr.s_addr = INADDR_ANY; // 모든 가능한 IP 주소
    serverAddr.sin_port = htons(9002); 

    // 서버 소켓 바인딩 설정
    if (bind(server_socket, (struct sockaddr*)&serverAddr, sizeof(serverAddr)) == -1) {
        perror("바인딩 실패");
        return 1;
    }

    // listen: 클라이언트의 connect 요청을 기다림
    int backlog = 5;    // 최대 클라이언트 수
    if(listen(server_socket, backlog) == -1){
        perror("대기 실패");
        return 1;
    }

    printf("서버가 클라이언트의 connect 요청을 기다리는 중...\n");

    socklen_t clientAddr_len = sizeof(client_socket);
    client_socket = accept(server_socket, (struct sockaddr *)&clientAddr, &clientAddr_len);

    if(client_socket == -1){
        perror("연결 실패\n");
        return 1;
    }
    printf("클라이언트 연결 성공\n");

    while(1){
		readLen = read(client_socket, rBuff, sizeof(rBuff)-1);
		rBuff[readLen] = '\0';
        printf("Client: %s", rBuff);
        write(client_socket, rBuff, strlen(rBuff));
    }
    close(client_socket);
    close(server_socket);

    return 0;
}