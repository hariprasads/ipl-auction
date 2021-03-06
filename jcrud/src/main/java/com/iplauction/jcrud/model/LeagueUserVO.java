package com.iplauction.jcrud.model;

import java.util.Date;
import java.util.List;

public class LeagueUserVO {

    private String userId;
    private String userName;
    private String userRole;
    private String leagueRole;
    private String teamName;
    private Double points;
    private List<PlayerInfoVO> playersSquad;
    private List<FinalSquadVO> finalSquad;
    private Long totalBudget;
    private Long spentAmount;
    private Long remainingBudget;
    private Date createdDateTime;
    private Date lastModifiedDateTime;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Date getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(Date createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public Date getLastModifiedDateTime() {
        return lastModifiedDateTime;
    }

    public void setLastModifiedDateTime(Date lastModifiedDateTime) {
        this.lastModifiedDateTime = lastModifiedDateTime;
    }

    public Double getPoints() {
        return points;
    }

    public void setPoints(Double points) {
        this.points = points;
    }

    public String getLeagueRole() {
        return leagueRole;
    }

    public void setLeagueRole(String leagueRole) {
        this.leagueRole = leagueRole;
    }

    public List<PlayerInfoVO> getPlayersSquad() {
        return playersSquad;
    }

    public void setPlayersSquad(List<PlayerInfoVO> playersSquad) {
        this.playersSquad = playersSquad;
    }

    public Long getTotalBudget() {
        return totalBudget;
    }

    public void setTotalBudget(Long totalBudget) {
        this.totalBudget = totalBudget;
    }

    public Long getSpentAmount() {
        return spentAmount;
    }

    public void setSpentAmount(Long spentAmount) {
        this.spentAmount = spentAmount;
    }

    public Long getRemainingBudget() {
        return remainingBudget;
    }

    public void setRemainingBudget(Long remainingBudget) {
        this.remainingBudget = remainingBudget;
    }

    public List<FinalSquadVO> getFinalSquad() {
        return finalSquad;
    }

    public void setFinalSquad(List<FinalSquadVO> finalSquad) {
        this.finalSquad = finalSquad;
    }
}
