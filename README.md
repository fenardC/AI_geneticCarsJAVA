# AI_geneticCarsJAVA
Artificial intelligence project for car animation.

# Abstract
This project aims at integrating demonstration about neural network.  
Some source code was original found freely on web with some video too.  
The JAVA technology was in use and I decided to make this work my way.  


Cars are learnt to go on several circuits. Then a good selected car is put 
on an unknown circuit to demonstrate how the neural network works.

The architecture of the neural network is built around two neurons that calculate 
commands for steering wheel and engine of the car. Each neuron is given input 
values from sensors (the distances from the outside of the circuit and the current car position).

The learning strategy is to experience population of cars with initial random
 values for the weights and threshold for the neurons. 
Depending on completion achieved on the circuit, cars are scored. Until at least one car succeeded
 in completing circuits, the experience is redone with modification of the weights and the thresholds
  thanks to a genetic algorithm that takes into account the score of the cars.

_It is interesting to see this, I think._
_Cyril FENARD._

