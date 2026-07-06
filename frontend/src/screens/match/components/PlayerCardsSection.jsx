import "../../../static/css/match/components/playerCardsSection.css";

export default function PlayerCardsSection({
    handCards,
    bagCards
}) {
    // se podría hacer otro componente que fuera cardContainer porque tal cual está el código ahora es código duplicado
    // pero por no complicarnos ahora mismo, lo vamos a dejar así
    return (
        <div className="player-section">
            <div className="cards-section">

                <div className="player-hand">
                    <div className="hand-cards">
                        {Array.isArray(handCards) &&
                            handCards.map((carta) => (
                                <div key={carta.id}>
                                    <img
                                        src={`/resources${carta.frontImage}`}
                                        alt={`Carta ${carta.letter}`}
                                        className="card"
                                    />
                                </div>
                            ))}
                    </div>
                </div>

                <div className="player-bag">
                    <div className="bag-cards">
                        {Array.isArray(bagCards) &&
                            bagCards.map((carta) => (
                                <div key={carta.id}>
                                    <img
                                        src={`/resources${carta.frontImage}`}
                                        alt={`Carta ${carta.letter}`}
                                        className="card"
                                    />
                                </div>
                            ))}
                    </div>
                </div>

            </div>
        </div>
    );
}