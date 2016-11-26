import json
import requests
from kafka import KafkaProducer
import time
import sys

cur_id = 0
api_key = 'RGAPI-a3cd65d1-dc96-4180-bf6f-1fa03cc26bf4'
region = sys.argv[1]
game_url = 'https://%s.api.pvp.net/observer-mode/rest/featured?api_key=%s' % (region, api_key)
match_url = 'https://%s.api.pvp.net/api/lol/%s/v2.2/match/%s?includeTimeline=true&api_key=%s' % (region, region,'%s' ,api_key)

producer = KafkaProducer(bootstrap_servers='hdp-m.c.mpcs53013-2016.internal:6667')

#producer.send('match-event', json.dumps({'key': "11_NA_6.21_TEAM_BUILDER_DRAFT_RANKED_5x5_1_2", 'x':7340, 'y':4888}))

while(True):
    game_re = requests.get(game_url)
    cur_games = json.loads(game_re.content)
    min_id = cur_games['gameList'][0]['gameId']
    file_id = 0
    out = None
    print "min_id", min_id
    for mid in range(max(cur_id, min_id-20000000), min_id-10000000):
        cur_id = mid
        match_re = requests.get(match_url % mid)
        print cur_id, match_re.status_code
        if match_re.status_code == 200:
            #write to file
            if mid/10000 != file_id:
                file_id = mid/10000
                if out != None:
                    out.close()
                filename = 'matches-%s-%s.txt' % (region, file_id)
                out = open(filename, 'a')
            out.write(match_re.content)
            out.write('\n')
            out.flush()
            #extract info needed
            match_js = json.loads(match_re.content)
            key = ""
            key += str(match_js['mapId'])
            key += "_"+str(match_js['region'])
            key += "_"+str(match_js['matchVersion'])[:4]
            key += "_"+str(match_js['queueType'])
            print match_js['matchId'], key
            win = -1
            if 'winner' in match_js:
                for team in match_js['teams']:
                        if team['winner'] == True:
                                win = int(team['teamId'])/100
            if match_js['mapId']==11 and 'timeline' in match_js and 'frames' in match_js['timeline']:
                for frame in match_js['timeline']['frames']:
                    if 'events' in frame:
                        for event in frame['events']:
                            if event['eventType'] == 'CHAMPION_KILL':
                                #print event
                                pos = event['position']
                                x = int(pos['x'])
                                y = int(pos['y'])
                                kill_team = 1 if event['killerId']<=5 else 2
                                win_kill = 1 if kill_team == win else 0
                                event_key = key+"_"+str(win_kill)+"_"+str(kill_team)
                                producer.send('match-event', json.dumps({'key': event_key, 'x':x, 'y':y}))
        producer.flush()
        time.sleep(3)
    time.sleep(60)
