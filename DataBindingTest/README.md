# [DataBinding]( https://developer.android.google.cn/topic/libraries/data-binding )



#### 导入

1. 首先在 **`app`** 的`build.gradle`中引入 **`lifecycle components`**

   ```groovy
    // Lifecycle components  2.2.0-beta01
   implementation "androidx.lifecycle:lifecycle-extensions:2.2.0-beta01"
   annotationProcessor "androidx.lifecycle:lifecycle-compiler:2.2.0-beta01"
   ```

2. 在 **`app`** 的`build.gradle`中开启数据绑定

   ```groovy
   android {
       ...
       dataBinding {
           enabled = true
       }
   }
   ```



#### 基础使用

1. **java**文件中

   每个布局文件生成一个绑定类。默认情况下，类的名称基于布局文件的名称，将其转换为`Pascal`大小写并向其添加*`Binding`*后缀。上面的布局文件名是， **`activity_main.xml`** 相应的生成类是 **`ActivityMainBinding`**。 

   ```java
   //Activity
   // Inflate view and obtain an instance of the binding class.
   UserBinding binding = DataBindingUtil.setContentView(this, R.layout.user);
   
   // Specify the current activity as the lifecycle owner.
   binding.setLifecycleOwner(this);
   
   /*******************************************************/
   
   //ViewGroup
   MyLayoutBinding binding = MyLayoutBinding.inflate(getLayoutInflater(), viewGroup, false);
   
   // inside a Fragment, ListView, or RecyclerView adapter,
   ListItemBinding binding = ListItemBinding.inflate(layoutInflater, viewGroup, false);
   // or
   ListItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.list_item, viewGroup, false)
   
   //others    
   MyLayoutBinding binding = MyLayoutBinding.bind(viewRoot);
   
   View viewRoot = LayoutInflater.from(this).inflate(layoutId, parent, attachToParent);
   ViewDataBinding binding = DataBindingUtil.bind(viewRoot);
   
   ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
   
   ```

2. **xml**文件中

   ```xml
   <?xml version="1.0" encoding="utf-8"?>
   <layout xmlns:android="http://schemas.android.com/apk/res/android">
       
      <data>
          <variable name="user" type="com.example.User"/>
      </data>
       
      <LinearLayout
          android:orientation="vertical"
          android:layout_width="match_parent"
          android:layout_height="match_parent">
          <TextView android:layout_width="wrap_content"
              android:layout_height="wrap_content"
              android:text="@{user.firstName}"/>
          <TextView android:layout_width="wrap_content"
              android:layout_height="wrap_content"
              android:text="@{user.lastName}"/>
      </LinearLayout>
       
   </layout> 
   ```



#### 表达语言

1. 表达式等：

   ```xml
   android:text="@{String.valueOf(index + 1)}"
   android:visibility="@{age > 13 ? View.GONE : View.VISIBLE}"
   android:transitionName='@{"image_" + id}'
   
   android:text="@{user.displayName ?? user.lastName}"
   
   android:text="@{user.displayName != null ? user.displayName : user.lastName}"
   ```

2. 集合

   ```xml
   <data>
       <import type="android.util.SparseArray"/>
       <import type="java.util.Map"/>
       <import type="java.util.List"/>
       <variable name="list" type="List&lt;String>"/>
       <variable name="sparse" type="SparseArray&lt;String>"/>
       <variable name="map" type="Map&lt;String, String>"/>
       <variable name="index" type="int"/>
       <variable name="key" type="String"/>
   </data>
   …
   android:text="@{list[index]}"
   …
   android:text="@{sparse[index]}"
   …
   android:text="@{map[key]}"
   
   <!--可以使用单引号将属性值引起来，这允许您在表达式中使用双引号 -->
   android:text='@{map["firstName"]}'
   <!--也可以使用双引号将属性值引起来。这样做时，字符串文字应该用反引号引起来-->
   android:text="@{map[`firstName`]}"
   ```

3. 资源

   ```xml
   android:padding="@{large? @dimen/largePadding : @dimen/smallPadding}"
   
   android:text="@{@string/nameFormat(firstName, lastName)}"
   android:text="@{@plurals/banana(bananaCount)}"
   ```

4. 事件处理

   源文档有很多例子，这里就列举两个，更多跳转到源文档看吧 = =

   * eg1:

     ```java
     public class MyHandlers {
         public void onClickFriend(View view) { ... }
     }
     ```

     ```xml
     <?xml version="1.0" encoding="utf-8"?>
     <layout xmlns:android="http://schemas.android.com/apk/res/android">
        <data>
            <variable name="handlers" type="com.example.MyHandlers"/>
            <variable name="user" type="com.example.User"/>
        </data>
        <LinearLayout
            android:orientation="vertical"
            android:layout_width="match_parent"
            android:layout_height="match_parent">
            <TextView android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="@{user.firstName}"
                android:onClick="@{handlers::onClickFriend}"/>
        </LinearLayout>
     </layout>
     ```

   * eg2:

     ```java
     public class Presenter {
         public void onSaveClick(Task task){}
     }
     ```

     ```xml
     
     <?xml version="1.0" encoding="utf-8"?>
     <layout xmlns:android="http://schemas.android.com/apk/res/android">
         <data>
             <variable name="task" type="com.android.example.Task" />
             <variable name="presenter" type="com.android.example.Presenter" />
         </data>
         <LinearLayout android:layout_width="match_parent" android:layout_height="match_parent">
             <Button android:layout_width="wrap_content" android:layout_height="wrap_content"
             android:onClick="@{() -> presenter.onSaveClick(task)}" />
         </LinearLayout>
     </layout>
     ```
   
5. **[BindingAdapter]( https://developer.android.google.cn/topic/libraries/data-binding/binding-adapters )**适配器

   ```java
   //第一个参数确定与属性关联的视图的类型。第二个参数确定给定属性在绑定表达式中接受的类型。
   //发生冲突时，您定义的绑定适配器将覆盖Android框架提供的默认适配器。
   @BindingAdapter("android:paddingLeft")
   public static void setPaddingLeft(View view, int padding) {
     view.setPadding(padding,
                     view.getPaddingTop(),
                     view.getPaddingRight(),
                     view.getPaddingBottom());
   }
   
   @BindingAdapter({"imageUrl", "error"})
   public static void loadImage(ImageView view, String url, Drawable error) {
     Picasso.get().load(url).error(error).into(view);
   }
   
   //使用示例
   <ImageView app:imageUrl="@{venue.imageUrl}" app:error="@{@drawable/venueError}" />
   
   //如果希望在设置任何属性时调用适配器，则可以将适配器的可选requireAll 标志设置 为false，默认为true
   @BindingAdapter(value={"imageUrl", "placeholder"}, requireAll=false)
   public static void setImageUrl(ImageView imageView, String url, Drawable placeHolder) {
     if (url == null) {
       imageView.setImageDrawable(placeholder);
     } else {
       MyImageLoader.loadInto(imageView, url, placeholder);
     }
   }
   ```
   
   

