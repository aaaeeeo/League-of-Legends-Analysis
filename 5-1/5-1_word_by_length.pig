A = load '/tmp/pg100.txt' as line;
B = foreach A generate flatten(TOKENIZE((chararray)$0)) as word;
C = foreach B generate SIZE(word) as len;
D = group C by len;
E = foreach D generate COUNT(C) AS occurrences, group as len;
F = order E by occurrences desc;
DUMP F;