README - Anthony PAGLIAI et Colin MOURARD

Ce fichier constitue bien sûr un complément de la JAVA DOC, il ne la remplace en aucun cas.
Pour plus de précision sur l'utilisation des méthodes en elles-mêmes, prière de vous y référer.



Utilisation du constructeur de plans de vol - Etapes indispensables

1- Avant toute chose, il faut CREER LE FOND DE CARTE. 
Pour cela, placer une image (ou utiliser) présente dans le dossier plan de vol. Ne pas oublier les guillemets et l'extension. Par exemple, pour charger un fichier jpg dont le nom est france south, il faut écrire "france south.jpg"

2- Les WAYPOINTS sont crées UN PAR UN.
Etant donné que nous n'avons pas pris en compte les coordonnées latitudes telles qu'elles sont en vrai, le constructeur par clic est beaucoup plus efficace puisque très précis. Par ailleurs, on peut tout de même entrer un WayPoint avec le clavier, les coordonnées correspondent à la position, en pixels, relativement à la taille du fichier image (et non pas de la fen�tre graphique !).
Par exemple, pour une map dont la taille est 4000*1500 pixels, si on entre les coordonn�es LONGITUDE 2000 et LATITUDE 750, cela correspondra au centre de la map, qui n'est pas forc�ment le centre de la fen�tre graphique, suivant les �ventuels d�placements et zooms subis par le fond de carte auparavant...

3- Les LEGS sont aussi crées UN PAR UN.
Les instructions �tant diff�rentes pour chacun, elles apparaitront au fur et à mesure dans la console.


Utilisation du constructeur de plans de vol - Ergonomie 

1- La méthode privilégiée pour tout mouvement ou zoom sur la carte est une méthode de la classe FondDeCarte, elle s'appelle    "void deplacementEtZoomClavier()" 
   On peut également utiliser "void zoomer(int zoomfinal)" si on connaît déjà la valeur du zoom final d'affichage de la        map.
