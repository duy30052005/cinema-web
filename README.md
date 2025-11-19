# cinema-web

## 1. Đăng kí

**curl -X POST --location "http://localhost/cinema/users"** \
**Header** "Content-Type: application/json" \
-**request** 
{\
"username" : " ",\
"password" : " ",\
"email" : " "\
}'\
-**response** {\
&emsp;"code": ,\
&emsp;"result": {\
&emsp;&emsp;&emsp;"userId": ,\
&emsp;&emsp;&emsp;"username": " ",\
&emsp;&emsp;&emsp;"password": " ",\
&emsp;&emsp;&emsp;"email": " ",\
&emsp;&emsp;&emsp;"role": " ",\
&emsp;&emsp;&emsp;"sentMessages": ,\
&emsp;&emsp;&emsp;"receivedMessages": \
&emsp;}\
}
## 2. Đăng nhập

**curl -X POST --location "http://localhost/cinema/auth/token"** \
**Header** "Content-Type: application/json" \
-**request**
{\
&emsp;"username" : " ",\
&emsp;"password" : " ",\

}'\
-**response** {\
&emsp;"code": ,\
&emsp;&emsp;"result": {\
&emsp;&emsp;&emsp;"token": " " ,\
&emsp;&emsp;&emsp;"authenticated": " "\
&emsp;&emsp;}\
&emsp;}
## 3. Gửi mã về Gmail

**curl -X POST --location "http://localhost/cinema/users"** \
**Header** "Content-Type: application/json" \
-**request**
{\
&emsp;"gmail" : " "\
}'\
-**response** {\
&emsp;"code": ,\
&emsp;&emsp;"result": {\
&emsp;&emsp;&emsp;"userId": ,\
&emsp;&emsp;&emsp;"username": "",\
&emsp;&emsp;&emsp;"password": "",\
&emsp;&emsp;&emsp;"email": "",\
&emsp;&emsp;&emsp;"role": "",\
&emsp;&emsp;&emsp;"sentMessages": ,\
&emsp;&emsp;&emsp;"receivedMessages": \
&emsp;}\
}
