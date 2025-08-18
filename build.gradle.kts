plugins {
    id("java")
}

configurations{
    compileOnly{
        extendsFrom(configurations.annotationProcessor.get())
    }
}

group = "org.example"
version = "1.0-SNAPSHOT"

// Removido toolchain para usar JDK local e simplificar build

repositories {
    mavenCentral()
}

dependencies {

    compileOnly("org.projectlombok:lombok:1.18.36")
    annotationProcessor("org.projectlombok:lombok:1.18.36")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}