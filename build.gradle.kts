import org.apache.tools.ant.taskdefs.condition.Os

buildscript {
    repositories {
        google()
        mavenCentral()
        mavenLocal()
        gradlePluginPortal()
    }
    dependencies {
        classpath(libs.android.gradle.plugin)
        classpath(libs.kotlin.gradle.plugin)
        classpath(libs.androidx.navigation.safeargs.kotlin)
        classpath(libs.hilt)
        classpath(libs.gradle.versions)
        classpath(libs.ktlint.gradle)
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

task("clean") {
    delete(rootProject.buildDir)
}

apply(plugin = "com.github.ben-manes.versions")

fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.uppercase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(version)
    return isStable.not()
}

tasks.withType<com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask> {
    resolutionStrategy {
        componentSelection {
            all {
                if (isNonStable(candidate.version) && !isNonStable(currentVersion)) {
                    reject("Release candidate")
                }
            }
        }
    }
}

allprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        version.set(rootProject.project.libs.versions.ktlint.runtime)
        android.set(true)
        verbose.set(true)
        outputToConsole.set(true)
        ignoreFailures.set(false)
        enableExperimentalRules.set(true)
        filter {
            exclude("**/generated/**")
            include("**/kotlin/**")
        }
    }
}

tasks {
    register<Copy>("copy") {
        doLast {
            val suffix = if (Os.isFamily(Os.FAMILY_WINDOWS)) {
                "windows"
            } else {
                "macos"
            }
            from(File(rootProject.rootDir, "scripts/pre-commit-$suffix")) {
                rename { it.removeSuffix(suffix) }
            }
            into(File(rootProject.rootDir, ".git/hooks"))
            dependsOn(":app:preBuild")
        }
    }
}
