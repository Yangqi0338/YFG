# yfl-pdm
PDM 后端项目


## 打包
将依赖包放入target/lib目录下
```
mvn dependency:copy-dependencies -DoutputDirectory=target/lib package
```
 
## 运行
```
java -Dloader.path=lib -Xms300m -Xmx500m -jar pdm-1.0.0.jar > nohup.out &
```