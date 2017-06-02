# Assassin

Assassin是由美柚出品的一个小巧、方便但功能强大的Android切片框架。

## 什么是Assassin

Assassin意为刺客。

它能帮助你将原本的一个类或者一个方法甚至一个属性进行修改、替换、拦截，实现Java级别的开放插桩切片。

Assassin通过编译期的插桩替换操作来达到dex修改的能力。

通过Assassin框架，你可以实现热补丁、自动化、无痕埋点等一系列在Java运行期无法完成的工作。

Assassin不需要注解，不需要繁琐的配置，不需要学习成本。

## 举个栗子

比如，武汉写了个类叫做Class A，里面有个方法叫Method A1，在App初始化过程中必然会调用这个Method A1，结果你发现Method A1这个方法实现的逻辑满足不了需求，或者武汉写的有bug，又或者你想加几行代码，又或者你想在App初始化过程中跳过Method A1不执行它，传统的做法是你跑到武汉这边来跟武汉说：MD，你写的方法有bug！赶紧给我改了。

但是如果这个时候武汉不在呢？又或者这个方法是第三方提供的，而且它是不能被继承重写覆盖的，那么怎么办呢？

这时候你只需要写个Method B， 通过Assassin你就可以直接将你写的Method B替换这个Method A1，让他在App初始化过程中不执行A1，而执行你的Method B，甚至你可以控制是否执行Method A，甚至可以执行完Method A，再执行你的Method B，执行完B再执行A。

通过Assassin，不光是Method A1， 整个Class A我都能替换。只要你开心。

或者是你想统计所有方法的运行时间，你只需要一行代码，通过Assassin就可以轻松完成插桩。

或者是你想监听某些方法的执行，你想他们被执行的时候告诉你，通过Assassin你也可以做到。

Assassin可以完成你当前做不到的事情。

## 衍生物

无痕埋点、热补丁、自动化、插桩等等

## 使用方法

### 添加插件

在主工程的build.gradle中加入

```groovy
classpath 'com.meiyou.aop:assassin-compiler:0.0.18-SNAPSHOT'。
```

在你的工程中加入插件和依赖

```groovy
apply plugin: 'assassin'


compile 'com.meiyou.aop:assassin:0.0.2-SNAPSHOT'
```

### 创建替换方法

随便找个地方写个类，实现IAssassinDelegate这个接口，并在里面写上你的方法，再在class中注解@AntiAssassin。

```java
@AntiAssassin
public class TestDelegate extends IAssassinDelegate{
//...
//你的方法体

}
```

@AntiAssassin注解的意思是该类不允许被Assassin。

### 配置到Assassin替换规则

在工程内创建规则配置文件，命名为assassin.pro

在里面声明插入或者是替换的方法名并定义你的替换方法位置坐标

```xml
-insert method{
#   com.meetyou.aop.assassin.MainActivity*onCreate;
  **.all;
#  *.<init>;
#  *.onClick;
}

-replace method{
#  *.show;
}

-receiver {
  com.meetyou.aop.assassin.TestDelegate;
}
```

### 完成编译

这时候重新编译或者运行就可以发现你的代码被插入了


## Developed By

 * Linhonghong - <QQ:371655539，mail:371655539@qq.com>

## Attention
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
