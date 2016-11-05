## 6-1, 6-2

### Compute and store view as `(origin, dest, type, delay, count)`  
`type`: the weather type (CLEAR, FOG, RAIN...)   
`delay`: the sum of depature delay time of specific weather type and particular route   
`count`: the count of flights of specific weather type and particular route   

### Example output:
```
(ORD,JFK,CLEAR,6365.0,573)
(ORD,JFK,THUNDER,2822.0,115)
(ORD,JFK,RAIN,9342.0,420)
(ORD,JFK,FOG,1364.0,57)
(ORD,JFK,SNOW,2272.0,152)
(ORD,JFK,HAIL,139.0,3)
```


## 6-3
Use elephant-bird's LzoThriftBlockPigStorage to store flight data.