plugins {
    kotlin("jvm") version "1.9.23"
    id("app.cash.sqldelight") version "2.0.2"
    kotlin("plugin.serialization") version "1.9.23"
    id("com.google.devtools.ksp") version "1.9.23-1.0.20"
}

group = "org.Cine"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    //SqlDelight
    implementation("app.cash.sqldelight:sqlite-driver:2.0.2")
    // Result
    implementation("com.michael-bull.kotlin-result:kotlin-result:2.0.0")
    // Koin
    implementation(platform("io.insert-koin:koin-bom:3.5.6"))
    implementation("io.insert-koin:koin-core") // Core
    implementation("io.insert-koin:koin-test") // Para test y usar checkModules
    // Koin Anotaciones
    implementation(platform("io.insert-koin:koin-annotations-bom:1.3.1")) // BOM
    implementation("io.insert-koin:koin-annotations") // Annotations
    ksp("io.insert-koin:koin-ksp-compiler:1.3.1") // KSP Compiler
    // Serialización JSON
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    // Logger
    implementation("org.lighthousegames:logging:1.3.0")
    implementation("ch.qos.logback:logback-classic:1.4.14")
    // Mock
    testImplementation("io.mockk:mockk:1.13.10")
    // Koin Test
    testImplementation("io.insert-koin:koin-test-junit5")

}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

sqldelight {
    databases {
        // Nombre de la base de datos
        create("AppDatabase") {
            // Paquete donde se generan las clases
            packageName.set("org.examples.database")
        }
    }
}