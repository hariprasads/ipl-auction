package com.iplauction.jcrud.controller;

import com.iplauction.jcrud.http.GenericServiceResponse;
import com.iplauction.jcrud.model.LeagueInfoVO;
import com.iplauction.jcrud.model.LeagueUserVO;
import com.iplauction.jcrud.model.PlayerInfoVO;
import com.iplauction.jcrud.model.TransferRequestsVO;
import com.iplauction.jcrud.service.LeagueInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

import static com.iplauction.jcrud.http.GenericServiceResponse.Status.FAIL;
import static com.iplauction.jcrud.http.GenericServiceResponse.Status.SUCCESS;

@Controller
@RequestMapping("/league")
public class LeagueController {

    @Autowired
    LeagueInfoService leagueInfoService;

    private static Logger logger = LoggerFactory.getLogger(LeagueController.class);

    @PostMapping()
    public ResponseEntity<GenericServiceResponse<LeagueInfoVO>> addLeagueInfo(
            @RequestHeader(name = "X-UserId") @Pattern(regexp="^[a-zA-Z0-9@./#&+-]+$", message = "User-Id cannot be empty and should be Alpha-Numeric") final String userId,
            @RequestBody LeagueInfoVO leagueInfoVO) {

        try {
            logger.info("addLeagueInfo started ==>");
            if(leagueInfoService.getLeagueByLeagueName(leagueInfoVO.getLeagueName()) != null){
                return new ResponseEntity<GenericServiceResponse<LeagueInfoVO>>(new GenericServiceResponse<LeagueInfoVO>(FAIL, "League already exists with the same name"), HttpStatus.BAD_REQUEST);
            }
            LeagueInfoVO leagueInfoVOResponse = leagueInfoService.addNewLeagueInfo(leagueInfoVO,userId);
            logger.info("addLeagueInfo completed <==");
            return new ResponseEntity<GenericServiceResponse<LeagueInfoVO>>(new GenericServiceResponse<LeagueInfoVO>(SUCCESS, "leagueInfo", leagueInfoVOResponse), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error while addLeagueInfo", e);
            return new ResponseEntity<GenericServiceResponse<LeagueInfoVO>>(new GenericServiceResponse<LeagueInfoVO>(FAIL, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping({"/all"})
    public ResponseEntity<GenericServiceResponse<List<LeagueInfoVO>>> getAllLeagueInfo() {
        try {
            List<LeagueInfoVO> leagueInfoVOS = leagueInfoService.getAllLeagueDetails();
            if (!leagueInfoVOS.isEmpty()) {
                return new ResponseEntity<GenericServiceResponse<List<LeagueInfoVO>>>(
                        new GenericServiceResponse<List<LeagueInfoVO>>(SUCCESS, "leagueInfos", leagueInfoVOS), HttpStatus.OK);
            }
            return new ResponseEntity<GenericServiceResponse<List<LeagueInfoVO>>>(new GenericServiceResponse<List<LeagueInfoVO>>(FAIL, "No League Info Found"),
                    HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<GenericServiceResponse<List<LeagueInfoVO>>>(new GenericServiceResponse<List<LeagueInfoVO>>(FAIL, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping({"/{leagueInfoId}"})
    public ResponseEntity<GenericServiceResponse<LeagueInfoVO>> getLeagueInfoById(
            @PathVariable(name = "leagueInfoId")   @Valid @NotNull String leagueInfoId) {
        try {
            logger.info("getLeagueInfoById {leagueInfoId} ==>", leagueInfoId);

            LeagueInfoVO leagueInfoVO = leagueInfoService.getLeagueInfoById(leagueInfoId);

            logger.info("getLeagueInfoById {leagueInfoId} is Complete <==", leagueInfoId);
            return new ResponseEntity<GenericServiceResponse<LeagueInfoVO>>(new GenericServiceResponse<LeagueInfoVO>(SUCCESS, "leagueInfo", leagueInfoVO), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error while getLeagueInfoById", e);
            return new ResponseEntity<GenericServiceResponse<LeagueInfoVO>>(new GenericServiceResponse<LeagueInfoVO>(FAIL, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping({"/joinLeague"})
    public ResponseEntity<GenericServiceResponse<LeagueInfoVO>> joinLeague(
            @RequestHeader(name = "X-UserId") @Pattern(regexp="^[a-zA-Z0-9@./#&+-]+$", message = "User-Id cannot be empty and should be Alpha-Numeric") final String userId,
            @RequestHeader(name = "X-LeagueId") @Pattern(regexp="^[a-zA-Z0-9@./#&+-]+$", message = "League-Id cannot be empty and should be Alpha-Numeric") final String leagueId,
            @RequestBody LeagueUserVO leagueUserVO) {

        try {
            logger.info("addLeagueInfo started ==>");
            if(!leagueInfoService.validateLeagueMemberCount(leagueId)){
                return new ResponseEntity<GenericServiceResponse<LeagueInfoVO>>(new GenericServiceResponse<LeagueInfoVO>(FAIL,"Cannot Join League : League already reached the max limit of 10"), HttpStatus.NOT_ACCEPTABLE);
            }

            if(!leagueInfoService.validateLeagueModeratorCount(leagueId)){
                return new ResponseEntity<GenericServiceResponse<LeagueInfoVO>>(new GenericServiceResponse<LeagueInfoVO>(FAIL, "Cannot Join League as Moderator : League already reached the max limit of 2 Moderator"), HttpStatus.NOT_ACCEPTABLE);
            }
            if(!leagueInfoService.validateLeaguePlayerCount(leagueId)){
                return new ResponseEntity<GenericServiceResponse<LeagueInfoVO>>(new GenericServiceResponse<LeagueInfoVO>(FAIL,"Cannot Join League as Player : League already reached the max limit of 8 Players"), HttpStatus.NOT_ACCEPTABLE);
            }
            if(!leagueInfoService.validateIfUserAlreadyExists(leagueId,userId)){
                return new ResponseEntity<GenericServiceResponse<LeagueInfoVO>>(new GenericServiceResponse<LeagueInfoVO>(FAIL,"Cannot Join League : User already exists in the League"), HttpStatus.NOT_ACCEPTABLE);
            }

            LeagueInfoVO leagueInfoVOResponse = leagueInfoService.joinLeague(leagueUserVO,leagueId,userId);
            logger.info("addLeagueInfo completed <==");
            return new ResponseEntity<GenericServiceResponse<LeagueInfoVO>>(new GenericServiceResponse<LeagueInfoVO>(SUCCESS, "leagueInfo", leagueInfoVOResponse), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error while addLeagueInfo", e);
            return new ResponseEntity<GenericServiceResponse<LeagueInfoVO>>(new GenericServiceResponse<LeagueInfoVO>(FAIL, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping({"/getUserLeagues"})
    public ResponseEntity<GenericServiceResponse<List<LeagueInfoVO>>> getLeagueInfoByUserId(
            @RequestHeader(name = "X-UserId") @Pattern(regexp="^[a-zA-Z0-9@./#&+-]+$", message = "User-Id cannot be empty and should be Alpha-Numeric") final String userId) {
        try {
            logger.info("getUserLeagues {leagueInfoId} ==>", userId);

            List<LeagueInfoVO> leagueInfoVOS = leagueInfoService.getLeagueInfoByUserId(userId);

            logger.info("getUserLeagues {leagueInfoId} is Complete <==", userId);
            return new ResponseEntity<GenericServiceResponse< List<LeagueInfoVO> >>(new GenericServiceResponse< List<LeagueInfoVO> >(SUCCESS, "leagueInfos", leagueInfoVOS), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error while getUserLeagues", e);
            return new ResponseEntity<GenericServiceResponse< List<LeagueInfoVO> >>(new GenericServiceResponse< List<LeagueInfoVO> >(FAIL, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping({"/sellPlayerToUser/{playerId}/{soldPrice}"})
    public ResponseEntity<GenericServiceResponse<LeagueInfoVO>> sellPlayerToUser(
            @RequestHeader(name = "X-UserId") @Pattern(regexp="^[a-zA-Z0-9@./#&+-]+$", message = "User-Id cannot be empty and should be Alpha-Numeric") final String userId,
            @RequestHeader(name = "X-LeagueId") @Pattern(regexp="^[a-zA-Z0-9@./#&+-]+$", message = "League-Id cannot be empty and should be Alpha-Numeric") final String leagueId,
            @PathVariable(name = "playerId")   @Valid @NotNull String playerId,
            @PathVariable(name = "soldPrice")   @Valid @NotNull Long soldPrice) {
        try {
            logger.info("getLeagueInfoByUserId {leagueInfoId} ==>", userId);

            if(leagueInfoService.isModerator(leagueId,userId)){
                return new ResponseEntity<GenericServiceResponse<LeagueInfoVO>>(new GenericServiceResponse<LeagueInfoVO>(FAIL,"Cannot Sell Player to Moderator"), HttpStatus.NOT_ACCEPTABLE);
            }
           if(leagueInfoService.isPlayerAlreadySold(leagueId,playerId)){
               return new ResponseEntity<GenericServiceResponse<LeagueInfoVO>>(new GenericServiceResponse<LeagueInfoVO>(FAIL,"Cannot Sell Player who has been sold already"), HttpStatus.NOT_ACCEPTABLE);
            }

            LeagueInfoVO leagueInfoVOS = leagueInfoService.sellPlayerToUser(leagueId,userId,playerId,soldPrice);

            logger.info("getLeagueInfoByUserId {leagueInfoId} is Complete <==", userId);
            return new ResponseEntity<GenericServiceResponse< LeagueInfoVO>>(new GenericServiceResponse< LeagueInfoVO >(SUCCESS, "leagueInfo", leagueInfoVOS), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error while getLeagueInfoByUserId", e);
            return new ResponseEntity<GenericServiceResponse< LeagueInfoVO >>(new GenericServiceResponse<LeagueInfoVO>(FAIL, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping({"/getTeamSquad"})
    public ResponseEntity<GenericServiceResponse< List<PlayerInfoVO>>> getTeamSquad(
            @RequestHeader(name = "X-UserId") @Pattern(regexp="^[a-zA-Z0-9@./#&+-]+$", message = "User-Id cannot be empty and should be Alpha-Numeric") final String userId,
            @RequestHeader(name = "X-LeagueId") @Pattern(regexp="^[a-zA-Z0-9@./#&+-]+$", message = "League-Id cannot be empty and should be Alpha-Numeric") final String leagueId) {
        try {
            logger.info("getLeagueInfoByUserId {leagueInfoId} ==>", userId);

            List<PlayerInfoVO> playerInfoVOS = leagueInfoService.getTeamSquad(leagueId,userId);

            logger.info("getLeagueInfoByUserId {leagueInfoId} is Complete <==", userId);
            return new ResponseEntity<GenericServiceResponse<  List<PlayerInfoVO>>>(new GenericServiceResponse<  List<PlayerInfoVO> >(SUCCESS, "squadInfo", playerInfoVOS), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error while getLeagueInfoByUserId", e);
            return new ResponseEntity<GenericServiceResponse<  List<PlayerInfoVO> >>(new GenericServiceResponse< List<PlayerInfoVO>>(FAIL, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping({"/updateTeamSquad"})
    public ResponseEntity<GenericServiceResponse< List<PlayerInfoVO>>> updateTeamSquad(
            @RequestHeader(name = "X-UserId") @Pattern(regexp="^[a-zA-Z0-9@./#&+-]+$", message = "User-Id cannot be empty and should be Alpha-Numeric") final String userId,
            @RequestHeader(name = "X-LeagueId") @Pattern(regexp="^[a-zA-Z0-9@./#&+-]+$", message = "League-Id cannot be empty and should be Alpha-Numeric") final String leagueId,
            @RequestBody List<PlayerInfoVO> playerInfoVOS) {
        try {
            logger.info("updateTeamSquad {leagueInfoId} ==>", userId);

            List<PlayerInfoVO> playerInfoVOS1 = leagueInfoService.updateTeamSquad(leagueId,userId,playerInfoVOS);

            logger.info("updateTeamSquad {leagueInfoId} is Complete <==", userId);
            return new ResponseEntity<GenericServiceResponse<  List<PlayerInfoVO>>>(new GenericServiceResponse<  List<PlayerInfoVO> >(SUCCESS, "squadInfo", playerInfoVOS1), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error while updateTeamSquad", e);
            return new ResponseEntity<GenericServiceResponse<  List<PlayerInfoVO> >>(new GenericServiceResponse< List<PlayerInfoVO>>(FAIL, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping({"/getUnsoldPlayers"})
    public ResponseEntity<GenericServiceResponse< List<PlayerInfoVO>>> getUnsoldPlayers(
            @RequestHeader(name = "X-LeagueId") @Pattern(regexp="^[a-zA-Z0-9@./#&+-]+$", message = "League-Id cannot be empty and should be Alpha-Numeric") final String leagueId) {
        try {
            logger.info("getUnsoldPlayers {leagueInfoId} ==>", leagueId);

            List<PlayerInfoVO> playerInfoVOS = leagueInfoService.getUnsoldPlayers(leagueId);

            logger.info("getUnsoldPlayers {leagueInfoId} is Complete <==", leagueId);
            return new ResponseEntity<GenericServiceResponse<  List<PlayerInfoVO>>>(new GenericServiceResponse<  List<PlayerInfoVO> >(SUCCESS, "unSoldPlayers", playerInfoVOS), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error while getUnsoldPlayers", e);
            return new ResponseEntity<GenericServiceResponse<  List<PlayerInfoVO> >>(new GenericServiceResponse< List<PlayerInfoVO>>(FAIL, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping({"/updateLeagueStatus/{leagueInfoId}/{leagueStatus}"})
    public ResponseEntity<GenericServiceResponse<LeagueInfoVO>> updateLeagueStatus(
            @PathVariable(name = "leagueInfoId")   @Valid @NotNull String leagueInfoId,
            @PathVariable(name = "leagueStatus")   @Valid @NotNull String leagueStatus) {
        try {
            logger.info("getLeagueInfoById {leagueInfoId} ==>", leagueInfoId);

            LeagueInfoVO leagueInfoVO = leagueInfoService.updateLeagueStatus(leagueInfoId,leagueStatus);

            logger.info("getLeagueInfoById {leagueInfoId} is Complete <==", leagueInfoId);
            return new ResponseEntity<GenericServiceResponse<LeagueInfoVO>>(new GenericServiceResponse<LeagueInfoVO>(SUCCESS, "leagueInfo", leagueInfoVO), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error while getLeagueInfoById", e);
            return new ResponseEntity<GenericServiceResponse<LeagueInfoVO>>(new GenericServiceResponse<LeagueInfoVO>(FAIL, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping({"/updateFinalSquad/{leagueInfoId}"})
    public ResponseEntity<GenericServiceResponse<LeagueInfoVO>> updateFinalSquad(
            @PathVariable(name = "leagueInfoId")   @Valid @NotNull String leagueInfoId) {
        try {
            logger.info("getLeagueInfoById {leagueInfoId} ==>", leagueInfoId);

            LeagueInfoVO leagueInfoVO = leagueInfoService.updateFinalSquad(leagueInfoId);

            logger.info("getLeagueInfoById {leagueInfoId} is Complete <==", leagueInfoId);
            return new ResponseEntity<GenericServiceResponse<LeagueInfoVO>>(new GenericServiceResponse<LeagueInfoVO>(SUCCESS, "leagueInfo", leagueInfoVO), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error while getLeagueInfoById", e);
            return new ResponseEntity<GenericServiceResponse<LeagueInfoVO>>(new GenericServiceResponse<LeagueInfoVO>(FAIL, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping({"/updateScores/{leagueInfoId}"})
    public ResponseEntity<GenericServiceResponse<LeagueInfoVO>> updateScores(
            @PathVariable(name = "leagueInfoId")   @Valid @NotNull String leagueInfoId) {
        try {
            logger.info("updateScores {leagueInfoId} ==>", leagueInfoId);

            LeagueInfoVO leagueInfoVO = leagueInfoService.updateScores(leagueInfoId);

            logger.info("updateScores {leagueInfoId} is Complete <==", leagueInfoId);
            return new ResponseEntity<GenericServiceResponse<LeagueInfoVO>>(new GenericServiceResponse<LeagueInfoVO>(SUCCESS, "leagueInfo", leagueInfoVO), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error while updateScores", e);
            return new ResponseEntity<GenericServiceResponse<LeagueInfoVO>>(new GenericServiceResponse<LeagueInfoVO>(FAIL, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping({"/updateData/{leagueInfoId}"})
    public ResponseEntity<GenericServiceResponse<Void>> updateData(
            @PathVariable(name = "leagueInfoId")   @Valid @NotNull String leagueInfoId) {
        try {
            logger.info("updateScores {leagueInfoId} ==>", leagueInfoId);

            leagueInfoService.updateData(leagueInfoId);

            logger.info("updateScores {leagueInfoId} is Complete <==", leagueInfoId);
            return new ResponseEntity<GenericServiceResponse<Void>>(new GenericServiceResponse<Void>(SUCCESS, "Updated"), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error while updateScores", e);
            return new ResponseEntity<GenericServiceResponse<Void>>(new GenericServiceResponse<Void>(FAIL, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping({"/addTransferRequest/{leagueInfoId}"})
    public ResponseEntity<GenericServiceResponse<Void>> addTransfer(
            @PathVariable(name = "leagueInfoId")   @Valid @NotNull String leagueInfoId,
            @RequestBody TransferRequestsVO transferRequestsVO) {
        try {
            logger.info("updateScores {leagueInfoId} ==>", leagueInfoId);

            leagueInfoService.addTransfer(transferRequestsVO,leagueInfoId);

            logger.info("updateScores {leagueInfoId} is Complete <==", leagueInfoId);
            return new ResponseEntity<GenericServiceResponse<Void>>(new GenericServiceResponse<Void>(SUCCESS), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error while updateScores", e);
            return new ResponseEntity<GenericServiceResponse<Void>>(new GenericServiceResponse<Void>(FAIL, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping({"/getAllTransferRequest/{leagueInfoId}"})
    public ResponseEntity<GenericServiceResponse<List<TransferRequestsVO>>> getTransferRequest(
            @PathVariable(name = "leagueInfoId")   @Valid @NotNull String leagueInfoId) {
        try {
            logger.info("updateScores {leagueInfoId} ==>", leagueInfoId);

            List<TransferRequestsVO> transferRequestsVO = leagueInfoService.getTransferRequest(leagueInfoId);

            logger.info("updateScores {leagueInfoId} is Complete <==", leagueInfoId);
            return new ResponseEntity<GenericServiceResponse<List<TransferRequestsVO>>>(new GenericServiceResponse<List<TransferRequestsVO>>(SUCCESS,"transferRequests", transferRequestsVO), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error while updateScores", e);
            return new ResponseEntity<GenericServiceResponse<List<TransferRequestsVO>>>(new GenericServiceResponse<List<TransferRequestsVO>>(FAIL, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping({"/getUserTransferRequests"})
    public ResponseEntity<GenericServiceResponse<List<TransferRequestsVO>>> getUserTransferRequests(
            @RequestHeader(name = "X-UserId") @Pattern(regexp="^[a-zA-Z0-9@./#&+-]+$", message = "User-Id cannot be empty and should be Alpha-Numeric") final String userId,
            @RequestHeader(name = "X-LeagueId") @Pattern(regexp="^[a-zA-Z0-9@./#&+-]+$", message = "League-Id cannot be empty and should be Alpha-Numeric") final String leagueId) {
        try {
            logger.info("updateScores {leagueInfoId} ==>", leagueId);

            List<TransferRequestsVO> transferRequestsVO = leagueInfoService.getUserTransferRequests(leagueId,userId);

            logger.info("updateScores {leagueInfoId} is Complete <==", leagueId);
            return new ResponseEntity<GenericServiceResponse<List<TransferRequestsVO>>>(new GenericServiceResponse<List<TransferRequestsVO>>(SUCCESS,"transferRequests", transferRequestsVO), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error while updateScores", e);
            return new ResponseEntity<GenericServiceResponse<List<TransferRequestsVO>>>(new GenericServiceResponse<List<TransferRequestsVO>>(FAIL, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping({"/removeUserTransferRequest/{transferOutId}"})
    public ResponseEntity<GenericServiceResponse<Void>> removeUserTransferRequest(
            @RequestHeader(name = "X-UserId") @Pattern(regexp="^[a-zA-Z0-9@./#&+-]+$", message = "User-Id cannot be empty and should be Alpha-Numeric") final String userId,
            @RequestHeader(name = "X-LeagueId") @Pattern(regexp="^[a-zA-Z0-9@./#&+-]+$", message = "League-Id cannot be empty and should be Alpha-Numeric") final String leagueId,
            @PathVariable(name = "transferOutId")   @Valid @NotNull String transferOutId) {
        try {
            logger.info("updateScores {leagueInfoId} ==>", leagueId);

            leagueInfoService.removeUserTransferRequest(leagueId,userId,transferOutId);

            logger.info("updateScores {leagueInfoId} is Complete <==", leagueId);
            return new ResponseEntity<GenericServiceResponse<Void>>(new GenericServiceResponse<Void>(SUCCESS), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error while updateScores", e);
            return new ResponseEntity<GenericServiceResponse<Void>>(new GenericServiceResponse<Void>(FAIL, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
