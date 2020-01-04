package jenigma;


/**
 * Une vue simplifiée de la machine <a
 * href="http://fr.wikipedia.org/wiki/Enigma_(machine)">enigma</a>, sans tableau
 * de permutations.
 * 
 * Des détails sur les différents rotors (substitutions) utilisées sur les
 * différents modèles sont <a
 * href="http://en.wikipedia.org/wiki/Enigma_rotor_details">disponibles en
 * ligne</a>.
 * 
 * On suppose dans cette interface que les rotors réels identifiés I à VIII
 * seront représentés par les entiers 1 à 8. Les autres entiers ne seront pas
 * définis. Ils pourront être utilisés pour représenter des rotors
 * personnalisés.
 * 
 * On utilisera un seul réflecteur, le réflecteur B.
 * 
 * On ne prendra pas en compte la possibilité de décaler les permutations des rotors.
 * 
 * Cette interface est conçue pour permettre la réalisation d'une représentation
 * graphique de la machine enigma.
 * 
 * 
 * @author leberre
 *
 */
public interface Enigma {

    /**
     * Changement du rotor à une position donnée.
     * 
     * @param pos
     *            sa position
     * @param rotorNumber
     *            un nombre entier positif, entre 1 et 8 pour les rotors
     *            officiels.
     */
    void setRotor(Position pos, int rotorNumber);

    /**
     * Permet d'explorer la configuration de la machine.
     * 
     * @param pos
     *            l'emplacement du rotor
     * @return l'identifiant du rotor ou 0 s'il n'y en a pas.
     */
    int getRotor(Position pos);

    /**
     * Positionne le rotor à la position {@link pos} à la lettre {@link letter}.
     * 
     * @param pos
     *            la position du rotor concerné
     * @param letter
     *            une lettre majuscule, de 'A' à 'Z'.
     */
    void moveRotorToLetter(Position pos, char letter);

    /**
     * Retourne la lettre courante du rotor à la position {@link pos}.
     * 
     * @param pos
     *            la position du rotor concerné.
     * @return une lettre majuscule, de 'A' à 'Z' si un rotor existe à cette
     *         position, un espace ' ' sinon.
     */
    char getRotorLetter(Position pos);

    /**
     * Retourne de manière compacte les lettres correspondant aux rotors. Un
     * rotor absent est représenté par un espace.
     * 
     * @return une chaine de 3 caractères correspondant aux lettres des rotors.
     */
    String getCurrentLetters();

    /**
     * Encode une lettre.
     * 
     * Toute la complexité de la machine enigma est cachée derrière cette
     * méthode.
     * 
     * @param c
     *            une lettre entre 'A' et 'Z'. Tout autre caractère sera
     *            représenté par un 'X'.
     * @return la lettre encodée
     */
    char encode(char c);
}
