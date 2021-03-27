import React from 'react';
import Box from '@material-ui/core/Box';
import Typography from '@material-ui/core/Typography';
import Grid from '@material-ui/core/Grid';
import { withStyles } from '@material-ui/core/styles';
import PlayersTable from './PlayersTable';

const styles = {
    title:{
        fontWeight: 500
    }
}

class Players extends React.Component{
    constructor(props){
        super(props)
    }

    createData = ({playerId, playerName, teamName, playerRole, basePrice, bagNumber, playerRace, matchesPlayed, runsScored, highestScore, battingAverage, battingStrikeRate, noOfWickets, bowlingEconomy, bowlingStrikeRate, noOfFourWickets, noOfFiveWickets}) => {
        return { playerId, playerName, teamName, playerRole, basePrice, bagNumber, playerRace, matchesPlayed, runsScored, highestScore, battingAverage, battingStrikeRate, noOfWickets, bowlingEconomy, bowlingStrikeRate, noOfFourWickets, noOfFiveWickets };
    }

    render(){
        return(
            <Box>
                <Typography className={this.props.classes.title} variant="h5"> Players List</Typography>
                <PlayersTable rows={this.props.list} />
            </Box>
            )
    }
}

export default (withStyles(styles)(Players));