# NavigationTest
[Navigation](https://developer.android.google.cn/guide/navigation/)的简单使用Demo<br/>

注意：如果要使用导航和 Android Studio，则必须使用 Android Studio 3.3 或更高版本。

* Navigation Graph
* NavHostFragment
* NavController

## 导航概述
导航组件由三个关键部分组成，它们相互协调工作。他们是：
* 导航图（新XML资源）-这是一种资源，它在一个集中位置包含所有与导航有关的信息。这包括应用程序中的所有位置，称为destinations，以及用户可以通过您的应用程序选择的可能路径。
* NavHostFragment（布局XML视图）-这是添加到布局中的特殊小部件。它从您的导航图显示不同的目的地。
* NavController（Kotlin / Java对象）-这是一个跟踪导航图中当前位置的对象。NavHostFragment当您浏览导航图时，它会在中协调交换目标内容。

导航时，将使用NavController对象，在“导航图”中告诉它您想去的地方或要走的路径。然后，NavController它将在NavHostFragment中显示适当的目的地。


## 导入
获取[最新版本](https://developer.android.google.cn/jetpack/androidx/releases/navigation)。<br/>

在`app`的 **`build.gradle`** 中添加依赖：
```groovy
dependencies {
  def nav_version = "2.1.0"

  // Java
  implementation "androidx.navigation:navigation-fragment:$nav_version"
  implementation "androidx.navigation:navigation-ui:$nav_version"

  // Kotlin
  implementation "androidx.navigation:navigation-fragment-ktx:$nav_version"
  implementation "androidx.navigation:navigation-ui-ktx:$nav_version"

}
```
## 简单跳转

## 传递参数


## 参考文章
* [Navigation](https://developer.android.google.cn/guide/navigation/)
* [导航原则](https://developer.android.google.cn/guide/navigation/navigation-principles)
* [Navigation使用入门](https://developer.android.google.cn/guide/navigation/navigation-getting-started)
* [导航 Codelab](https://codelabs.developers.google.com/codelabs/android-navigation/index.html?index=..%2F..%2Findex#0)