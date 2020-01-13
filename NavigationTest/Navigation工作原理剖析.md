# Navigation工作原理剖析

## Navigation工作流程 ##

从`NavHostFragment`开始，它是一个内容承载容器，从它的`onCreate()`方法开始看起：

```java
@CallSuper
@Override
public void onCreate(@Nullable Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	final Context context = requireContext();

    //A-1 -- NavHostController页面导航，页面跳转核心类  一个入口  
    //在这里将内容切换的能力委托给了NavHostController
	mNavController = new NavHostController(context);
	mNavController.setLifecycleOwner(this);
	mNavController.setOnBackPressedDispatcher(requireActivity().getOnBackPressedDispatcher());
	// Set the default state - this will be updated whenever
	// onPrimaryNavigationFragmentChanged() is called
	mNavController.enableOnBackPressed(
			mIsPrimaryBeforeOnCreate != null && mIsPrimaryBeforeOnCreate);
	mIsPrimaryBeforeOnCreate = null;
	mNavController.setViewModelStore(getViewModelStore());
    //A-2 -- onCreateNavController
	onCreateNavController(mNavController);

	...
}

@CallSuper
protected void onCreateNavController(@NonNull NavController navController) {
    //3.DialogFragmentNavigator
	navController.getNavigatorProvider().addNavigator(
			new DialogFragmentNavigator(requireContext(), getChildFragmentManager()));
    //4.FragmentNavigator
	navController.getNavigatorProvider().addNavigator(createFragmentNavigator());
}

protected Navigator<? extends FragmentNavigator.Destination> createFragmentNavigator() {
    return new FragmentNavigator(requireContext(), getChildFragmentManager(), getId());
}
```

```java
//NavHostController.java
public NavHostController(@NonNull Context context) {
    super(context);
}

//NavController.java
public NavController(@NonNull Context context) {
    mContext = context;
    while (context instanceof ContextWrapper) {
        if (context instanceof Activity) {
            mActivity = (Activity) context;
            break;
        }
        context = ((ContextWrapper) context).getBaseContext();
    }
    //1.NavGraphNavigator  注意其构造函数中又把NavigatorProvider传递了进去
    mNavigatorProvider.addNavigator(new NavGraphNavigator(mNavigatorProvider));
    //2.ActivityNavigator
    mNavigatorProvider.addNavigator(new ActivityNavigator(mContext));
}
```

### NavController ###

再`NavHostFragment`的`onCreate()`方法中，将**内容切换**的能力委托给了**NavHostController**，先是创建了一个`NavHostController`，接着调用了 **`onCreateNavController`** 方法。其中一共添加了**4**个**Navigator**（两个方法各添加了2个）进去，`Navigator`，顾名思义，是导航器的意思，这四个导航器分别有着不同的职责：

* **NavGraphNavigator** —— 当页面资源信息被解析完成之后，会通过它跳转到配置的默认启动页，也就是第一个也页面。
* **ActivityNavigator** —— 给`Activity`提供跳转、切换的能力。
* **DialogFragmentNavigator ** ——  给`DialogFragment`提供跳转、切换的能力。
* **FragmentNavigator** —— 给`Fragment`提供跳转、切换的能力。

代码中的 **`mNavigatorProvider`** 内部其实是通过 **`HashMap`** 以键值对的形式存储了一个个导航器（`Navigator`）。

```java
//NavigatorProvider.java
public class NavigatorProvider {
    
    private final HashMap<String, Navigator<? extends NavDestination>> mNavigators =
            new HashMap<>();
    
    public final Navigator<? extends NavDestination> addNavigator(
            @NonNull Navigator<? extends NavDestination> navigator) {
        //去取Navigator的NAME注解
        String name = getNameForNavigator(navigator.getClass());

        return addNavigator(name, navigator);
    }
    
    public Navigator<? extends NavDestination> addNavigator(@NonNull String name,
            @NonNull Navigator<? extends NavDestination> navigator) {
        if (!validateName(name)) {
            throw new IllegalArgumentException("navigator name cannot be an empty string");
        }
        return mNavigators.put(name, navigator);
    }
    
    @NonNull
    static String getNameForNavigator(@NonNull Class<? extends Navigator> navigatorClass) {
        String name = sAnnotationNames.get(navigatorClass);
        if (name == null) {
            Navigator.Name annotation = navigatorClass.getAnnotation(Navigator.Name.class);
            name = annotation != null ? annotation.value() : null;
            if (!validateName(name)) {
                throw new IllegalArgumentException("No @Navigator.Name annotation found for "
                        + navigatorClass.getSimpleName());
            }
            sAnnotationNames.put(navigatorClass, name);
        }
        return name;
    }
    
    ...
}
```

### Navigator ###

