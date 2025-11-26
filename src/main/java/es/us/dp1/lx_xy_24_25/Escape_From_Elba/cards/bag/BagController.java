package es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.bag;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.Card;



@RestController
@RequestMapping("/api/v1/bag")
public class BagController {

    BagService bagService;
    
    @Autowired
    public BagController(BagService bagService){
        this.bagService=bagService;
    }



    @GetMapping("/validate/{cards}")
    public ResponseEntity<Boolean> validateWord(@PathVariable List<Card> cards) {
        Boolean isValid = bagService.checkBagIsValid(cards);
        return ResponseEntity.ok(isValid);
    }
     

    
}
