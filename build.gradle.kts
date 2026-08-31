// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    id("org.jetbrains.kotlin.plugin.parcelize") version "2.4.10" apply false
    id("com.google.devtools.ksp") version "2.3.11" apply false
}