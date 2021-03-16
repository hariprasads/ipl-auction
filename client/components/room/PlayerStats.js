import React from 'react'
import {Button, Container, Image, Row, Col, Table} from 'react-bootstrap';
import { withStyles } from '@material-ui/core/styles';

const styles = {
    playerDetailsRow: {
        marginBottom: 13,
        fontSize: 15
    },
    playerData: {
        fontWeight: 700,
        marginLeft: 10
    },
    playerTable: {
        marginTop: 5
    },
    playerInfo: {
        marginTop: 25
    },
    statsTitle:{
        fontWeight: 700,
    },
    playerDetailsStatsTitle: {
        fontSize: 16,
        marginTop: 12
    },
    playerStatsTable: {
        paddingLeft: 30,
        // marginTop: 20
    },
    playerInfoView: {
        paddingLeft: 0
    }
};


class PlayerStats extends React.Component{

    constructor(props){
        super(props);
        this.state = {
            players: [{
                        "Player": "SR Tendulkar (INDIA)",
                        "Span": "1992-2011",
                        "Mat": 45,
                        "Runs": 2278,
                        "HS": 152,
                        "BatAv": 56.95,
                        "Hundreds": 6,
                        "Wkts": 8,
                        "BBI": "28-Feb",
                        "BowlAv": 67.37,
                        "FvWkts": 0,
                        "Ct": 12,
                        "St": 0,
                        "Ave Diff": -10.42,
                        "URL": "https://stats.espncricinfo.com/ci/content/player/35320.html",
                        "image": "https://www.espncricinfo.com/inline/content/image/501527.html?alt=1",
                        "Country": "INDIA",
                        "Role": "Batsman",
                        "Price": "2 Cr",
                        "Bags": "Marquee",
                        "type": "Indian",
                        "team": "Mumbai Indians",
                        "stats": "IPL"
                    }],
            index: 0
        }
    }

    render(){
        return(
            <Col md={8} className={this.props.classes.playerInfo}>
                <Container>
                        <Row>
                            <Col xs={6} md={4} className="player-image">
                                <Image src="https://www.espncricinfo.com/inline/content/image/501527.html?" height="200px" width="200px" rounded  fluid  />
                            </Col>
                            <Col md={8} className={this.props.classes.playerInfoView}>
                                <Row className={this.props.classes.playerDetailsRow}>
                                    Name: <span className={this.props.classes.playerData}> {this.state.players[this.state.index].Player} </span>
                                </Row>
                                <Row className={this.props.classes.playerDetailsRow}>
                                    Team: <span className={this.props.classes.playerData}> {this.state.players[this.state.index].team} </span>
                                </Row>
                                <Row className={this.props.classes.playerDetailsRow}>
                                    Role: <span className={this.props.classes.playerData}> {this.state.players[this.state.index].Role} </span>
                                </Row>
                                <Row className={this.props.classes.playerDetailsRow}>
                                    Category: <span className={this.props.classes.playerData}> {this.state.players[this.state.index].type} </span>
                                </Row>
                                <Row className={this.props.classes.playerDetailsRow}>
                                    Base Price: <span className={this.props.classes.playerData}> {this.state.players[this.state.index].Price} </span>
                                </Row>
                                {/* <Row className={`${styles.playerBidDetailsRow}`}>
                                    <Col md={10}>
                                        <Row className={`${styles.playerDetailsRow}`}>
                                            Current Price: <span className = {`${styles.playerPrice}`}> {this.state.currentPriceValue} {this.state.currentPriceDenomination}</span>
                                        </Row>
                                        <Row className={`${styles.playerDetailsRow}`}>
                                            Bid Team: <span className = {`${styles.playerPrice}`}> {this.state.bidTeam}</span>
                                        </Row>
                                        
                                    </Col>
                                    <Col md={2}>
                                        <Button variant="danger" disabled={this.state.sold} className={`${styles.bidButton}`} onClick={this.bidSubmitted}> Bid &#x270B; </Button>
                                    </Col>
                                </Row>
                                <Row className={`${styles.soldButtonRow}`}>
                                    <Button variant="success" className={`${styles.soldButton}`} disabled={this.state.sold} onClick={this.bidSold}> Sold &#128276; </Button>
                                    <Confetti active={ this.state.sold } config={config}/>
                                </Row> */}
                            </Col>
                            <Col md={8} className={this.props.classes.playerStatsTable}>
                                <Row className={this.props.classes.playerDetailsStatsTitle}>
                                    <span className={this.props.classes.statsTitle}> {this.state.players[this.state.index].stats} Career Statistics </span>
                                </Row>
                                <Row className={this.props.classes.playerDetailsRow}>
                                    <Table striped bordered hover size="sm" className={this.props.classes.playerTable} >
                                        <thead>
                                            <tr>
                                                <th>Matches</th>
                                                <th>Runs</th>
                                                <th>High Score</th>
                                                <th>Bat avg.</th>
                                                <th>100s</th>
                                                <th>Wickets</th>
                                                <th>Bowl avg.</th>
                                                <th>5 wkts</th>
                                                <th>Catches</th>
                                                <th>Stumpings</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr>
                                                <td>{this.state.players[this.state.index].Mat}</td>
                                                <td>{this.state.players[this.state.index].Runs}</td>
                                                <td>{this.state.players[this.state.index].HS}</td>
                                                <td>{this.state.players[this.state.index].BatAv}</td>
                                                <td>{this.state.players[this.state.index].Hundreds}</td>
                                                <td>{this.state.players[this.state.index].Wkts}</td>
                                                <td>{this.state.players[this.state.index].BowlAv}</td>
                                                <td>{this.state.players[this.state.index].FvWkts}</td>
                                                <td>{this.state.players[this.state.index].Ct}</td>
                                                <td>{this.state.players[this.state.index].St}</td>
                                            </tr>
                                        </tbody>
                                    </Table>    
                                </Row>
                            </Col>
                            
                        </Row>
                        
                    </Container>
                </Col>
        )
    }
}

export default (withStyles(styles)(PlayerStats));