plugins {
    id("java")
    id("maven-publish")
    id("jacoco")
    id("com.diffplug.spotless") version "6.25.0"
    id("io.freefair.lombok") version "8.11"
}

group = "org.voegl.analogkey4j"
version = "0.0.3"

java.sourceCompatibility = JavaVersion.VERSION_17
java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.hid4java:hid4java:0.8.0")

    testImplementation(platform("org.junit:junit-bom:5.11.3"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.11.3")
    testImplementation("org.mockito:mockito-core:5.14.2")
    testImplementation("com.google.truth:truth:1.4.4")
}

spotless {
    format("misc") {
        target("**/*.properties", ".gitignore")
        trimTrailingWhitespace()
        indentWithTabs()
        endWithNewline()
    }
    java {
        googleJavaFormat()
        formatAnnotations()
    }
    kotlinGradle {
        trimTrailingWhitespace()
        indentWithSpaces(2)
        endWithNewline()
    }
}

jacoco {
    toolVersion = "0.8.12"
    reportsDirectory.set(layout.buildDirectory.dir("jacoco"))
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        html.required.set(true)
        html.outputLocation.set(layout.buildDirectory.dir("jacoco/html"))
        xml.required.set(false)
        csv.required.set(true)
        csv.outputLocation.set(layout.buildDirectory.file("jacoco/csv/report.csv"))
    }
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
    jacoco {
        isEnabled = true
    }
    finalizedBy(tasks.jacocoTestReport)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            groupId = rootProject.group.toString()
            artifactId = project.name
            version = project.version.toString()
        }
    }

    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/lvoegl/analogkey4j")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
