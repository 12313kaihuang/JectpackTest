# Navigation工作原理剖析

Navigation工作流程
---

从`NavHostFragment`开始，它是一个内容承载容器，从它的`onCreate()`方法开始看起：

```java
@CallSuper
@Override
public void onCreate(@Nullable Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	final Context context = requireContext();

    //NavHostController页面导航，页面跳转核心类  一个入口  
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
	onCreateNavController(mNavController);

	Bundle navState = null;
	if (savedInstanceState != null) {
		navState = savedInstanceState.getBundle(KEY_NAV_CONTROLLER_STATE);
		if (savedInstanceState.getBoolean(KEY_DEFAULT_NAV_HOST, false)) {
			mDefaultNavHost = true;
			requireFragmentManager().beginTransaction()
					.setPrimaryNavigationFragment(this)
					.commit();
		}
	}

	if (navState != null) {
		// Navigation controller state overrides arguments
		mNavController.restoreState(navState);
	}
	if (mGraphId != 0) {
		// Set from onInflate()
		mNavController.setGraph(mGraphId);
	} else {
		// See if it was set by NavHostFragment.create()
		final Bundle args = getArguments();
		final int graphId = args != null ? args.getInt(KEY_GRAPH_ID) : 0;
		final Bundle startDestinationArgs = args != null
				? args.getBundle(KEY_START_DESTINATION_ARGS)
				: null;
		if (graphId != 0) {
			mNavController.setGraph(graphId, startDestinationArgs);
		}
	}
}

@CallSuper
protected void onCreateNavController(@NonNull NavController navController) {
	navController.getNavigatorProvider().addNavigator(
			new DialogFragmentNavigator(requireContext(), getChildFragmentManager()));
	navController.getNavigatorProvider().addNavigator(createFragmentNavigator());
}
```





DeepLink源码简析
---

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