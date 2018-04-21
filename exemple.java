// Exemples qui ne peuvent pas être exécutés tel quels, je n'ai pas tout mis car mon framework fait 20k lignes en 200 fichiers (et ya pas de commentaire d'habitude :p).
// Mais ça donne une idée

// ============== DEBUT DU PROGRAMME ===============
// Définition du réseau de neurones
NeuralNetwork network = new NeuralNetwork(5.0);

// Les données utilisables
for (int i = 0 ; i < nbCapteur ; i++) {
	network.addInput(new NeuralInputValue());
}
network.addInput(new NeuralInputValue()); // Pour la vitesse actuelle de la voiture

// Structure simple : 2 neurones
NeuralLayer layerOuput = new NeuralLayer(NeuralActivation.SIGMOID, 2); // SIGMOID = Fonction douce qui borne entre 0 et 1
network.addLayer(layerOuput);
network.connectAllInputOnFirstLayer();

// Instanciation de l'algorithme génétique
population = new GeneticPopulation();
population.generatePopulation(100, network.generateDnaModel().randomDna()); // On donne comme ADN modèle un ADN généré à partir du réseau de neurones

// ============== A CHAQUE DEBUT DE COURSE ==============
// On génère les voitures avec les individus génétiques

for (GeneticIndividual indiv : population.getListIndividual()) {
	Car car = new Car(TYPE_SENSOR_LINE, 0.5);
	car.putOnTrack(currentTrack);
	car.setIndiv(indiv);
}

// =============== A CHAQUE FIN DE COURSE ================
// Pour chaque voiture, on donne son score
car.getIndiv().setScore(score);

// On crée la nouvelle generation
population.proceedNextGeneration();

// ============== A CHAQUE TICK DE CALCUL ==============
// Chargement de l'IA depuis l'ADN de la voiture
network.initFromDna((GeneticDnaNeuralNetwork) car.getIndiv().getDna());
int i;
// On donne les informations de la voiture au réseau
for (i = 0 ; i < sensorLineNumber ; i++) {
	network.setInputValue(i, car.getSensorValue("s" + i)); // Capteurs
	i++;
}
network.setInputValue(i, car.getEngine() / 128.0); // Vitesse de la voiture
network.calculate(); // On lance le calcul de tous les neurones du réseau

// On récupère la liste des valeurs, en l'occurence, 2 valeurs
List<Double> listResult = network.getListResult();
int engineCommand = Misc.mix(-128, 128, listResult.get(0)); // mix = fonction qui transforme un ratio donné entre 0 et 1 en une valeur entre le 1er et le 2ème paramètre
int wheelCommand = Misc.mix(-128, 128, listResult.get(1));

// On donne les commandes à la voiture
car.setEngineCommand(engineCommand);
car.setWheelCommand(wheelCommand);

// Tick de la voiture sur le circuit
car.tick(millis);
