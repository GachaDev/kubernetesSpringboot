# CRM Inmobiliaria con Kubernetes

## Idea del proyecto

La idea es hacer un CRM que se encargue de la organización de una inmobiliaria la cual tenga varios vendedores y que este desplegado con Kubernetes

En Kubernetes se desplegarían la API Rest con Apache Tomcat y una nueva instancia de MySQL

## ¿Qué es Kubernetes?

Kubernetes es un sistema de orquestación de contenedores de código abierto diseñado para automatizar la implementación, escalado y administración de aplicaciones en contenedores.

## ¿Para qué sirve Kubernetes?
Kubernetes sirve para gestionar y automatizar la implementación, escalado y operación de aplicaciones en contenedores dentro de un entorno distribuido. Gracias a su capacidad de orquestación, permite asegurar la alta disponibilidad de los servicios, equilibrar la carga de trabajo y optimizar el uso de los recursos de hardware. Además, facilita la recuperación ante fallos, ya que si un contenedor deja de funcionar, Kubernetes lo reemplaza automáticamente. Su uso es ideal para arquitecturas modernas basadas en microservicios, donde se requiere flexibilidad, resiliencia y facilidad de despliegue.

## Conceptos básicos de Kubernetes
Pods: Unidad mínima de despliegue en Kubernetes que puede contener uno o más contenedores.
Deployments: Controla la gestión y actualización de los pods.
Services: Expone las aplicaciones dentro o fuera del clúster y facilita la comunicación entre componentes.
ConfigMaps y Secrets: Almacenan configuraciones y datos sensibles (como credenciales de MySQL).
Namespaces: Permiten segmentar recursos dentro del clúster para una mejor organización.
Volúmenes Persistentes (PV y PVC): Permiten almacenar datos de forma persistente, útil para la base de datos MySQL.

## Arquitectura de Kubernetes:
Nodo maestro y nodos trabajadores:

El nodo maestro gestiona el clúster y los recursos.
Los nodos trabajadores ejecutan los contenedores.
Componentes principales:

- API Server: Punto de entrada para la administración del clúster.
- etcd: Almacén de datos clave-valor para la configuración del clúster.
- Scheduler: Asigna pods a los nodos según la disponibilidad de recursos.
- Controller Manager: Maneja controladores como el ReplicaSet y el Deployment.
- Kubelet: Ejecuta y gestiona los contenedores en cada nodo.

## ¿Por qué Kubernetes para el CRM?
- Escalabilidad: Se pueden agregar más instancias de la API y la base de datos según la demanda.
- Alta disponibilidad: Si un nodo falla, Kubernetes lo reemplaza automáticamente.
- Automatización: Kubernetes maneja el despliegue y actualizaciones sin tiempo de inactividad.
- Gestión eficiente de recursos: Kubernetes optimiza el uso de CPU y memoria en los nodos.
- Seguridad y configuración centralizada: Uso de Secrets y ConfigMaps para credenciales y configuraciones.
