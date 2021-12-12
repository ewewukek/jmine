## jmine

![](demo.png?raw=true)

Yet another minesweeper clone written in Java. Mimics the behavior of classic Windows 95 - Windows XP minesweepers. Difficulty level can be selected by right clicking the new game button.

### Development

Project is targeted at Java 1.8 but you can modify `build.gradle` to build on any other JDK version.

Building is simply `./gradlew build` or `gradlew.bat build`. Resulting jar will appear in `build/libs`.

### Customization

Images can be replaced and element positions can be modified to some degree. Check out `src/main/resources` for an example. I'm not sure about legal status of classic sprites so they're not included here (but [here](https://ewewukek.me/files/jmine-windows95.zip) is a link for those who prefer classic look). Default theme is made from scratch and available to anyone under [CC0](https://creativecommons.org/publicdomain/zero/1.0/).
