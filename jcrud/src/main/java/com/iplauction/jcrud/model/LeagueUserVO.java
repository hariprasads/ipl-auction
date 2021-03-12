package com.iplauction.jcrud.model;

import java.util.Date;

public class LeagueUserVO {

    private String userId;
    private String userName;
    private String userRole;
    private String leagueRole;
    private String teamName;
    private Long points;
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

    public Long getPoints() {
        return points;
    }

    public void setPoints(Long points) {
        this.points = points;
    }

    public String getLeagueRole() {
        return leagueRole;
    }

    public void setLeagueRole(String leagueRole) {
        this.leagueRole = leagueRole;
    }
}
