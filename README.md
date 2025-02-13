# CRM Inmobiliaria con Kubernetes

## Idea del proyecto

La idea es hacer un CRM que se encargue de la organización de una inmobiliaria la cual tenga varios vendedores y que este desplegado con Kubernetes

En Kubernetes se desplegarían la API Rest con Apache Tomcat y una nueva instancia de MySQL

## ¿Qué es Kubernetes?

Kubernetes es un sistema de orquestación de contenedores de código abierto diseñado para automatizar la implementación, escalado y administración de aplicaciones en contenedores.

## ¿Para qué sirve Kubernetes?
Kubernetes sirve para gestionar y automatizar la implementación, escalado y operación de aplicaciones en contenedores dentro de un entorno distribuido. Gracias a su capacidad de orquestación, permite asegurar la alta disponibilidad de los servicios, equilibrar la carga de trabajo y optimizar el uso de los recursos de hardware. Además, facilita la recuperación ante fallos, ya que si un contenedor deja de funcionar, Kubernetes lo reemplaza automáticamente. Su uso es ideal para arquitecturas modernas basadas en microservicios, donde se requiere flexibilidad, resiliencia y facilidad de despliegue.

## Conceptos básicos de Kubernetes
- Pods: Unidad mínima de despliegue en Kubernetes que puede contener uno o más contenedores.
- Deployments: Controla la gestión y actualización de los pods.
- Services: Expone las aplicaciones dentro o fuera del clúster y facilita la comunicación entre componentes.
- ConfigMaps y Secrets: Almacenan configuraciones y datos sensibles (como credenciales de MySQL).
- Namespaces: Permiten segmentar recursos dentro del clúster para una mejor organización.
- Volúmenes Persistentes (PV y PVC): Permiten almacenar datos de forma persistente, útil para la base de datos MySQL.

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

## Endpoints

### 1. Usuarios

  - `GET /usuarios/`: Permite obtener la información de todos los usuarios.
    - **RUTA PRIVADA** Todas las peticiones a este endpoint se permiten si eres admin.
    - **Entrada**: N/A.
    - **Salida**: Lista con todos los objetos UserDTO de los usuarios.
    - **Excepciones**:
      - Unauthorized (Error 403): No tienes suficientes permisos.
      - Internal Server Error (Error 500): Error interno de la base de datos.
  - `GET /usuarios/{id}`: Permite obtener información de un usuario a traves de su id.
    - **RUTA PRIVADA** Todas las peticiones a este endpoint se permiten si eres admin o se le permite acceder al usuario que este asociado a la id solicitada.
    - **Entrada**: Parametro `id`.
    - **Salida**: Objeto UserDTO.
    - **Excepciones**:
      - Bad Request Exception (Error 400): El campo id no puede estar vacío.
      - Illegal number exception (Error 400): La id debe de ser un número.
      - Unauthorized (Error 403): No tienes suficientes permisos.
      - Username not found (Error 404): No existe ningun usuario asociado a esa id.
      - Internal Server Error (Error 500): Error interno de la base de datos.
  - `POST /usuarios/register`: Permite crear un usuario.
    - **RUTA PÚBLICA** Todas las peticiones a este endpoint deben permitirse.
    - **Entrada**: JSON con `username` y `password`.
    - **Salida**: Objeto UserDTO.
    - **Excepciones**:
      - Bad Request Exception (Error 400): Los campos username y password no pueden estar vacios.
      - Internal Server Error (Error 500): Error interno de la base de datos.
  - `POST /usuarios/login`: Permite al usuario autenticarse.
    - **RUTA PÚBLICA** Todas las peticiones a este endpoint deben permitirse.
    - **Entrada**: JSON con `username` y `password`.
    - **Salida**: Token de la sesión si las credenciales son válidas.
    - **Excepciones**:
      - Bad Request Exception (Error 400): Los campos username y password no pueden estar vacios.
      - Invalid Password Exception (Error 401): La contraseña introducida es incorrecta.
      - Username not found (Error 404): No existe ningun usuario asociado a ese username.
      - Internal Server Error (Error 500): Error interno de la base de datos.
  - `PUT /usuarios/{username}`: Permite actualizar el username o password de un usuario.
    - **RUTA PRIVADA** Todas las peticiones a este endpoint se permiten si eres admin o se le permite acceder al usuario que este asociado al username pasado por parametro.
    - **Entrada**: Parametro `username` y JSON UserDTO con la nueva información del usuario.
    - **Salida**: Nuevo objeto UserDTO.
    - **Excepciones**:
      - Bad Request Exception (Error 400): El campo username y los campos del objeto user no pueden estar vacios.
      - Unauthorized (Error 403): No tienes suficientes permisos.
      - Username not found (Error 404): No existe ningun usuario asociado a ese username.
      - Internal Server Error (Error 500): Error interno de la base de datos.
  - `PUT /usuarios/internal/{username}`: Permite actualizar el username, rol, id propietario y contraseña de un usuario.
    - **RUTA PRIVADA** Todas las peticiones a este endpoint se permiten si eres admin o se le permite acceder al usuario que este asociado al username pasado por parametro.
    - **Entrada**: Parametro `username` y JSON UserUpdateDTO con la nueva información del usuario.
    - **Salida**: Nuevo objeto UserUpdateDTO.
    - **Excepciones**:
      - Bad Request Exception (Error 400): El campo username y los campos del objeto user no pueden estar vacios.
      - Unauthorized (Error 403): No tienes suficientes permisos.
      - Username not found (Error 404): No existe ningun usuario asociado a ese username.
      - Internal Server Error (Error 500): Error interno de la base de datos.
  - `DELETE /usuarios/{username}`: Permite al administrador eliminar un usuario por su username.
    - **RUTA PRIVADA** Las peticiones se permiten si eres administrador.
    - **Entrada**: Parametro `username`.
    - **Salida**: N/A.
    - **Excepciones**:
      - Bad Request Exception (Error 400): El campo username no puede estar vacío.
      - Unauthorized (Error 403): No tienes suficientes permisos.
      - Username not found (Error 404): No existe ningun usuario asociado a ese username
      - Internal Server Error (Error 500): Error interno de la base de datos.

