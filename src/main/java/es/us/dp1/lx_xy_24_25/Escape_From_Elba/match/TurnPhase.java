package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match;

public enum TurnPhase {
    ORDER_DECISION,
    DRAW, //Robar cartas del mazo primero
    ACTIONS,  //Realizar acciones según los puntos de acción disponibles
    DISCARD // Descartar cartas al final del turno

}
