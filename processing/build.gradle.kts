plugins {
    `jm-fin-calc-java-conventions`
}

group = "org.finance.calcs.processing"
version = "1.0"

dependencies {
    implementation(project(":core"))
    testImplementation(testFixtures(project(":core")))
}