# UTNG Runner - Wear OS

Este proyecto implementa el juego UTNG Runner directamente en un reloj Wear OS usando Compose for Wear OS, arquitectura MVVM limpia, principio de responsabilidad única y separación de capas[cite: 1]. Al terminar tendrás una app publicable en Google Play[cite: 1].

## Desarrolladora
* **Alumna:** Mirza Natzielly Morales Lezama
* **Institución:** Universidad Tecnológica del Norte de Guanajuato (UTNG)

## Visión General del Proyecto
UTNG Runner es un juego endless runner que corre nativamente en la pantalla circular del reloj inteligente[cite: 1]. El ingeniero (personaje) corre por el campus de la UTNG esquivando obstáculos (tareas, exámenes, bugs y repositorios) y recogiendo créditos académicos[cite: 1].

## Stack Tecnológico
* **Lenguaje:** Kotlin 1.9+[cite: 1]
* **UI Framework:** Compose for Wear OS (androidx.wear.compose)[cite: 1]
* **Arquitectura:** MVVM + Clean Architecture (3 capas)[cite: 1]
* **Motor de juego:** Canvas + coroutines (loop a 60 fps)[cite: 1]
* **Estado:** StateFlow + rememberStateOf[cite: 1]
* **Persistencia:** DataStore Preferences (best score)[cite: 1]
* **Sensor de FC:** Health Services API (PassiveMonitoringClient)[cite: 1]
* **Haptics:** WearableHapticFeedback[cite: 1]
* **Testing:** JUnit 4 + coroutinesTest[cite: 1]
* **Build:** Gradle KTS · minSdk 30 (Wear OS 3.0)[cite: 1]

## Arquitectura de Capas
Regla de oro de arquitectura limpia: las capas internas NO conocen a las capas externas[cite: 1]. Domain no importa nada de Data ni de UI[cite: 1]. Data no importa nada de UI[cite: 1].

* **Presentación (`utngrunner.presentation`):** Composables, ViewModel, Estado de UI[cite: 1]. No tiene lógica de negocio[cite: 1].
* **Dominio (`utngrunner.domain`):** Entidades del juego, casos de uso, interfaces de repositorio[cite: 1]. Kotlin puro, sin Android[cite: 1].
* **Datos (`utngrunner.data`):** Implementaciones de repositorio, DataStore, Health Services[cite: 1]. Conoce Android SDK[cite: 1].

## Requisitos y Ejecución
* **SDK Mínimo:** minSdk 30 (Wear OS 3.0 mínimo)[cite: 1].
* **Pruebas Unitarias:** Ejecutar tests con el comando `./gradlew :wear:test`[cite: 1]. Deben pasar sin emulador porque el dominio es Kotlin puro[cite: 1].
* **Controles:** Toca o gira la corona del reloj (Rotary Input) para que el personaje salte o se deslice[cite: 1].

*Video de funcionalidad*
https://drive.google.com/file/d/125Z3Ps2KXgOeRi-6TRgxuB7SnQoeGOMc/view

<img width="465" height="473" alt="Captura de pantalla 2026-06-18 131007" src="https://github.com/user-attachments/assets/216a88b7-e214-43c6-aa7b-f38685b7a8d2" />
<img width="482" height="497" alt="Captura de pantalla 2026-06-18 131029" src="https://github.com/user-attachments/assets/d8608d54-558b-4b95-8ac7-729b914a15e0" />
