package org.avida.hime.hime.storage;

//The interfaces are for implementing the concrete Database storage solution later depending on what we actually use.
public interface IMatchStorage {
    //should probably have tournament Id for tourneys with same name?
    public void addMatchResult(String tournamentName, String firstUserId, int firstUserPoints, String secondUserId, int secondUserPoints);
    public MatchResult GetMatchResults(String tournamentName, String userId);
}
