namespace java edu.uchicago.mpcs53013.flight

struct Flight {
	1: required i32 station;
	2: required i16 year;
	3: required i16 month;
	4: required i16 day;
	5: required string carrier;
	6: required string origin;
	7: required string dest;
	8: required i64 dep_delay;
	9: required i64 arr_delay;
}