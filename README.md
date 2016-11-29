# Project - League of Legends Killing Positions Heatmap    
Zuoming Li   
**cnetid**: zuomingli   

## Description

My project draw and show a heatmap of killing positions from matches data from hot online game League of Legends.  

Website URL: http://104.197.248.161:8080/    
Source code in Gitlab: https://gitlab.com/aaaeeeo/2016mpcs53013-zuomingli/tree/project    
Deploy files on cluster:
`@hdp-m:/home/lee_zuoming/` and `@webserver:/home/lee_zuoming/`    
#### Usage Note
1. Region ***Brazil*** don't have any batch view data. Select it only on region filter to test speed layer.   
2. I have tried to optimize drawing options, but `Max` and `Radius` may still need to be adjusted in order to get a beautiful heatmap.  


## Data
#### Source
I downloaded data from Riot offical API for League of Legends and used this API for speed layer as well.   
API document: https://developer.riotgames.com/api/methods   
#### Size
Original data contains **290579** random matches from 3 regions' servers with size **41GB**.
## Batch Layer  
#### Ingest  
**Source code**: Java project `matchIngest`   
**Data on cluster hdfs**: `/inputs/pos` and `/inputs/match`    
Read pre-downloaded json data and store into **hdfs sequence file** with **thrift binary**.    
**minimal-json** is used to parse json: https://github.com/ralfstx/minimal-json   

#### Mapreduce
**Source code**: Pig script `batch.pig` and `myudfs.py`  
**Batch view table on Hbase**: `match_pos`       
I used pig to compute each catogory's counts of poistions, transform them to json format and store to **Hbase**. Like `key:"11_NA_6.21_TEAM_BUILDER_DRAFT_RANKED_5x5_1_2", value:[321,122,12],[122,31,2]`.      
And I computed and stored all data of specific map specially.    
**Python UDF** is used in pig script.    


## Server Layer
#### Back-End
**Source code**: `server.py`   
**Python Bottle** server listening on port 8080.    
**gevent** is used to muti-thread the server.    
**happybase** is used to connect hbase from python.  
**gzip** is used to compress json data.   

#### Front-End
**Source code**: `web`    
Library used: jQuery, Bootstrap, [bootstrap-select](https://silviomoreto.github.io/bootstrap-select/), [simpleheat](https://github.com/mourner/simpleheat)   

## Speed Layer
#### Stream from API
**Source code**: `live.py`   
**Kafka topic on cluster**: `match-event`   
I used python script download new matches data from API and put them in **kafka** topic and store them into local files inorder to regenerate batch view.     
**NOTE:** I comment out the part of storing live data to files on cluster because this will generate large amount of data.   

#### Storm
**Source code**: Java project `matchTopology`    
**Storm task on cluster**: `zuomingli-match-live`   
**Speed view table on Hbase**: `match_pos_speed`   

## Deploy
On hdp-m:    
```
yarn jar match_ingest.jar edu.uchicago.mpcs53013.match.SerializeMatches ~/inputs
pig batch.pig
storm jar storm.jar edu.uchicago.mpcs53013.MatchTopology zuomingli-match-live 10
```
On webserver:    
I used tmux to hold more than one sessions.  
```
python server.py
python live.py na
python live.py kr
python live.py br
```
