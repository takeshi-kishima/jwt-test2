# これは
Spring Security、JWTの一例です。

[DEWA Kazuyuki/出羽和之さん](https://github.com/yukihane/hello-java/tree/main/spring/springboot-auth-example-202006 "すごくわかりやすい神！")　を参考にしてます（というかそのまま）

1. h2databaseインメモリでユーザを保持させて動いてます
1. O/RマッパーにJPA使ってます

## 動作を見るには
ユーザを新規追加して…
```
curl -H "Content-Type: application/json" -X POST -d '{
    "username": "takeshi",
    "password": "password"
}' http://localhost:4446/users/sign-up
```
追加したユーザでログインして…
```
curl -i -H "Content-Type: application/json" -X POST -d '{
    "username": "takeshi",
    "password": "password"
}' http://localhost:4446/login
```
ログインしたBearerトークンを使ってAPIが呼べる
```
curl -i -H "Authorization: Bearer <loginで取得したトークン文字列>" \
http://localhost:4446/users
```

です！