### 2. Propiedad

  - `GET /propiedades/`: Permite obtener la información de todas las propiedades.
    - **RUTA PRIVADA** Todas las peticiones a este endpoint se permiten si eres admin.
    - **Entrada**: N/A.
    - **Salida**: Lista con todos los objetos PropiedadDTO de todas las propiedades.
    - **Excepciones**:
      - Unauthorized (Error 403): No tienes suficientes permisos.
      - Internal Server Error (Error 500): Error interno de la base de datos.
  - `GET /propiedades/{id}`: Permite obtener información de una propiedad a traves de su id.
    - **RUTA PRIVADA** Todas las peticiones a este endpoint se permiten si eres admin o se le permite acceder al usuario que este asociado a la propiedad solicitada.
    - **Entrada**: Parametro `id`.
    - **Salida**: Objeto PropiedadDTO.
    - **Excepciones**:
      - Bad Request Exception (Error 400): El campo id no puede estar vacío.
      - Illegal number exception (Error 400): La id debe de ser un número.
      - Unauthorized (Error 403): No tienes suficientes permisos.
      - Propiedad not found (Error 404): No existe ninguna propiedad asociada a esa id.
      - Internal Server Error (Error 500): Error interno de la base de datos.
  - `POST /propiedades/`: Permite crear una propiedad.
    - **RUTA PRIVADA** Todas las peticiones a este endpoint deben permitirse si eres administrador.
    - **Entrada**: JSON con `id_propietario`, `direccion`, `precio` y `oculta`.
    - **Salida**: Objeto PropiedadDTO.
    - **Excepciones**:
      - Bad Request Exception (Error 400): Los campos `id_propietario`, `direccion`, `precio` y `oculta` no pueden estar vacios.
      - Illegal number exception (Error 400): La id del propietario y el precio deben de ser un número.
      - Propietario Not Found Exception (Error 404): No hay ningun propietario asociado a esa id.
      - Internal Server Error (Error 500): Error interno de la base de datos.
  - `PUT /propiedades/{id}`: Permite actualizar la propiedad.
    - **RUTA PRIVADA** Todas las peticiones a este endpoint se permiten si eres admin.
    - **Entrada**: Id de la propiedad por parametro y JSON PropiedadDTO con la nueva información de la propiedad.
    - **Salida**: Nuevo objeto PropiedadDTO.
    - **Excepciones**:
      - Bad Request Exception (Error 400): Los campos del objeto propiedad o la id no pueden estar vacios.
      - Illegal number exception (Error 400): La id, la id del propietario y el precio deben de ser un número.
      - Unauthorized (Error 403): No tienes suficientes permisos.
      - Propiedad not found (Error 404): No existe ninguna propiedad con esa id.
      - Internal Server Error (Error 500): Error interno de la base de datos.
  - `DELETE /propiedades/{id}`: Permite al administrador eliminar una propiedad por su id.
    - **RUTA PRIVADA** Las peticiones se permiten si eres administrador.
    - **Entrada**: Parametro `id`.
    - **Salida**: N/A.
    - **Excepciones**:
      - Bad Request Exception (Error 400): El campo id no puede estar vacío.
      - Illegal number exception (Error 400): La id debe de ser un número.
      - Unauthorized (Error 403): No tienes suficientes permisos.
      - Propiedad not found (Error 404): No existe ninguna propiedad asociada a esa id.
      - Internal Server Error (Error 500): Error interno de la base de datos.

