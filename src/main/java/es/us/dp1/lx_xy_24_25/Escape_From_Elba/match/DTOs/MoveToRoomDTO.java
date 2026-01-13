package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MoveToRoomDTO {

    private Integer userId; 

    private Integer roomId; 

    public MoveToRoomDTO(){
    }

    public MoveToRoomDTO(Integer userId, Integer roomId){
        this.userId=userId; 
        this.roomId=roomId; 
    }
    
}
