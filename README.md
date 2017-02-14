# IntelliJ IDEA Database console extractors

# Table-Text-Groovy.txt.groovy

Makes it possible to output to a format similar to MySQL output for easy copy-pasting as (fixed width) text. Output is like this:

```
+-------+-----------------+---------------------+
| sapID | fulfilment_type | start_time          |
+-------+-----------------+---------------------+
| 3595  | COLLECTION      | 1970-01-05 08:00:00 |
| 3595  | COLLECTION      | 1970-01-05 10:00:00 |
| 3595  | COLLECTION      | 1970-01-05 12:00:00 |
| 3595  | COLLECTION      | 1970-01-05 14:00:00 |
| 3595  | COLLECTION      | 1970-01-05 16:00:00 |
+-------+-----------------+---------------------+
5 rows in set.
```

Warning: because we have to loop through all the values to determine the max width before we start writing output we have to read all the values into memory first. Be careful with very large datasets.
