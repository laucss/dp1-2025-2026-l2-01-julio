package es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.bag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/bag")
public class BagController {

    BagService bagService;
    
    @Autowired
    public BagController(BagService bagService){
        this.bagService=bagService;
    }

    
    @PostMapping("/validate-weapon/{matchId}")
    public ResponseEntity<WeaponValidationDTO> validateWeapon(@RequestBody @Valid BagInGameDTO bagDTO, @PathVariable Integer matchId) {
        WeaponValidationDTO response = bagService.validateWeapon(bagDTO, matchId);
        return ResponseEntity.ok(response);
    }}