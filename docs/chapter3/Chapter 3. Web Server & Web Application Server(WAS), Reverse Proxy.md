
>[!topic]
>Web Server와 Web application Server의 차이 이해하기
>Reverse Proxy 이해하기

# WAS
Web Application Server
정의
- WAS는 클라이언트 요청에 대해 적절한 데이터를 만들어주는 서버를 Web Application Server라고 부른다. 

Node.js, SpringBoot가 WAS이다. 

**EC2, NGINX 사용**
- aws EC2의 보안 그룹에서 TCP 80번 포트를 열어둔 이유
	-  EC2의 아이피 주소를 통해, EC2를 식별 후 80번 포트로 열린(Web server인)NGINX로 요청을 보내기 위함이다. 

## NGINX에서의 정적콘텐츠 호스팅

NginX에서는 설정 파일이 있고 웹 서버가 실행 때 이 설정 파일을 읽으면서 실행이 된다. 

> ubuntu 기준, /etc/nginx/sites-available 디렉토리에서 default가 nginx의 설정 파일이다.

**root /var/www/html**을 통해 정적 콘텐츠를 찾아낼 **시작 디렉토리를 설정**하고
**index**를 통해 **기본 요청이 온 경우 어떤 파일을 줄지 설정**하여,
**/ 요청에 대해**
**/var/www/html/index.nginx-debian.thml을 응답으로 준 것을 확인**할 수 있습니다.

```
location /temp{ 
	root /var/www; 
	index temp.html 
	try_files $uri $uri/ =404; 
}
```
location 블락은 특정 요청 uri가 들어왔을 때 어떻게 처리할지 결정하는 섹션이다. 
`location /temp`는 URI가 `/temp`로 시작하는 모든 요청을 처리합니다. 이 블록 내의 설정은 다음과 같다.

1. `root /var/www;`
    - 이 지시어는 요청받은 파일의 루트 디렉토리를 `/var/www`로 설정한다. 따라서, 요청 URI와 `root` 지시어가 결합하여 실제 파일 시스템 상의 경로를 만든다.
    - 
1. `index temp.html;`
    - 이 지시어는 `/temp` 경로로 접속했을 때 기본적으로 제공할 파일을 `temp.html`로 설정한다. 예를 들어, 사용자가 `http://your-domain.com/temp`에 접근하면, Nginx는 `/var/www/temp/temp.html` 파일을 반환하려고 시도한다.
    - 
1. `try_files $uri $uri/ =404;`
    - 이 지시어는 실제로 파일이나 디렉토리가 존재하는지를 체크한다. `$uri`는 요청받은 URI를 나타내고, `$uri/`는 해당 URI가 디렉토리를 가리킬 경우를 처리한다. 만약 파일이나 디렉토리가 존재하지 않으면, `=404` 지시어에 의해 404 Not Found 에러를 반환한다.


![[Pasted image 20240429131220.png]]
![[Pasted image 20240429132114.png]]
![[Pasted image 20240429132137.png]]
![[Pasted image 20240429132344.png]]
# WAS 이해해보기

> www.naver.com은 www.naver.com:80으로 즉, 웹 서버로 요청을 보내는 건데, 왜 응답은 WAS에서 주지???

www.naver.com 은 먼저 DNS를 통해 아이피 주소로 변환이 된다. 
-> ==<네이버 서버 컴퓨터 ip주소 : 웹 서버의 프로세스가 부여 받은 포트번호>==
로 요청을 보내는 것이다. 

## Reverse Proxy

정의 
- 클라이언트와 서버 간의 통신을 중개하고 보안, 성능 개선 등의 목적을 위해 중간에 위치하는 서버를 말한다. 

서버는 하나의 컴퓨터이기 때문에 응답을 받기만 하는 것이 아니고 내부적으로 connect() 시스템 콜과 같은 요청을 보내는 시스템 콜을 통해 다른 서버 프로세스에게 다시 요청을 보낼 수 있다. 

**포워드 프록시 vs 리버스 프록시**

포워드 프록시 : 또 다른 외부(다른 컴퓨터) 서버로 요청을 보내준다. 
리버스 프록시 : 내부 (같은 컴퓨터)의 다른 서버로 요청을 보내준다. 