package edu.uchicago.mpcs53013.match;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import com.eclipsesource.json.*;


import edu.uchicago.mpcs53013.matchThrift.*;


public abstract class MatchProcessor {
	
	public MatchProcessor(){
		
	}

	void processLine(String line, File file) throws IOException {
		JsonObject orin = Json.parse(line).asObject();
		MatchInfo match = new MatchInfo();
		long matchId = orin.get("matchId").asLong();
		match.setMatchId(matchId) ;
		match.setMatchCreation(orin.get("matchCreation").asLong());
		match.setMatchMode(orin.get("matchMode").asString());
		match.setMatchType(orin.get("matchType").asString());
		match.setMatchVersion(orin.get("matchVersion").asString());
		match.setRegion(orin.get("region").asString());
		match.setMapId((short)orin.get("mapId").asInt());
		match.setQueueType(orin.get("queueType").asString());
		match.setSeason(orin.get("season").asString());
		for (JsonValue value : orin.get("teams").asArray()) {
			if(value.asObject().getBoolean("winner", false) == true)
				match.setWinTeam((short)value.asObject().get("teamId").asInt());
		}
		//System.out.println(match.toString());
		storeMatch(match);
		
		if(orin.get("timeline")!=null && orin.get("timeline").asObject().get("frames")!=null) {
			JsonArray frames = orin.get("timeline").asObject().get("frames").asArray();
			for(JsonValue value : frames) {
				if(value.asObject().get("events") != null) {
					JsonArray events = value.asObject().get("events").asArray();
					for(JsonValue event_value : events) {
						JsonObject event = event_value.asObject();
						if(event.get("eventType").asString().equals("CHAMPION_KILL")) {
							KillPos pos = new KillPos();
							pos.setMatchId(matchId);
							pos.setX((short)event.get("position").asObject().get("x").asInt());
							pos.setY((short)event.get("position").asObject().get("y").asInt());
							pos.setKillerId((short)event.get("killerId").asInt());
							pos.setVictimId((short)event.get("victimId").asInt());
							pos.setTimestamp(event.get("timestamp").asInt());
							//System.out.println(pos.toString());
							storePos(pos);
						}
					}
				}
			}
			
		}
	}
	
	abstract void storeMatch(MatchInfo match) throws IOException;
	abstract void storePos(KillPos pos) throws IOException;
	
	BufferedReader getFileReader(File file) throws FileNotFoundException, IOException {
		return new BufferedReader(new InputStreamReader(new FileInputStream(file)));
	}
	
	void processFile(File file) throws IOException {		
		BufferedReader br = getFileReader(file);
		String line;
		while((line = br.readLine()) != null) {
			processLine(line, file);
		}
	}

	void processDirectory(String directoryName) throws IOException {
		File directory = new File(directoryName);
		File[] directoryListing = directory.listFiles();
		for(File file : directoryListing)
			if(file.getName().startsWith("matches-")) {
				System.out.println(file.getName());
				processFile(file);
			}
	}

}
