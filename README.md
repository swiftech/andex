andex
=====


对于 Android 开发来说，交互流程处理，状态处理等问题是比较繁琐的问题，即重要又容易出错，andex 的目的就是为了解决此类问题。


### 使用方法

* 在你自己的工程中添加对 andex 的引用。


### 功能概述

##### Activity 和 Fragment 流程管理

* Activity 控制
跳转到另一个 Activity

```java
new ActivityBuilder(this, NextActivity.class)
    .withId(1001)
    .with("title", "Next Activity")
    .start();
```


* Fragment 控制
跳转到另一个 Fragment：

```java
SecondFragment secondFragment = new SecondFragment();
new FragmentBuilder(this, secondFragment).replace(R.id.fl_content)
        .withId(1001)
        .with("title", "Second Fragment")
        .with("content", "This is the second fragment")
        .start();
```


##### 状态管理器

用户和 App 的交互本质上是 UI 对于处于不同状态下所作出的变化，而程序对于 UI 交互的处理是异步化的，这使得处理交互的代码分散在各处，不便于阅读和维护。
StatusBus 是 andex 提供的状态管理器，它能很好的管理 UI 交互的状态，大量简化代码量。

一个基本的例子，处理一个页面的视图状态和编辑状态之间的切换：
```java
StatusBus statusBus = StatusBus.newInstance(StatusDemoActivity.class, StatusDemoActivity.this, getWindow().getDecorView());
statusBus.status("VIEW")
        .in(ActionBuilder.create()
                .text(R.id.tv_edit, "Edit")
                .text(R.id.btn1, "Save")
                .disable(R.id.btn1, R.id.et1, R.id.et2)
                .bgColorInt(R.id.btn1, Color.argb(255, 0, 255, 0))
        )
        .status("EDIT")
        .in(ActionBuilder.create()
                .text(R.id.tv_edit, "Cancel")
                .text(R.id.btn1, "Save")
                .enable(R.id.btn1, R.id.et1, R.id.et2)
                .bgColorInt(R.id.btn1, Color.argb(255, 255, 0, 0))
        );

tvEdit = findViewById(R.id.tv_edit);
tvEdit.setOnClickListener(v -> {
    if (statusBus.isStatus("VIEW")) {
        statusBus.post("EDIT");
    } else if (statusBus.isStatus("EDIT")) {
        statusBus.post("VIEW");
    }
});

btnOk = findViewById(R.id.btn1);
btnOk.setOnClickListener(v -> {
    statusBus.post("VIEW");
});

statusBus.post("VIEW");
```

正如您所见，所有改变 UI 的代码全部都集中到了一起，容易阅读和管理，上面的示例代码定义了两种状态"视图"和"编辑"，`in()` 方法表示其包含的 `ActionBuilder` 所创建的代码在进入此状态时执行。
而所有发生状态改变的地方只需要调用 post() 方法改变状态即可，相应 UI 属性根据状态进入和退出而发生相应的改变。
 `ActionBuilder` 提供快速修改常见的 UI 属性的配置方法，简化了这些繁冗的代码。

`ActionBuilder` 只实现了常见的 UI 属性配置方法，对于没有覆盖到的 UI 属性的修改，可以用定制化的方法：

```java
statusBus.status("VIEW")
        .in(ActionBuilder.create()
                .text(R.id.tv_edit, "Edit")
        )
        .in(() -> {
            ((TextView)findViewById(R.id.tv_edit)).setLines(4);
        })  
}
```

