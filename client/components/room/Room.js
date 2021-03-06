import React from 'react';
import { Button } from 'react-bootstrap';
import { API_ENDPOINT, API_URL, JWT_TOKEN, USER_ID } from '../../config/config';
import { clearLocalStorage, getLocalStorage, setLocalStorage } from '../../utils/storageUtil';
import axios from 'axios';
import PlayerStats from './PlayerStats';
import TeamSummary from './TeamSummary';
import ModeratorZone from './ModeratorZone';
import PlayerPopupModal from './PlayerPopupModal';
import _ from 'lodash';
import { ROLE_MODERATOR } from '../../constants/constants';
import {
  joinAuctionRoom,
  onJoinRoom,
  messageTestListen,
  startAuction,
  endAuction,
  getCurrentPlayerData,
  getAuctionStatus,
  setNextPlayer,
  submitBid,
  getBidUpdates,
  getRoomDetails,
  sellPlayer,
  playerSold,
  getFoldUpdates,
  foldBid,
  disconnect,
  onEndAuction
} from '../../socket/socket';



class Room extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      role: 'player',
      isActive: false,
      currentPlayer: null,
      auctionSummary: null,
      currentIndex: 0,
      bidDetails: null,
      sold: true,
      soldData: null,
      playersPopUp: false,
      popData: [],
      roomDetail: null,
      joinedRoom: false,
      fold: false,
      foldedArray: [],
      onlineUsers: []
    };
  }

  componentDidMount() {
    this.getUserRole();
    this.enterAuctionRoom();

    onJoinRoom((err, data) => {
        let users = new Set(data);

        this.setState({
            joinedRoom: true,
            onlineUsers: [...users]
        });
    });

    messageTestListen((err, data) => {
      console.log('Interval');
      console.log(data);
    });

    getCurrentPlayerData((err, data) => {
      this.setState({
        currentPlayer: data,
        sold: false,
        soldData: null,
      });
    });

    getAuctionStatus((err, data) => {
      this.setState({
        isActive: data.isActive,
      });
    });

    onEndAuction((err, data) => {
      this.setState({
        isActive: false
      },() => {
        
        let idKey = `currentIndex#${this.state.roomDetail.leagueId}`;
        clearLocalStorage(idKey);
        this.props.endAuction();
      });
    });

    getRoomDetails((err, data) => {
      this.setState({
        isActive: data.isActive,
        currentPlayer: data.currentPlayerInBid,
      });
    });

    getBidUpdates((err, data) => {
      if (data.currentBid == 0) {
        this.setState({
          sold: false,
        });
      } else {
        this.setState({
          bidDetails: data,
        });
      }
    });

    playerSold((err, data) => {
      this.setState({
        bidDetails: null,
        sold: true,
        fold: false,
        soldData: data,
        foldedArray: [],
      });
      this.props.sellPlayer(data);
    });

    getFoldUpdates((err, data) => {
      let tempArray = this.state.foldedArray;
      tempArray.push(data);
      this.setState({
        foldedArray: tempArray,
      });
    });
  
}


  componentWillUnmount(){
      disconnect();
  }

  static getDerivedStateFromProps(nextProps, prevState) {
    if (prevState.roomDetail !== nextProps.detail) {
      if(nextProps.detail){
        const data = {
          userId: getLocalStorage(USER_ID),
          roomId: nextProps.detail.leagueId,
        };

        if (!prevState.joinedRoom) {
          joinAuctionRoom(data);
        }

        let idKey = `currentIndex#${nextProps.detail.leagueId}`;
        
        let index = prevState.currentIndex;
        if(getLocalStorage(idKey)){
          index = getLocalStorage(idKey);
        }


        return {
          roomDetail: nextProps.detail,
          currentIndex: index,
        };
      }
    }

    // Return null to indicate no change to state.
    return null;
  }

  enterAuctionRoom = () => {
    if (this.state.roomDetail) {
      console.log('ENter auction room');
      const data = {
        userId: getLocalStorage(USER_ID),
        roomId: this.state.roomDetail.leagueId,
      };
      joinAuctionRoom(data);
    }
  };

  bidBtnClick = (val) => {
    let data = {
      userId: getLocalStorage(USER_ID),
      roomId: this.state.roomDetail.leagueId,
      nextBid: val.nextBid,
      playerId: val.playerId,
    };
    submitBid(data);
  };

  makeBid = _.debounce(this.bidBtnClick, 500);

  handleStartButton = () => {
    const data = {
      isActive: true,
      roomId: this.state.roomDetail.leagueId,
    };

    startAuction(data);

    const bearer_token = getLocalStorage(JWT_TOKEN);
    const bearer = 'Bearer ' + bearer_token;
    const url = `${API_ENDPOINT}/iplauction/league/updateLeagueStatus/${this.state.roomDetail.leagueId}/STARTED`;

    const headers = {
        'Authorization': bearer
    }
    // POST CALL
    axios.put(url, {}, {
        headers: headers
    })
    .then((response) => {
        console.log(response);
    })
    .catch((error) => {
        console.log(error);
    });

  };

  handleEndButton = ()=> {
    const data = {
      roomId: this.state.roomDetail.leagueId
    }
    endAuction(data);
    
  }

  getUserRole = () => {
    let userId = getLocalStorage(USER_ID);

    let user = {};

    if (this.state.roomDetail) {
      user = _.find(this.state.roomDetail.leagueUsers, ['userId', userId]);
      if (this.state.role != user.leagueRole) {
        this.setState({
          role: user.leagueRole,
        });
      }
    }
  };

  getActionButtons = () => {
    if (this.state.role == ROLE_MODERATOR) {
      if (!this.state.isActive) {
        return <Button onClick={this.handleStartButton}>Start Auction</Button>;
      }
    }
  };

  getPlayer = () => {
    if (this.state.currentIndex < this.props.playerSet.length) {
      const data = {
        roomId: this.state.roomDetail.leagueId,
        player: this.props.playerSet[this.state.currentIndex],
      };
      setNextPlayer(data);
      let idKey = `currentIndex#${this.state.roomDetail.leagueId}`;
      setLocalStorage(idKey, this.state.currentIndex + 1);
      this.setState({
        currentIndex: this.state.currentIndex + 1,
      });
    }
  };

  getNextBag = () => {
    this.props.getNextBag();
    const data = {
      roomId: this.state.roomDetail.leagueId,
      player: null,
    };
    setNextPlayer(data);
    let idKey = `currentIndex#${this.state.roomDetail.leagueId}`;
    setLocalStorage(idKey, 0);
    this.setState({
      currentIndex: 0,
    });
  };

  
  soldProcess = (val) => {
    let data = {
      roomId: this.state.roomDetail.leagueId,
      nextBid: val.nextBid,
      playerId: val.playerId,
    };

    if(this.state.bidDetails){
        let ownerPlayerId = this.state.bidDetails.playerOwnerUserId;
        let currentBid = this.state.bidDetails.currentBid;
        const bearer_token = getLocalStorage(JWT_TOKEN);
        const bearer = 'Bearer ' + bearer_token;
        const url = `${API_ENDPOINT}/iplauction/league/sellPlayerToUser/${val.playerId}/${currentBid}`;

        const headers = {
            'X-UserId': ownerPlayerId,
            'X-LeagueId': this.state.roomDetail.leagueId,
            'Authorization': bearer
        }

        axios.post(url, {}, {
            headers: headers
        })
        .then((response) => {
            sellPlayer(data);
        })
        .catch((error) => {
            console.log(error);
        });
    }else{
      sellPlayer(data);
    }
    
  };

  soldBtnClicked = _.debounce(this.soldProcess, 500);

  handlePopUp = (data) => {
    this.setState({
      playersPopUp: true,
      popData: data,
    });
  };

  foldBtnClicked = () => {
    this.setState({
      fold: true,
    });
    let data = {
      roomId: this.state.roomDetail.leagueId,
      userId: getLocalStorage(USER_ID),
    };
    foldBid(data);
  };

  getAuctionUI = () => {
      let playersRemaining = 0;
      if(this.props.playerSet){
        playersRemaining = this.props.playerSet.length - this.state.currentIndex;
      }

      let leagueUsers = [];
      if(this.state.roomDetail){
        leagueUsers = this.state.roomDetail.leagueUsers;
      }
    const bidHistory = this.state.bidDetails ? this.state.bidDetails.bidHistory : [];
    if (this.state.role == ROLE_MODERATOR) {
      return (
        <div>
          <ModeratorZone
            sold={this.state.sold}
            submitPlayer={this.getPlayer}
            playersRemaining={playersRemaining}
            nextBag={this.getNextBag}
            currentBag={this.props.currentBag}
            futureBag={this.props.nextBag}
            onEndAuction={this.handleEndButton}
            leagueId={this.state.roomDetail.leagueId}
          />
          <PlayerStats
            myTable={this.props.loggedUser}
            sold={this.state.sold}
            soldData={this.state.soldData}
            sellPlayer={this.soldBtnClicked}
            teams={this.props.teams}
            data={this.state.currentPlayer}
            bidHistory={bidHistory}
            bidDetails={this.state.bidDetails}
            role={this.state.role}
            onlineUsers={this.state.onlineUsers}
          />
          <TeamSummary
            foldedArray={this.state.foldedArray}
            onOpenPopup={this.handlePopUp}
            data={leagueUsers}
            role={this.state.role}
          />
        </div>
      );
    } else {
      return (
        <div>
          <PlayerStats
            foldBtn={this.foldBtnClicked}
            fold={this.state.fold}
            myTable={this.props.loggedUser}
            sold={this.state.sold}
            soldData={this.state.soldData}
            data={this.state.currentPlayer}
            teams={this.props.teams}
            submitBid={this.makeBid}
            bidHistory={bidHistory}
            bidDetails={this.state.bidDetails}
            role={this.state.role}
          />
          <TeamSummary
            foldedArray={this.state.foldedArray}
            onOpenPopup={this.handlePopUp}
            data={leagueUsers}
            role={this.state.role}
          />
        </div>
      );
    }
  };

  closePlayerPopup = () => {
    this.setState({
      playersPopUp: false,
    });
  };

  render() {
    this.getUserRole();

    return (
      <div>
        <h4> Auction Room - {this.state.roomDetail ? this.state.roomDetail.leagueName : ''}</h4>
        {!this.state.isActive ? this.getActionButtons() : ''}
        {/* {!this.state.isActive ? <Button onClick={this.enterAuctionRoom}>Enter Auction</Button> : ""} */}
        {this.state.isActive ? (
          this.getAuctionUI()
        ) : (
          <div style={{marginTop: 15}}> Waiting for Moderator to start the auction</div>
        )}
        <PlayerPopupModal
          data={this.state.popData}
          show={this.state.playersPopUp}
          onExit={this.closePlayerPopup}
        />
      </div>
    );
  }
}

export default Room;
