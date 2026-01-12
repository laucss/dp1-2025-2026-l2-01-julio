package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MoveNpcToRoomDTO {

    private Integer userId; 

    private Integer roomId; 

    private Integer npcId;


    public MoveNpcToRoomDTO(){
    }

    public MoveNpcToRoomDTO(Integer userId, Integer roomId, Integer npcId){
        this.userId=userId; 
        this.roomId=roomId; 
        this.npcId=npcId;
    }
    
}
