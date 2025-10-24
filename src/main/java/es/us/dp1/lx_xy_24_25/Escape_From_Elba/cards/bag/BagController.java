package es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.bag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/bag")
public class BagController {

    BagService bagService;
    @Autowired
    public BagController(BagService bagService){
        this.bagService=bagService;
    }


    /*

    Poner una cosa así que llame al service de bag y tal 

    checkear lo que he puesto abajo pq lo he copiado de chati pq me tenía que ir 


    @PostMapping("/validate")
    public ResponseEntity<?> validateWord(@RequestBody WordRequest request) {
        boolean isValid = bagService.isValidWordForBag(request.getPlayerId(), request.getWord());
        return ResponseEntity.ok(Map.of("valid", isValid));
    }
     
     */
    
}
