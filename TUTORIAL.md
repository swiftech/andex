
##### 简化基本操作

* 

* TextView和EditText还可以直接设值，无需繁琐的操作

```java
setTextViewText(R.id.textView, "Something to display");
setEditTextString(R.id.editText, "Something to display");
```

* 批量disable或者enable多个视图组件

```java
// 直接
disableViews(view0, view1, view2, ...);
enableViews(view0, view1, view2, ...);
// 通过资源ID
disableViews(R.id.view0, R.id.view1, R.id.view2, ...);
enableViews(R.id.view0, R.id.view1, R.id.view2, ...);
```

* 批量show, hide或者移除多个视图组件

    ```java
    // 直接
    showViews(view0, view1, ...);
    hideViews(view0, view1, ...);
    unblockViews(view0, view1, ...);
    // 通过资源ID
    showViews(R.id.view0, R.id.view1, ...);
    hideViews(R.id.view0, R.id.view1, ...);
    unblockViews(R.id.view0, R.id.view1, ...);
    ```

* 简化调试输出

```java
debug("what you want to log")
warn("what you want to log")
error("what you want to log")
```

* 简化Toast显示

```java
// Activity中
showToast("Toast Message");
showToastShort("Toast Message");

// 在Service或者BroadcastReceiver中
AndroidUtils.showToast("Toast Message");
AndroidUtils.showToastShort("Toast Message");
```

##### 工具方法

* 获取屏幕像素宽度和高度

```java
int width = AndroidUtils.getScreenWidth(context);
int height = AndroidUtils.getScreenHeight(context);
```

* 获取设备的IMEI

```java
String imei = AndroidUtils.getDeviceIMEI(context);
```

