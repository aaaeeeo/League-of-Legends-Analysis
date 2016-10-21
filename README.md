# 4-1
``/word-count-zuomingli/src/main/java/edu/uchicago/mpcs53013/word_count_zuomingli/WordCount.java``
#### On my mac(I'm using local environment instead of VM):
Using combiner let running time drop to **45155** from **97677**
#### On cluster:
Using combiner let CPU time drop to **34410** from **42340**  
 
  
Combiner combine and reduce the data from mapper, thus reduced size of data transferred to the reduce task. So, it minimizes the time taken for data transfer between mapper and reducer, speeding up the task.

# 4-2
``/word-count-zuomingli/src/main/java/edu/uchicago/mpcs53013/word_count_zuomingli/CharCount.java``  

output in ``4-2_chartype_out.txt``

# 4-3
``/word-count-zuomingli/src/main/java/edu/uchicago/mpcs53013/word_count_zuomingli/WordCount_4_3.java``  

output in ``4-3_wordcount_4-3_out.txt``

# 4-4
``/word-count-zuomingli/src/main/java/edu/uchicago/mpcs53013/word_count_zuomingli/WordCount_4_4.java``  

output in ``4-4_wordcount_4-4_out.txt``