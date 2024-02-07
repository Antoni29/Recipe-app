# Recipe-app
Este proyecto es acerca de una app que consulta un listado de recetas mediante una API gratuita
llamada [spoonacular API](https://spoonacular.com/food-api/docs#Search-Recipes-Complex), la cual
nos proporciona el listado y el detalle de alguna receta mediante su ID.

## Patrones de diseño y librerias
Decidí utilizar el patron de _Inyección de dependencias_ utilizanso la libreria **Hilt** ya que esto
nos permite tener mas orden en el código y siempre que necesitemos alguna instancia va a estar disponible.
Tambien se puede decir que se utiliza el patrón _Singleton_ puesto que utilizamos la notación @Singleton
para indicar que dicha instancia será única.

Al hacer uso de la libreria **Retrofit** para consumir los endpoits de la API tamien utilizamos el patron
_Facade_ el cual nos permite utilizar una interface de alto nivel mucho mas facil de usar.


## Arquitectura
Para arquitectura utilizo **MVVM** con **Clean Architecture** ya que de esta manera siento que la estructura
del proyecto queda mucho mas organizada y haciendo uso de Clean Architecture teniendo los _UseCase_ y
los _Repository_ hace mucho mas facil el escribir _UnitTests_.

## Otras librerias
Ademas de lo antes mencionado, también utilizo la librería de **Glide** que nos facilita el mostrar
una imagen en un ImageView a través de la URL de la misma.
También se hace uso de la librería **SwipeRefreshLayout** que es utilizada para mostrar un mini loader
hasta arriba del RecyclerView que se encuentra en HomeActivity para indicar cuando se está consultando
data a la API.
