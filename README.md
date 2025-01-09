# **Librería Genérica con Arquitectura Hexagonal**

[![Licencia](https://img.shields.io/badge/license-Custom-orange)](LICENSE)  
[![Java](https://img.shields.io/badge/language-Java-blue)](https://www.java.com/)

## **Descripción**
Esta librería es una base genérica para implementar proyectos con la **Arquitectura Hexagonal**, proporcionando una estructura modular y desacoplada para el desarrollo de aplicaciones robustas y escalables.

Incluye:
- Una **estructura estándar** para los módulos de dominio, aplicación e infraestructura.
- Interfaces genéricas y ejemplos para manejar **casos de uso** y **adaptadores**.
- Una base para incorporar fácilmente dependencias externas como bases de datos, servicios externos o colas de mensajes.

Este proyecto es ideal para desarrolladores que deseen comenzar rápidamente con un diseño basado en arquitectura hexagonal.

---

## **Características principales**
- **Modularidad:** Separación clara entre capas y módulos.
- **Desacoplamiento:** Dependencia de interfaces en lugar de implementaciones concretas.

---

## **Estructura del Proyecto**
La librería está organizada según los principios de la arquitectura hexagonal:

```plaintext
src/main/java
├── es.bxg.commonlib
│   ├── application    # Lógica de aplicación y casos de uso
│   ├── domain         # Entidades, agregados, y lógica de dominio
│   ├── infrastructure # Adaptadores y configuración técnica

```

## Licencia

Este software está disponible bajo la [Licencia de Uso Personal y Educativo](LICENSE).

- **Uso permitido:** Fines personales, educativos o de investigación no comercial.
- **Uso prohibido:** Fines comerciales, redistribución pública o uso por empresas privadas.

Para obtener más información o consultar sobre licencias comerciales, contacta a [tu correo].