import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.tasks.bundling.AbstractArchiveTask
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.jvm.tasks.Jar

plugins {
    id("java")
    id("java-library")
    id("com.diffplug.spotless") version "8.1.0"
    id("com.gradleup.shadow") version "8.3.9"
    id("checkstyle")
    eclipse
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
    toolchain { languageVersion.set(JavaLanguageVersion.of(17)) }
}

group = "org.maxgamer"

version = "5.1.2.5-SNAPSHOT"

val apiVersion = "1.19"

repositories {
    mavenCentral()
    gradlePluginPortal()
    mavenLocal()
    maven(url = "https://repo.purpurmc.org/snapshots")
    maven(url = "https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven(url = "https://repo.dmulloy2.net/repository/public/")
    maven(url = "https://repo.codemc.io/repository/maven-public/")
    maven(url = "https://oss.sonatype.org/content/groups/public/")
    maven(url = "https://repo.md-5.net/content/repositories/snapshots/")
    maven(url = "https://repo.md-5.net/content/repositories/releases/")
    maven(url = "https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven(url = "https://ci.ender.zone/plugin/repository/everything/")
    maven(url = "https://jitpack.io")
    maven(url = "https://mvn.intellectualsites.com/content/groups/public/")
    maven(url = "https://repo.onarandombox.com/content/groups/public")
    maven(url = "https://s01.oss.sonatype.org/content/repositories/snapshots/")
    maven(url = "https://repo.papermc.io/repository/maven-public/")
    maven(url = "https://repo.bg-software.com/repository/api/")
    maven(url = "https://maven.enginehub.org/repo/")
    maven(url = "https://repo.songoda.com/repository/public/")
    maven(url = "https://repo.craftaro.com/repository/public/")
    maven(url = "https://repo.minebench.de/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
    compileOnly("com.griefcraft.lwc:LWCX:2.2.7")
    compileOnly("com.github.TechFortress:GriefPrevention:16.18.1")
    compileOnly("com.github.dmulloy2:ProtocolLib:5.0.0")
    implementation("com.dumptruckman.minecraft:JsonConfiguration:1.1")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7.1")
    compileOnly("com.github.true-og.OpenInv:openinvapi:a85e8ebc28")
    compileOnly("fr.neatmonster:nocheatplus:3.17-SNAPSHOT")
    compileOnly(files("lib/Clearlag-3.2.2.jar"))
    compileOnly("me.clip:placeholderapi:2.11.6")
    compileOnly("com.github.angeschossen:LandsAPI:6.35.0")

    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testImplementation("com.github.seeseemelk:MockBukkit-v1.20:3.87.0")
    testImplementation("org.xerial:sqlite-jdbc:3.40.1.0")
    testImplementation("mysql:mysql-connector-java:8.0.33")

    implementation("org.apache.commons:commons-compress:1.22")
    implementation("org.apache.commons:commons-lang3:3.12.0")
    compileOnly(files("lib/SpartanAPI-9.1.jar"))
    compileOnly("com.github.TownyAdvanced:Towny:0.97.0.5")
    compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.0.9")
    compileOnly("com.plotsquared:PlotSquared-Core:6.11.1") { isTransitive = false }
    compileOnly(files("lib/Residence5.0.1.0.jar"))
    compileOnly("com.massivecraft:Factions:1.6.9.5-U0.6.0")
    compileOnly(files("lib/BlocksHub-3.2.1.jar"))
    implementation("com.rollbar:rollbar-java:1.10.0")
    implementation("com.github.PotatoCraft-Studio:MineDown:1.7.1-modified")
    compileOnly(files("lib/GemsEconomy-4.9.2.jar"))
    compileOnly(files("lib/TNE-0.1.1.17-PRE-2.jar"))
    compileOnly("com.songoda:skyblock:2.3.30")
    compileOnly("com.songoda:SongodaCore:2.6.18")
    implementation("commons-codec:commons-codec:1.15")
    implementation("org.slf4j:slf4j-nop:1.7.32")
    implementation("io.papermc:paperlib:1.0.7")
    compileOnly("com.bgsoftware:SuperiorSkyblockAPI:1.11.1")
    implementation("me.lucko:helper:5.6.13")
    implementation("me.lucko:helper-profiles:1.2.0")
    compileOnly("com.github.alex9849:advanced-region-market:3.5.4")
    compileOnly(files("lib/IridiumSkyblock-3.2.3.jar"))
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("commons-io:commons-io:2.11.0")
    implementation("org.maxgamer.storage:simplixstorage:3.2.3.3")
    compileOnly("de.tr7zw:item-nbt-api-plugin:2.12.0")
    compileOnly("net.tnemc:Reserve:0.1.5.4")

    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
    testCompileOnly("org.projectlombok:lombok:1.18.30")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.30")
    compileOnly("org.jetbrains:annotations:24.0.1")
    testCompileOnly("org.jetbrains:annotations:24.0.1")
}

tasks.named<ProcessResources>("processResources") {
    val props = mapOf("version" to project.version.toString(), "apiVersion" to apiVersion)
    inputs.properties(props)
    filesMatching("plugin.yml") { expand(props) }
    from("crowdin/lang") { into("crowdin/lang") }
    from("LICENSE") { into("/") }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.withType<Test>().configureEach { useJUnitPlatform() }

tasks.withType<AbstractArchiveTask>().configureEach {
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
}

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("-parameters")
    options.compilerArgs.add("-Xlint:deprecation")
    options.encoding = "UTF-8"
    options.isFork = true
}

spotless {
    java {
        eclipse().configFile("config/formatter/eclipse-java-formatter.xml")
        leadingTabsToSpaces()
        removeUnusedImports()
    }
    kotlinGradle {
        ktfmt().kotlinlangStyle().configure { it.setMaxWidth(120) }
        target("build.gradle.kts", "settings.gradle.kts", "eclipse.gradle.kts")
    }
}

checkstyle {
    toolVersion = "10.18.1"
    configFile = file("config/checkstyle/checkstyle.xml")
    isIgnoreFailures = true
    isShowViolations = true
}

tasks.named("compileJava") { dependsOn("spotlessApply") }

tasks.named("spotlessCheck") { dependsOn("spotlessApply") }

tasks.named<Jar>("jar") { archiveClassifier.set("part") }

tasks.named<ShadowJar>("shadowJar") {
    archiveClassifier.set("")
    minimize()

    relocate("com.google.errorprone", "org.maxgamer.quickshop.shade.com.google.errorprone")
    relocate("org.checkerframework", "org.maxgamer.quickshop.shade.org.checkerframework")
    relocate("org.apache.commons.lang3", "org.maxgamer.quickshop.shade.org.apache.commons.lang3")
    relocate("org.objectweb.asm", "org.maxgamer.quickshop.shade.org.objectweb.asm")
    relocate("org.slf4j", "org.maxgamer.quickshop.shade.org.slf4j")
    relocate("net.minidev.json", "org.maxgamer.quickshop.shade.net.minidev.json.minidev.json")
    relocate("com.fasterxml.jackson.core", "org.maxgamer.quickshop.shade.com.fasterxml.jackson.jackson.core")
    relocate("me.lucko", "org.maxgamer.quickshop.shade.me.lucko")
    relocate("com.rollbar", "org.maxgamer.quickshop.shade.com.rollbar")
    relocate(
        "com.dumptruckman.bukkit.configuration",
        "org.maxgamer.quickshop.shade.com.dumptruckman.bukkit.configuration",
    )
    relocate("org.apache.commons.compress", "org.maxgamer.quickshop.shade.org.apache.commons.compress")
    relocate("org.apache.commons.codec", "org.maxgamer.quickshop.shade.org.apache.commons.codec")
    relocate("me.lucko.helper", "org.maxgamer.quickshop.shade.me.lucko.helper")
    relocate("com.github.SparklingComet", "org.maxgamer.quickshop.shade.com.github.SparklingComet")
    relocate("de.themoep", "org.maxgamer.quickshop.shade.de.themoep")
    relocate("io.papermc.lib", "org.maxgamer.quickshop.shade.io.papermc.lib")
    relocate("com.github.ben-manes.caffeine", "org.maxgamer.quickshop.shade.com.github.ben-manes.caffeine")
    relocate("org.enginehub.squirrelid", "org.maxgamer.quickshop.shade.org.enginehub.squirrelid")
    relocate("kotlin", "org.maxgamer.quickshop.shade.org.kotlin")
    relocate("javax.annotation", "org.maxgamer.quickshop.shade.org.javax.annotation")
    relocate("com.flowpowered.math", "org.maxgamer.quickshop.shade.com.flowpowered.math")
    relocate("okio", "org.maxgamer.quickshop.shade.okio")
    relocate("okhttp3", "org.maxgamer.quickshop.shade.okhttp3")
    relocate("net.kyori.minecraft", "org.maxgamer.quickshop.shade.net.kyori.minecraft")
    relocate("net.minidev.asm", "org.maxgamer.quickshop.shade.net.minidev.asm")
    relocate("com.squareup", "org.maxgamer.quickshop.shade.org.com.squareup")
    relocate("de.leonhard.storage", "org.maxgamer.quickshop.shade.de.leonhard.storage")

    exclude("META-INF/*.SF")
    exclude("META-INF/*.DSA")
    exclude("META-INF/*.RSA")
    exclude("META-INF/*.kotlin_module")
    exclude("META-INF/*.txt")
    exclude("META-INF/proguard/*")
    exclude("META-INF/services/*")
    exclude("META-INF/versions/9/*")
    exclude("*License*")
    exclude("*LICENSE*")
}

tasks.named("build") { dependsOn("spotlessApply", "shadowJar") }

apply(from = "eclipse.gradle.kts")
