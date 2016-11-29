from bottle import get, run, post, request, static_file, response
from gevent import monkey
import happybase
import json
import StringIO
import gzip
import collections
import sys
monkey.patch_all()

cache = {}

@get('/')
def index():
    return static_file("index.html", root='web')

@get('/imgs/<filename:re:.*\.png>')
def send_image(filename):
    return static_file(filename, root='web/imgs', mimetype='image/png')

@post('/pos')
def pos():
    connection = happybase.Connection('hdp-m.c.mpcs53013-2016.internal', port=9090)
    connection.open()
    table = connection.table('match_pos')
    tablesp = connection.table('match_pos_speed')
    keys = ['map', 'region', 'version', 'queue', 'win', 'team']
    para_list = []
    paras = request.body.read()
    for key in keys:
        para_list.append(request.forms.getall(key))

    def recur(comb, para_list):
        if len(para_list) == 0:
            return comb
        newcomb = []
        for cur in comb:
            for para in para_list[0]:
                cstr = ( cur+"_"+para if len(cur)>0 else para)
                newcomb.append(cstr)
        return recur(newcomb, para_list[1:])
        
    combs = recur([""], para_list)

    json_str = ""
    json_str_sp = ""
    count = 0
    count_sp = 0
    for comb in combs:
        row = ( table.row(comb) if paras not in cache else [] )
        rowsp = tablesp.row(comb)
        if len(row)>0:
            count += 1
            json_str += (',' if len(json_str)>0 else '') + row['pos:str']
        if len(rowsp)>0:
            count_sp += 1
            json_str_sp += (',' if len(json_str_sp)>0 else '') + rowsp['pos:str']
    print count, count_sp
    counts = do_count(collections.Counter(), json_str, paras)
    if len(json_str_sp) > 0:
        counts = do_count(counts, json_str_sp, None)
    pos_counts = [ [int(x[0].split(',')[0]), int(x[0].split(',')[1]) ,x[1]] for x in counts.items()]
    pos_str = str(pos_counts).replace(' ','')
    max_val = (counts.most_common(1)[0][1] if len(counts)>0 else 0)
    print max_val
    res = '{"max":'+str(max_val) + ',"data":'+ pos_str +'}'
    connection.close()
    out = StringIO.StringIO()
    with gzip.GzipFile(fileobj=out, mode="w") as f:
      f.write(res)
    response.content_type = 'application/json'
    response.set_header('Content-Encoding', 'gzip')
    return out.getvalue()

def do_count(counts, json_str, paras):
    if paras is not None and paras in cache:
        print "cached"
        return collections.Counter(cache[paras])
    pos_list = json.loads('['+json_str+']')
    pos_key_list = [ ('%s,%s'%(x[0],x[1]), x[2]) for x in pos_list]
    for tup in pos_key_list:
        counts[tup[0]] += tup[1]
    if paras is not None:
        cache[paras] = collections.Counter(counts)
    return counts

@post('/pos_all')
def pos_all():
    response.content_type = 'application/json'
    response.set_header('Content-Encoding', 'gzip')
    mapid = request.forms.get('map')
    if mapid in cache:
        return cache[mapid]
    connection = happybase.Connection('hdp-m.c.mpcs53013-2016.internal', port=9090)
    connection.open()
    table = connection.table('match_pos')
    tablesp = connection.table('match_pos_speed')
    row = table.row(mapid)
    rowsp = tablesp.row(mapid)
    val = row['pos:str']
    if len(rowsp)>0:
        val += ','+rowsp['pos:str']
    print row['pos:max']
    res = '{"max":'+str(int(row['pos:max']))+',"data":['+val+']}'
    connection.close()
    out = StringIO.StringIO()
    with gzip.GzipFile(fileobj=out, mode="w") as f:
      f.write(res)
    cache[mapid] = out.getvalue()
    return out.getvalue()

run(host='0.0.0.0', port=sys.argv[1], debug=True, server='gevent')