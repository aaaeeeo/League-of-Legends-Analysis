CREATE TABLE word_by_length AS
SELECT len, count(1) AS count FROM
(SELECT length(word) AS len FROM
(SELECT explode(split(line, '\\s')) AS word FROM docs) w ) l
   GROUP BY len ORDER BY len;

SELECT * FROM word_by_length;