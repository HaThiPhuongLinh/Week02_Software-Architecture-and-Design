plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation ("com.github.javaparser:javaparser-core:3.25.8")
    implementation ("dev.mccue:guava-base:0.0.4")
    implementation ("org.fusesource.jansi:jansi:2.4.1")
    implementation ("org.apache.opennlp:opennlp-tools:2.3.1")
    implementation ("edu.stanford.nlp:stanford-corenlp:4.5.5")
}

tasks.test {
    useJUnitPlatform()
}   