# RollViewPager
自动轮播的Viewpager

[中文](https://github.com/Jude95/RollViewPager/blob/master/README_ch.md) | [English](https://github.com/Jude95/RollViewPager/blob/master/README.md)

支持无限循环。
触摸时会暂停播放，直到结束触摸一个延迟周期以后继续播放。
看起来就像这样。指示器可以为点可以为数字还可以自定义，位置也可以变。  
![example](example.jpg)

##依赖
```groovy
compile 'com.jude:rollviewpager:1.3.2'
```

## 使用
```xml
<com.jude.rollviewpager.RollPagerView
    android:layout_width="match_parent"
    android:layout_height="180dp"
    app:rollviewpager_play_delay="3000"/>
```
        
        
`app:rollviewpager_play_delay="3000"`  播放间隔时间，单位ms。填0则不播放。默认为0 
`app:rollviewpager_hint_gravity="center"`  指示器位置,提供`left`,`center`,`right`。默认`center`  
`app:rollviewpager_hint_color="#7c7c7c"`  指示器背景颜色.默认黑色  
`app:rollviewpager_hint_alpha`="80"  指示器背景透明度。0全透明，255不透明。默认0.  
`app:rollviewpager_hint_paddingLeft`="16dp"  指示器左边距  
`app:rollviewpager_hint_paddingRight`="16dp"  指示器右边距  
`app:rollviewpager_hint_paddingTop`="16dp"  指示器上边距  
`app:rollviewpager_hint_paddingBottom`="16dp"  指示器下边距  
一般指定一下间隔时间就好了。

##HintView
提供了HintView是对指示器进行自定义。  
`setHintView(HintView hintview)`   
 
+ HintView  
    + ShapeHintView  
        + IconHintView  
        + ColorPointHintView    
    + TextHintView  

用法：

```java
mRollViewPager.setHintView(new IconHintView(this,R.drawable.point_focus,R.drawable.point_normal));
mRollViewPager.setHintView(new ColorPointHintView(this, Color.YELLOW,Color.WHITE));
mRollViewPager.setHintView(new TextHintView(this));
mRollViewPager.setHintView(null);//隐藏指示器
```
    
##Adapter
提供以下三种种方便的PagerAdapter供使用。  
本ViewPager也可以使用其他任意PagerAdapter。  

####StaticPagerAdapter
存储页面的Adapter。view添加进去就存储不会再次`getView`，减少页面创建消耗，消耗内存。一般自动播放的情况这种方案比较好。不然会大量构造View。
概念参照FragmentPagerAdapter。可以用于其他ViewPager。

####DynamicPagerAdapter
动态的Adapter。当创建3号view时会销毁1号view(递推)，会时常调用`getView`。增加页面创建消耗，减小内存消耗。
概念参照FragmentStatePagerAdapter。可以用于其他ViewPager。  


```java
//2个Adapter用法一样;
mRollViewPager.setAdapter(new TestNomalAdapter());
private class TestNomalAdapter extends StaticPagerAdapter{
    private int[] imgs = {
            R.drawable.img1,
            R.drawable.img2,
            R.drawable.img3,
            R.drawable.img4,
    };

    @Override
    public View getView(ViewGroup container, int position) {
        ImageView view = new ImageView(container.getContext());
        view.setImageResource(imgs[position]);
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return view;
    }
    
    @Override
    public int getCount() {
        return imgs.length;
    }
}
```

####LoopPagerAdapter
无限循环的Adapter。无限循环上采用的是getCount返回int大数的方法(并没有什么缺点,另外估计1s的间隔时间你在有生之年看不到他播放到底)。实测比第N页跳转到第1页的效果好。  
数据采用StaticPagerAdapter的方案。节省创建View开销。
本Adapter只能用于本RollViewPager;    
无需其他设置，很简单。

```java
mRollViewPager.setAdapter(new TestLoopAdapter(mRollViewPager));
private class TestLoopAdapter extends LoopPagerAdapter{
    private int[] imgs = {
            R.drawable.img1,
            R.drawable.img2,
            R.drawable.img3,
            R.drawable.img4,
    };
    
    public TestLoopAdapter(RollPagerView viewPager) {
        super(viewPager);
    }
    
    @Override
    public View getView(ViewGroup container, int position) {
        ImageView view = new ImageView(container.getContext());
        view.setImageResource(imgs[position]);
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return view;
    }
    
    @Override
    public int getRealCount() {
        return imgs.length;
    }
}
```

#### 播放控制

```java
rollViewPager.pause()
rollViewPager.resume()
rollViewPager.isPlaying()
```

License
-------

    Copyright 2015 Jude

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
