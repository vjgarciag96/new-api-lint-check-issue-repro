**DESCRIBE THE ISSUE IN DETAIL:**

Lint's `NewAPI` check fails to detect "illegal" usages of `Matcher.group(String)` (https://developer.android.com/reference/java/util/regex/Matcher#group(java.lang.String)) when used via Kotlin stlib APIs (https://github.com/JetBrains/kotlin/blob/b76452e15f37f616a0393747b3c624eb61535fb7/libraries/stdlib/src/kotlin/text/regex/MatchResult.kt#L42) in API levels < 26.

I don't know about the internals of the `NewApi` check, so I'm not sure this is something that's expected to be flagged as is, or whether Kotlin stdlib's should be doing something else for that to happen - opening the issue to get to the bottom of this.

**STEPS TO REPRODUCE:**
1. Create an Android project with minSdk version < 26.
2. Write some code that uses Kotlin stdlib's `Regex` and named groups. For example:

```kotlin
val myRegex = Regex("title=(?<title>.*)")

fun getTitle(from: String): String? {
    val titleMatch = myRegex.find(from) ?: return null
    val titleGroup = titleMatch.groups["title"] ?: return null
    return titleGroup.value
}

getTitle("title=Is this an Android Lint bug?")
```

✅ EXPECTED: Lint flags the usage of named groups `groups["title"]` as "requires API level 26"

❌ ACTUAL: Lint does not flag this, but app throws an exception at runtime in API levels < 26 when using this API.


**ADDITIONAL INFORMATION**

- API levels < 26 exception stacktrace:

```
FATAL EXCEPTION: main

Process: com.vicgarci.newapiinvalidusagerepro, PID: 10015
java.lang.NoSuchMethodError: No virtual method start(Ljava/lang/String;)I in class Ljava/util/regex/Matcher; or its super classes (declaration of 'java.util.regex.Matcher' appears in /system/framework/core-oj.jar)
at kotlin.internal.jdk8.JDK8PlatformImplementations.getMatchResultNamedGroup(JDK8PlatformImplementations.kt:45)
at kotlin.text.MatcherMatchResult$groups$1.get(Regex.kt:370)
at kotlin.text.jdk8.RegexExtensionsJDK8Kt.get(RegexExtensions.kt:23)
```