```java
//Navigator.java
//需要传递一个NavDestination类型的泛型 -- 其作用是为了限定一种Navigator只能创建一种Destination节点信息。
//一个Destination可以代表一个个的页面（activity、fragment、dialog等）
public abstract class Navigator<D extends NavDestination> {
    
    /**
     * This annotation should be added to each Navigator subclass to denote the default name used
     * to register the Navigator with a {@link NavigatorProvider}.
     * 应该将此注释添加到每个Navigator子类中，以表示用{@link NavigatorProvider}注册导航器时使用的默认名称。
     *
     * 作用：
     *   1. 用于NavigatorProvider中存储Navigator所需的key值,value为Navigator本身。
     *   2. 用于Destination中的跳转（跳转需是要根据key值去取相应的Destination）
     *
     *
     * @see NavigatorProvider#addNavigator(Navigator)
     * @see NavigatorProvider#getNavigator(Class)
     */
    @Retention(RUNTIME)
    @Target({TYPE})
    @SuppressWarnings("UnknownNullness") // TODO https://issuetracker.google.com/issues/112185120
    public @interface Name {
        String value();
    }
    
    // 具体的页面跳转
    public abstract NavDestination navigate(@NonNull D destination, @Nullable Bundle args,
            @Nullable NavOptions navOptions, @Nullable Extras navigatorExtras);
    
    //需不需要拦截系统返回键，来做一些回退栈的操作
    public abstract boolean popBackStack();
    
    //状态保存
    @Nullable
    public Bundle onSaveState() {
        return null;
    }
    
    //状态恢复
    public void onRestoreState(@NonNull Bundle savedState) {
    }
    
    /**
     * Interface indicating that this class should be passed to its respective
     * 
     * {@link Navigator} to enable Navigator specific behavior. 跳转时可以提供一些额外的行为，如过度元素，转场动画等。
     */
    public interface Extras {
    }
}
```

```java
public class NavDestination {
    public NavDestination(@NonNull String navigatorName) {
        mNavigatorName = navigatorName;
    }
}
```

#### ActivityNavigator ####

```java
//ActivityNavigator.java
@Navigator.Name("activity")
public class ActivityNavigator extends Navigator<ActivityNavigator.Destination> {
    
    @NonNull
    @Override
    public Destination createDestination() {
        //一个静态内部类
        return new Destination(this);
    }
    
    @Nullable
    @Override
    public NavDestination navigate(@NonNull Destination destination, @Nullable Bundle args,
            @Nullable NavOptions navOptions, @Nullable Navigator.Extras navigatorExtras) {
        if (destination.getIntent() == null) {
            throw new IllegalStateException("Destination " + destination.getId()
                    + " does not have an Intent set.");
        }
        Intent intent = new Intent(destination.getIntent());
        ...
        if (navigatorExtras instanceof Extras) {
            Extras extras = (Extras) navigatorExtras;
            ActivityOptionsCompat activityOptions = extras.getActivityOptions();
            //启动Activity
            if (activityOptions != null) {
                ActivityCompat.startActivity(mContext, intent, activityOptions.toBundle());
            } else {
                mContext.startActivity(intent);
            }
        } else {
            mContext.startActivity(intent);
        }
        
        ...

        // You can't pop the back stack from the caller of a new Activity,
        // so we don't add this navigator to the controller's back stack
        return null;
    }
}
```

#### DialogFragmentNavigator ####


## DeepLink源码简析 ##

先看`NavController.handleDeepLink(new Intent())`方法：

```java
//NavController.java
public boolean handleDeepLink(@Nullable Intent intent) {
    if (intent == null) {
        return false;
    }
    Bundle extras = intent.getExtras();
    int[] deepLink = extras != null ? extras.getIntArray(KEY_DEEP_LINK_IDS) : null;
    Bundle bundle = new Bundle();
    Bundle deepLinkExtras = extras != null ? extras.getBundle(KEY_DEEP_LINK_EXTRAS) : null;
    if (deepLinkExtras != null) {
        bundle.putAll(deepLinkExtras);
    }
    //intent.getData() 即为所传递过来的uri
    if ((deepLink == null || deepLink.length == 0) && intent.getData() != null) {
        NavDestination.DeepLinkMatch matchingDeepLink = mGraph.matchDeepLink(intent.getData());
        if (matchingDeepLink != null) {
            //buildDeepLinkIds() --> 构建DeepLinkIds,包含了从根节点到当前节点的id数组
            deepLink = matchingDeepLink.getDestination().buildDeepLinkIds();
            bundle.putAll(matchingDeepLink.getMatchingArgs());
        }
    }
    
    ...
        
    if ((flags & Intent.FLAG_ACTIVITY_NEW_TASK) != 0) {
        // Start with a cleared task starting at our root when we're on our own task
        if (!mBackStack.isEmpty()) {
            popBackStackInternal(mGraph.getId(), true);
        }
        int index = 0;
        //遍历deepLinkIds数组
        while (index < deepLink.length) {
            int destinationId = deepLink[index++];
            //每一个NavDestination代表着一个页面
            NavDestination node = findDestination(destinationId);
            if (node == null) {
                throw new IllegalStateException("unknown destination during deep link: "
                                                + NavDestination.getDisplayName(mContext, destinationId));
            }
            //通过navigate方法将所涉及的页面一个一个打开
            navigate(node, bundle,
                     new NavOptions.Builder().setEnterAnim(0).setExitAnim(0).build(), null);
        }
        return true;
    }
    
    ...
}
```

```java
//NavDestination.java
/**
 * Build an array containing the hierarchy from the root down to this destination.
 * 构建一个包含从根到此目的地的层次结构的数组。
 * @return An array containing all of the ids from the root to this destination
 */
@NonNull
int[] buildDeepLinkIds() {
    ArrayDeque<NavDestination> hierarchy = new ArrayDeque<>();
    NavDestination current = this;
    do {
        NavGraph parent = current.getParent();
        if (parent == null || parent.getStartDestination() != current.getId()) {
            hierarchy.addFirst(current);
        }
        current = parent;
    } while (current != null);
    int[] deepLinkIds = new int[hierarchy.size()];
    int index = 0;
    for (NavDestination destination : hierarchy) {
        deepLinkIds[index++] = destination.getId();
    }
    return deepLinkIds;
}
```

*举例*：

![](img/handleDeepLink.png)

如果外部应用打开客户端并携带了`uri: ppjoke://page/pageD` ,那么  **A、B、C、D都会被打开** 。