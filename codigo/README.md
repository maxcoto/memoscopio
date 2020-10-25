# memoscopio

El proyecto consiste en un juego de memoria en donde hay que encontrar 10 pelotitas amarillas utilizando la pelotita azul.

La pelotita azul se mueve en la pantalla utilizando los valores del sensor del acelerómetro.

Al iniciar se otorgan 5 segundos en una cuenta regresiva donde se ven en pantalla las pelotitas amarillas. Terminado ese tiempo, las pelotitas amarillas se esconden y hay que recorrer la pantalla con la azul para encontrarlas, pasando por encima de las amarillas.

Si se coloca el dedo sobre el sensor de proximidad, el juego se pone en modo pausa, mostrando todas las pelotitas amarillas nuevamente para poder recordar en donde se ubicaban y deteniendo la pelotita azul. Pero cuidado! ya que poner pausa agrega una penalidad de 10 segundos cada vez que se utiliza.

El puntaje final es el tiempo que se tardó en encontrar todas las pelotitas amarillas más la penalidad por utilizar pausas.

Este puntaje será almacenado y mostrado en un ranking, donde se verán los tiempos de todos los usuarios que jugaron.
