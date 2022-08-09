package com.myApplication.teamCreator;

public class Team {
   Player[] teamMembers;

    public Team(int playerAmount){
        this.teamMembers = new Player[playerAmount];
    }
    public Team(){}
    public Team(Player[] players){
        this.teamMembers = players;
    }
    public void setTeamMember(Player player,int index){
        this.teamMembers[index] = player;
    }
    public Player getTeamMember(int index){
        return this.teamMembers[index];
    }
    public double getAverageTeamStrength(){
        double entireStrength = 0d;
        for (Player teamMember : this.teamMembers) {
            entireStrength += teamMember.getSpecificStrength();
        }
        return entireStrength/ this.teamMembers.length;
    }
    public double getRoundedAverageTeamStrength(){
        double entireStrength = 0d;
        for (Player teamMember : this.teamMembers) {
            entireStrength += teamMember.getSpecificStrength();
        }
        return Math.round(entireStrength/ this.teamMembers.length*100d)/100d;
    }
    public int getTeamMembersAmount(){
        return this.teamMembers.length;
    }
    public Player[] getTeamMembers(){
        return this.teamMembers;
    }
}
