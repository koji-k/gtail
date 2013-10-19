# GTail
Groovyによるtail実装です。  
特にtailコマンドの無いWindows環境で開発をしている場合に役に立ちます。

## 実行環境
Groovy2.1以降が必要

## 実行方法
基本的にtailコマンドと同様です。

```
[koji:src]$ groovy GTail.groovy ../test/data/en_utf8.txt
This is en_utf8.txt
Hello
W
o
r
l
d
```

```
[koji:src]$ groovy GTail.groovy -n 5 ../test/data/en_utf8.txt
W
o
r
l
d
```


`-f`か`--force`を渡す事で、通常のtailコマンド同様リアルタイムにログを監視できます。  
その他、ファイルの文字コードを指定する事も出来ます。

```
[koji:src]$ groovy GTail.groovy -c sjis ../test/data/jp_sjis.txt
これは日本語だよ！
おらおら
今日もいい天気
明日は知らない
```

```
[koji:src]$ groovy GTail.groovy -c cp1252 ../test/data/de_cp1252.txt
äöüß
Ich heiße Koji!
Die Datei ist CP1252
```

