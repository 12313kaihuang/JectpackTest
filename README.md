# [ArchitectureComponentsTest]( https://developer.android.google.cn/topic/libraries/architecture )——架构组件的一些demo


## Demos

* [RoomTest](/RoomTest)  Room数据库Demo
* [DataBindingTest](/DataBindingTest)  数据绑定Demo

## 导入相关

1. 根目录的**`build.gradle`**加入`google repository`

   ```groovy
   allprojects {
       repositories {
           google()  //add google repository
           jcenter()
       }
   }
   ```

2. 根目录的**`build.gradle`**或自定义的`gradle`文件定义全局版本号：

   ```groovy
   ext {
       roomVersion = '2.2.0'
       archLifecycleVersion = '2.2.0-beta01'
       coreTestingVersion = '2.1.0'
       materialVersion = '1.0.0'
   }
   ```

3. `app`的**`build.gradle`**中导入依赖

   ```groovy
   // Room components
   implementation "androidx.room:room-runtime:$rootProject.roomVersion"
   annotationProcessor "androidx.room:room-compiler:$rootProject.roomVersion"
   androidTestImplementation "androidx.room:room-testing:$rootProject.roomVersion"
   
   // Lifecycle components
   implementation "androidx.lifecycle:lifecycle-extensions:$rootProject.archLifecycleVersion"
   annotationProcessor "androidx.lifecycle:lifecycle-compiler:$rootProject.archLifecycleVersion"
   
   // UI
   implementation "com.google.android.material:material:$rootProject.materialVersion"
   
   // Testing
   androidTestImplementation "androidx.arch.core:core-testing:$rootProject.coreTestingVersion"
   ```
