package es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.deck;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.Card;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.CardDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.MatchService;


@RestController
@RequestMapping("/api/v1/deck")
public class DeckController {

    DeckService deckService;
    MatchService matchService;  

   @Autowired
    public DeckController(DeckService deckService, MatchService matchService) {
        this.deckService=deckService;
        this.matchService=matchService;
    }

    @GetMapping("/{matchId}")
    public ResponseEntity<?> iniciarDeck(@PathVariable Integer matchId) {
        DeckInGame deck = deckService.initializeDeck(matchId); 
    

        if (deck == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: No se pudo crear el mazo de los cojones");
        }

        return ResponseEntity.ok(deck);

    }


    // @PostMapping("/{matchId}/{playerId}/draw")
    // public ResponseEntity<CardDTO> drawCardFromDeck( @PathVariable Integer matchId, @PathVariable Integer playerId) {
    @PostMapping("/{matchId}/draw")
    public ResponseEntity<CardDTO> drawCardFromDeck( @PathVariable Integer matchId) {
        Card card = deckService.drawCard(matchId); 
        // Card card = matchService.playerDrawsCardFromDeck(matchId, playerId);

        return ResponseEntity.ok(new CardDTO(card));

    }
    
}
