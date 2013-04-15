andex
=====

Extension for Android SDK



andex是一个Android SDK的扩展框架。使用Android SDK开发的时候，有很多功能虽然很常见，但是Java语言的孱弱以及Adnroid SDK的繁琐都使得常规的代码都很冗长。andex旨在把这些冗余过度的常规代码省略掉，让你可以把时间和精力都花在有难度有挑战的事情上。

#使用方法#

andex使用的方法很简单，只需要将您的Activity继承自andex的BaseActivity，您就可以获得大多数调用简便的扩展功能了。

```java
		public class MyActivity extends BaseActivity {
		
		}
```


##API指南##

### 简化基本操作 ###

	1. 获取视图组件更方便

		原来：
		
		```java
				TextView tv = (TextView)findViewById(R.id.textView);
		```

		现在
		
		```java
				TextView tv = getTextView(R.id.textView);
		```

		以此类推，其他常用组件（包括Layout）也都可以通过getXXX的方式获取，看起来有点小儿科，不过这个操作实在太频繁了，使用andex至少避免了转型。

	20. TextView和EditText还可以直接设值，无需繁琐的操作
	
		原来：
		
		```java
				TextView tv = (TextView)findViewById(R.id.textView);
				tv.setText("Something to display");
		```
		
		现在：
	
		```java
				setTextViewText(R.id.textView, "Something to display");
		```

	30. 批量disable或者enable多个视图组件
	
		```java
				// 直接
				disableViews(view0, view1, view2, ...);
				enableViews(view0, view1, view2, ...);
				// 通过资源ID
				disableViews(R.id.view0, R.id.view1, R.id.view2, ...);
				enableViews(R.id.view0, R.id.view1, R.id.view2, ...);
		```

	31. 批量show, hide或者移除多个视图组件
	
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

	40. 单击组件的操作特别多，因此需要简化
	
		原来：
		
		```java
				View view = this.findViewById(R.id.view);
				if(view != null) {
					view.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
		        		// 
						}	
			    	});
		  		}
		```

		现在：
		
		```java
				  onViewClicked(R.id.button, new CallbackAdapter() {
				    public void invoke(Object view) {
				      //
				    }
				  });
		```

	5. 简化调试输出

		原来：
		
		```java
				Log.d("tag", "What you want to log");
				Log.w("tag", "What you want to log");
				Log.e("tag", "What you want to log");
		```

		现在：
		
		```java
				debug("what you want to log")
				warn("what you want to log")
				error("what you want to log")
		```

	6. 简化Toast显示
	
		原来：
		
		```java
				Toast.makeText(context, "Toast Message", Toast.LENGTH_LONG).show();
				Toast.makeText(context, "Toast Message", Toast.LENGTH_SHORT).show();
		```

		现在：
		
		```java
				showToast("Toast Message");
				showToastShort("Toast Message");
				// 或者
				AndroidUtils.showToast("Toast Message");
				AndroidUtils.showToastShort("Toast Message");
		```

###简化列表视图###

###简化对话框###

###工具方法###

	1. 获取屏幕宽度和高度
	
		```java
				AndroidUtils.getScreenWidth(context);
				AndroidUtils.getScreenHeight(context);
		```
	
	20. 获取设备的IMEI
	
		```java
				AndroidUtils.getDeviceIMEI(context);
		```

		