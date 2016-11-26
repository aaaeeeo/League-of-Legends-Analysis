REGISTER ./piggybank.jar;
REGISTER ./elephant-bird-core-4.14.jar;
REGISTER ./elephant-bird-pig-4.14.jar;
REGISTER ./elephant-bird-hadoop-compat-4.14.jar;
REGISTER ./libthrift-0.9.0.jar;
REGISTER ./thrift_structure.jar;
Register 'myudfs.py' using jython as myfuncs;


DEFINE MatchThriftBytesToTuple com.twitter.elephantbird.pig.piggybank.ThriftBytesToTuple('edu.uchicago.mpcs53013.matchThrift.MatchInfo');
DEFINE PosThriftBytesToTuple com.twitter.elephantbird.pig.piggybank.ThriftBytesToTuple('edu.uchicago.mpcs53013.matchThrift.KillPos');

MATCH_DATA = LOAD '/inputs/match' USING org.apache.pig.piggybank.storage.SequenceFileLoader() as (key:long, value: bytearray);
MATCH_INFO = FOREACH MATCH_DATA GENERATE FLATTEN(MatchThriftBytesToTuple(value));

POS_DATA = LOAD '/inputs/pos' USING org.apache.pig.piggybank.storage.SequenceFileLoader() as (key:long, value: bytearray);
KILL_POS = FOREACH POS_DATA GENERATE FLATTEN(PosThriftBytesToTuple(value));

MATCH_POS = JOIN MATCH_INFO by matchId, KILL_POS by matchId;

MATCH_POS_SE = FOREACH MATCH_POS GENERATE mapId as map_id, region as region, SUBSTRING(matchVersion, 0, 4) as version, queueType as queue_type, (winTeam==100 ? 1 : 2) as win_team, (killerId<6 ? 1 : 2) as kill_team, x as x, y as y;

MATCH_POS_PO = FOREACH MATCH_POS_SE GENERATE map_id, region, version, queue_type, (win_team==kill_team ? 1 : 0) as win_kill, kill_team, x/20 as x, y/20 as y;

MATCH_POS_GR = GROUP MATCH_POS_PO by (map_id, region, version, queue_type, win_kill, kill_team, x, y);
MATCH_POS_COUNT = FOREACH MATCH_POS_GR GENERATE group, COUNT(MATCH_POS_PO) as count;

MATCH_POS_FLT = FOREACH MATCH_POS_COUNT GENERATE flatten(group), count;
MATCH_POS_STR = FOREACH MATCH_POS_FLT GENERATE map_id, region, version, queue_type, win_kill, kill_team, CONCAT('[',(chararray)x,',',(chararray)y,',',(chararray)count,']') as str, count;
MATCH_POS_BAG = GROUP MATCH_POS_STR BY (map_id, region, version, queue_type, win_kill, kill_team);
MATCH_POS_SG = FOREACH MATCH_POS_BAG GENERATE CONCAT((chararray)group.map_id, '_', group.region, '_', group.version, '_', group.queue_type, '_', (chararray)group.win_kill, '_', (chararray)group.kill_team) as key, myfuncs.concat_bag(MATCH_POS_STR.str) AS str;
MATCH_POS_MAX_SG = FOREACH MATCH_POS_BAG GENERATE CONCAT((chararray)group.map_id, '_', group.region, '_', group.version, '_', group.queue_type, '_', (chararray)group.win_kill, '_', (chararray)group.kill_team) as key, MAX(MATCH_POS_STR.count) as max;

MATCH_POS_PO_ALL = FOREACH MATCH_POS GENERATE mapId as map_id, x/20 as x, y/20 as y;
MATCH_POS_GR_ALL = GROUP MATCH_POS_PO_ALL by (map_id, x, y);
MATCH_POS_COUNT_ALL = FOREACH MATCH_POS_GR_ALL GENERATE group, COUNT(MATCH_POS_PO_ALL) as count;
MATCH_POS_FLT_ALL = FOREACH MATCH_POS_COUNT_ALL GENERATE flatten(group), count;
MATCH_POS_STR_ALL = FOREACH MATCH_POS_FLT_ALL GENERATE map_id, CONCAT('[',(chararray)x,',',(chararray)y,',',(chararray)count,']') as str, count;
MATCH_POS_BAG_ALL = GROUP MATCH_POS_STR_ALL BY map_id;
MATCH_POS_ALL = FOREACH MATCH_POS_BAG_ALL GENERATE (chararray)group AS key, myfuncs.concat_bag(MATCH_POS_STR_ALL.str) AS str;
MATCH_POS_MAX_ALL = FOREACH MATCH_POS_BAG_ALL GENERATE (chararray)group AS key, MAX(MATCH_POS_STR_ALL.count) AS max;

MATCH_POS_UNION = UNION MATCH_POS_ALL, MATCH_POS_SG;
MATCH_POS_UNION_MAX = UNION MATCH_POS_MAX_ALL, MATCH_POS_MAX_SG;


STORE MATCH_POS_UNION INTO 'hbase://match_pos'
  USING org.apache.pig.backend.hadoop.hbase.HBaseStorage('pos:str');
STORE MATCH_POS_UNION_MAX INTO 'hbase://match_pos'
  USING org.apache.pig.backend.hadoop.hbase.HBaseStorage('pos:max');

