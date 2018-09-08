# ComponentDemo
组件化Demo原型
##组件化原理
实现组件化需要处理三部分内容：  
* AndroidManifest中的组件声明；  
* 代码；  
* 资源；

##解决方案
每个组件都是一个独立的apk，组件里面资源id分配不同的范围段（可用ACDD来生成）；
###AndroidManifest
将组件里的AndroidManifest中声明的组件都拷贝到主工程中进行注册；
###代码
在Application初始化时：  
1. 创建新的ClassLoader，将应用原ClassLoader作为父ClassLoader，新ClassLoader维护一个DexClassLoader列表，每个DexClassLoader负责加载组件里面的类；  
2. 将将主工程应用包名对应的ClassLoader的替换为新的ClassLoader；

注意：组件里的类和主应用的类在打包后不要重复，保证类的唯一性，否则可能会造成崩溃；
###资源
在替换完ClassLoader后，获取应用的AssetManager，利用AssetManager的addAssetPath加载组件里面的资源；