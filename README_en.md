# RollViewPager
A Viewpager can auto-play.

[中文](https://github.com/Jude95/RollViewPager/blob/master/README.md) | [English](https://github.com/Jude95/RollViewPager/blob/master/README_en.md)

The touch will pause playback, continue to play until a delay period after the end of the touch.  
looks like this,Indicator can be customized to point or number, the gravity can be changed also.   
![example](example.jpg)

##依赖
`compile 'com.jude:rollviewpager:1.0.5'`

##xml属性
`app:rollviewpager_play_delay="3000"`  period，unit is ms。0 for no auto-play。**default is 0**.   
`app:rollviewpager_hint_mode="point"`  type of indicator,`point` or `number`,default is null.   
`app:rollviewpager_hint_gravity="center"` gravity. `left`,`center`,`right`。default is `center`.  
`app:rollviewpager_hint_color="#7c7c7c"`  color for indicator's container.default is black.  
`app:rollviewpager_hint_alpha`="80"  alpha for indicator's container。0 for complete transparent，255 for no transparent。default is 0.  
`app:rollviewpager_hint_paddingLeft`="16dp"  padding for indicator's container  
`app:rollviewpager_hint_paddingRight`="16dp"  
`app:rollviewpager_hint_paddingTop`="16dp"  
`app:rollviewpager_hint_paddingBottom`="16dp"  

Generally just need set the `play_delay` and `hint_mode`.  

##Adapter
There offer two Adapter for use conveniently.you can use it on any ViewPager.the RollViewPager can also user other PagerAdapter.

####StaticPagerAdapter
this Adapter will store the every page(View), Once create multiple use.may take up more memory.
Like FragmentPagerAdapter

####DynamicPagerAdapter
this Adapter will not store the View.it create new View every time.save the memory.    
Like FragmentStatePagerAdapter  

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
