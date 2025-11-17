package es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.deck;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.Card;


@RestController
@RequestMapping("/api/v1/deck")
public class DeckController {

    DeckService ds; 

   @Autowired
    public DeckController(DeckService ds){
        this.ds=ds;
    }

    @GetMapping("/{matchId}")
    public ResponseEntity<?> iniciarDeck(@PathVariable Integer matchId) {
        DeckInGame deck = ds.initializeDeck(matchId); 
    

        if (deck == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: No se pudo crear el mazo de los cojones");
        }

        return ResponseEntity.ok(deck);

    }

    @PostMapping("/{matchId}/draw")
    public ResponseEntity<Card> robarcarta( @PathVariable Integer matchId) {
        Card card = ds.drawCard(matchId); 

        return ResponseEntity.ok(card);


    }
    
}
