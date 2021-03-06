package com.iplauction.jcrud.controller;

import com.iplauction.jcrud.http.GenericServiceResponse;
import com.iplauction.jcrud.model.LeagueInfoVO;
import com.iplauction.jcrud.model.MatchStats;
import com.iplauction.jcrud.model.PlayerInfoVO;
import com.iplauction.jcrud.model.UserInfoVO;
import com.iplauction.jcrud.service.PlayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

import static com.iplauction.jcrud.http.GenericServiceResponse.Status.FAIL;
import static com.iplauction.jcrud.http.GenericServiceResponse.Status.SUCCESS;

@Controller
@RequestMapping("/player")
public class PlayerController {

    @Autowired
    PlayerService playerService;

    private static Logger logger = LoggerFactory.getLogger(PlayerController.class);

    @PostMapping()
    public ResponseEntity<GenericServiceResponse<PlayerInfoVO>> addNewPlayer(
            @RequestBody PlayerInfoVO playerInfoVO) {

        try {
            logger.info("addNewPlayer started ==>");
            PlayerInfoVO playerInfoVO1 = playerService.addNewPlayer(playerInfoVO);
            logger.info("addNewPlayer completed <==");
            return new ResponseEntity<GenericServiceResponse<PlayerInfoVO>>(new GenericServiceResponse<PlayerInfoVO>(SUCCESS, "playerInfo", playerInfoVO1), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error while addNewPlayer", e);
            return new ResponseEntity<GenericServiceResponse<PlayerInfoVO>>(new GenericServiceResponse<PlayerInfoVO>(FAIL, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping({"/addPlayerList"})
    public ResponseEntity<GenericServiceResponse<List<PlayerInfoVO>>> addPlayerList(
            @RequestBody List<PlayerInfoVO> playerInfoVOS) {

        try {
            logger.info("addPlayerList started ==>");
            if(!CollectionUtils.isEmpty(playerInfoVOS)) {
                for(PlayerInfoVO playerInfoVO : playerInfoVOS) {
                    playerService.addNewPlayer(playerInfoVO);
                }
            }
            logger.info("addPlayerList completed <==");
            return new ResponseEntity<GenericServiceResponse<List<PlayerInfoVO>>>(new GenericServiceResponse<List<PlayerInfoVO>>(SUCCESS), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error while addPlayerList", e);
            return new ResponseEntity<GenericServiceResponse<List<PlayerInfoVO>>>(new GenericServiceResponse<List<PlayerInfoVO>>(FAIL, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping({"/all"})
    public ResponseEntity<GenericServiceResponse<List<PlayerInfoVO>>> getAllPlayers() {
        try {
            List<PlayerInfoVO> playerInfoVOS = playerService.getAllPlayers();
            if (!playerInfoVOS.isEmpty()) {
                return new ResponseEntity<GenericServiceResponse<List<PlayerInfoVO>>>(
                        new GenericServiceResponse<List<PlayerInfoVO>>(SUCCESS, "playerInfos", playerInfoVOS), HttpStatus.OK);
            }
            return new ResponseEntity<GenericServiceResponse<List<PlayerInfoVO>>>(new GenericServiceResponse<List<PlayerInfoVO>>(FAIL, "No Player Info Found"),
                    HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<GenericServiceResponse<List<PlayerInfoVO>>>(new GenericServiceResponse<List<PlayerInfoVO>>(FAIL, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping({"getPlayerById/{playerId}"})
    public ResponseEntity<GenericServiceResponse<PlayerInfoVO>> getPlayerById(
            @PathVariable(name = "playerId")   @Valid @NotNull String playerId) {
        try {
            logger.info("getPlayerById {leagueInfoId} ==>", playerId);

            PlayerInfoVO playerInfoVO = playerService.getPlayerInfoById(playerId);

            logger.info("getPlayerById {leagueInfoId} is Complete <==", playerId);
            return new ResponseEntity<GenericServiceResponse<PlayerInfoVO>>(new GenericServiceResponse<PlayerInfoVO>(SUCCESS, "playerIfo", playerInfoVO), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error while getting scan requests", e);
            return new ResponseEntity<GenericServiceResponse<PlayerInfoVO>>(new GenericServiceResponse<PlayerInfoVO>(FAIL, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping({"getPlayersBag/{bagNumber}"})
    public ResponseEntity<GenericServiceResponse<List<PlayerInfoVO>>> getPlayersByBag(@PathVariable(name = "bagNumber")   @Valid @NotNull String bagNumber) {
        try {
            List<PlayerInfoVO> playerInfoVOS = playerService.getPlayersByBag(bagNumber);
            if (!playerInfoVOS.isEmpty()) {
                return new ResponseEntity<GenericServiceResponse<List<PlayerInfoVO>>>(
                        new GenericServiceResponse<List<PlayerInfoVO>>(SUCCESS, "playerInfos", playerInfoVOS), HttpStatus.OK);
            }
            return new ResponseEntity<GenericServiceResponse<List<PlayerInfoVO>>>(new GenericServiceResponse<List<PlayerInfoVO>>(FAIL, "No Player Info Found"),
                    HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<GenericServiceResponse<List<PlayerInfoVO>>>(new GenericServiceResponse<List<PlayerInfoVO>>(FAIL, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping({"/calculatePoints"})
    public ResponseEntity<GenericServiceResponse<List<PlayerInfoVO>>> calculatePoints(
            @RequestBody MatchStats matchStats) {

        try {
            List<PlayerInfoVO> playerInfoVOList = null;
                    logger.info("calculatePoints started ==>");
            if(matchStats != null) {
                playerInfoVOList  = playerService.calculatePoints(matchStats);
            }
            logger.info("calculatePoints completed <==");
            return new ResponseEntity<GenericServiceResponse<List<PlayerInfoVO>>>(
                    new GenericServiceResponse<List<PlayerInfoVO>>(SUCCESS, "playerInfos", playerInfoVOList), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error while calculatePoints", e);
            return new ResponseEntity<GenericServiceResponse<List<PlayerInfoVO>>>(new GenericServiceResponse<List<PlayerInfoVO>>(FAIL, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping({"/setLatestFlag/{hasPlayedToday}"})
    public ResponseEntity<GenericServiceResponse<List<PlayerInfoVO>>> setLatestFlag(
            @RequestBody List<String> playerIds,@PathVariable(name = "hasPlayedToday")   @Valid @NotNull boolean hasPlayedToday) {

        try {
            List<PlayerInfoVO> playerInfoVOList = null;
            logger.info("setLatestFlag started ==>");
            if(playerIds != null) {
                playerInfoVOList = playerService.setLatestFlag(playerIds, hasPlayedToday);
            }
            logger.info("setLatestFlag completed <==");
            return new ResponseEntity<GenericServiceResponse<List<PlayerInfoVO>>>(
                    new GenericServiceResponse<List<PlayerInfoVO>>(SUCCESS, "playerInfos", playerInfoVOList), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error while setLatestFlag", e);
            return new ResponseEntity<GenericServiceResponse<List<PlayerInfoVO>>>(new GenericServiceResponse<List<PlayerInfoVO>>(FAIL, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
