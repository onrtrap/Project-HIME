package org.avida.hime.hime.storage;

import org.avida.hime.hime.models.Participant;

//I don't know the types for the user IDs yet, so I'll go with string for now.
public interface IParticipantStorage {
    public void addNewParticipant(String participantId, String participantName);
    public Participant getParticipant(String participantId);
}
