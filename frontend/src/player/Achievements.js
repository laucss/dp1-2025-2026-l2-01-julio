export default function(){

    const achievementList = achievements.map((a) => { 
        return ( 
            <tr key={a.id}> <td className="text-center">{a.name}</td> 
                <td className="text-center"> {a.description} </td> 
                <td className="text-center"> <img src={a.badgeImage? a.badgeImage : imgnotfound } alt={a.name} width="50px"/> </td> 
                <td className="text-center"> {a.threshold} </td> 
                <td className="text-center"> {a.metric} </td> 
                <td className="text-center">
                    <Button outline color="warning" >
                        <Link to={`/achievements/`+a.id} className="btn sm"
                            style={{ textDecoration: "none" }}>Edit</Link>
                    </Button>
                    <Button outline color="danger" onClick={() => deleteFromList(
                        `/api/v1/achievements/${a.id}`,
                        a.id,
                        [achievements, setAchievements],
                        [alerts, setAlerts],
                        setMessage,
                        setVisible
                        )}>
                        Delete
                    </Button>
                </td>
            </tr> 
        ); 
    }); 
}