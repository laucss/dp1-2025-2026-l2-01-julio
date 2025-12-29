package es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class DictionaryService {

    /*
     * Función de este service: cargar un diccionario local (dictionary.txt en la carpeta resources) con palabras en inglés.
     * Este archivo txt, se ha hecho de forma manual, porque nos pasó que la api a la que llamábamos para validar palabras dejó de funcionar el día de la presentación del sprint, 
     *      entonces se nos recomendó hacer este diccionario local para intentar evitar este tipo de errores, sobre todo en la entrega final del proyecto. 
     *
     * También se va a añadir en este archivo la carga de la lista de armas (weapons.txt) para no tener que hacer otro archivo service más.
     *      Esta lista de armas la da el propio juego en sus reglas, para facilitar las cosas. 
     */



    // diccionario de palabras 
    private Set<String> dictionary = new HashSet<>(); 

    // lista de armas
    private Set<String> weapons = new HashSet<>(); 


    // cargar el diccionario al iniciar el servicio
    public void loadDictionary() throws IOException {
        InputStream is = getClass().getResourceAsStream("dictionary.txt");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            dictionary = reader.lines()
                        .map(String::toLowerCase)
                        .collect(Collectors.toSet()); 
            }
    }

    // cargamos la lista de armas al iniciar el servicio
    public void loadWeapons() throws IOException {
        InputStream is = getClass().getResourceAsStream("weapons.txt");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            weapons = reader.lines()
                        .map(String::toLowerCase)
                        .collect(Collectors.toSet()); 
            }
    }
                     

    // para checkear si una palabra está en el diccionario
    public boolean containsWord(String word) {
        return dictionary.contains(word.toLowerCase());
    }

    // para checkear si una palabra es un arma dentro de la lista dada por el juego 
    public boolean isWeapon(String word) {
        return weapons.contains(word.toLowerCase());
    }
    
}
