import "../../static/css/rules/rulesPage.css"

export default function Rules() {
    return (
        <div className="rules-page">
            <div className="rules-card">
                <header className="rules-header">
                    <h1>Rules</h1>
                </header>

                <section className="rules-section">
                    <h2>Materials</h2>
                    <p>
                        The materials needed to play are very simple, as they mainly consist of two items.<br />
                        The board, which is made up of a different number of rooms; each room displays its name (which will be important
                        for one of the main game mechanics) and two dice — one black and one white — which will be used to place players in
                        random rooms.
                        <br /><br />
                    </p>
                    <p>
                        The cards, which feature two essential parts: the first letter of the card's name, located in the upper left corner,
                        and the set of escape words that can be formed using that letter.
                    </p>
                </section>

                <section className="rules-section">
                    <h2>Objective</h2>
                    <p>
                        The game begins by positioning the visitors. Each player rolls two dice — one black and one white — and places their
                        character in the room indicated by the results. Then, three cards are dealt to each player (if you wish to play with
                        more than six players, it is recommended to deal only two initial cards). A random player is chosen to start, and the
                        next turn goes to the player on their left.
                        <br /><br />
                    </p>
                    <p>
                        The goal is to obtain a set of cards — by drawing from the deck or stealing from other players — that allows you to
                        escape. Each card represents a letter (and shows all the game’s words in which that letter appears). You may also have
                        cards face-up on the table considered your “bag”; these cards allow you to form words that help you escape, fight, and
                        move around. To escape, it is necessary to increase your strength (all players start with 1 strength point), which is
                        gained by winning battles against other players.
                    </p>
                </section>

                <section className="rules-section">
                    <h2>Battles</h2>
                    <p>
                        A player who enters a room already occupied is always considered the attacker (in case of a tie, the attacker wins).
                        Both players roll one die and add their strength points (it is also possible to use weapons if you can form them using
                        the word in your “bag”). If you lose a battle against another player, you gain strength (you must discard a card of your
                        choice from your hand or “bag” if the opponent is Niall Campbell).
                        <br /><br />
                    </p>
                    <p>
                        However, if you win against another player, you may choose either to steal a card from their “bag” or a random card from
                        their hand (against Niall Campbell, you take the top card from the discard pile; against any other NPC visitor, draw one
                        from the draw deck).
                    </p>
                </section>

                <section className="rules-section">
                    <h2>Turns</h2>
                    <p>
                        The first step is drawing cards. You may draw as many as you want until you have 7 cards in your hand (the number of cards
                        you have affects the number of action points in your turn). If you have 7 or more cards, you receive no action points. If
                        you have fewer, you get 7 minus the number of cards in your hand — for example, if you have 5 cards, you get 2 points.
                        <br /><br />
                    </p>
                    <p>
                        You can spend your points on different actions:
                        <br />- Move to a room adjacent to yours.
                        <br />- Move an NPC visitor to a room adjacent to theirs.
                        <br />- Jump to a room if you can form its name with the word in your “bag.” For example, if you have “APPEARS,” you can go to “SPA” or
                        “SAFE AREA” (if it’s a compound word, you only need to form one of its parts), as long as it’s not an escape tower.
                        <br />- Attempt an escape when you’re in one of the escape towers (unless it’s “EMPEROR” or “CAMPBELL,” whose words can be used anywhere)
                        and can form its name with your “bag” word. For a successful attempt, roll a die and get a number lower than your strength.
                        If the attempt fails, roll the dice to move to a random room.
                    </p>
                    <p>
                        Finally, you may discard as many cards as you want and must empty your “bag,” keeping only 2 letters — or 3 if you can form a word with them.
                    </p>
                </section>

                <section className="rules-section">
                    <h2>Explanatory Video of Escape From Elba Rules</h2>
                    <div className="rules-video-wrapper">
                        <iframe
                            className="rules-video"
                            src="https://www.youtube.com/embed/JjQOi04i2-0?si=LMb-skomsHNsHqmz"
                            title="Escape From Elba - Explanatory Video"
                            frameBorder="0"
                            allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share"
                            allowFullScreen
                        />
                    </div>
                </section>
            </div>
        </div>
    );
}