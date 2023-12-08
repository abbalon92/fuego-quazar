# fuego-quasar

Fuego de quazar es un servicio que contesta a la Operacion Fuego de Quasar Challenge de Mercado libre el cual debe realizar el calculo estimado de la posicion de un portacargo a travez del ingreso de unos unas distancias proporcionados por unos satelites y la camptura de la traza de un mensaje secreto.

Espeficicaciones:
- Lenguaje: Java V.17 
- Framework: Spring Boot V2.0 Maven
- Gestor de dependencias: Maven
- Logs: Logback
- Lombok
- Junit V.5
- Swagger V.3


## Istalación

Para realizar la instalacion del servicio en ambiente local primero se debe realizar la clonacion del proyecto con el siguiente comando

Nota. Para realizar esta accion debe contar con los accesos al repositorio.

```
git clone https://gitlab.com/prueba-meli/fuego-quazar.git
```
Luego de terminada la clonacion importar el proyecto en tu IDE de preferencia. Luego de esto realizar una actualizacion de referencias o descarga de recursos para la instalacion de las dependencias.

Si se prefiere se puede ejecutar el siguiente comando dentro de la ruta en donde se encuentra el proyecto 

```
mvn clean install
```
Nota. tener presente de que el equipo debe contar con una instalacion de Maven en tu equipo local.

Luego de finalizado los pasos anteriores ya puedes proceder a correr el servicio en tu ambiente local ya sea atravez de tu IDE o por terminal.

## Uso

Para el uso se puede apoyar en herramientas como Postman o Insomnia en donde podras cargar la especificacion de los serivios que puedes descargar ingresando al siguiente link:
- [Usar este enlace cuando tu servicio local este corriendo](http://localhost:5000/api-docs.yaml)
- [Usar este enlace si se va a consumir el servicio desde AWS](http://fuego-quazar.us-east-2.elasticbeanstalk.com/api-docs.yaml)

Estos servicios no requieren de un token de sesion ya que es solo un ejercicio y no consume informacion sensible

## Test

Si desea realizar pruebas: puede utilizar el siguiente objeto para las diferentes peticiones.

```
[
    {
        "name": "kenobi",
        "distance": 538.5164807,
        "message": ["este","","","mensaje",""]
    },
    {
        "name": "skywalker",
        "distance": 565.6854249,
        "message": ["","es", "","","secreto"
        ]
    },
    {
        "name": "sato",
        "distance": 824.6211251,
        "message": ["este","","un","",""]
    }
]
```
Y este es el resultado esperado en los servicios de ubicacion
```
{
    "position": {
        "x": -299.99999996060967,
        "y": 299.9999999697573
    },
    "message": "este es un mensaje secreto"
}
```

## Documentacion

Para visualizar la documentacion de las APIs las puedes ven en los siguientes enlaces

- Local: http://localhost:5000/swagger-ui/index.html

- AWS: 
    http://fuego-quazar.us-east-2.elasticbeanstalk.com/swagger-ui/index.html

Con respecto a los metodos todos cuentan con JavaDoc

