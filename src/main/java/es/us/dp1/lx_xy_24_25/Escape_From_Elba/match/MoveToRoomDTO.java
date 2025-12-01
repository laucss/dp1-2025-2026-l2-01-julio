package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MoveToRoomDTO {

    private Integer userId; 

    private String roomName; 


    public MoveToRoomDTO(){
    }

    public MoveToRoomDTO(Integer userId, String roomName){
        this.userId=userId; 
        this.roomName=roomName; 
    }
    
}
