andex
=====

> Extension for Android SDK



andex是一个Android SDK的扩展框架。andex旨在把简化常见代码的开发，让你可以把时间和精力都花在有难度有挑战的事情上。

### 使用方法

* 将andex下载后解压缩到你的工作目录，在Eclipse中建立新的Android工程至andex目录，将工程设置为library。

* 在你自己的工程中添加对andex工程的引用。

* 使用andex很简单，只需要将您的Activity继承自andex的BaseActivity，您就可以获得大多数调用简便的扩展功能了。

	```java
			public class MyActivity extends BaseActivity {
			
			}
	```

4. 最后运行Run as Android Application 即可调试你的程序了。


### API指南

##### 简化基本操作

	* TextView和EditText还可以直接设值，无需繁琐的操作
	
		原来：

		```java
				TextView tv = (TextView)findViewById(R.id.textView);
				tv.setText("Something to display");
		```
		
		现在：

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

	* 单击组件的操作特别多，因此需要简化
	
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

	* 简化调试输出

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

	* 简化Toast显示
	
		原来：

		```java
				Toast.makeText(context, "Toast Message", Toast.LENGTH_LONG).show();
				Toast.makeText(context, "Toast Message", Toast.LENGTH_SHORT).show();
		```

		现在：

		```java
				// Activity中
				showToast("Toast Message");
				showToastShort("Toast Message");
				
				// 在Service或者BroadcastReceiver中
				AndroidUtils.showToast("Toast Message");
				AndroidUtils.showToastShort("Toast Message");
		```

##### 简化列表视图

##### 简化对话框

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