### 3. Propietario

  - `GET /propietarios/`: Permite obtener la información de todos los propietarios.
    - **RUTA PRIVADA** Todas las peticiones a este endpoint se permiten si eres admin.
    - **Entrada**: N/A.
    - **Salida**: Lista con todos los objetos PropietarioDTO de todos los propietarios.
    - **Excepciones**:
      - Unauthorized (Error 403): No tienes suficientes permisos.
      - Internal Server Error (Error 500): Error interno de la base de datos.
  - `GET /propietarios/{id}`: Permite obtener información de una propietario a traves de su id.
    - **RUTA PRIVADA** Todas las peticiones a este endpoint se permiten si eres admin.
    - **Entrada**: Parametro `id`.
    - **Salida**: Objeto PropietarioDTO.
    - **Excepciones**:
      - Bad Request Exception (Error 400): El campo id no puede estar vacío.
      - Illegal number exception (Error 400): La id debe de ser un número.
      - Unauthorized (Error 403): No tienes suficientes permisos.
      - Propietario not found (Error 404): No existe ningun propietario asociado a esa id.
      - Internal Server Error (Error 500): Error interno de la base de datos.
  - `POST /propietarios/`: Permite crear un propietario.
    - **RUTA PRIVADA** Todas las peticiones a este endpoint deben permitirse si eres administrador.
    - **Entrada**: JSON con `nombre`, `apellidos`, `telefono`, `genero`, `casado` y `n_hijos`.
    - **Salida**: Objeto PropietarioDTO.
    - **Excepciones**:
      - Bad Request Exception (Error 400): Los campos `nombre`, `apellidos`, `telefono`, `genero`, `casado` y `n_hijos` no pueden estar vacios.
      - Illegal number exception (Error 400): n_hijos debe de ser un número.
      - Internal Server Error (Error 500): Error interno de la base de datos.
  - `PUT /propietarios/{id}`: Permite actualizar el propietario.
    - **RUTA PRIVADA** Todas las peticiones a este endpoint se permiten si eres admin.
    - **Entrada**: Id del propietario por parametro y JSON PropietarioDTO con la nueva información del propietario.
    - **Salida**: Nuevo objeto PropietarioDTO.
    - **Excepciones**:
      - Bad Request Exception (Error 400): Los campos del objeto propietario o la id no pueden estar vacios.
      - Illegal number exception (Error 400): La id y n_hijos deben de ser un número.
      - Unauthorized (Error 403): No tienes suficientes permisos.
      - Propietario not found (Error 404): No existe ningun propietario con esa id.
      - Internal Server Error (Error 500): Error interno de la base de datos.
  - `DELETE /propietarios/{id}`: Permite al administrador eliminar un propietario por su id.
    - **RUTA PRIVADA** Las peticiones se permiten si eres administrador.
    - **Entrada**: Parametro `id`.
    - **Salida**: N/A.
    - **Excepciones**:
      - Bad Request Exception (Error 400): El campo id no puede estar vacío.
      - Illegal number exception (Error 400): La id debe de ser un número.
      - Unauthorized (Error 403): No tienes suficientes permisos.
      - Propietario not found (Error 404): No existe ningun propietario asociada a esa id.
      - Internal Server Error (Error 500): Error interno de la base de datos.
      - Internal Server Error (Error 500): No se puede eliminar un propietario con propiedades asociadas sin asignarlas a otro propietario.
