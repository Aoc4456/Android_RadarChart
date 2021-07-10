# Android Repository Template
「チームで育てるAndroidアプリ設計」 4.4.1 リポジトリテンプレートを作ろう　を参考にして作成したテンプレート. 
https://peaks.cc/books/architecture_with_team

## 使い方

### パッケージ名の変更

① `app/build.gradle` と ② `app/src/main/AndroidManifest.xml` にある "com.example.repositorytemplate" を任意のパッケージ名に変更する

① app/build.gradle

```
  defaultConfig {
    applicationId "com.example.repositorytemplate" // ← 修正
    ...
  }
```

② app/src/main/AndroidManifest.xml

```
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
  package="com.example.repositorytemplate"> //  ← 修正
```

### Firebase のセットアップ
 1. [Firebaseプロジェクトをセットアップ](https://firebase.google.com/docs/android/setup?hl=ja#console)
 2. `app/google-services.json`をプロジェクトに追加 (.gitignore に記載されているため、リモートリポジトリで管理しない)


### Circle CIのセットアップ

次の環境変数をcircleciに設定する。

| 環境変数名 | 説明 | 取得方法 |
| --- | --- | --- |
| FIREBASE_TOKEN | Firebase App Distributionで使用するToken |  [手順 2. Firebase で認証する](https://firebase.google.com/docs/app-distribution/android/distribute-gradle?hl=ja#step_2_authenticate_with_firebase) |
| GOOGLE_SERVICES_JSON | base64でエンコードしたgoogle-services.json | [GithubActionsでgitignoreしたgoogle-services.jsonを読み込む](https://qiita.com/sudo5in5k/items/5b6da5dbba3fc2514319) |

## このテンプレートの機能

### ktlint
- ビルド時にLintCheckする。違反があればビルドが失敗する。

### CI/CD (CircleCI , Firebase App Distribution)
- プッシュしたとき、UnitTest,androidTest が実行される
- 加えて、masterブランチへのプッシュ時は、Firebase App Distribution に debug.apk が配布される

### ライブラリ
- Timber (ログ出力)

### その他
- gitIgnore が配置済み
- debug.keystore が配置済み
- buildTypes : debug は applicationId、labelの末尾に.debug がつく
- 仮のアイコンが設定済み

## ルール
以下のコーディング規約に従う  
(命名については、Kotlin スタイルガイドに従う)
- Android Developer Kotlin スタイルガイド   
https://developer.android.com/kotlin/style-guide?hl=ja  


- コントリビューター向け AOSP Java コードスタイル  
https://source.android.com/setup/contribute/code-style#follow-field-naming-conventions
