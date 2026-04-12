plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

kotlin {
    jvmToolchain(23)
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(23))
}