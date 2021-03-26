const { get, set } = require("./config/redis");


module.exports = function (io, socket) {

  socket.on('create auction', (room) => {
    socket.join(room);
    console.log('moderator created ', room);
  });

  socket.on('joinAuction', async (data) => {
    console.log("--------------------->> User joined room");
    console.log(data);
    socket.join(data.roomId);
    socket.emit('notification', `${data.user} joined the room`);
    io.in(data.roomId).emit('notification', `${data.user} joined the room`);
    const roomStatus = await fetchAuctionDetailsFromCache(data);
    io.in(data.roomId).emit('room-status', roomStatus);
    // Emit Room details only to the user to who has joined; Maintain the room active status here
  });

  socket.on('emit back', (data) => {
    console.log(data, " received");
    io.in(data.roomId).emit('new event from server', data.msg);
    //in case you want to emit to everyone in room except sender
    //socket.to(data.roomId).emit('new event from server', data.msg);
  });

  socket.on('start-auction', async (data) => {
    console.log("Start Auction ------->");
    console.log(data);
    await insertAuctionDetailsInCache(data);
    io.in(data.roomId).emit('auction-started', data);
  });

  socket.on('next-player', async (data) => {
    console.log("Next Player Details ------->");
    console.log(data);
    await createAuctionRoomPlayerKeyInCache(data.roomId, data.player.playerId);
    io.in(data.roomId).emit('current-player', data.player);
  })

  socket.on('submit-bid', async (data) => {
    console.log("New Bid Submitted ------->");
    console.log(data);
    const playerBidDetails = await fetchCurrentBidForPlayerFromCache(data.roomId, data.playerId);
    if (data.nextBid > playerBidDetials.currentBid) {
      playerBidDetails.currentBid = data.nextBid;
      playerBidDetails.bidHistory.push({ userId: data.userId, bid: data.nextBid, time: Date.now() });
      playerBidDetails.playerOwnerUserId = data.userId;
      const promiseArray = [];
      promiseArray.push(updateAuctionRoomPlayerKeyInCache(data.roomId, data.playerId, playerBidDetails));
      promiseArray.push(await updateAuctionDetailsInCache(data.roomId, data.playerId));
      await Promise.all(promiseArray);
      io.in(data.roomId).emit('bid-updates', playerBidDetails);
    }
  })

  async function insertAuctionDetailsInCache(data) {
    await set('AR#' + data.roomId, JSON.stringify(data));
  }

  async function updateAuctionDetailsInCache(roomId, playerId) {
    const newObject = { isActive: true, currentPlayerInBid: playerId };
    await set('AR#' + roomId, JSON.stringify(newObject));
  }

  async function fetchAuctionDetailsFromCache(data) {
    const result = await get('AR#' + data.roomId);
    const parsedResult = JSON.parse(result);
    console.log(parsedResult);
    return parsedResult;
  }

  async function createAuctionRoomPlayerKeyInCache(roomId, playerId) {
    const value = { currentBid: 0, playerOwnerUserId: 0, bidHistory: [] };
    await set('AR#' + roomId + 'PID#' + playerId, JSON.stringify(value));
  }

  async function fetchCurrentBidForPlayerFromCache(roomId, playerId) {
    const result = await get('AR#' + roomId + 'PID#' + playerId);
    const parsedResult = JSON.parse(result);
    console.log(parsedResult);
    return parsedResult;
  }

  async function updateAuctionRoomPlayerKeyInCache(roomId, playerId, playerBidDetails) {
    const value = { currentBid: 0, playerOwnerUserId: 0, bidHistory: [] };
    await set('AR#' + roomId + 'PID#' + playerId, JSON.stringify(playerBidDetails));
  }

};