plugins {
    id("java")
}

repositories {
    mavenCentral()
}

dependencies {
    // Apache Commons
    implementation("org.apache.commons:commons-math3:3.6.1")
    implementation("org.apache.commons:commons-lang3:3.18.0")

    // Google (Guava)
    implementation("com.google.guava:guava:33.4.5-jre")

    //Lombok
    compileOnly("org.projectlombok:lombok:1.18.36")
    annotationProcessor("org.projectlombok:lombok:1.18.36")
    testCompileOnly("org.projectlombok:lombok:1.18.36")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.36")

    //Mapstruct
    implementation("org.mapstruct:mapstruct:1.6.3")
    annotationProcessor("org.mapstruct:mapstruct:1.6.3")
    testImplementation("org.mapstruct:mapstruct-processor:1.6.3")
    testAnnotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")

    //Jackson Data Serialization
    implementation("com.fasterxml.jackson.core:jackson-databind:2.+")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.+")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jdk8:2.+2")
    annotationProcessor("com.fasterxml.jackson.core:jackson-annotations:2.+")

    //Unit Testing Related
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.hamcrest:hamcrest-all:1.3")
}

testing {
    suites {
        // Configure the built-in test suite
        val test by getting(JvmTestSuite::class) {
            // Use JUnit Jupiter test framework
            useJUnitJupiter("5.12.1")
        }
    }
}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}