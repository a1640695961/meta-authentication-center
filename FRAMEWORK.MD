## 基于统一认证中心的模型重新定义

##### 用户名密码授权方式
curl -vu rajithapp:secret -X POST 'http://127.0.0.1:40520/ac/oauth/token?username=admin&password=admin&grant_type=password&scope=client'


curl -vu rajithapp:secret -X POST 'http://127.0.0.1:40520/ac/oauth/token?grant_type=password&username=admin&password=admin&scope=client'




curl  -X POST 'http://127.0.0.1:40520/ac/oauth/authorize?response_type=code&client_id=rajithapp&scope=client&redirect_uri=www.baidu.com'&access_token=f7bd4ba3-290f-4bdf-9f47-757553c76b0a


此应用的themlef中如果使用应用内图片会出现莫名奇妙的授权失败问题


#### 自定义授权方式

curl -vu rajithapp:secret -X POST 'http://127.0.0.1:40520/ac/oauth/token?username=admin&password=admin&grant_type=login_free&scope=client'




curl -vu rajithapp:secret -X POST 'http://127.0.0.1:40520/ac/oauth/token?company_id=0f56fe65-9bc1-4046-ac37-0d3cb3771303&mobile_no=18888888888&login_free_key=111111&timestamp=1&grant_type=login_free&scope=client'