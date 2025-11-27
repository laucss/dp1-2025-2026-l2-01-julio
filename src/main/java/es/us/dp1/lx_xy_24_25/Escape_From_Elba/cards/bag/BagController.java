package es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.bag;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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



    @PostMapping("/validate")
    public ResponseEntity<Boolean> validateWord(@RequestBody BagInGameDTO cardsDTO) {
        System.out.println(cardsDTO);
        Boolean isValid = bagService.checkBagIsValid(cardsDTO.getCards());
        System.out.println(isValid);
        return ResponseEntity.ok(isValid);
    }
     

    
}
