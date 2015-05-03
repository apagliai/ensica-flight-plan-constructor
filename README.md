# Flight-Plan-Constructor
Flight plans constructor. Developped with BlueJ (Java Programming)

====================================================================================

Steps to use this application
-----------------------------

1. `FondDeCarte`: Before doing anything, you must load a map. To manage it, you must use a map in the repository of the project. This map must be in a JPEG format. For instance, to load a map named Toulouse Arrival, you must write "Toulouse Arrival.jpg".
2. `Waypoint`: They are created one by one. The *by-click-constructor* is much more efficient and precise. With the *by-coordonate-constructor*, coordonates are giving regarding the map size, and not the graphic window size. For instance, a 4000*1500p map can receive a Waypoint with coordonates (2000;750) which is the center of the map, and not the center of the graphic window -it depends on if you previously zoomed or moved on the map.
3. `Leg`: each leg has a different behaviour. Follow instructions for each ...

Instructions
------------

The best method is a `FondDeCarte` method named **deplacementEtZoomClavier**. Please read the JavaDoc of this method to know how to use it.

Miscellaneous
=============

This README completes the JavaDoc but does not replace it! To get further information about methods, please refer to it.
