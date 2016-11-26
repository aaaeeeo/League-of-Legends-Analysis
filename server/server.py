from bottle import get, run, post, request, static_file, response
from gevent import monkey
import happybase
import json
import StringIO
import gzip
monkey.patch_all()


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
    max_val = 1
    for comb in combs:
        row = table.row(comb)
        rowsp = tablesp.row(comb)
        if len(row)>0:
            count += 1
            json_str += (',' if len(json_str)>0 else '') + row['pos:str']
            max_val += int(row['pos:max'])
        if len(rowsp)>0:
            count_sp += 1
            json_str_sp += (',' if len(json_str_sp)>0 else '') + rowsp['pos:str']
    print count, count_sp
    res = '{"max":'+str(max_val*0.05) + ',"data":['+ (json_str if count>0 else json_str_sp) +']}'
    connection.close()
    out = StringIO.StringIO()
    with gzip.GzipFile(fileobj=out, mode="w") as f:
      f.write(res)
    response.content_type = 'application/json'
    response.set_header('Content-Encoding', 'gzip')
    return out.getvalue()

@post('/pos_all')
def pos_all():
    connection = happybase.Connection('hdp-m.c.mpcs53013-2016.internal', port=9090)
    connection.open()
    table = connection.table('match_pos')
    tablesp = connection.table('match_pos_speed')
    mapid = request.forms.get('map')
    row = table.row(mapid)
    rowsp = tablesp.row(mapid)
    val = row['pos:str']
    if len(rowsp)>0:
        val += ','+rowsp['pos:str']
    res = '{"max":'+str(int(row['pos:max'])*0.8)+',"data":['+val+']}'
    connection.close()
    out = StringIO.StringIO()
    with gzip.GzipFile(fileobj=out, mode="w") as f:
      f.write(res)
    response.content_type = 'application/json'
    response.set_header('Content-Encoding', 'gzip')
    return out.getvalue()

run(host='0.0.0.0', port=8080, debug=True, server='gevent')