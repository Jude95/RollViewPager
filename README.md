# RollViewPager
A Viewpager can auto-play.

[中文](https://github.com/Jude95/RollViewPager/blob/master/README_ch.md) | [English](https://github.com/Jude95/RollViewPager/blob/master/README.md)

The touch will pause playback, continue to play until a delay period after the end of the touch.  
looks like this,Indicator can be customized to point or number, the gravity can be changed also.  
![example](example.jpg)

## Depandence
```groovy
compile 'com.jude:rollviewpager:1.3.2'
```

## Usage
```xml
<com.jude.rollviewpager.RollPagerView
    android:layout_width="match_parent"
    android:layout_height="180dp"
    app:rollviewpager_play_delay="3000"/>
```

`app:rollviewpager_play_delay="3000"`  period，unit is ms。0 for no auto-play。**default is 0**.  
`app:rollviewpager_hint_gravity="center"` graviengty. `left`,`center`,`right`。default is `center`.  
`app:rollviewpager_hint_color="#7c7c7c"`  color for indicator's container.default is black.  
`app:rollviewpager_hint_alpha`="80"  alpha for indicator's container。0 for complete transparent，255 for no transparent。default is 0.  
`app:rollviewpager_hint_paddingLeft`="16dp"  padding for indicator's container  
`app:rollviewpager_hint_paddingRight`="16dp"  
`app:rollviewpager_hint_paddingTop`="16dp"  
`app:rollviewpager_hint_paddingBottom`="16dp"  

Generally just need set the `play_delay`.  

##HintView
provide Hintview to DIY the indicator;
`setHintView(HintView hintview)`

+ HintView
    + ShapeHintView
        + IconHintView
        + ColorPointHintView
    + TextHintView

For example:
```java
mRollViewPager.setHintView(new IconHintView(this,R.drawable.point_focus,R.drawable.point_normal));
mRollViewPager.setHintView(new ColorPointHintView(this, Color.YELLOW,Color.WHITE));
mRollViewPager.setHintView(new TextHintView(this));
mRollViewPager.setHintView(null);//hide the indicator
```
##Adapter
There offer two Adapter for use conveniently.the RollViewPager can also user other PagerAdapter.

####StaticPagerAdapter
this Adapter will store the every page(View), Once create multiple use.may take up more memory.
Like FragmentPagerAdapter.this can use for any ViewPager;

####DynamicPagerAdapter
this Adapter will not store the View.it create new View every time.save the memory.
Like FragmentStatePagerAdapter.this can use for any ViewPager;
```java
//the usage of the 2 adapter is same;
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
A loop adapter.realize by return MAX_INT in getCount().
same as StaticPagerAdapter in page store.Once create multiple use.
this adapter only for RollViewPager.
the usage is very simple。
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
#### Play Control
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
