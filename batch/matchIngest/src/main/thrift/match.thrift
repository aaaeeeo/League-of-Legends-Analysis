namespace java edu.uchicago.mpcs53013.matchThrift

struct MatchInfo {
	1: required i64 matchId;
	2: required string matchMode;
	3: required string matchType;
	4: required string matchVersion;
	5: required i16 winTeam;
	6: required i64 matchCreation;
	7: required string region;
	8: required i16 mapId;
	9: required string queueType;
	10: required string season;
}

struct KillPos {
	1: required i64 matchId;
	2: required i16 x;
	3: required i16 y;
	4: required i16 killerId;
	5: required i16 victimId;
	6: required i32 timestamp;
}

